package site.pixeldetective.websocketserver.match;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MatchPool {
    private static MatchPool instance;

    private static Map<Integer, JSONObject> matchPool;

    private MatchPool() {
    }

    public static MatchPool getInstance() {
        if (instance == null) {
            synchronized (MatchPool.class) {
                if (instance == null) {
                    instance = new MatchPool();
                    matchPool = new HashMap<>();
                }
            }
        }
        return instance;
    }
    public JSONObject getOtherData(int sessionId) {
        return matchPool.get(sessionId);
    }
    public void setMyData(int sessionId, JSONObject jsonObject) {
        matchPool.put(sessionId, jsonObject);
    }
    public boolean isSessionIdInMatchPool(int sessionId) {
        return matchPool.containsKey(sessionId);
    }

    public void delete (int sessionId1, int sessionId2) {
        matchPool.remove(sessionId1);
        matchPool.remove(sessionId2);
    }
}