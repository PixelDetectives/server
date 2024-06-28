package site.pixeldetective.websocketserver.handler;

import org.java_websocket.WebSocket;
import org.json.JSONObject;
import site.pixeldetective.websocketserver.gameroom.GameRoomPool;
import site.pixeldetective.websocketserver.userpool.UserPool;

public class WebSocketGameHandler {

    public WebSocketGameHandler() {

    }

    public static boolean userCreateRoom(WebSocket webSocketId, String roomName, int difficulty) {
        try {
            return GameRoomPool.getInstance().userCreatRoom(webSocketId, roomName, difficulty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static void quickMatching(String webSocketId) {

    }

    public static void getCurrentRoomList() {
        UserPool.getInstance().broadCastData("currentRooms", GameRoomPool.getInstance().getCurrentRoomList());
    }
}
