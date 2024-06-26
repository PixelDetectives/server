package site.pixeldetective.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import site.pixeldetective.server.db.DBConnector;
import site.pixeldetective.server.dto.LoginDTO;

public class LoginDAO {
	private static Connection conn = DBConnector.getConnection();
	public LoginDTO loginUser (LoginDTO loginData) {
		String sql = "select u_pw from users where u_id = ? and u_pw = ?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, loginData.getId());
			pstmt.setString(2, loginData.getPw());
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return new LoginDTO(loginData.getId(), loginData.getPw());
			}else {
				return null;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e);
		}
	
	}
}
