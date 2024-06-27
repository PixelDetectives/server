package site.pixeldetective.server.router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Objects;
import java.util.stream.Collectors;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import site.pixeldetective.server.dao.LoginDAO;
import site.pixeldetective.server.dao.UserDAO;
import site.pixeldetective.server.dto.LoginDTO;
import site.pixeldetective.server.dto.UserDTO;
import site.pixeldetective.server.jwt.JwtSender;

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

			UserDAO userDAO = new UserDAO();
			UserDTO userDto = new UserDTO();
			userDto.setuId(id);
			userDto.setuPw(pw);
			UserDTO rowAffected = userDAO.loginUser(userDto);
			// 로그인 실패시
			if(Objects.isNull(rowAffected)){
				statusCode = 401;							// 인증 실패 상태코드
				JSONObject jsonResponse = new JSONObject();
				jsonResponse.put("message", "실패");
				response = jsonResponse.toString();

			}else {
				statusCode = 201;
				JSONObject jsonResponse = new JSONObject();
				jsonResponse.put("message", "성공");
				response = jsonResponse.toString();

				JwtSender js = new JwtSender();
				// JWT를 받아옴 서버 메서드 호출
				String jwt = js.createJWT(rowAffected.getuId(), rowAffected.getuNum(), rowAffected.getuName(), rowAffected.getuId());
				exchange.getResponseHeaders().set("Authorization", "Bearer " + jwt);
			}
		}
		exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
		
	}

}
