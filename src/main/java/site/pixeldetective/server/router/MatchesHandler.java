package site.pixeldetective.server.router;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import site.pixeldetective.server.dao.MatchDAO;
import site.pixeldetective.server.dao.TestDAO;
import site.pixeldetective.server.dto.MatchDTO;
import site.pixeldetective.server.dto.TestDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import static site.pixeldetective.server.router.IndexRouter.getDateFromString;
import static site.pixeldetective.server.router.IndexRouter.queryToMap;

public class MatchesHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        int statusCode = 200;
        /**
         * GET요청
         *  http://localhost:9000/pixel-detective/api/matches?search=검색 번호
         *  > select * from matches where m_player1 = 검색번호 JSON으로 전송
         */
        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            // 요청에 바디에 있는 데이터 utf-8 인코딩으로 읽어온다.
            System.out.println(exchange.getRequestURI().toString());

            URI requestURI = exchange.getRequestURI();
            Map<String, String> queryParams = queryToMap(requestURI.getQuery());

            int searchValue = Integer.parseInt(queryParams.get("search"));
            System.out.println("search" +searchValue );

            MatchDAO matchDAO = new MatchDAO();
            List<MatchDTO> matchList = matchDAO.getAllMatches(searchValue);
            // JSON으로 변환
            JSONArray jsonArray = new JSONArray();
            for (MatchDTO matches : matchList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("mNum", matches.getmNum());
                jsonObject.put("mPlayer1", matches.getmPlayer1());
                jsonObject.put("mPlayer2", matches.getmPlayer2());
                jsonObject.put("mResult", matches.getmResult());
                jsonObject.put("mStart", matches.getmStart());
                jsonObject.put("mEnd", matches.getmEnd());
                jsonArray.put(jsonObject);
            }
            response = jsonArray.toString();

            /**
             * POST요청
             *  http://localhost:9000/pixel-detective/api/matches + BODY JSON {}
             *  > insert 2 번 발생  = 검색번호 JSON으로 전송
             */
        } else if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {

            System.out.println("POST MATCHES 요청 ~@!@!@");
            // 요청에 바디에 있는 데이터 utf-8 인코딩으로 읽어온다.
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
            // BufferedReader를 사용하여 InputStreamReader에서 데이터를 읽어옵니다.
            BufferedReader br = new BufferedReader(isr);
            // lines()를 통해 모든 라인을 읽어와 collect를 통해 join 합니다.
            String body = br.lines().collect(Collectors.joining());

            // JSON 파싱
            // 요청은 JSON으로 일어나므로 JSONObject로 파싱
            JSONObject jsonRequest = new JSONObject(body);
            // name의 키 값을 가져온다.
            int mPlayer1 = jsonRequest.getInt("mPlayer1");
            int mPlayer2 = jsonRequest.getInt("mPlayer2");
            int mResult = jsonRequest.getInt("mResult");
            String mStart = jsonRequest.getString("mStart");
            String mEnd = jsonRequest.getString("mEnd");


            Timestamp start = getDateFromString(mStart);
            Timestamp end = getDateFromString(mEnd);



            // 새 테스트 생성
            MatchDAO matchDAO = new MatchDAO();
            MatchDTO newMatches = new MatchDTO();
            newMatches.setmPlayer1(mPlayer1);
            newMatches.setmPlayer2(mPlayer2);
            newMatches.setmResult(mResult);
//            newMatches.setmStart(start);
//            newMatches.setmEnd(end);



            int rowAffected = matchDAO.createMatch(newMatches);
            statusCode = 201;
            // 응답 생성
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("message", "테스트가 성공적으로 생성되었습니다.");
            response = jsonResponse.toString();
        }

        else {
            response = new JSONObject().put("error", "허용되지 않는 메소드입니다.").toString();
            statusCode = 405; // 405 Method Not Allowed
        }

        System.out.println(response);


        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
