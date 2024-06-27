package site.pixeldetective.server.dto;

/*
    r_num	r_player1(u_num)	r_player2(u_num)	r_name	r_difficulty	g_num
*/
public class RoomDTO {
    private int r_roomID;
    private int u_num_player1;
    private int u_num_player2;
    private String r_name;
    private String r_difficulty;
    private int g_num;

    public RoomDTO(int r_roomID, int u_num_player1, int u_num_player2, String r_name, String r_difficulty, int g_num) {
        this.r_roomID = r_roomID;
        this.u_num_player1 = u_num_player1;
        this.u_num_player2 = u_num_player2;
        this.r_name = r_name;
        this.r_difficulty = r_difficulty;
        this.g_num = g_num;
    }

    public RoomDTO(int u_num_player1, int u_num_player2, String r_name, String r_difficulty, int g_num) {
        this.u_num_player1 = u_num_player1;
        this.u_num_player2 = u_num_player2;
        this.r_name = r_name;
        this.r_difficulty = r_difficulty;
        this.g_num = g_num;
    }

    public RoomDTO() {
    }

    public int getR_roomID() {
        return r_roomID;
    }
    public void setR_roomID(int r_roomID) {
        this.r_roomID = r_roomID;
    }
    public int getU_num_player1() {
        return u_num_player1;
    }
    public void setU_num_player1(int u_num_player1) {
        this.u_num_player1 = u_num_player1;
    }
    public int getU_num_player2() {
        return u_num_player2;
    }
    public void setU_num_player2(int u_num_player2) {
        this.u_num_player2 = u_num_player2;
    }
    public String getR_name() {
        return r_name;
    }
    public void setR_name(String r_name) {
        this.r_name = r_name;
    }
    public String getR_difficulty() {
        return r_difficulty;
    }
    public void setR_difficulty(String r_difficulty) {
        this.r_difficulty = r_difficulty;
    }
    public int getG_num() {
        return g_num;
    }
    public void setG_num(int g_num) {
        this.g_num = g_num;
    }
}
