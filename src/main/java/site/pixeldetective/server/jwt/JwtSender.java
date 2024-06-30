package site.pixeldetective.server.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// 로그인시
public class JwtSender {
    private static final String SECRET_KEY = "mySecretKey";
    private static final Set<String> tokenBlacklist = new HashSet<>();

    // 새로운 토큰을 만든다
    public static String createJWT(String username, int uNum, String uName, String uId) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        return JWT.create()
                .withIssuer("auth0")
                .withSubject(username)
                .withClaim("u_num", uNum)
                .withClaim("u_name", uName)
                .withClaim("u_id", uId)
                .withExpiresAt(new Date(System.currentTimeMillis() + 7200 * 1000)) // 2시간 후 만료
                .sign(algorithm);
    }

    // 토큰의 정보를 검증함
    public static boolean verifyJWT(String token) {
        if (tokenBlacklist.contains(token)) {
            return false; // 토큰이 블랙리스트에 있는지 확인
        }

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

    // 토큰의 담긴 정보를 복호화함
    public static DecodedJWT decodeJWT(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    public static void invalidateJWT(String token) {
        tokenBlacklist.add(token);
    }

    // JWT에서 uNum을 반환하는 메서드
    public static Integer getUNumFromJWT(String token) {
        DecodedJWT decodedJWT = decodeJWT(token);
        if (decodedJWT != null) {
            return decodedJWT.getClaim("u_num").asInt();
        }
        return null;
    }

    // JWT에서 uName을 반환하는 메서드
    public static String getUNameFromJWT(String token) {
        DecodedJWT decodedJWT = decodeJWT(token);
        if (decodedJWT != null) {
            return decodedJWT.getClaim("u_name").asString();
        }
        return null;
    }

    // JWT에서 uId를 반환하는 메서드
    public static String getUIdFromJWT(String token) {
        DecodedJWT decodedJWT = decodeJWT(token);
        if (decodedJWT != null) {
            return decodedJWT.getClaim("u_id").asString();
        }
        return null;
    }

    public static void main(String[] args) {
        JwtSender j = new JwtSender();

        String token = j.createJWT("user123", 1, "JohnDoe", "user123");

        System.out.println("Generated Token: " + token);

        boolean isValid = verifyJWT(token);

        System.out.println("Is token valid? " + isValid);

        DecodedJWT decodedJWT = decodeJWT(token);

        if (decodedJWT != null) {
            String username = decodedJWT.getSubject();
            int uNum = decodedJWT.getClaim("u_num").asInt();
            String uName = decodedJWT.getClaim("u_name").asString();
            String uId = decodedJWT.getClaim("u_id").asString();

            System.out.println("Decoded username: " + username);
            System.out.println("Decoded u_num: " + uNum);
            System.out.println("Decoded u_name: " + uName);
            System.out.println("Decoded u_id: " + uId);
        }

        // 각 정보를 반환하는 메서드 호출 예시
        System.out.println("uNum from JWT: " + getUNumFromJWT(token));
        System.out.println("uName from JWT: " + getUNameFromJWT(token));
        System.out.println("uId from JWT: " + getUIdFromJWT(token));

        // 토큰을 무효화
        invalidateJWT(token);

        // 무효화된 토큰 검증
        isValid = verifyJWT(token);

        System.out.println("Is token valid after invalidation? " + isValid);
    }
}
