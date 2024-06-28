package site.pixeldetective.websocketserver.gameroom;

import org.java_websocket.WebSocket;
import org.json.JSONObject;
import site.pixeldetective.websocketserver.userpool.CurrentUser;
import site.pixeldetective.websocketserver.userpool.UserPool;

public class GameRoom {
    private String roomName;

    private int difficulty;

    // 첫 번째로 매칭된 사람이 user1
    private int currentUserSessionId1;

    // 나중에 매칭된 사람이 user2
    private int currentUserSessionId2;

    public GameRoom() {

    }

    public int getCurrentUser1() {
        return currentUserSessionId1;
    }

    public void setCurrentUser1(int sessionId) {
        this.currentUserSessionId1 = sessionId;
    }

    public int getCurrentUser2() {
        return currentUserSessionId2;
    }

    public void setCurrentUser2(int sessionId) {
        this.currentUserSessionId2 = sessionId;
    }

    public String getRoomName() {

        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("r_id", currentUserSessionId1);
        jsonObject.put("r_name", roomName);
        jsonObject.put("r_difficulty", difficulty);
        return jsonObject.toString();
    }
}
