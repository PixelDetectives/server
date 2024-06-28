package site.pixeldetective.websocketserver.userpool;

import org.json.JSONObject;

public class CurrentUser {
    private int uId;
    private String uName;

    // Join : 현재 로그인 한 유저
    // Matching : 현재 매칭을 찾고 있거나 Room을 create하여 대기하고 있는 유저
    //
    private String status;
    public CurrentUser() {

    }
    public CurrentUser(int uId, String uName, String status) {
        this.uId = uId;
        this.uName = uName;
        this.status = status;
    }
    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uId", this.uId);
        jsonObject.put("uName", this.uName);
        jsonObject.put("status", this.status);
        return jsonObject.toString();
    }
}
