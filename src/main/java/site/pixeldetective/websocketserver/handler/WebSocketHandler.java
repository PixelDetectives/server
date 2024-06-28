package site.pixeldetective.websocketserver.handler;

import org.java_websocket.WebSocket;
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
    public static String getCurrentUserList() {
        return UserPool.getInstance().getCurrentUserList();
    }
    public static void removeCurrentUser(WebSocket webSocketId) {
        UserPool.getInstance().removeCurrentUser(webSocketId);
    }
    public static synchronized void currentUserStatusMatching(WebSocket webSocketId) {
        UserPool.getInstance().currentUserStatusMatching(webSocketId);
    }
    public static synchronized void currentUserStatusJoin(WebSocket webSocketId) {
        UserPool.getInstance().currentUserStatusJoin(webSocketId);
    }
    public static synchronized void broadCastring() {

    }
}

