package site.pixeldetective.websocketserver;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONArray;
import org.json.JSONObject;
import site.pixeldetective.websocketserver.gameroom.GameRoom;
import site.pixeldetective.websocketserver.handler.ChatHandler;
import site.pixeldetective.websocketserver.handler.WebSocketGameHandler;
import site.pixeldetective.websocketserver.handler.WebSocketHandler;
//import site.pixeldetective.websocketserver.httpconnect.HttpConnector;
import site.pixeldetective.websocketserver.userpool.CurrentUser;
import site.pixeldetective.websocketserver.userpool.UserPool;

import java.net.InetSocketAddress;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.util.EnumSet;
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
        try {
            int sessionId = conn.hashCode();
            System.out.println(conn.getRemoteSocketAddress());
            System.out.println(clientHandshake.getResourceDescriptor());
            String uId = clientHandshake.getFieldValue("u_id");
            String uName = clientHandshake.getFieldValue("u_name");
            System.out.println(conn.hashCode() + " " + uId + " " + uName);
            CurrentUser currentUser = new CurrentUser(uId, uName, "join");
            UserPool.getInstance().addUser(sessionId, conn, currentUser);
            System.out.println("New client connected : " + conn.getRemoteSocketAddress());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
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
        WebSocketHandler.removeCurrentUser(conn.hashCode());
        System.out.println("Client disconnected : " + conn.getRemoteSocketAddress());
    }
    // client에서 메시지 혹은 요청 명령어를 보냄
    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            JSONObject request = new JSONObject(message);
            String command = request.getString("command");
            JSONObject jsonObject = new JSONObject();
            String uId;
            switch (command) {
                case "hello":
                    WebSocketHandler.sayHello();
                    conn.send("hello");
                    break;
                case "getUserCount":
                    conn.send("" + WebSocketHandler.getUserCount());
                    break;
                case "sendChat":
                    String content = request.getString("message");
                    CurrentUser currentUser = UserPool.getInstance().getUser(conn.hashCode());
                    String nickname = currentUser.getuName();
                    ChatHandler.receiveMessage(nickname, content);
                    conn.send("receive chat " + nickname + " " + content);
                    break;
                case "getCurrentUserList":
                    WebSocketHandler.getCurrentUserList();
                    break;
                case "statusMatching":
                    WebSocketHandler.currentUserStatusMatching(conn.hashCode(), conn);
                    conn.send("turn to Matching");
                    break;
                case "cancleMatching":
                    WebSocketHandler.currentUserStatusJoin(conn.hashCode(), conn);
                    conn.send("turn to Join");
                    break;
                case "createRoom":
                    int difficulty = request.getInt("r_difficulty");
                    String roomName = request.getString("r_name");
                    WebSocketHandler.currentUserStatusMatching(conn.hashCode(), conn);
                    boolean ret = WebSocketGameHandler.userCreateRoom(conn.hashCode(), roomName, difficulty);
                    if (ret) {
                        jsonObject.put("status", "success");
                    } else {
                        jsonObject.put("status", "fail");
                    }
                    conn.send(jsonObject.toString());
                    WebSocketGameHandler.braodCaseCurrentRoomList();
                    break;
                case "getCurrentRoomList":
                    jsonObject.put("type", "currentRooms");
                    JSONArray jsonArray = WebSocketGameHandler.getCurrentRoomList();
                    jsonObject.put("data", jsonArray.toString());
                    conn.send(jsonObject.toString());
                    break;
                case "joinRoom":
                    uId = request.getString("session");
                    WebSocketHandler.currentUserStatusMatching(conn.hashCode(), conn);
                    GameRoom gameRoom = WebSocketGameHandler.getGameRoomByUId(uId);
                    if (gameRoom == null) {
                        conn.send("could not found");
                    } else {
                        int currentUser1 = gameRoom.getCurrentUser1();
                        int currentUser2 = gameRoom.getCurrentUser2();
                        UserPool.getInstance().getConnection(currentUser1).send("game matching");
                        UserPool.getInstance().getConnection(currentUser2).send("game matching");
                    }
                    break;
                case "roomDelete":
                    uId = UserPool.getInstance().getUser(conn.hashCode()).getuId();
                    WebSocketGameHandler.removeGameRoomByUId(uId);
                    conn.send("server close room");
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


//{
//    "data":"" +
//        "[{\"r_id\":\"{\\\"uId\\\":1,\\\"uName\\\":\\\"1\\\",\\\"status\\\":\\\"1\\\"}\",\"r_difficulty\":0,\"r_name\":\"이것은 방 이름 입니다.\"}," +
//        "{\"r_id\":\"{\\\"uId\\\":1,\\\"uName\\\":\\\"1\\\",\\\"status\\\":\\\"1\\\"}\",\"r_difficulty\":0,\"r_name\":\"이것은 방 이름입니다.\"}]",
//        "type":"currentRooms"}