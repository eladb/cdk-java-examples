package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.CfnTag;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.dynamodb.TableProps;
import software.amazon.awscdk.services.ec2.CfnVPC;
import software.amazon.awscdk.services.ec2.CfnVPCProps;
import software.amazon.awscdk.services.events.EventRule;
import software.amazon.awscdk.services.events.EventRuleProps;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.Runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This stack defines a Lambda function that writes a random hash to a DynamoDB table every minute.
 */
public class RandomWriterStack extends Stack {
    public RandomWriterStack(final App parent, final String name) {
        super(parent, name);

        Table table = new Table(this, "Table", TableProps.builder()
                .withPartitionKey(Attribute.builder().withName("ID").withType(AttributeType.String).build())
                .build());

        Function fn = new Function(this, "Handler", FunctionProps.builder()
                .withRuntime(Runtime.NODE_J_S810)
                .withHandler("index.handler")
                .withCode(Code.asset("./lambda/random-writer"))
                .build());

        fn.addEnvironment("TABLE_NAME", table.getTableName());
        table.grantWriteData(fn.getRole());

        new EventRule(this, "Schedule", EventRuleProps.builder()
                .withScheduleExpression("rate(1 minute)")
                .withTargets(Collections.singletonList(fn))
                .build());
    }
}
