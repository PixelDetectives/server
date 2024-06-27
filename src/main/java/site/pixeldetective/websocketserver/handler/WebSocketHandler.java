package site.pixeldetective.websocketserver.handler;

import site.pixeldetective.websocketserver.userpool.UserPool;

public class WebSocketHandler {
    private WebSocketHandler() {

    }

    public static void sayHello() {
        System.out.println("Say Hello");
    }

    public static int getUserCount() {

        return UserPool.getInstance().getUserCount();
    }
}
