package site.pixeldetective.server.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import site.pixeldetective.server.db.DBConnector;
import site.pixeldetective.server.dto.SignUpDTO;

public class SignUpDAO {
	private static Connection conn = DBConnector.getConnection();
	
	public int signupUser (SignUpDTO signData) {
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
