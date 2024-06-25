package site.pixeldetective.server.router;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class NotFoundHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        String response = "not Found";

        // 응답 헤더를 설정합니다.
        exchange.sendResponseHeaders(404, response.getBytes().length);

        // 응답 본문을 작성합니다.
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
