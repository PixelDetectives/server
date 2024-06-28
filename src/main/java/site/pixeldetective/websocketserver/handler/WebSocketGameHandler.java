package site.pixeldetective.websocketserver.handler;

import org.java_websocket.WebSocket;
import org.json.JSONArray;
import org.json.JSONObject;
import site.pixeldetective.websocketserver.gameroom.GameRoom;
import site.pixeldetective.websocketserver.gameroom.GameRoomPool;
import site.pixeldetective.websocketserver.userpool.UserPool;

public class WebSocketGameHandler {

    public WebSocketGameHandler() {

    }

    public static boolean userCreateRoom(int sessionId, String roomName, int difficulty) {
        try {
            return GameRoomPool.getInstance().userCreateRoom(sessionId, roomName, difficulty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static void quickMatching(String webSocketId) {

    }

    public static JSONArray getCurrentRoomList() {
        return GameRoomPool.getInstance().getCurrentRoomList();
    }
    public static void braodCaseCurrentRoomList() {
        UserPool.getInstance().broadCastData("currentRooms", GameRoomPool.getInstance().getCurrentRoomList());
    }
    public static GameRoom getGameRoomByUId(String uId) {
        return GameRoomPool.getInstance().getGameRoomByUId(uId);
    }
    public static void removeGameRoomByUId(String uId) {
        GameRoomPool.getInstance().removeGameRoomByUId(uId);
    }
    public static GameRoom getGameRoomBySessionId1(int currentUserSessionId1) {
        return GameRoomPool.getInstance().getGameRoomBySessionId1(currentUserSessionId1);
    }
}
