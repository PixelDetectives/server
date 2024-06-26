package site.pixeldetective.server.router;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;

public class IndexRouter implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        try {
            // 현재 요청 URL 조회
            String path = exchange.getRequestURI().getPath();
            // /api 뒤에 오는 요청 파악
            String subPath = path.replaceFirst("/pixel-detective/api", "");
            // 기본 핸들러 NotFoundHandler로 고정
            HttpHandler handler = new NotFoundHandler();
            switch (subPath) {
                // 기본 URL로 요청시
                case "":
                case "/":
                    // 응답 문자열 파악
                    String response = "server connect";
                    // 헤더에 statusCode와 응답 길이 설정
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    // outputStream에 바디에 담기
                    OutputStream os = exchange.getResponseBody();
                    // 응답의 바이트 수 만큼 쓰고 전송
                    os.write(response.getBytes());
                    // os 종료
                    os.close();
                    break;
                case "/test":
                    // test로 들어올 경우
                    handler = new TestHandler();
                    break;
                case "/matches" ,"matche":
                    // matches,matche로 들어올 경우
                    handler = new MatchesHandler();
                    break;
                default:
                    handler = new NotFoundHandler();
                    break;
            }
            handler.handle(exchange);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}