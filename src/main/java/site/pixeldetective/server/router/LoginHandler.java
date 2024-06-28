package site.pixeldetective.server.router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import site.pixeldetective.server.dao.ChatDAO;
import site.pixeldetective.server.dao.UserDAO;
import site.pixeldetective.server.dto.ChatDTO;
import site.pixeldetective.server.dto.UserDTO;
import site.pixeldetective.server.jwt.JwtSender;

import static site.pixeldetective.server.router.IndexRouter.queryToMap;

public class LoginHandler implements HttpHandler{
	JwtSender js = new JwtSender();
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
				exchange.getResponseHeaders().set("Authorization", "false fail " );

			}else {
				statusCode = 201;
				JSONObject jsonResponse = new JSONObject();
				jsonResponse.put("message", "성공");


				// JWT를 받아옴 서버 메서드 호출
				// 헤더에 저장해서 보내봤는데도 어떻게 안 되서 그냥 이렇게 응답메세지에 보냄
				String jwt = js.createJWT(rowAffected.getuId(), rowAffected.getuNum(), rowAffected.getuName(), rowAffected.getuId());

				jsonResponse.put("jwt", jwt);
				response = jsonResponse.toString();
			}
		}else if(exchange.getRequestMethod().equalsIgnoreCase("GET")){
			System.out.println(2131312);

			URI requestURI = exchange.getRequestURI();
			Map<String, String> queryParams = queryToMap(requestURI.getQuery());
			String jwt = queryParams.get("jwt");

			// JSON으로 변환
			boolean result = js.verifyJWT(jwt);

			System.out.println(result);
			System.out.println(result);
			JSONObject jsonResponse = new JSONObject();
			jsonResponse.put("result", result);

			response = jsonResponse.toString();
		}
		exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
		
	}

}
