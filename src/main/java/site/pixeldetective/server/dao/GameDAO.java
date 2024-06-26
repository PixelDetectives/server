package site.pixeldetective.server.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import site.pixeldetective.server.db.DBConnector;
import site.pixeldetective.server.dto.GameDTO;

public class GameDAO {
	 private static Connection conn = DBConnector.getConnection();
	 
	 public List<GameDTO> selelctGames() throws SQLException{
		 List<GameDTO> games = new ArrayList<>();
		 
		 return games;
		 
	 }
	 
}