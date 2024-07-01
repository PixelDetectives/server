package site.pixeldetective.websocketserver.ongame;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OnGame {

    private int onGameId;


    public Map<Integer, List<Integer>> answers;
    private String myName;
    private String otherName;

    private String imgURL1;
    private String imgURL2;
    private int TOTAL_HITS;



    private int userHits = 0;
    private int userMiss = 0;

    private int difficulty;

    private long startTime;

    private List<Boolean> answerMark;
    public OnGame() {

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



    public int getOnGameId() {
        return onGameId;
    }

    public void setOnGameId(int onGameId) {
        this.onGameId = onGameId;
    }



    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
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
    public List<Boolean> getAnswerMark() {
        return answerMark;
    }

    public void setAnswerMark(List<Boolean> answerMark) {
        this.answerMark = answerMark;
    }
    public int getUserHits() {
        return userHits;
    }

    public void setUserHits(int userHits) {
        this.userHits = userHits;
    }

    public int getUserMiss() {
        return userMiss;
    }

    public void setUserMiss(int userMiss) {
        this.userMiss = userMiss;
    }
    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("onGameId", onGameId);
        jsonObject.put("answers", answers);
        jsonObject.put("myName", myName);
        jsonObject.put("otherName", otherName);
        jsonObject.put("imgURL1", imgURL1);
        jsonObject.put("imgURL2", imgURL2);
        jsonObject.put("TOTAL_HITS", TOTAL_HITS);
        jsonObject.put("userHits", userHits);
        jsonObject.put("userMiss", userMiss);
        jsonObject.put("difficulty", difficulty);
        jsonObject.put("onGameId", onGameId);
        jsonObject.put("startTime", startTime);
        return jsonObject.toString();
    }
}
