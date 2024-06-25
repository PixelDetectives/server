package site.pixeldetective.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import site.pixeldetective.server.router.IndexRouter;

public class Main {
    static final int PORT = 9000;
    public static void main(String[] args) {
        try {
            // 서버를 지정된 포트에서 시작합니다.
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

            // 루트 경로("/")에 대한 요청을 처리할 핸들러를 설정합니다.
            server.createContext("/", new IndexRouter());

            // 서버가 요청을 처리할 수 있도록 합니다.
            server.setExecutor(null); // 기본 실행기를 사용합니다.
            server.start();

            System.out.println("Server started on port " + PORT);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}