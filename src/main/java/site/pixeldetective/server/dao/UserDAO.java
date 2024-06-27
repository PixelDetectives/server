package site.pixeldetective.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import site.pixeldetective.server.db.DBConnector;
import site.pixeldetective.server.dto.UserDTO;

public class UserDAO {
	private static Connection conn = DBConnector.getConnection();
	
	//로그인 SQL
	public UserDTO loginUser (UserDTO loginData) {
		String sql = "select u_id, u_name from users where u_id = ? and u_pw = ?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, loginData.getId());
			pstmt.setString(2, loginData.getPw());
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return new UserDTO(loginData.getId(), loginData.getPw());
			}else {
				return null;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	
	}
	
	//회원가입 SQL
	public int signupUser (UserDTO signData) {
		String sql = "insert into users (u_id, u_name, u_pw) values(?, ?, ?)";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, signData.getId());
			pstmt.setString(2, signData.getName());
			pstmt.setString(3, signData.getPw());
			return pstmt.executeUpdate();
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
