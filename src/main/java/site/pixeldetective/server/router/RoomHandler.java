package site.pixeldetective.server.router;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import site.pixeldetective.server.dao.RoomDAO;
import site.pixeldetective.server.dto.RoomDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class RoomHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        int statusCode = 200;
        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            // select
            RoomDAO roomDAO = new RoomDAO();
            List<RoomDTO> roomDTOList = roomDAO.getAllRooms();
            // JSON으로 변환
            JSONArray jsonArray = new JSONArray();
            for (RoomDTO room : roomDTOList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("r_num", room.getR_roomID());
                jsonObject.put("r_player1", room.getU_num_player1());
                jsonObject.put("r_player2", room.getU_num_player2());
                jsonObject.put("r_name", room.getR_name());
                jsonObject.put("r_difficulty", room.getR_difficulty());
                jsonObject.put("g_num", room.getG_num());
                jsonArray.put(jsonObject);
            }
            response = jsonArray.toString();
        } else if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            // insert
            // 요청에 바디에 있는 데이터 utf-8 인코딩으로 읽어온다.
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
            // BufferedReader를 사용하여 InputStreamReader에서 데이터를 읽어옵니다.
            BufferedReader br = new BufferedReader(isr);
            // lines()를 통해 모든 라인을 읽어와 collect를 통해 join 합니다.
            String body = br.lines().collect(Collectors.joining());

            // JSON 파싱
            // 요청은 JSON으로 일어나므로 JSONObject로 파싱
            JSONObject jsonRequest = new JSONObject(body);

            // r_num, r_player1, r_player2, r_name, r_difficulty, g_num의 키 값을 가져온다.
            // 새 room 생성
            RoomDAO roomDAO = new RoomDAO();
            RoomDTO newroom = new RoomDTO();
            newroom.setU_num_player1(jsonRequest.getInt("r_player1"));
            newroom.setU_num_player2(jsonRequest.getInt("r_player2"));
            newroom.setR_name(jsonRequest.getString("r_name"));
            newroom.setR_difficulty(jsonRequest.getString("r_difficulty"));
            newroom.setG_num(jsonRequest.getInt("g_num"));

            int rowAffected = roomDAO.createRoom(newroom);
            statusCode = 201;
            // 응답 생성
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "테스트가 성공적으로 생성되었습니다.");
            response = jsonResponse.toString();
        }else {
            response = new JSONObject().put("error", "허용되지 않는 메소드입니다.").toString();
            statusCode = 405; // 405 Method Not Allowed
        }
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
}
