package site.pixeldetective.websocketserver.handler;

import org.java_websocket.WebSocket;
import org.json.JSONArray;
import org.json.JSONObject;
import site.pixeldetective.websocketserver.userpool.UserPool;

public class WebSocketHandler {

    private WebSocketHandler() {

    }

    public static void sayHello() {
        System.out.println("Say Hello");
    }

    public static JSONObject getUserCount() {
        return UserPool.getInstance().getUserCount();
    }
    public static JSONArray getCurrentUserList() {
        return UserPool.getInstance().getCurrentUserList();
    }
    public static void removeCurrentUser(int sessionId) {
        UserPool.getInstance().removeCurrentUser(sessionId);
    }
    public static synchronized void currentUserStatusMatching(int sessionId, WebSocket conn) {
        UserPool.getInstance().currentUserStatusMatching(sessionId, conn);
    }
    public static synchronized void currentUserStatusJoin(int sessionId, WebSocket conn) {
        UserPool.getInstance().currentUserStatusJoin(sessionId, conn);
    }

}

