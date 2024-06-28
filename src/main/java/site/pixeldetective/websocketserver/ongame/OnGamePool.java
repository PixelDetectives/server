package site.pixeldetective.websocketserver.ongame;

import site.pixeldetective.websocketserver.userpool.UserPool;

import java.util.*;

public class OnGamePool {

    private static volatile OnGamePool instance;
    private static volatile Map<Integer, OnGame> onGamePool = new HashMap<>();

    // private 생성자로 외부에서 인스턴스 생성을 금지한다.
    private OnGamePool() {
        // 초기화 코드가 필요하면 추가
    }

    public static OnGamePool getInstance() {
        if (instance == null) {
            instance = new OnGamePool();
        }
        return instance;
    }
    public static OnGame createOnGame(int gameRoomId, int sessionId1, int sessionId2, int difficulty) {
        OnGame onGame = new OnGame(sessionId1, sessionId2, difficulty);
        onGamePool.put(gameRoomId, onGame);
        onGame.setOnGameId(gameRoomId);
        onGame.setImgURL1("https://sesac-projects-s3.s3.ap-northeast-2.amazonaws.com/image1.png");
        onGame.setImgURL2("https://sesac-projects-s3.s3.ap-northeast-2.amazonaws.com/image2.png");
        onGame.setTOTAL_HITS(5);
        onGame.setMyName(UserPool.getInstance().getUser(sessionId1).getuName());
        onGame.setOtherName(UserPool.getInstance().getUser(sessionId2).getuName());
        onGame.setDifficulty(difficulty);
        onGame.setStartTime(System.currentTimeMillis());
        Map<Integer,  List<Integer>> answers = new HashMap<>();
//      for (int i = 0; i < 4; i++) {
//      }
        answers.put(0, new ArrayList<>(Arrays.asList(217, 377, 130)));
        answers.put(1, new ArrayList<>(Arrays.asList(77, 87, 75)));
        answers.put(2, new ArrayList<>(Arrays.asList(388, 349, 75)));
        answers.put(3, new ArrayList<>(Arrays.asList(37, 413, 40)));
        onGame.setAnswers(answers);
        return onGame;
    }
    public static OnGame getOnGameByOnGameId(int onGameId) {
        return onGamePool.get(onGameId);
    }
}
