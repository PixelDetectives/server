package site.pixeldetective.server.router;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import site.pixeldetective.server.dao.AnswerDAO;
import site.pixeldetective.server.dto.AnswerDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class AnswerHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        int statusCode = 200;

        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            // GET 요청 처리
            String query = exchange.getRequestURI().getQuery();
            if (query != null && query.startsWith("g_num=")) {
                try {
                    int g_num = Integer.parseInt(query.substring(6));

                    // AnswerDAO를 사용하여 g_num에 해당하는 AnswerDTO 리스트 조회
                    try {
                        List<AnswerDTO> answers = new AnswerDAO().selectAnswer(g_num);

                        // JSON으로 변환
                        JSONArray jsonArray = new JSONArray();
                        for (AnswerDTO answer : answers) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("a_num", answer.getA_num());
                            jsonObject.put("g_num", answer.getG_num());
                            jsonObject.put("a_radius", answer.getA_radius());
                            jsonObject.put("a_x", answer.getA_x());
                            jsonObject.put("a_y", answer.getA_y());
                            jsonArray.put(jsonObject);
                        }

                        response = jsonArray.toString();
                    } catch (SQLException e) {
                        response = new JSONObject().put("error", "Database error occurred.").toString();
                        statusCode = 500; // Internal Server Error
                    }
                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    response = new JSONObject().put("error", "Invalid g_num parameter.").toString();
                    statusCode = 400; // Bad Request
                }
            } else {
                response = new JSONObject().put("error", "Missing g_num parameter.").toString();
                statusCode = 400; // Bad Request
            }
        } else if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            // POST 요청 처리
            try {
                // 요청 바디에서 데이터 읽기
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String body = br.lines().collect(Collectors.joining());

                // JSON 파싱
                JSONObject jsonRequest = new JSONObject(body);
                int g_num = jsonRequest.getInt("g_num");

                // 현재 시각을 Timestamp로 생성
                Timestamp sent_at = new Timestamp(System.currentTimeMillis());

                // AnswerDTO 객체 생성
                AnswerDTO answerData = new AnswerDTO();
                answerData.setG_num(g_num);
                answerData.setA_radius(jsonRequest.getInt("a_radius"));
                answerData.setA_x(jsonRequest.getInt("a_x"));
                answerData.setA_y(jsonRequest.getInt("a_y"));

                // AnswerDAO를 사용하여 데이터베이스에 삽입
                int rowsAffected = new AnswerDAO().insertAnswer(answerData);

                // 응답 생성
                JSONObject jsonResponse = new JSONObject();
                if (rowsAffected > 0) {
                    jsonResponse.put("message", "Answer added successfully.");
                    statusCode = 201; // Created
                } else {
                    jsonResponse.put("message", "Failed to add answer.");
                    statusCode = 500; // Internal Server Error
                }
                response = jsonResponse.toString();
            } catch (IOException | NumberFormatException e) {
                response = new JSONObject().put("error", "Invalid data format.").toString();
                statusCode = 400; // Bad Request
            } catch (Exception e) {
                response = new JSONObject().put("error", "Internal server error.").toString();
                statusCode = 500; // Internal Server Error
            }
        } else {
            // 다른 메소드는 허용되지 않음
            response = new JSONObject().put("error", "Method not allowed.").toString();
            statusCode = 405; // Method Not Allowed
        }

        // HTTP 응답 전송
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}