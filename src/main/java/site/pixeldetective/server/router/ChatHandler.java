package site.pixeldetective.server.router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import site.pixeldetective.server.dao.ChatDAO;
import site.pixeldetective.server.dto.ChatDTO;

public class ChatHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        int statusCode = 200;
        
        
        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
        	List<ChatDTO> chatList = new ChatDAO().selectChat();
            // JSON으로 변환
            JSONArray jsonArray = new JSONArray();
            for (ChatDTO chat : chatList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("chat_id", chat.getChat_id());
                jsonObject.put("message", chat.getMessage());
                jsonObject.put("u_name", chat.getuName());
                jsonObject.put("sender", chat.getSender());
                jsonObject.put("sent_at", chat.getSent_at().toString()); // Timestamp를 문자열로 변환
                jsonArray.put(jsonObject);
            }
            
            response = jsonArray.toString();
            
        } else if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {

            System.out.println("ChatHandler Post");
            // 요청에 바디에 있는 데이터 utf-8 인코딩으로 읽어온다.
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
            // BufferedReader를 사용하여 InputStreamReader에서 데이터를 읽어옵니다.
            BufferedReader br = new BufferedReader(isr);
            // lines()를 통해 모든 라인을 읽어와 collect를 통해 join 합니다.
            String body = br.lines().collect(Collectors.joining());

            // JSON 파싱
            // 요청은 JSON으로 일어나므로 JSONObject로 파싱
            JSONObject jsonRequest = new JSONObject(body);


            String message = jsonRequest.getString("message");
            int sender = jsonRequest.getInt("sender");

            String u_name = jsonRequest.getString("u_name");


            // 현재 시각을 Timestamp로 생성
            Timestamp sent_at = new Timestamp(System.currentTimeMillis());

            // ChatDTO 객체 생성 (sent_at을 포함한 생성자 사용)
            ChatDTO chatData = new ChatDTO(message,u_name, sender, sent_at);


            // ChatDAO를 사용하여 채팅 메시지 데이터베이스에 삽입
            int rowsAffected = new ChatDAO().insertChat(chatData);

            // 응답 생성
            JSONObject jsonResponse = new JSONObject();
            if (rowsAffected > 0) {
                jsonResponse.put("message", "채팅 메시지가 성공적으로 추가되었습니다.");
                statusCode = 201; // Created
            } else {
                jsonResponse.put("message", "채팅 메시지 추가에 실패했습니다.");
                statusCode = 500; // Internal Server Error
            }
            response = jsonResponse.toString();
            
        } else {
            // 지원하지 않는 메소드일 경우 에러 응답 생성
            response = new JSONObject().put("error", "허용되지 않는 메소드입니다.").toString();
            statusCode = 405; // Method Not Allowed
        }
        
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}