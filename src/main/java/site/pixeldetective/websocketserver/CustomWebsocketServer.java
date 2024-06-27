package site.pixeldetective.websocketserver;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import site.pixeldetective.websocketserver.handler.WebSocketHandler;
import site.pixeldetective.websocketserver.userpool.UserPool;

import java.net.InetSocketAddress;

public class CustomWebsocketServer extends WebSocketServer {
    private final int PORT;
    public static UserPool userPool;
    public CustomWebsocketServer(InetSocketAddress webSocketPort) {
        super(webSocketPort);
        this.PORT = webSocketPort.getPort();
    }

    // 새로운 client가 서버에 접속함.
    // WebSocket 웹 소켓 통신을 관리한다.
    // conn 연결된 클라이언트의 통신을 처리하고 데이터를 송수신하거나 연결을 닫는 작업을 수행
//    close(int code, String reason): Closes the WebSocket connection.
//    send(String message): Sends a message to the client.
//    sendBytes(byte[] bytes): Sends a byte array to the client.

    // ClientHandshake : 클라이언트 연결 요청 핸드셰이크 요청
    // getResourceDescriptor() : 요청 리소스 반환 URI
    // getFieldValue(String name) : 핸드쉐이크 요청 헤더 값

    // 사용자가 입력을 요청할 때
    @Override
    public void onOpen(WebSocket conn, ClientHandshake clientHandshake) {
        System.out.println(conn.getRemoteSocketAddress());
        System.out.println(clientHandshake.getResourceDescriptor());

        System.out.println(clientHandshake.getFieldValue("u_id"));
        System.out.println(clientHandshake.getFieldValue("u_name"));


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
            conn.send("hello");
        }
        if (message.equals("getUserCount")) {
            conn.send("" + WebSocketHandler.getUserCount());
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
