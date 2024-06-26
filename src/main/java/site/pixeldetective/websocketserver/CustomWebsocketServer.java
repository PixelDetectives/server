package site.pixeldetective.websocketserver;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import site.pixeldetective.websocketserver.handler.WebSocketHandler;

import java.net.InetSocketAddress;

public class CustomWebsocketServer extends WebSocketServer {
    private final int PORT;
    public CustomWebsocketServer(InetSocketAddress webSocketPort) {
        super(webSocketPort);
        this.PORT = webSocketPort.getPort();
    }

    // 새로운 client가 서버에 접속함.
    @Override
    public void onOpen(WebSocket conn, ClientHandshake clientHandshake) {
        System.out.println("New client connected : " + conn.getRemoteSocketAddress());
    }
    // client가 접속을 끊음.
    @Override
    public void onClose(WebSocket conn, int i, String s, boolean b) {
        System.out.println("Client disconnected : " + conn.getRemoteSocketAddress());
    }
    // client에서 메시지를 보냄
    @Override
    public void onMessage(WebSocket conn, String message) {
//        String[] parts = message.split(":"); // 메시지를 파싱합니다.
//        if (parts.length == 2 && parts[0].equals("command")) {
//            handleCommand(conn, parts[1]);
//        } else {
//            System.out.println("Received message from " + conn.getRemoteSocketAddress() + " : " + message);
//        }
        if (message.equals("hello")) {
            WebSocketHandler.sayHello();
        }
        System.out.println("Received message from " + conn.getRemoteSocketAddress() + " : " + message);
    }

    // client 가 에러를 보냄
    @Override
    public void onError(WebSocket conn, Exception e) {
        System.err.println("Error with client " + conn.getRemoteSocketAddress() + " : " + e.getMessage());
    }
    // websocket 서버가 실행됨
    @Override
    public void onStart() {
        System.out.println("Websocket Server started on port " + PORT);

    }
}
