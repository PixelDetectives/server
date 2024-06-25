package site.pixeldetective.server.router;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;

public class IndexRouter implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String subPath = path.substring("/api".length());
        HttpHandler handler;
        switch (subPath) {
            case "/test":
                handler = new HelloWorldHandler();
                break;
            default:
                handler = new NotFoundHandler();
                break;
        }
        handler.handle(exchange);
    }
}