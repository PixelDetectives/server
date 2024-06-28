package site.pixeldetective.websocketserver.ongame;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class OnGame {

    private int onGameId;

    private int sessionId1;
    private int sessionId2;

    public Map<Integer, List<Integer>> answers;
    private String myName;
    private String otherName;

    private String imgURL1;
    private String imgURL2;
    private int TOTAL_HITS;
    private int user1Hits = 0;
    private int user1Miss = 0;

    private int user2Hits = 0;

    private int user2Miss = 0;

    private int difficulty;



    private long startTime;
    public OnGame() {

    }
    public OnGame(int sessionId1, int sessionId2, int difficulty) {
        this.sessionId1 = sessionId1;
        this.sessionId2 = sessionId2;
        this.difficulty = difficulty;
    }
    public String getImgURL1() {
        return imgURL1;
    }

    public void setImgURL1(String imgURL1) {
        this.imgURL1 = imgURL1;
    }

    public String getImgURL2() {
        return imgURL2;
    }

    public void setImgURL2(String imgURL2) {
        this.imgURL2 = imgURL2;
    }

    public int getTOTAL_HITS() {
        return TOTAL_HITS;
    }

    public void setTOTAL_HITS(int TOTAL_HITS) {
        this.TOTAL_HITS = TOTAL_HITS;
    }

    public int getUser1Hits() {
        return user1Hits;
    }

    public void setUser1Hits(int user1Hits) {
        this.user1Hits = user1Hits;
    }

    public int getUser1Miss() {
        return user1Miss;
    }

    public void setUser1Miss(int user1Miss) {
        this.user1Miss = user1Miss;
    }

    public int getUser2Hits() {
        return user2Hits;
    }

    public void setUser2Hits(int user2Hits) {
        this.user2Hits = user2Hits;
    }
    public int getOnGameId() {
        return onGameId;
    }

    public void setOnGameId(int onGameId) {
        this.onGameId = onGameId;
    }


    public int getSessionId1() {
        return sessionId1;
    }

    public void setSessionId1(int sessionId1) {
        this.sessionId1 = sessionId1;
    }



    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
    public int getSessionId2() {
        return sessionId2;
    }

    public void setSessionId2(int sessionId2) {
        this.sessionId2 = sessionId2;
    }

    public int getUser2Miss() {
        return user2Miss;
    }

    public void setUser2Miss(int user2Miss) {
        this.user2Miss = user2Miss;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }
    public Map<Integer, List<Integer>> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Integer, List<Integer>> answers) {
        this.answers = answers;
    }
    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId1", sessionId1);
        jsonObject.put("sessionId2", sessionId2);
        jsonObject.put("answers", answers);
        jsonObject.put("myName", myName);
        jsonObject.put("otherName", otherName);
        jsonObject.put("imgURL1", imgURL1);
        jsonObject.put("imgURL2", imgURL2);
        jsonObject.put("TOTAL_HITS", TOTAL_HITS);
        jsonObject.put("user1Hits", user1Hits);
        jsonObject.put("user1Miss", user1Miss);
        jsonObject.put("user2Hits", user2Hits);
        jsonObject.put("user2Miss", user2Miss);
        jsonObject.put("difficulty", difficulty);
        jsonObject.put("onGameId", onGameId);
        jsonObject.put("startTime", startTime);
        return jsonObject.toString();
    }
}
