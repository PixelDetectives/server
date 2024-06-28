package site.pixeldetective.websocketserver.gameroom;

import org.java_websocket.WebSocket;
import org.json.JSONArray;
import org.json.JSONObject;
import site.pixeldetective.websocketserver.userpool.CurrentUser;
import site.pixeldetective.websocketserver.userpool.UserPool;

import java.util.concurrent.ConcurrentHashMap;

public class GameRoomPool {

    private static GameRoomPool instance;
    private final ConcurrentHashMap<Integer, GameRoom> currentCreatedRooms = new ConcurrentHashMap<>();

    private GameRoomPool() {
    }

    public static synchronized GameRoomPool getInstance() {
        if (instance == null) {
            instance = new GameRoomPool();
        }
        return instance;
    }

    public boolean userCreateRoom(int sessionId, String roomName, int difficulty) {
        try {
            GameRoom gameRoom = new GameRoom();
            gameRoom.setRoomName(roomName);
            gameRoom.setCurrentUser1(sessionId);
            currentCreatedRooms.put(sessionId, gameRoom);
            System.out.println(gameRoom.toString());
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void userDeleteRoom(WebSocket webSocketId) {
        try {
            currentCreatedRooms.remove(webSocketId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public JSONArray getCurrentRoomList() {
        JSONArray jsonArray = new JSONArray();
        for (GameRoom gameRoom : currentCreatedRooms.values()) {
            JSONObject jsonObject = new JSONObject(gameRoom.toString());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }
    public GameRoom getGameRoomByUId(String uId) {
        GameRoom gr = null;
        for (GameRoom gameRoom : currentCreatedRooms.values()) {
            if (uId.equals(UserPool.getInstance().getUser(gameRoom.getCurrentUser1()).getuId())) {
                return gameRoom;
            }
        }
        return gr;
    }
    public void removeGameRoomByUId(String uId) {
        int sId = -1;
        for (int sessionId : currentCreatedRooms.keySet()) {
            if (uId.equals(UserPool.getInstance().getUser(currentCreatedRooms.get(sessionId).getCurrentUser1()).getuId())) {
                sId = sessionId;
            }
        }
        currentCreatedRooms.remove(sId);
    }
}