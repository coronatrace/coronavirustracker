import { AttributeValue, ItemList } from "aws-sdk/clients/dynamodb";

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
  infectedTimestamp: Number;
  userId: string;
  fromInfectionId?: string;
  detectionSource: DetectionSource;
  createdTimestamp: Number;
  deletedTimestamp?: Number;
}

export interface Contact {
  userId: string;
  id: string;
  contactUserId: string;
  contactTimestamp: Number;
  createdTimestamp: Number;
  expirationTimestamp: Number;
}

export interface contactsResponse {
  [key: string]: Contact;
}

export interface AddInfectionToDBInput {
  contacts: ItemList;
  infectionsTable: string;
  infectedTimestamp: number;
  infectionId: string;
}
