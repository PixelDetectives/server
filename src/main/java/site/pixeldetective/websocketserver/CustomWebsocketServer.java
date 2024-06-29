package site.pixeldetective.websocketserver;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONArray;
import org.json.JSONObject;
import site.pixeldetective.websocketserver.gameroom.GameRoom;
import site.pixeldetective.websocketserver.gameroom.GameRoomPool;
import site.pixeldetective.websocketserver.handler.ChatHandler;
import site.pixeldetective.websocketserver.handler.WebSocketGameHandler;
import site.pixeldetective.websocketserver.handler.WebSocketHandler;
//import site.pixeldetective.websocketserver.httpconnect.HttpConnector;
import site.pixeldetective.websocketserver.ongame.OnGame;
import site.pixeldetective.websocketserver.ongame.OnGamePool;
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
            WebSocketGameHandler.braodCaseCurrentRoomList();
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
                case "cancelMatching":
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
                    int currentUserSessionId1 = request.getInt("currentUserSessionId1");
//                    WebSocketHandler.currentUserStatusMatching(conn.hashCode(), conn);
                    GameRoom gameRoom = WebSocketGameHandler.getGameRoomBySessionId1(currentUserSessionId1);
                    gameRoom.setCurrentUser2(conn.hashCode());
                    if (gameRoom == null) {
                        conn.send("could not found");
                    } else {
                        int currentUser1 = gameRoom.getCurrentUser1();
                        int currentUser2 = gameRoom.getCurrentUser2();
                        jsonObject = new JSONObject();
                        jsonObject.put("type", "gameStart");
                        jsonObject.put("status", "success");
                        UserPool.getInstance().getConnection(currentUser1).send(jsonObject.toString());
                        UserPool.getInstance().getConnection(currentUser2).send(jsonObject.toString());
                        int gameRoomId = gameRoom.hashCode();
                        OnGame onGame = OnGamePool.createOnGame(gameRoomId, currentUser1, currentUser2, gameRoom.getDifficulty());
                        jsonObject = new JSONObject();
                        jsonObject.put("type", "gameData");
                        JSONObject gameData = new JSONObject(onGame.toString());
                        jsonObject.put("data", gameData);
                        UserPool.getInstance().getConnection(currentUser1).send(jsonObject.toString());
                        UserPool.getInstance().getConnection(currentUser2).send(jsonObject.toString());
                        WebSocketGameHandler.deleteGameRoomBySessionId1(currentUserSessionId1);
                    }
                    break;
                case "roomDelete":
                    int currentUserSessionId = request.getInt("currentUserSessionId");
                    conn.send("server close room");
                    System.out.println("server try roomDelete");
                case "gameOver":
                    JSONObject userData = new JSONObject();
                    long endTime = System.currentTimeMillis();
                    int onGameId = request.getInt("onGameId");
                    OnGame onGame = OnGamePool.getInstance().getOnGameByOnGameId(onGameId);
                    jsonObject = new JSONObject();
                    String user1Result = "";

                    String user2Result = "";
                    if (onGame.getUser1Hits() > onGame.getUser2Hits()) {
                        user1Result = "WIN";
                        user2Result = "LOSE";
                    } else if (onGame.getUser1Hits() == onGame.getUser2Hits()) {
                        if (onGame.getUser1Miss() < onGame.getUser2Miss()) {
                            user1Result = "WIN";
                            user2Result = "LOSE";
                        } else if ((onGame.getUser1Miss() == onGame.getUser2Miss())){
                            user1Result = "DRAW";
                            user2Result = "DRAW";
                        } else {
                            user1Result = "LOSE";
                            user2Result = "WIN";
                        }
                    } else {
                        user1Result = "LOSE";
                        user2Result = "WIN";
                    }
                    jsonObject.put("type", "gameOver");
                    JSONObject gameData = new JSONObject(onGame.toString());
                    userData.put("hits", gameData.getInt("user1Hits"));
                    userData.put("miss", gameData.getInt("user1Miss"));
                    userData.put("total", gameData.getInt("user1Hits") + gameData.getInt("user1Miss"));

                    userData.put("time",  (int) (endTime - gameData.getLong("startTime")) / 1000);
                    userData.put("result", user1Result);
                    jsonObject.put("data", userData);
                    UserPool.getInstance().getConnection(onGame.getSessionId1()).send(jsonObject.toString());                    jsonObject = new JSONObject();
                    jsonObject = new JSONObject();
                    userData = new JSONObject();
                    jsonObject.put("type", "gameOver");
                    userData.put("hits", gameData.getInt("user2Hits"));
                    userData.put("miss", gameData.getInt("user2Miss"));
                    userData.put("total", gameData.getInt("user2Hits") + gameData.getInt("user2Miss"));
                    userData.put("time", (int) (endTime - gameData.getLong("startTime")) / 1000);
                    userData.put("result", user2Result);
                    jsonObject.put("data", userData);
                    UserPool.getInstance().getConnection(onGame.getSessionId2()).send(jsonObject.toString());
                    break;
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