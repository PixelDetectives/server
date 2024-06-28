package site.pixeldetective.server.router;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import site.pixeldetective.server.db.DBConnector;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class UserInfoHander implements HttpHandler {
    private static Connection conn = DBConnector.getConnection();
    public Map<String, String> getQuries(String queryString) {
        Map<String, String> queries = new HashMap<>();
        if (queryString != null) {
            String[] params = queryString.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];
                    System.out.println("key : " + key + " , value : " + value);
                    queries.put(key, value);
                }
            }
        }
        return queries;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("server receive");
        String response = "";
        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            URI uri = exchange.getRequestURI();
            System.out.println(uri.toString());
            Map<String, String> queries = getQuries(uri.getQuery());
            String sql = "select * from users where u_num = ?";
            try {
                System.out.println("connect to db  " + queries.get("uNum"));
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(queries.get("uNum")));
                ResultSet rs = pstmt.executeQuery();
                System.out.println("get result");
                JSONObject jsonObject = new JSONObject();
                System.out.println("make json");
                if (rs.next()) {
                    jsonObject.put("uNum", rs.getInt("u_num"));
                    jsonObject.put("uName", rs.getString("u_name"));
                }
                System.out.println("make json string");
                response = jsonObject.toString();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        System.out.println("send to client");
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}

