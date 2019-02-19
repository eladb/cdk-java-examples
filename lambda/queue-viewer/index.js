const { SQS } = require('aws-sdk');

exports.handler = async function handler(event, context) {
  console.log(JSON.stringify(event, undefined, 2));

  const sqs = new SQS();
  const response = await sqs.getQueueAttributes({
    QueueUrl: process.env.QUEUE_URL,
    AttributeNames: [ 'All' ]
  }).promise();

  return {
    statusCode: 200,
    body: JSON.stringify(response.Attributes, undefined, 2),
    headers: {
      'content-type': 'application/json'
    }
  };
};
