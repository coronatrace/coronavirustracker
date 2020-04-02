import { Construct } from "@aws-cdk/core";
import { Table } from "@aws-cdk/aws-dynamodb";
import { PolicyStatement, Effect } from "@aws-cdk/aws-iam";
import {
  Function,
  FunctionProps,
  Code,
  Runtime,
  StartingPosition
} from "@aws-cdk/aws-lambda";
import { DynamoEventSource } from "@aws-cdk/aws-lambda-event-sources";

export interface NewInfectionRecordedFuncProps {
  infectionTable: Table;
  contactsTable: Table;
}

export class NewInfectionsRecordedFunc extends Function {
  constructor(
    scope: Construct,
    id: string,
    props: NewInfectionRecordedFuncProps
  ) {
    const lambdaProps: FunctionProps = {
      runtime: Runtime.NODEJS_12_X,
      code: Code.fromAsset("./lambdas"),
      handler: "index.newInfectionRecorded",
      environment: {
        DYNAMO_DB_INFECTIONS_TABLE_NAME: props.infectionTable.tableName,
        DYNAMO_DB_CONTACTS_TABLE_NAME: props.contactsTable.tableName
      },
      initialPolicy: [
        new PolicyStatement({
          effect: Effect.ALLOW,
          actions: [
            "dynamodb:DescribeStream",
            "dynamodb:GetRecords",
            "dynamodb:GetShardIterator",
            "dynamodb:ListStreams",
            "dynamodb:Query",
            "dynamodb:Scan",
            "dynamodb:BatchWriteItem",
            "dynamodb:PutItem",
            "dynamodb:UpdateItem"
          ],
          resources: [props.infectionTable.tableArn]
        }),
        new PolicyStatement({
          effect: Effect.ALLOW,
          actions: [
            "dynamodb:BatchGetItem",
            "dynamodb:GetItem",
            "dynamodb:Query",
            "dynamodb:Scan"
          ],
          resources: [props.contactsTable.tableArn]
        })
      ]
    };

    super(scope, id, lambdaProps);

    this.addEventSource(
      new DynamoEventSource(props.infectionTable, {
        startingPosition: StartingPosition.TRIM_HORIZON,
        batchSize: 1,
        bisectBatchOnError: true,
        retryAttempts: 10
      })
    );
  }
}
