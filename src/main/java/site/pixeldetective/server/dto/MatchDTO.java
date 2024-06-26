package site.pixeldetective.server.dto;


import java.sql.Timestamp;

/*
desc matches
m_num	int	NO	PRI		auto_increment
m_player1	int	YES
m_player2	int	YES
m_result	int	YES
m_start	date	YES
m_end	date	YES
* */
public class MatchDTO {
    private int mNum;
    private int mPlayer1;
    private int mPlayer2;
    private int mResult; // 플레이어1의 1:승   2:패  3:무
    private Timestamp mStart;
    private Timestamp mEnd;


    public int getmPlayer1() {
        return mPlayer1;
    }

    public MatchDTO(int mPlayer1, int mPlayer2, int mResult, Timestamp mStart, Timestamp mEnd) {
        this.mPlayer1 = mPlayer1;
        this.mPlayer2 = mPlayer2;
        this.mResult = mResult;
        this.mStart = mStart;
        this.mEnd = mEnd;
    }

    public MatchDTO(int mNum, int mPlayer1, int mPlayer2, int mResult, Timestamp mStart, Timestamp mEnd) {
        this.mNum = mNum;
        this.mPlayer1 = mPlayer1;
        this.mPlayer2 = mPlayer2;
        this.mResult = mResult;
        this.mStart = mStart;
        this.mEnd = mEnd;
    }

    public MatchDTO() {
    }


    @Override
    public String toString() {
        return "MatchDTO{" +
                "mNum=" + mNum +
                ", mPlayer1=" + mPlayer1 +
                ", mPlayer2=" + mPlayer2 +
                ", mResult=" + mResult +
                ", mStart='" + mStart + '\'' +
                ", mEnd='" + mEnd + '\'' +
                '}';
    }

    public int getmNum() {
        return mNum;
    }

    public void setmNum(int mNum) {
        this.mNum = mNum;
    }

    public void setmPlayer1(int mPlayer1) {
        this.mPlayer1 = mPlayer1;
    }

    public int getmPlayer2() {
        return mPlayer2;
    }

    public void setmPlayer2(int mPlayer2) {
        this.mPlayer2 = mPlayer2;
    }

    public int getmResult() {
        return mResult;
    }

    public void setmResult(int mResult) {
        this.mResult = mResult;
    }

    public Timestamp getmStart() {
        return mStart;
    }

    public void setmStart(Timestamp mStart) {
        this.mStart = mStart;
    }

    public Timestamp getmEnd() {
        return mEnd;
    }

    public void setmEnd(Timestamp mEnd) {
        this.mEnd = mEnd;
    }
}
