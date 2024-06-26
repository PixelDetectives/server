package site.pixeldetective.server.router;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import site.pixeldetective.server.dao.GameDAO;
import site.pixeldetective.server.dto.GameDTO;

public class GameHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        int statusCode = 200;
        
        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
        	List<GameDTO> gameList;
			try {
				gameList = new GameDAO().selelctGames();
				   
                // JSON으로 변환
                JSONArray jsonArray = new JSONArray();
                for (GameDTO game : gameList) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("g_num", game.getG_num());
                    jsonObject.put("g_image1", game.getG_image1());
                    jsonObject.put("g_image2", game.getG_image2());
                    jsonObject.put("g_name", game.getG_name());
                    jsonObject.put("g_difficulty", game.getG_difficulty());
                    jsonArray.put(jsonObject);
                }
                    response = jsonArray.toString();
			}catch (SQLException e) {
				response = new JSONObject().put("error", "허용되지 않는 메소드입니다.").toString();
	            statusCode = 500;
			}
            
        } else {
        	response = new JSONObject().put("error", "허용되지 않는 메소드입니다.").toString();
            statusCode = 405;
        }
        
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}