package site.pixeldetective.websocketserver.ongame;

import org.json.JSONArray;
import org.json.JSONObject;
import site.pixeldetective.websocketserver.handler.OnGameHandler;
import site.pixeldetective.websocketserver.userpool.UserPool;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class OnGamePool {

    private static volatile OnGamePool instance;
    private static volatile Map<Integer, OnGame> onGamePool = new HashMap<>();

    private OnGamePool() {

    }

    public static OnGamePool getInstance() {
        if (instance == null) {
            instance = new OnGamePool();
            onGamePool = new HashMap<>();
        }
        return instance;
    }
    public static OnGame createOnGame(int sessionId, JSONObject jsonObject) {
        OnGame onGame = new OnGame();
        onGame.setOnGameId(sessionId);
        onGame.setImgURL1(jsonObject.getString("gImage1"));
        onGame.setImgURL2(jsonObject.getString("gImage2"));
        JSONArray answersArray = jsonObject.getJSONArray("answers");
        Map<Integer, List<Integer>> answers = new HashMap<>();
        for (int i = 0; i < answersArray.length(); i++) {
            List<Integer> answer = new ArrayList<>();
            answer.add(answersArray.getJSONObject(i).getInt("aX"));
            answer.add(answersArray.getJSONObject(i).getInt("aY"));
            answer.add(answersArray.getJSONObject(i).getInt("aRadius"));
            answers.put(i, answer);
        }
        onGame.setAnswers(answers);
        onGame.setTOTAL_HITS(answers.size());
        onGame.setMyName(UserPool.getInstance().getUser(sessionId).getuName());
        onGame.setDifficulty(jsonObject.getInt("gDifficulty"));
        onGame.setStartTime(System.currentTimeMillis());
        List<Boolean> answerMark = new ArrayList<>();
        for (int i = 0; i < onGame.getTOTAL_HITS(); i++) {
            answerMark.add(false);
        }
        onGame.setAnswerMark(answerMark);
        onGamePool.put(sessionId, onGame);
        return onGame;
    }
    public static OnGame getOnGameByOnGameId(int onGameId) {
        return onGamePool.get(onGameId);
    }

    public static JSONObject checkAndReturnResult(int mySessionId, int otherSessionId, int x1, int y1) {

        OnGame myOngame = getOnGameByOnGameId(mySessionId);
        OnGame otherOngame = getOnGameByOnGameId(otherSessionId);

        Map<Integer, List<Integer>> answers = myOngame.getAnswers();
        JSONObject jsonObject = new JSONObject();
        double minDist = 10000000.0;
        int minIdx = -1;

        for (int idx : answers.keySet()) {
            List<Integer> answer = answers.get(idx);
            int x2 = answer.get(0);
            int y2 = answer.get(1);
            double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
            if (distance< answer.get(2)) {
                if (distance < minDist) {
                    minDist = distance;
                    minIdx = idx;
                }
            }
        }

        if (minIdx != -1 && myOngame.getAnswerMark().get(minIdx) == true) {
            jsonObject.put("status", "none");
        } else if (minIdx == -1) {
            onGamePool.get(mySessionId).setUserMiss(onGamePool.get(mySessionId).getUserMiss() + 1);
            jsonObject.put("status", "miss");
        } else {
            onGamePool.get(mySessionId).setUserHits(onGamePool.get(mySessionId).getUserHits() + 1);
            jsonObject.put("status", "hits");
            jsonObject.put("correctIdx", minIdx);
            otherOngame.getAnswerMark().set(minIdx, true);
            myOngame.getAnswerMark().set(minIdx, true);
        }
        jsonObject.put("currUserName", UserPool.getInstance().getUser(mySessionId).getuName());
        return jsonObject;
    }
    public void deleteOnGamePool(int sessionId) {
        onGamePool.remove(sessionId);
    }
}
