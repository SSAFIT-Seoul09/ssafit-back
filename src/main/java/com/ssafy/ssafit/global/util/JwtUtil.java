package com.ssafy.ssafit.global.util;

import com.ssafy.ssafit.global.exception.ErrorCode;
import com.ssafy.ssafit.global.util.exception.CookieException;
import com.ssafy.ssafit.global.util.exception.TokenException;
import com.ssafy.ssafit.user.domain.model.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Slf4j(topic = "JwtUtil") // Logger를 가져오는 어노테이션
@Component
public class JwtUtil {

    // 토큰 블랙리스트 메모리. Redis설정을 해주면 그때 바꾸자.
    private final Set<String> blacklistedTokens = new HashSet<>(); // 블랙리스트 저장 (임시로 메모리 사용)

    // Header KEY 값. 지금은 쿠키의 네임값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY. role에 대한 값
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자. 규칙같은 것이라 지키는게 좋다. Bearer가 붙어있으면 토큰이라는 표시이다. bearer뒤에 공백을 붙여준다.
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간 (밀리세컨트 기준이다. 60분 * 60초 * 1000L)
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey. application.properties에 있는 시크릿키를 불러온다.
    private String secretKey;
    private Key key;    // JWT 만드는 방법 중 제일 최신 방법이다. jwt 0.11.5버전. 시크릿 키를 Key에 담아서 암호화, 복호화에 사용할 예정이다.
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;   // HS256알고리즘을 사용할것이다.

    // 로그 설정. 로그를 찍을때 사용. 로깅이란, 어플리케이션이 작동할때, 프로그램의 상태나 동작을 시간순으로 기록하는걸 로깅이라고 한다. @SLf4j는 어노테이션으로 가져오기. 이 방법은 수동으로 가져오기.
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);  //base64로 인코딩 된 키를 사용중이기 때문에 디코딩을 해주고 key로 사용해야한다.
        key = Keys.hmacShaKeyFor(bytes);    // 디코딩했으니 바이트값을 변환하여 사용하려는것이다.
        log.info("JWT 키 초기화 완료");
    }

    // JWT 생성(토큰 생성)
    public String createToken(Long userId, String nickname, UserRole role) throws TokenException {
        Date date = new Date();

        log.info("JWT 토큰 생성 완료: userId={}, role={}, issuedAt={}", userId, role);
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(Long.toString(userId)) // 사용자 식별자값(ID). userId를 string으로 변환해서 넣어줌.
                        .claim("nickname", nickname)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한. claim이라 하고, key value를 넣어준다.
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간 (현재시간으로부터 60분 지속되게 하기 위함)
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘. 우리의 키와 알고리즘으로 암호화가 진행된다.
                        .compact();
    }

    // 생성된 JWT를 Cookie에 저장
    public void addJwtToCookie(String token, HttpServletResponse res) throws CookieException {
        token = URLEncoder.encode(token, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token);   // Name-Value. header와 토큰을 값으로 넣어줌
        cookie.setPath("/");
        cookie.setHttpOnly(true);      // XSS 방어
        cookie.setSecure(true);        // HTTPS에서만 전송
        cookie.setMaxAge(60 * 60);     // 1시간 유효

        res.addCookie(cookie);
        log.debug("JWT 토큰을 쿠키에 저장 완료: {}", cookie.getName());
    }

    // Cookie에 들어있던 JWT 토큰을 Substring(토큰 앞에 Bearer와 공백을 붙여주기 때문에 토큰을 가져올때에는 substring을 해서 앞에 부분을 제거해서 사용해야한다.
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7); // Bearer + " " 총 7개.
        }
        log.error("유효하지 않은 토큰 형식 (Bearer 없음)");
        throw TokenException.of(ErrorCode.TOKEN_NOT_FOUND, "유효하지 않은 토큰 형식입니다.");
    }

    // JWT 검증(토큰 검증)
    public boolean validateToken(String token) {
        try {
            // JWT 토큰 검증
            Jwts.parserBuilder()
                    .setSigningKey(key)  // 서명 키 설정
                    .build()
                    .parseClaimsJws(token);  // 토큰 파싱 및 검증

            log.debug("JWT 토큰 유효성 검증 성공");
            return true;  // 검증 성공
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            // 서명 문제 또는 변조된 토큰
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
            throw TokenException.of(ErrorCode.INVALID_TOKEN_SIGNATURE, "유효하지 않는 JWT 서명 입니다");
        } catch (ExpiredJwtException e) {
            // 만료된 토큰
            logger.error("Expired JWT token, 만료된 JWT token 입니다.", e);
            throw TokenException.of(ErrorCode.EXPIRED_TOKEN, "만료된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            // 지원되지 않는 토큰
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
            throw TokenException.of(ErrorCode.UNSUPPORTED_TOKEN, "지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            // 잘못된 토큰
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.", e);
            throw TokenException.of(ErrorCode.INVALID_JWT_CLAIMS, "잘못된 JWT 토큰입니다.");
        }
    }

    // JWT에서 시용자 정보 가져오기(토큰에서 정보 가져오기)
    public Claims getUserInfoFromToken(String token) {
        log.debug("JWT 토큰에서 사용자 정보 추출 시작");
        // 검증할때와 비슷하다. 왜냐하면 가져오는 과정도 토큰을 까봐야하기 때문. Jwt는 Claim기반 웹 토큰이다.
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // HttpServletRequest 에서 Cookie Value : JWT 가져오기
    public String getTokenFromRequest(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    log.debug("JWT 쿠키 발견: {}, {}", cookie.getName(), cookie.getValue());
                    return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8); // Encode 되어 넘어간 Value 다시 Decode
                }
            }
        }
        log.warn("JWT 토큰를 찾을 수 없음");
        throw CookieException.of(ErrorCode.TOKEN_NOT_FOUND);
    }

    // 토큰 블랙리스트 여부 확인
    public boolean isTokenBlacklisted(String token) {
        log.debug("블랙리스트 조회 결과: {} → {}", token);
        return blacklistedTokens.contains(token);
    }

    // 블랙리스트에 토큰 추가
    public void addBlacklistToken(String token) {
        // 토큰 Bearer제거
        String subStringToken = substringToken(token);
        blacklistedTokens.add(subStringToken);
        log.info("토큰 블랙리스트에 추가됨: {}", subStringToken);
    }
}