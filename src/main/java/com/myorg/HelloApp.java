package com.myorg;

import software.amazon.awscdk.App;

public class HelloApp {
    public static void main(final String argv[]) {
        App app = new App();

        new RandomWriterStack(app, "random-writer");
        new GhostStack(app, "ghost");
        new QueueViewerStack(app, "queue-viewer");

        app.run();
    }
}
