package site.pixeldetective.server.router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.stream.Collectors;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import site.pixeldetective.server.dao.LoginDAO;
import site.pixeldetective.server.dto.LoginDTO;

public class LoginHandler implements HttpHandler{

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String response = "";
		int statusCode = 200;
		if(exchange.getRequestMethod().equalsIgnoreCase("POST")) {
			InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String body = br.lines().collect(Collectors.joining());
			
			JSONObject jsonRequest = new JSONObject(body);
			String id = jsonRequest.getString("u_id");
			String pw = jsonRequest.getString("u_pw");
			
			LoginDAO loginDAO = new LoginDAO();
			LoginDTO loginDTO = new LoginDTO();
			loginDTO.setId(id);
			loginDTO.setPw(pw);
			LoginDTO rowAffected = loginDAO.loginUser(loginDTO);
			statusCode = 201;
			
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("message", "성공");
			response = jsonResponse.toString();
		}
		exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
		
	}

}
