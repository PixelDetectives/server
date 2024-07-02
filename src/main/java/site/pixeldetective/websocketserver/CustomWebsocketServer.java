package site.pixeldetective.websocketserver;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONArray;
import org.json.JSONObject;
import site.pixeldetective.server.jwt.JwtSender;
import site.pixeldetective.websocketserver.gameroom.GameRoom;
import site.pixeldetective.websocketserver.gameroom.GameRoomPool;
import site.pixeldetective.websocketserver.handler.ChatHandler;
import site.pixeldetective.websocketserver.handler.OnGameHandler;
import site.pixeldetective.websocketserver.handler.WebSocketGameHandler;
import site.pixeldetective.websocketserver.handler.WebSocketHandler;
//import site.pixeldetective.websocketserver.httpconnect.HttpConnector;
import site.pixeldetective.websocketserver.match.MatchPool;
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

    @Override
    public void onOpen(WebSocket conn, ClientHandshake clientHandshake) {
        System.out.println("onOpen start");
        try {
            int sessionId = conn.hashCode();
            System.out.println(conn.getRemoteSocketAddress());
            String resourceDescriptor = clientHandshake.getResourceDescriptor();
            System.out.println(resourceDescriptor);

            // 쿼리 파라미터를 파싱
            String uId = null;
            String uName = null;

            if (resourceDescriptor != null && resourceDescriptor.contains("?")) {
                String query = resourceDescriptor.split("\\?")[1];
                String[] params = query.split("&");

                for (String param : params) {
                    String[] keyValue = param.split("=");
                    if (keyValue.length == 2) {
                        String key = keyValue[0];
                        String value = keyValue[1];
                        if (key.equals("u_id")) {
                            uId = value;
                        } else if (key.equals("u_name")) {
                            uName = value;
                        }
                    }
                }
            }

            System.out.println("u_id:" + uId);
            System.out.println("u_name:" + uName);
            System.out.println(conn.hashCode() + " " + uId + " " + uName);

            if (uId != null && uName != null) {
                CurrentUser currentUser = new CurrentUser(uId, uName, "join");
                UserPool.getInstance().addUser(sessionId, conn, currentUser);

                WebSocketGameHandler.braodCaseCurrentRoomList();
                System.out.println("New client connected : " + conn.getRemoteSocketAddress());
            } else {
                System.out.println("u_id 또는 u_name 값이 없습니다.");
            }
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
                    currentUser.setuName(JwtSender.getUNameFromJWT(request.getString("token")));
                    currentUser.setuId(JwtSender.getUIdFromJWT(request.getString("token")));
                    System.out.println(currentUser.getuName());
                    System.out.println(currentUser.getuId());
                    String nickname = currentUser.getuName();
                    ChatHandler.receiveMessage(nickname, content);
                    //conn.send("receive chat " + nickname + " " + content);
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
//                    int currentUserSessionId1 = request.getInt("currentUserSessionId1");
////                  WebSocketHandler.currentUserStatusMatching(conn.hashCode(), conn);
//                    GameRoom gameRoom = WebSocketGameHandler.getGameRoomBySessionId1(currentUserSessionId1);
//                    gameRoom.setCurrentUser2(conn.hashCode());
//                    if (gameRoom == null) {
//                        conn.send("could not found");
//                    } else {
//                        int currentUser1 = gameRoom.getCurrentUser1();
//                        int currentUser2 = gameRoom.getCurrentUser2();
//                        jsonObject = new JSONObject();
//                        jsonObject.put("type", "gameStart");
//                        jsonObject.put("status", "success");
//                        UserPool.getInstance().getConnection(currentUser1).send(jsonObject.toString());
//                        UserPool.getInstance().getConnection(currentUser2).send(jsonObject.toString());
//                        int gameRoomId = gameRoom.hashCode();
//                        OnGame onGame = OnGamePool.createOnGame(gameRoomId, currentUser1, currentUser2, gameRoom.getDifficulty());
//                        jsonObject = new JSONObject();
//                        jsonObject.put("type", "gameData");
//                        JSONObject gameData = new JSONObject(onGame.toString());
//                        jsonObject.put("data", gameData);
//                        UserPool.getInstance().getConnection(currentUser1).send(jsonObject.toString());
//                        UserPool.getInstance().getConnection(currentUser2).send(jsonObject.toString());
//                        WebSocketGameHandler.deleteGameRoomBySessionId1(currentUserSessionId1);
//                    }
                    break;
                case "roomDelete":
                    int currentUserSessionId = request.getInt("currentUserSessionId");
                    conn.send("server close room");
                    System.out.println("server try roomDelete");
                case "gameOver":
                    JSONObject userData = new JSONObject(request.getString("data"));
                    System.out.println("userData : " + userData.toString());
                    long endTime = System.currentTimeMillis();
                    long startTime = OnGamePool.getOnGameByOnGameId(conn.hashCode()).getStartTime();
                    MatchPool.getInstance().setMyData(conn.hashCode(), userData);
                    WebSocket matchConn = UserPool.getInstance().getMatchedUser(conn.hashCode());
                    JSONObject gameResult = new JSONObject();
                    gameResult.put("type", "gameResult");
                    if (MatchPool.getInstance().isSessionIdInMatchPool(matchConn.hashCode())) {
                        JSONObject otherData = MatchPool.getInstance().getOtherData( matchConn.hashCode());
                        JSONObject gameData = new JSONObject();
                        gameData.put("myHits", userData.getInt("hits"));
                        gameData.put("myMiss", userData.getInt("miss"));
                        gameData.put("otherHits", otherData.getInt("hits"));
                        gameData.put("otherMiss", otherData.getInt("miss"));
                        gameData.put("time", endTime - startTime);
                        gameData.put("myNickName", UserPool.getInstance().getUser(conn.hashCode()).getuName());
                        gameData.put("otherNickName", UserPool.getInstance().getUser(matchConn.hashCode()).getuName());
                        gameResult.put("data", gameData.toString());
                        conn.send(gameResult.toString());
                        gameData = new JSONObject();
                        gameData.put("myHits", otherData.getInt("hits"));
                        gameData.put("myMiss", otherData.getInt("miss"));
                        gameData.put("otherHits", userData.getInt("hits"));
                        gameData.put("otherMiss", userData.getInt("miss"));
                        gameData.put("otherNickName", UserPool.getInstance().getUser(conn.hashCode()).getuName());
                        gameData.put("myNickName", UserPool.getInstance().getUser(matchConn.hashCode()).getuName());
                        gameResult.put("data", gameData.toString());
                        matchConn.send(gameResult.toString());
                    }
                    break;
                case "quickMatching":
                    System.out.println("quickMatching " + "by " + conn.hashCode());
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("type", "quickMatching");
                    UserPool.getInstance().currentUserStatusMatching(conn.hashCode(), conn);
                    WebSocket otherConn = UserPool.getInstance().getUserStatusMatching(conn);
                    if (otherConn != null) {
                        CurrentUser otherUser = UserPool.getInstance().getUser(otherConn.hashCode());
                        CurrentUser hostUser = UserPool.getInstance().getUser(conn.hashCode());
                        String gameQuickMatchingString = OnGameHandler.getInstance().getGameByRandomDifficulty();
                        JSONObject gameQuickMatching = new JSONObject(gameQuickMatchingString);
                        gameQuickMatching.put("myName", hostUser.getuName());
                        gameQuickMatching.put("otherName", otherUser.getuName());

                        jsonObject1.put("data", gameQuickMatching.toString());
                        conn.send(jsonObject1.toString());
                        gameQuickMatching.put("myName", otherUser.getuName());
                        gameQuickMatching.put("otherName", hostUser.getuName());
                        jsonObject1.put("data", gameQuickMatching.toString());
                        System.out.println(jsonObject1.toString());
                        otherConn.send(jsonObject1.toString());
                        UserPool.getInstance().setMatchedUsers(conn.hashCode(), otherConn.hashCode(), gameQuickMatchingString);

                    }
                    break;
                case "gamePoint":
                    JSONObject data = request.getJSONObject("data");
                    int x = data.getInt("x");
                    int y = data.getInt("y");

                    JSONObject resultGamePointer = new JSONObject();
                    resultGamePointer.put("x", x);
                    resultGamePointer.put("y", y);

                    Integer matchedUserId = UserPool.getInstance().matchedUsers.get(conn.hashCode());
                    JSONObject jsonObject2 = OnGamePool.checkAndReturnResult(conn.hashCode(), matchedUserId, x, y);
                    JSONObject retPointer = new JSONObject();
                    retPointer.put("type", "pointResult");
                    retPointer.put("data", jsonObject2.toString());
                    if (matchedUserId != null) {
                        WebSocket matchedConn = UserPool.getInstance().getConnection(matchedUserId);
                        matchedConn.send(retPointer.toString());
                    }
                    conn.send(retPointer.toString());
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
    public void onStart() {}
}


//{
//    "data":"" +
//        "[{\"r_id\":\"{\\\"uId\\\":1,\\\"uName\\\":\\\"1\\\",\\\"status\\\":\\\"1\\\"}\",\"r_difficulty\":0,\"r_name\":\"이것은 방 이름 입니다.\"}," +
//        "{\"r_id\":\"{\\\"uId\\\":1,\\\"uName\\\":\\\"1\\\",\\\"status\\\":\\\"1\\\"}\",\"r_difficulty\":0,\"r_name\":\"이것은 방 이름입니다.\"}]",
//        "type":"currentRooms"}