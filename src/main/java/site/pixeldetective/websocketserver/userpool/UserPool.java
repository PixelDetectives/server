package site.pixeldetective.websocketserver.userpool;

import org.java_websocket.WebSocket;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserPool {
    // 사용자 정보를 저장하는 ConcurrentHashMap
    private static volatile ConcurrentHashMap<Integer, WebSocket> currentConnections = new ConcurrentHashMap<>();
    private static volatile  ConcurrentHashMap<Integer, CurrentUser> currentUsers = new ConcurrentHashMap<>();

    private static UserPool instance;


    private UserPool() {

    }
    public WebSocket getConnection(int sessionId) {
        return currentConnections.get(sessionId);
    }
    public static synchronized UserPool getInstance() {
        if (instance == null) {
            instance = new UserPool();
        }
        return instance;
    }

    // 사용자 추가
    public synchronized void addUser(int sessionId, WebSocket ws, CurrentUser currentUser) {
        currentConnections.put(sessionId, ws);
        currentUsers.put(sessionId, currentUser);
        broadCastData("currentUsers", getCurrentUsers());
    }

    // 사용자 조회
    public CurrentUser getUser(int sessionId) {
        return currentUsers.get(sessionId);
    }

    // 사용자 삭제
    public void removeUser(int sessionId) {
        currentConnections.remove(sessionId);
        currentUsers.remove(sessionId);
    }

    // 모든 사용자 조회
    // 리스트로 반환
//    public List<CurrentUser> getAllUsers() {
//        List<CurrentUser> currentUsers = new ArrayList<>();
//        for (CurrentUser currentUser : currentUsers) {
//            currentUsers.add(currentUser);
//        }
//        return currentUsers;
//    }

    public synchronized JSONArray getCurrentUsers() {
        JSONArray jsonArray = new JSONArray();
        for (CurrentUser currentUser : currentUsers.values()) {
            jsonArray.put(currentUser.toString());
        }
        return jsonArray;
    }
    // 현재 사용자 수 반환
    public synchronized JSONObject getUserCount() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("size", currentUsers.size());
        return jsonObject;
    }
    public synchronized JSONArray getCurrentUserList() {
        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<Integer, CurrentUser> entry : currentUsers.entrySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uId", entry.getValue().getuId());
            jsonObject.put("uName", entry.getValue().getuName());
            jsonArray.put(jsonObject);
        }

        return jsonArray;
    }
    public synchronized void removeCurrentUser(int sessionId) {
        currentConnections.remove(sessionId);
        currentUsers.remove(sessionId);
        broadCastData("currentUsers", getCurrentUsers());
    }
    public synchronized void currentUserStatusMatching(int sessionId, WebSocket ws) {
        CurrentUser currentUser = UserPool.getInstance().getUser(sessionId);
        removeCurrentUser(sessionId);
        currentUser.setStatus("Matching");
        addUser(sessionId, ws, currentUser);
    }

    public synchronized void currentUserStatusJoin(int sessionId, WebSocket ws) {
        CurrentUser currentUser = UserPool.getInstance().getUser(sessionId);
        removeCurrentUser(sessionId);
        currentUser.setStatus("Join");
        addUser(sessionId, ws, currentUser);
    }
    public synchronized void broadCastData(String type, JSONObject jsonObject) {
        JSONObject retObject = new JSONObject();
        retObject.put("type", type);
        retObject.put("data", jsonObject.toString());
        for (int sessionId : currentConnections.keySet()) {
            currentConnections.get(sessionId).send(retObject.toString());
        }
    }
    public synchronized void broadCastData(String type, JSONArray jsonArray) {
        JSONObject retObject = new JSONObject();
        retObject.put("type", type);
        retObject.put("data", jsonArray.toString());
        for (int sessionId : currentConnections.keySet()) {
            currentConnections.get(sessionId).send(retObject.toString());
        }
    }
}