package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.apigateway.LambdaRestApi;
import software.amazon.awscdk.services.apigateway.LambdaRestApiProps;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.sqs.IQueue;
import software.amazon.awscdk.services.sqs.Queue;

/**
 * This example demonstrates how to write a custom construct called "QueueViewer".
 *
 * When defined in a stack, it accepts a queue input and exposes a public endpoint via API Gateway
 * that responds to HTTP GET requests with the current queue attributes.
 */
public class QueueViewerStack extends Stack {
    public QueueViewerStack(final App app, final String name) {
        super(app, name);

        // define the queue
        Queue queue = new Queue(this, "MyQueue");

        // define a queue viewer for this queue
        new QueueViewer(this, "MyQueueViewer", new QueueViewerProps() {
            public IQueue getQueue() {
                return queue;
            }
        });
    }

    /**
     * Input properties for {@link QueueViewer}.
     */
    public interface QueueViewerProps {
        /**
         * @return the queue to view.
         */
        IQueue getQueue();
    }

    /**
     * This is a reusable custom construct. It encapsulates the idea of exposing an HTTP endpoint
     * that responds with a JSON object that contains the current set of queue attributes for a given queue.
     *
     * Under the hood, it uses Amazon API Gateway and AWS Lambda.
     */
    public final class QueueViewer extends Construct {
        public QueueViewer(final Construct scope, final String id, final QueueViewerProps props) {
            super(scope, id);



            Function handler = new Function(this, "Handler", FunctionProps.builder()
                    .withCode(Code.asset("./lambda/queue-viewer"))
                    .withHandler("index.handler")
                    .withRuntime(Runtime.NODE_J_S810)
                    .build());

            new LambdaRestApi(this, "API", LambdaRestApiProps.builder()
                    .withHandler(handler)
                    .build());

            handler.addEnvironment("QUEUE_URL", props.getQueue().getQueueUrl());
            props.getQueue().grant(handler.getRole(), "sqs:GetQueueAttributes");
        }
    }
}
