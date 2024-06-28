package site.pixeldetective.websocketserver.gameroom;

import org.java_websocket.WebSocket;
import org.json.JSONArray;
import org.json.JSONObject;
import site.pixeldetective.websocketserver.userpool.UserPool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameRoomPool {

    private static GameRoomPool instance;
    private static ConcurrentHashMap<WebSocket, GameRoom> currentCreatedRooms = new ConcurrentHashMap<>();
    private GameRoomPool() {

    }
    public static synchronized GameRoomPool getInstance() {
        if (instance == null) {
            instance = new GameRoomPool();
        }
        return instance;
    }
    public static boolean userCreatRoom(WebSocket webSocketId, String roomName, int difficulty) {
        try {
            GameRoom gameRoom = new GameRoom();
            gameRoom.setRoomName(roomName);
            gameRoom.setCurrentUser1(UserPool.getInstance().getUser(webSocketId));

            // difficulty에 따른 문제 가져오기
            gameRoom.setImgURL1("https://sesac-projects-s3.s3.ap-northeast-2.amazonaws.com/image1.png");
            gameRoom.setImgURL2("https://sesac-projects-s3.s3.ap-northeast-2.amazonaws.com/image2.png");
            gameRoom.setTOTAL_HITS(5);
            currentCreatedRooms.put(webSocketId, gameRoom);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public static void userDeleteRoom(WebSocket webSocketId) {
        try {
            currentCreatedRooms.remove(webSocketId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static JSONArray getCurrentRoomList() {
        JSONArray jsonArray = new JSONArray();
        for (GameRoom gameRoom : currentCreatedRooms.values()) {
            JSONObject jsonObject = new JSONObject(gameRoom.toString());
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }
}
