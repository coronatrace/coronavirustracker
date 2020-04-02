import { Stack, Construct, StackProps, CfnOutput, Fn } from "@aws-cdk/core";
import {
  CfnIdentityPool,
  CfnIdentityPoolRoleAttachment,
  UserPool,
  UserPoolClient
} from "@aws-cdk/aws-cognito";
import {
  GraphQLApi,
  FieldLogLevel,
  MappingTemplate,
  KeyCondition,
  CfnGraphQLApi
} from "@aws-cdk/aws-appsync";
import { join } from "path";
import {
  Table,
  BillingMode,
  AttributeType,
  StreamViewType
} from "@aws-cdk/aws-dynamodb";
import {
  Role,
  FederatedPrincipal,
  PolicyStatement,
  Effect
} from "@aws-cdk/aws-iam";
import { readFileSync } from "fs";
import { NewInfectionsRecordedFunc } from "./newInfectionFunction";
import { StringParameter } from "@aws-cdk/aws-ssm";
export class ApiStack extends Stack {
  constructor(scope: Construct, id: string, props?: StackProps) {
    super(scope, id, props);

    /**
     * API
     */

    const api = new GraphQLApi(this, "api", {
      name: `api`,
      logConfig: {
        fieldLogLevel: FieldLogLevel.ALL
      },
      schemaDefinitionFile: join(__dirname, "../schemas/schema.graphql")
    });
    const cfnApi = api.node.defaultChild as CfnGraphQLApi;
    cfnApi.authenticationType = "AWS_IAM";

    /**
     * Identity pool
     */
    const userPool = new UserPool(this, "users", {
      userPoolName: "users"
    });

    const userPoolClient = new UserPoolClient(this, "userPoolClient", {
      userPool
    });

    const identityPool = new CfnIdentityPool(this, "identityPool", {
      allowUnauthenticatedIdentities: true,
      identityPoolName: "identityPool",
      cognitoIdentityProviders: [
        {
          clientId: userPoolClient.userPoolClientId,
          providerName: userPool.userPoolProviderName
        }
      ]
    });

    const unauthenticatedIdentityPoolRole = new Role(
      this,
      "unauthenticatedIdentityPoolRole",
      {
        assumedBy: new FederatedPrincipal(
          "cognito-identity.amazonaws.com",
          {
            StringEquals: {
              "cognito-identity.amazonaws.com:aud": identityPool.ref
            },
            "ForAnyValue:StringLike": {
              "cognito-identity.amazonaws.com:amr": "unauthenticated"
            }
          },
          "sts:AssumeRoleWithWebIdentity"
        )
      }
    );

    new CfnIdentityPoolRoleAttachment(this, "identityPoolRoleAttachment", {
      identityPoolId: identityPool.ref,
      roles: {
        unauthenticated: unauthenticatedIdentityPoolRole.roleArn,
        authenticated: unauthenticatedIdentityPoolRole.roleArn
      }
    });

    unauthenticatedIdentityPoolRole.addToPolicy(
      new PolicyStatement({
        effect: Effect.ALLOW,
        actions: ["appsync:GraphQL"],
        resources: [api.arn, Fn.join("", [api.arn, "/*"])]
      })
    );

    /**
     * Data
     */

    const contactTable = new Table(this, "contactTable", {
      billingMode: BillingMode.PAY_PER_REQUEST,
      partitionKey: {
        name: "userId",
        type: AttributeType.STRING
      },
      sortKey: {
        name: "contactTimestampContactUserId",
        type: AttributeType.STRING
      }
    });

    const infectionTable = new Table(this, "infectionTable", {
      billingMode: BillingMode.PAY_PER_REQUEST,
      partitionKey: {
        name: "id",
        type: AttributeType.STRING
      },
      stream: StreamViewType.NEW_IMAGE
    });

    /**
     * Resolvers & data sources
     */

    // Contact
    const contactDataSource = api.addDynamoDbDataSource(
      "Contact",
      "The contact data source",
      contactTable
    );

    const createContactResolverVtl = readFileSync(
      join(__dirname, "../resolvers/createContact.vtl"),
      "utf8"
    );
    contactDataSource.createResolver({
      typeName: "Mutation",
      fieldName: "createContact",
      requestMappingTemplate: MappingTemplate.fromString(
        createContactResolverVtl
      ),
      responseMappingTemplate: MappingTemplate.dynamoDbResultItem()
    });

    // Infection
    const infectionDataSource = api.addDynamoDbDataSource(
      "Infection",
      "The infection data source",
      infectionTable
    );

    infectionDataSource.createResolver({
      typeName: "Query",
      fieldName: "getInfection",
      requestMappingTemplate: MappingTemplate.dynamoDbGetItem("id", "id"),
      responseMappingTemplate: MappingTemplate.dynamoDbResultItem()
    });

    infectionDataSource.createResolver({
      typeName: "Query",
      fieldName: "getInfectionsByUser",
      requestMappingTemplate: MappingTemplate.dynamoDbQuery(
        KeyCondition.eq("userId", "userId")
      ),
      responseMappingTemplate: MappingTemplate.dynamoDbResultList()
    });

    const createInfectionResolverVtl = readFileSync(
      join(__dirname, "../resolvers/createInfection.vtl"),
      "utf8"
    );
    infectionDataSource.createResolver({
      typeName: "Mutation",
      fieldName: "createInfection",
      requestMappingTemplate: MappingTemplate.fromString(
        createInfectionResolverVtl
      ),
      responseMappingTemplate: MappingTemplate.dynamoDbResultItem()
    });

    const deleteInfectionResolverVtl = readFileSync(
      join(__dirname, "../resolvers/deleteInfection.vtl"),
      "utf8"
    );
    infectionDataSource.createResolver({
      typeName: "Mutation",
      fieldName: "deleteInfection",
      requestMappingTemplate: MappingTemplate.fromString(
        deleteInfectionResolverVtl
      ),
      responseMappingTemplate: MappingTemplate.dynamoDbResultItem()
    });

    new NewInfectionsRecordedFunc(this, "newInfectionRecordedFunction", {
      contactsTable: contactTable,
      infectionTable: infectionTable
    });

    // Add the names of the tables to SSM Param Store
    new StringParameter(this, "DynamoDBInfectionsTableName", {
      stringValue: infectionTable.tableName,
      description: "The name of the infection table on DynamoDB",
      parameterName: "/dynamoDb/tables/infections"
    });

    new StringParameter(this, "DynamoDBContactsTableName", {
      stringValue: infectionTable.tableName,
      description: "The name of the contacts table on DynamoDB",
      parameterName: "/dynamoDb/tables/contacts"
    });

    /**
     * Outputs
     */

    new CfnOutput(this, "userPoolId", {
      value: userPool.userPoolId,
      exportName: "userPoolId"
    });

    new CfnOutput(this, "userPoolClientId", {
      value: userPoolClient.userPoolClientId,
      exportName: "userPoolClientId"
    });

    new CfnOutput(this, "identityPoolId", {
      value: identityPool.ref,
      exportName: "identityPoolId"
    });

    new CfnOutput(this, "apiUrl", {
      value: api.graphQlUrl,
      exportName: "apiUrl"
    });

    new CfnOutput(this, "stackRegion", {
      value: this.region,
      exportName: "stackRegion"
    });
  }
}
