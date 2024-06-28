package site.pixeldetective.websocketserver.userpool;

import org.java_websocket.WebSocket;
import org.json.JSONArray;
import org.json.JSONObject;
import site.pixeldetective.websocketserver.handler.WebSocketGameHandler;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserPool {
    // 사용자 정보를 저장하는 ConcurrentHashMap
    private static final ConcurrentHashMap<WebSocket, CurrentUser> currentUsers = new ConcurrentHashMap<>();

    private static UserPool instance;

    private UserPool() {

    }
    public static synchronized UserPool getInstance() {
        if (instance == null) {
            instance = new UserPool();
        }
        return instance;
    }

    // 사용자 추가
    public synchronized void addUser(WebSocket webSocketId, CurrentUser currentUser) {
        currentUsers.put(webSocketId, currentUser);
        broadCastData("currentUsers", getCurrentUsers());
    }

    // 사용자 조회
    public CurrentUser getUser(WebSocket webSocketId) {
        return currentUsers.get(webSocketId);
    }

    // 사용자 삭제
    public void removeUser(WebSocket webSocketId) {
        currentUsers.remove(webSocketId);
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
    public synchronized int getUserCount() {
        return currentUsers.size();
    }
    public synchronized String getCurrentUserList() {
        StringBuilder sb = new StringBuilder();
        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<WebSocket, CurrentUser> entry : currentUsers.entrySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uId", entry.getValue().getuId());
            jsonObject.put("uName", entry.getValue().getuName());
            jsonArray.put(jsonObject);
        }
        sb.append(jsonArray);
        return sb.toString();
    }
    public synchronized void removeCurrentUser(WebSocket webSocketId) {
        currentUsers.remove(webSocketId);
        broadCastData("currentUsers", getCurrentUsers());
    }
    public synchronized void currentUserStatusMatching(WebSocket webSocketId) {
        CurrentUser currentUser = UserPool.getInstance().getUser(webSocketId);
        removeCurrentUser(webSocketId);
        currentUser.setStatus("Matching");
        addUser(webSocketId, currentUser);
    }

    public synchronized void currentUserStatusJoin(WebSocket webSocketId) {
        CurrentUser currentUser = UserPool.getInstance().getUser(webSocketId);
        removeCurrentUser(webSocketId);
        currentUser.setStatus("Join");
        addUser(webSocketId, currentUser);
    }
    public synchronized void broadCastData(String type, JSONObject jsonObject) {
        JSONObject retObject = new JSONObject();
        retObject.put("type", type);
        retObject.put("data", jsonObject.toString());
        for (WebSocket webSocketId : currentUsers.keySet()) {
            webSocketId.send(retObject.toString());
        }
    }
    public synchronized void broadCastData(String type, JSONArray jsonArray) {
        JSONObject retObject = new JSONObject();
        retObject.put("type", type);
        retObject.put("data", jsonArray.toString());
        for (WebSocket webSocketId : currentUsers.keySet()) {
            webSocketId.send(retObject.toString());
        }
    }
}