package site.pixeldetective.server.router;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import site.pixeldetective.server.dao.AnswerDAO;
import site.pixeldetective.server.dao.GameDAO;
import site.pixeldetective.server.dto.AnswerDTO;
import site.pixeldetective.server.dto.GameDTO;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public class RandomGameHander implements HttpHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            try {
                CompletableFuture<GameDTO> gameFuture = CompletableFuture.supplyAsync(() -> {
                    try {
                        return new GameDAO().randomGames();
                    } catch (SQLException e) {
                        throw new CompletionException(e);
                    }
                });

                CompletableFuture<List<AnswerDTO>> answersFuture = gameFuture.thenCompose(game ->
                        CompletableFuture.supplyAsync(() -> {
                            try {
                                return new AnswerDAO().selectAnswer(game.getG_num());
                            } catch (SQLException e) {
                                throw new CompletionException(e);
                            }
                        })
                );

                CompletableFuture<JSONObject> resultFuture = gameFuture.thenCombine(answersFuture, (game, answers) -> {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("gName", game.getG_name());
                    jsonObject.put("gNum", game.getG_num());
                    jsonObject.put("gImage1", game.getG_image1());
                    jsonObject.put("gImage2", game.getG_image2());
                    jsonObject.put("gDifficulty", game.getG_difficulty());

                    JSONArray answersArray = new JSONArray();
                    for (AnswerDTO answer : answers) {
                        JSONObject answerObject = new JSONObject();
                        answerObject.put("aNum", answer.getA_num());
                        answerObject.put("aX", answer.getA_x());
                        answerObject.put("aY", answer.getA_y());
                        answerObject.put("aRadius", answer.getA_radius());
                        answersArray.put(answerObject);
                    }
                    jsonObject.put("answers", answersArray);
                    return jsonObject;
                });

                JSONObject result = resultFuture.get();  // 여기서 블로킹이 발생합니다.
                response = result.toString();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
