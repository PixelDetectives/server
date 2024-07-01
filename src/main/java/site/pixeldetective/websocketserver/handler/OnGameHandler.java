package site.pixeldetective.websocketserver.handler;

import site.pixeldetective.websocketserver.ongame.OnGame;
import site.pixeldetective.websocketserver.ongame.OnGamePool;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class OnGameHandler {

    private String baseUri = "http://localhost:9000/pixel-detective/api";
    private static volatile OnGameHandler instance;

    private OnGameHandler() {

    }

    public static OnGameHandler getInstance() {
        if (instance == null) {
            instance = new OnGameHandler();
        }
        return instance;
    }

    public String getGameByRandomDifficulty() throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseUri + "/game/random"))
                .GET()
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
