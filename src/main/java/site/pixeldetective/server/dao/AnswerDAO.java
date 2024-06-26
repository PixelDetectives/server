package site.pixeldetective.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import site.pixeldetective.server.db.DBConnector;
import site.pixeldetective.server.dto.AnswerDTO;
import site.pixeldetective.server.dto.GameDTO;

public class AnswerDAO {
    private static Connection conn = DBConnector.getConnection();

    public List<AnswerDTO> selectAnswer(int g_num) throws SQLException {
        List<AnswerDTO> answers = new ArrayList<>();
        String sql = "SELECT * FROM answer WHERE g_num = ?";

        try{
        	PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, g_num);
            ResultSet rs = pstmt.executeQuery();
            
                while (rs.next()) {
                    int a_num = rs.getInt("a_num");
                    int a_radius = rs.getInt("a_radius");
                    int a_x = rs.getInt("a_x");
                    int a_y = rs.getInt("a_y");

                    AnswerDTO answer = new AnswerDTO(a_num, g_num, a_radius, a_x, a_y);
                    answers.add(answer);
                }
        }catch (SQLException e) {
        	System.out.println(e.getMessage());
        	throw new RuntimeException(e);
        }
		 return answers; 
	 }
    
    public int insertAnswer(AnswerDTO answerData) throws SQLException {
        String sql = "INSERT INTO answer (g_num, a_radius, a_x, a_y) VALUES (?, ?, ?, ?)";
        int rowsAffected = 0;

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, answerData.getG_num());
            pstmt.setInt(2, answerData.getA_radius());
            pstmt.setInt(3, answerData.getA_x());
            pstmt.setInt(4, answerData.getA_y());

            rowsAffected = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new SQLException(e);
        }

        return rowsAffected;
    }
}