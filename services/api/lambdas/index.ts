import { Callback, Context, DynamoDBStreamEvent } from "aws-lambda";

export function newInfectionRecorded(
  event: DynamoDBStreamEvent,
  context: Context,
  callback: Callback
) {
  event.Records.forEach(record => {
    console.log("Stream record: ", JSON.stringify(record, null, 2));

    if (record.eventName == "INSERT") {
      // will do something with this
      console.log("ADD");
    }
    if (record.eventName == "REMOVE") {
      // do something when its removed
      console.log("REMOVE");
    }
  });
  callback(null, `Successfully processed ${event.Records.length} records.`);
}
