import { Callback, Context, DynamoDBStreamEvent } from "aws-lambda";
import { DynamoDB } from "aws-sdk";
import { ItemList, PutItemInput, QueryInput } from "aws-sdk/clients/dynamodb";
import {
  DetectionSource,
  DynamoDBStreamImage,
  InfectionInterface
} from "../interfaces/interface";

export function newInfectionRecorded(
  event: DynamoDBStreamEvent,
  context: Context,
  callback: Callback
) {
  const [contactsTableName, infectionsTableName] = getDynamoDbTablesNames(
    process.env
  );

  const client = new DynamoDB({ apiVersion: "2012-08-10" });

  event.Records.forEach(async record => {
    if (!record.dynamodb?.NewImage) {
      // make the error much better
      throw Error("Invalid Request. New Image Missing!");
    }
    if (record.eventName == "INSERT") {
      const newInfectionRecord = parseInfectionRecord(record.dynamodb.NewImage);
      // query contacts who have come across this infected person
      const dbContactsResponse = await getContactsOfAnInfected(
        newInfectionRecord.userId,
        contactsTableName, // should alway be set
        client
      );

      // todo: paginate at this point in future as the data grows.
      if (!dbContactsResponse.Items) {
        throw Error("Could not find any new contacts");
      }
      const contacts = filterContactsDuplicate(dbContactsResponse.Items);
      await addNewInfectionsFromContact(
        contacts,
        infectionsTableName, // should always be set
        newInfectionRecord.infectedTimestamp.toUTCString(),
        client
      );
    }
  });
  callback(null, `Successfully processed ${event.Records.length} records.`);
}

/**
 * Take in a {NewImageInterface} and Convert it to an object
 *
 */
function parseInfectionRecord(
  newImage: DynamoDBStreamImage
): InfectionInterface {
  if (
    !newImage.id.S ||
    !newImage.infectedTimestamp.S ||
    !newImage.userId.S ||
    !newImage.detectionSource.S ||
    !newImage.createdTimestamp.S
  ) {
    throw Error("Invalid Request. Required Field are not present");
  }

  return {
    id: newImage.id.S,
    infectedTimestamp: new Date(Date.parse(newImage.infectedTimestamp.S)),
    createdTimestamp: new Date(Date.parse(newImage.createdTimestamp.S)),
    deletedTimestamp: newImage.deletedTimestamp.S
      ? new Date(Date.parse(newImage.deletedTimestamp.S))
      : undefined,
    detectionSource: <DetectionSource>newImage.detectionSource.S,
    userId: newImage.userId.S
  };
}

/**
 * Fetch the list of close contacts with an infected person from dynamoDb
 */
async function getContactsOfAnInfected(
  userId: string,
  tableName: string,
  client: DynamoDB = new DynamoDB()
) {
  try {
    const dynamoDbParams: QueryInput = {
      TableName: tableName,
      KeyConditionExpression: "contactUserId = :userId",
      ExpressionAttributeValues: {
        userId: { S: userId }
      }
    };
    const dbResponse = await client.query(dynamoDbParams).promise();
    console.log({ contacts: dbResponse });
    if (!dbResponse) {
      throw Error("Could not find any new contacts");
    }
    return dbResponse;
  } catch (e) {
    console.error(e);
    throw e;
  }
}

/**
 * Remove multiple entries of a contact having come close to an infected person.
 */
function filterContactsDuplicate(dynamoDbContacts: ItemList) {
  const seen = new Set();
  const filteredContacts = dynamoDbContacts.filter(contact => {
    if (contact.contactUserId.S) {
      const duplicate = seen.has(contact.contactUserId.S);
      seen.add(contact.id);
      return !duplicate;
    }
    return false;
  });
  return filteredContacts;
}

/**
 * Add the list of contacts to the list of infected persons
 *
 * @param contacts list of contacts who have across an infected person
 * @param infectionsTable the name of the dynamoDb table with infections data
 * @param infectedTimestamp when the infection was registered
 * @param client DynamoDB Client
 */
function addNewInfectionsFromContact(
  contacts: ItemList,
  infectionsTable: string,
  infectedTimestamp: string,
  client: DynamoDB = new DynamoDB()
) {
  try {
    const data = contacts.map(contact => {
      const item: PutItemInput = {
        TableName: infectionsTable,
        Item: {
          id: { S: "" },
          userId: { S: contact.contactUserId.S },
          infectedTimestamp: { S: infectedTimestamp },
          fromInfectionId: { S: contact.userId.S },
          detectionSource: { S: DetectionSource.contact },
          createdTimestamp: { S: new Date(Date.now()).toISOString() }
        }
      };
      return client.putItem(item).promise();
    });
    // went against the idea of batch insert due to limits of 25 items
    return Promise.all(data);
  } catch (e) {
    console.error(e);
    throw e;
  }
}

function getDynamoDbTablesNames(environment = process.env) {
  const infectionsTableName = environment.DYNAMO_DB_INFECTIONS_TABLE_NAME;
  const contactsTableName = environment.DYNAMO_DB_CONTACTS_TABLE_NAME;

  if (!infectionsTableName || !contactsTableName) {
    throw Error(
      "The names of the infections and contacts dynamoDb tables are required!"
    );
  }

  return [infectionsTableName, contactsTableName];
}
