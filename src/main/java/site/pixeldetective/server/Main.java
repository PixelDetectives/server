package site.pixeldetective.server;

import com.sun.net.httpserver.HttpServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import site.pixeldetective.server.router.IndexRouter;
import site.pixeldetective.websocketserver.CustomWebsocketServer;

public class Main {
    static final int HTTP_PORT = 9000;
    static final int WEB_SOCKET_PORT = 9001;
    public static void main(String[] args) {
        try {
            // 서버를 지정된 포트에서 시작합니다.
            HttpServer server = HttpServer.create(new InetSocketAddress(HTTP_PORT), 0);
            // 루트 경로("/")에 대한 요청을 처리할 핸들러를 설정합니다.
            server.createContext("/", new IndexRouter());
            // 서버가 요청을 처리할 수 있도록 합니다.
            server.setExecutor(null); // 기본 실행기를 사용합니다.
            server.start();
            System.out.println("Server started on port " + HTTP_PORT);

            WebSocketServer webSocketServer = new CustomWebsocketServer(new InetSocketAddress(WEB_SOCKET_PORT));
            webSocketServer.start();
            System.out.println("Websocket Server started on port " + WEB_SOCKET_PORT);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}