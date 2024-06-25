package site.pixeldetective.server.router;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import site.pixeldetective.server.dto.TestDTO;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.json.JSONArray;
import site.pixeldetective.server.dao.TestDAO;

public class HelloWorldHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        int statusCode = 200;
        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            TestDAO testDAO = new TestDAO();
            List<TestDTO> testList = testDAO.getAllTests();
            // JSON으로 변환
            JSONArray jsonArray = new JSONArray();
            for (TestDTO test : testList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", test.getId());
                jsonObject.put("name", test.getName());
                jsonArray.put(jsonObject);
            }
            response = jsonArray.toString();
        }  else {
            response = new JSONObject().put("error", "Unsupported request method").toString();
            statusCode = 404; // 405 Method Not Allowed
        }
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
