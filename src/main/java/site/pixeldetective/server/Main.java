package site.pixeldetective.server;

import com.mysql.cj.xdevapi.Client;
import com.sun.net.httpserver.HttpServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import site.pixeldetective.server.router.IndexRouter;
import site.pixeldetective.websocketserver.CustomWebsocketServer;
import site.pixeldetective.websocketserver.userpool.UserPool;

public class Main {
    static final int HTTP_PORT = 9000;
    static final int WEB_SOCKET_PORT = 9001;

    public static void main(String[] args) {
        try {
            // HttpServer 시작
            HttpServer server = HttpServer.create(new InetSocketAddress(HTTP_PORT), 0);
            server.createContext("/", new IndexRouter());
            server.setExecutor(null);
            server.start();
            System.out.println("HttpServer started on port " + HTTP_PORT);

            // WebSocketServer 시작
            WebSocketServer webSocketServer = new CustomWebsocketServer(new InetSocketAddress(WEB_SOCKET_PORT));
            webSocketServer.start();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}