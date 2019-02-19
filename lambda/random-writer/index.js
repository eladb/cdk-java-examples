const { DynamoDB } = require('aws-sdk');
const crypto = require('crypto');

exports.handler = async function handler(event, context) {
  console.log(JSON.stringify(event, undefined, 2));

  var seed = `${Date.now}${Math.random()}`;
  const id = crypto.createHash('sha1').update(seed).digest('hex');

  const ddb = new DynamoDB();
  await ddb.putItem({
    TableName: process.env.TABLE_NAME,
    Item: {
      ID: { S: id }
    }
  }).promise();
};
