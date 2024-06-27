package site.pixeldetective.server.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import site.pixeldetective.server.db.DBConnector;
import site.pixeldetective.server.dto.ChatDTO;
import site.pixeldetective.server.dto.TestDTO;

public class ChatDAO {
    private static Connection conn = DBConnector.getConnection();
    
	public int insertChat(ChatDTO chatData){
	    int re = -1;
	    String sql = "INSERT INTO chat (message,uName, sender, sent_at) VALUES (?,?, ?, NOW())";

	    try {
	    		PreparedStatement pstmt = conn.prepareStatement(sql);
	    		
		    	pstmt.setString(1, chatData.getMessage());
				pstmt.setString(2, chatData.getuName());
		    	pstmt.setInt(3, chatData.getSender());
		    	
	            return pstmt.executeUpdate();
	            
	        } catch (SQLException e) {
	            throw new RuntimeException(e);
	        }
	}
	
	public List<ChatDTO> selectChat(){
		List<ChatDTO> chatList = new ArrayList<>();
		
		String sql = "SELECT chat_id,u_name, message, sender, sent_at FROM chat ORDER BY sent_at asc";
		
	                try {
	                	PreparedStatement pstmt = conn.prepareStatement(sql);            	
	                	ResultSet rs = pstmt.executeQuery();           	
	                	while(rs.next()) {
	                		int chat_id = rs.getInt("chat_id");
	                		String message = rs.getString("message");
							String uName = rs.getString("u_name");
	                		int sender = rs.getInt("sender");
	                		Timestamp sent_at = rs.getTimestamp("sent_at");  
	                		
	                		ChatDTO chat = new ChatDTO(message,uName, sender, sent_at);
	                		chatList.add(chat);
	                	}
	                }catch(SQLException e) {
	                	System.out.println(e.getMessage());
	                	throw new RuntimeException(e);
	                }
		return chatList;
	}
}