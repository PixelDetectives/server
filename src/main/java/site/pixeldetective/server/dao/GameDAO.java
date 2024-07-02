package site.pixeldetective.server.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import site.pixeldetective.server.db.DBConnector;
import site.pixeldetective.server.dto.ChatDTO;
import site.pixeldetective.server.dto.GameDTO;

public class GameDAO {
	 //private static Connection conn = DBConnector.getConnection();
	 
	 public List<GameDTO> selectGames() throws SQLException{
		 List<GameDTO> games = new ArrayList<>();
		 
		 String sql = "SELECT g_num, g_image1, g_image2, g_name, g_difficulty FROM game";
		 
         try (Connection conn = DBConnector.getConnection();
    		 PreparedStatement pstmt = conn.prepareStatement(sql);            	
    		 ResultSet rs = pstmt.executeQuery();          
        		 ){
         	
         	while(rs.next()) {
         		int g_num = rs.getInt("g_num");
                String g_image1 = rs.getString("g_image1");
                String g_image2 = rs.getString("g_image2");
                String g_name = rs.getString("g_name");
                int g_difficulty = rs.getInt("g_difficulty");
                
                GameDTO game = new GameDTO(g_num, g_image1, g_image2, g_name, g_difficulty);
                games.add(game);
         	}
         }catch (SQLException e) {
             throw new SQLException(e);
         }
		 return games; 
	 }
	public GameDTO randomGames() throws SQLException {
		GameDTO game = new GameDTO();
		String getGameSQL = "SELECT * FROM game ORDER BY RAND() LIMIT 1";
		try (Connection conn = DBConnector.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(getGameSQL);
			 ResultSet rs = pstmt.executeQuery();
		){
			if (rs.next()) {
				int g_num = rs.getInt("g_num");
				String g_image1 = rs.getString("g_image1");
				String g_image2 = rs.getString("g_image2");
				String g_name = rs.getString("g_name");
				int g_difficulty = rs.getInt("g_difficulty");
				game = new GameDTO(g_num, g_image1, g_image2, g_name, g_difficulty);
			}
		}catch (SQLException e) {
			throw new SQLException(e);
		}
		return game;
	}
}