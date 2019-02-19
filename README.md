## Java CDK Examples

This repo contains a bunch of CDK examples in Java. We will merge it into the aws-cdk-examples repo shortly.

Each example is defined in a separate stack:

- **RandomWriterStack**: This example defines a Lambda function that writes a random hash to a DynamoDB table every minute.
- **GhostStack**: This example defines a load-balanced Fargate ECS cluster which hosts a single instance of the "ghost" blogging platform (https://ghost.org)
- **QueueViewerStack**: This example demonstrates how to write a custom construct called "QueueViewer". It accepts a queue input and exposes a public endpoint via API Gateway that responds to HTTP GET requests with the current queue attributes.

