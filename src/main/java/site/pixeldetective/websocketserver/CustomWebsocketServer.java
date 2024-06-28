package site.pixeldetective.websocketserver;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;
import site.pixeldetective.websocketserver.handler.ChatHandler;
import site.pixeldetective.websocketserver.handler.WebSocketGameHandler;
import site.pixeldetective.websocketserver.handler.WebSocketHandler;
//import site.pixeldetective.websocketserver.httpconnect.HttpConnector;
import site.pixeldetective.websocketserver.userpool.CurrentUser;
import site.pixeldetective.websocketserver.userpool.UserPool;

import java.net.InetSocketAddress;
import java.net.http.HttpHeaders;
import java.util.Random;

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
//        int uId = Integer.parseInt(clientHandshake.getFieldValue("u_id"));
//        CurrentUser currentUser = HttpConnector.getInstance().getCurrentUserById(uId, null);
        CurrentUser currentUser = new CurrentUser(1, "1", "1");
        UserPool.getInstance().addUser(conn, currentUser);
        System.out.println("New client connected : " + conn.getRemoteSocketAddress());
    }
    // client가 접속을 끊음.
    // conn 연결된 WebScoket 객체
    // i 연결 닫힘 상태코드
    // 1000(정상 종료),
    // 1001(브라우저 닫힘, 서버 재시작),
    // 1002(프로토콜 오류),
    // 1003(지원 되지 않는 데이터 통신)
    // s : 닫힐 때 받은 이유의 문자열
    // b : 연결이 닫힌 곳(t client , f : server)
    @Override
    public void onClose(WebSocket conn, int i, String s, boolean b) {
        System.out.println(s + " " + b + " " + i);
        WebSocketHandler.removeCurrentUser(conn);
        System.out.println("Client disconnected : " + conn.getRemoteSocketAddress());
    }
    // client에서 메시지 혹은 요청 명령어를 보냄
    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            JSONObject request = new JSONObject(message);
            String command = request.getString("command");
            switch (command) {
                case "hello":
                    WebSocketHandler.sayHello();
                    conn.send("hello");
                    break;
                case "getUserCount":
                    conn.send("" + WebSocketHandler.getUserCount());
                    break;
                case "sendChat":
                    String nickname = request.getString("nickname");
                    String content = request.getString("message");
                    ChatHandler.receiveMessage(nickname, content);
                    conn.send("receive chat " + nickname + " " + content);
                    break;
                case "getCurrentUserList":
                    conn.send(WebSocketHandler.getCurrentUserList());
                    break;
                case "quickMatching":
                    WebSocketHandler.currentUserStatusMatching(conn);
                    conn.send("turn to Matching");
                    break;
                case "cancleMatching":
                    WebSocketHandler.currentUserStatusJoin(conn);
                    conn.send("turn to Join");
                    break;
                case "roomCreate":
                    System.out.println("server try roomCreate");
                    int difficulty = request.getInt("r_difficulty");
                    String roomName = request.getString("r_name");
                    boolean ret = WebSocketGameHandler.userCreateRoom(conn, roomName, difficulty);
                    if (ret) {
                        conn.send("room created");
                    } else {
                        conn.send("something is happened");
                    }
                    WebSocketGameHandler.getCurrentRoomList();
                    break;
                case "getCurrentRoomList":
                    WebSocketGameHandler.getCurrentRoomList();
                    break;
                case "roomDelete":
                    System.out.println("server try roomDelete");

                default:
                    break;
            }
            System.out.println("Received message from " + conn.getRemoteSocketAddress() + " : " + message);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
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
