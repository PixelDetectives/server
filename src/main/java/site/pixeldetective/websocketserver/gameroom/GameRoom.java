package site.pixeldetective.websocketserver.gameroom;

import site.pixeldetective.websocketserver.userpool.CurrentUser;

public class GameRoom {


    private String roomName;
    private String user1WebSocketId;
    private String user2WebSocketId;

    // 첫 번째로 매칭된 사람이 user1
    private CurrentUser currentUser1;

    // 나중에 매칭된 사람이 user2
    private CurrentUser currentUser2;



    private String imgURL1;
    private String imgURL2;
    private int TOTAL_HITS;
    private int user1Hits;
    private int user1Miss;

    private int user2Hits;
    private int userr2Miss;

    public GameRoom() {

    }




    public String getUser1WebSocketId() {
        return user1WebSocketId;
    }

    public void setUser1WebSocketId(String user1WebSocketId) {
        this.user1WebSocketId = user1WebSocketId;
    }

    public String getUser2WebSocketId() {
        return user2WebSocketId;
    }

    public void setUser2WebSocketId(String user2WebSocketId) {
        this.user2WebSocketId = user2WebSocketId;
    }

    public CurrentUser getCurrentUser1() {
        return currentUser1;
    }

    public void setCurrentUser1(CurrentUser currentUser1) {
        this.currentUser1 = currentUser1;
    }

    public CurrentUser getCurrentUser2() {
        return currentUser2;
    }

    public void setCurrentUser2(CurrentUser currentUser2) {
        this.currentUser2 = currentUser2;
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

    public int getUserr2Miss() {
        return userr2Miss;
    }

    public void setUserr2Miss(int userr2Miss) {
        this.userr2Miss = userr2Miss;
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

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

}
