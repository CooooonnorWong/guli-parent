package com.atguigu.guli.service.ucenter;

import io.jsonwebtoken.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

/**
 * @author Connor
 * @date 2022/8/2
 */
@SpringBootTest
public class JwtTest {

    private static final long EXPIRATION = 1000 * 60 * 60;
    private static final String PRIVATE_KEY = "0101010";

    @Test
    public void buildJwt() {
        String token = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .setHeaderParam("typ", "jwt")
                .setHeaderParam("alg", "HS256")
                .setSubject("GULI_TOKEN")
                .claim("nickname", "Connor")
                .claim("avatar", "ss.jpg")
                .signWith(SignatureAlgorithm.HS256, PRIVATE_KEY)
                .compact();
        System.out.println(token);
    }

    @Test
    public void parseJwt() {
        String token = "eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTk0MzkxODYsInN1YiI6IkdVTElfVE9LRU4iLCJuaWNrbmFtZSI6IkNvbm5vciIsImF2YXRhciI6InNzLmpwZyJ9.tZu5SDbNwb-SsY2SB0KFxumLEPQyu_Y6ZEizkNcwTv8";
        Jwt jwt = Jwts.parser().setSigningKey(PRIVATE_KEY).parse(token);
        Header header = jwt.getHeader();
        Object body = jwt.getBody();
        System.out.println(header.get("typ"));
        System.out.println(header.get("alg"));
        System.out.println(body);

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(PRIVATE_KEY).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        String nickname = (String) claims.get("nickname");
        String avatar = (String) claims.get("avatar");
        System.out.println("claims = " + claims);
        System.out.println(nickname);
        System.out.println(avatar);
    }

    @Test
    public void testBase64() {
        String str = "张三";
        byte[] encode = Base64.getEncoder().encode(str.getBytes(StandardCharsets.UTF_8));
        System.out.println(new String(encode));

        byte[] decode = Base64.getDecoder().decode(encode);
        System.out.println(new String(decode, StandardCharsets.UTF_8));
    }

    @Test
    public void testUrlEncoder() throws UnsupportedEncodingException {

        String url = "http://localhost:8110/edu/teacher?name=张三";
        String encodeUrl = URLEncoder.encode(url, "UTF-8");
        System.out.println(encodeUrl);

        String decodeUrl = URLDecoder.decode(url, "UTF-8");
        System.out.println(decodeUrl);
    }

}
