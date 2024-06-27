package site.pixeldetective.websocketserver.userpool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserPool {
    // 사용자 정보를 저장하는 ConcurrentHashMap
    private static final ConcurrentHashMap<String, Integer> userMap = new ConcurrentHashMap<>();

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
    public void addUser(String userId, Integer id) {

        userMap.put(userId, id);
    }

    // 사용자 조회
    public Integer getUser(String userId) {
        return userMap.get(userId);
    }

    // 사용자 삭제
    public void removeUser(String userId) {
        userMap.remove(userId);
    }

    // 모든 사용자 조회
    public Map<String, Integer> getAllUsers() {
        return new ConcurrentHashMap<>(userMap);
    }

    // 현재 사용자 수 반환
    public int getUserCount() {
        return userMap.size();
    }
}