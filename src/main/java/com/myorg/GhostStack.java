package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.ec2.VpcNetwork;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.ClusterProps;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.LoadBalancedFargateService;
import software.amazon.awscdk.services.ecs.LoadBalancedFargateServiceProps;

/**
 * This example defines a load-balanced Fargate ECS cluster which hosts a single instance of the "ghost"
 * blogging platform (https://ghost.org)
 */
public class GhostStack extends Stack {
    public GhostStack(final App app, final String name) {
        super(app, name);

        VpcNetwork vpc = new VpcNetwork(this, "VPC");

        Cluster cluster = new Cluster(this, "Cluster", ClusterProps.builder()
                .withVpc(vpc)
                .build());

        new LoadBalancedFargateService(this, "Service", LoadBalancedFargateServiceProps.builder()
                .withCluster(cluster)
                .withImage(ContainerImage.fromDockerHub("ghost"))
                .withContainerPort(2368)
                .build());
    }
}
