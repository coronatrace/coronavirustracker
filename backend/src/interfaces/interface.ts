import {
  QueryInput,
  BatchWriteItemInput,
  AttributeValue,
  ItemList,
  WriteRequest,
  PutItemInput
} from "aws-sdk/clients/dynamodb";

export interface DynamoDBStreamImage {
  [key: string]: AttributeValue;
}

export enum DetectionSource {
  test = "test",
  symptoms = "symptoms",
  contact = "contacts"
}

export interface InfectionInterface {
  id: string;
  infectedTimestamp: Date;
  userId: string;
  fromInfectionId?: string;
  detectionSource: DetectionSource;
  createdTimestamp: Date;
  deletedTimestamp?: Date;
}

export interface Contact {
  userId: string;
  id: string;
  contactUserId: string;
  contactTimestamp: Date;
  createdTimestamp: Date;
  expirationTimestamp: Date;
}

export interface contactsResponse {
  [key: string]: Contact;
}
