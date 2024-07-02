package site.pixeldetective.websocketserver.match;

import org.json.JSONObject;

public class Match {
    private int sessionId1;
    private int sessionId2;
    private JSONObject sessionId1Data;
    private JSONObject sessionId2Data;
    public Match() {

    }
    public int getSessionId1() {
        return sessionId1;
    }

    public void setSessionId1(int sessionId1) {
        this.sessionId1 = sessionId1;
    }

    public int getSessionId2() {
        return sessionId2;
    }

    public void setSessionId2(int sessionId2) {
        this.sessionId2 = sessionId2;
    }

    public JSONObject getSessionId1Data() {
        return sessionId1Data;
    }

    public void setSessionId1Data(JSONObject sessionId1Data) {
        this.sessionId1Data = sessionId1Data;
    }

    public JSONObject getSessionId2Data() {
        return sessionId2Data;
    }

    public void setSessionId2Data(JSONObject sessionId2Data) {
        this.sessionId2Data = sessionId2Data;
    }



}
