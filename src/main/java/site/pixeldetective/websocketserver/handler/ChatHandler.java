package site.pixeldetective.websocketserver.handler;

import org.json.JSONObject;
import site.pixeldetective.websocketserver.userpool.UserPool;

public class ChatHandler {

    public ChatHandler() {

    }
    public static synchronized void receiveMessage(String nickname, String content) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nickname" , nickname);
            jsonObject.put("message", content);
            UserPool.getInstance().broadCastData("newChatMessage", jsonObject);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
