package site.pixeldetective.server.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;

public class JwtSender {
    private static final String SECRET_KEY = "mySecretKey";

    private static String createJWT(String username) {
        // HMAC256 알고리즘을 사용하여 서명하기 위한 알고리즘 객체 생성
        // SECRET_KEY는 서명에 사용되는 비밀 키로, 외부에 노출되지 않도록 주의해야 합니다.
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        // JWT 생성기 시작
        return JWT.create()
                // 토큰 발급자(issuer) 설정
                .withIssuer("auth0")
                // 토큰의 주제(subject) 설정, 여기서는 username을 사용
                .withSubject(username)
                // 토큰 만료 시간 설정, 현재 시간으로부터 1시간 후로 설정
                .withExpiresAt(new Date(System.currentTimeMillis() + 7200 * 1000))  // 1시간 후 만료
                // 앞서 설정한 알고리즘으로 토큰 서명
                .sign(algorithm);
    }

    private static boolean verifyJWT(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }
    // JWT 토큰에서 주제(subject)를 추출하는 메서드
    private static String decodeJWT(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        } catch (JWTVerificationException exception) {
            // 검증 및 디코딩에 실패하면 null 반환
            return null;
        }
    }

    public static void main(String[] args) {
        // JwtSender 인스턴스 생성
        JwtSender j = new JwtSender();

        // 사용자 이름을 사용하여 JWT 토큰 생성
        String token = j.createJWT("username123");

        // 생성된 토큰 출력
        System.out.println("Generated Token: " + token);

        // 생성된 토큰을 검증
        boolean isValid = verifyJWT(token);

        // 검증 결과 출력
        System.out.println("Is token valid? " + isValid);

        // 토큰에서 주제(subject) 추출
        String username = decodeJWT(token);

        // 추출된 주제 출력
        System.out.println("Decoded username: " + username);
    }
}