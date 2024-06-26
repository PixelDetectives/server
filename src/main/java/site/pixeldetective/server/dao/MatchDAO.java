package site.pixeldetective.server.dao;

import site.pixeldetective.server.db.DBConnector;
import site.pixeldetective.server.dto.MatchDTO;
import site.pixeldetective.server.dto.TestDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchDAO {
    private static Connection conn = DBConnector.getConnection();

    public MatchDAO() {
    }


    /**
     *
     * @param search = 경기 기록 m_player1의 경기기록
     * @return
     */
    public List<MatchDTO> getAllMatches(int search) {
        List<MatchDTO> matchesList = new ArrayList<MatchDTO>();
        String sql = "select * from matches where m_player1 = " + search;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int mNum = rs.getInt(1);
                int mPlayer1 = rs.getInt(2);
                int mPlayer2 = rs.getInt(3);
                int mResult = rs.getInt(4);
                Date mStart = rs.getDate(5);
                Date mEnd = rs.getDate(6);
                matchesList.add(new MatchDTO(mNum,mPlayer1, mPlayer2,mResult,mStart,mEnd));
                System.out.println("matchesList 출력합니다.~~~~~~");
                System.out.println(matchesList.toString());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return matchesList;
    }

    /**
     *        결기결과의 반대방향인 createMatchReverse를 내부에서 호출한다.
     *        즉 직접
     * @param matchDTO 경기결과
     * @return
     */
    public int createMatch(MatchDTO matchDTO) {
        createMatchReverse(matchDTO);
        String sql1 = "insert into matches values(null,?,?,?,?,?);";
        try (PreparedStatement pstmt = conn.prepareStatement(sql1)) {
            pstmt.setInt(1, matchDTO.getmPlayer1());
            pstmt.setInt(2, matchDTO.getmPlayer2());
            pstmt.setInt(3, matchDTO.getmResult());
            pstmt.setDate(4, matchDTO.getmStart());
            pstmt.setDate(5, matchDTO.getmEnd());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // 실제로는 createMatch에서만 호출될 메소드이다. 밖에서 호출하면 안 됨
    private int createMatchReverse(MatchDTO matchDTO) {
        String sql1 = "insert into matches values(null,?,?,?,?,?);";
        try (PreparedStatement pstmt = conn.prepareStatement(sql1)) {
            pstmt.setInt(2, matchDTO.getmPlayer1());
            pstmt.setInt(1, matchDTO.getmPlayer2());
            pstmt.setInt(3, matchDTO.getmResult());
            pstmt.setDate(4, matchDTO.getmStart());
            pstmt.setDate(5, matchDTO.getmEnd());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
