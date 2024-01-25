package kinggora.portal.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import kinggora.portal.exception.ErrorCode;
import kinggora.portal.model.data.response.TokenInfo;
import kinggora.portal.security.exception.JwtAuthException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JWT 관련 유틸리티 클래스
 * JWT 생성, 유효성 검증, 암호화, 복호화 수행
 * properties 파일로부터 비밀 키, 토큰 유효기간 설정 초기화
 */
@Component
@PropertySource("classpath:/application-jwt.properties")
public class JwtProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private final long accessTokenExpireTime;
    private final long refreshTokenExpireTime;
    private final Key key;

    /**
     * 토큰 암호화 키, 유효 시간 초기화
     * BASE64 형식으로 인코딩한 비밀 키 기반의 토큰 암호화 키 생성
     *
     * @param secretKey 비밀 키
     */
    public JwtProvider(@Value("${jwt.secret-key}") String secretKey,
                       @Value("${jwt.access-token-expire-time}") long accessTokenExpireTime,
                       @Value("${jwt.refresh-token-expire-time}") long refreshTokenExpireTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    /**
     * JWT 응답 객체(TokenInfo) 생성
     * Jwts Builder를 통해 accessToken, refreshToken 생성
     * claim(name-value): 'sub'-username, 'auth'-authorities
     * signWith: 서명에 사용할 암호화 키와 해싱 알고리즘
     * expiration: 유효기간
     *
     * @param username    회원 식별자
     * @param authorities 회원 권한
     * @return JWT 응답 객체
     */
    public TokenInfo generateToken(String username, String authorities) {
        long now = (new Date()).getTime();
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + accessTokenExpireTime);
        String accessToken = Jwts.builder()
                .setSubject(username)
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + refreshTokenExpireTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenInfo.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * 토큰 Claims 추출
     * 1. JWT Parser 생성: 토큰 서명 검증에 사용할 암호화 키 설정
     * 2. JWT 파싱: body(claims), signature 분리
     * 3. Claims 반환
     *
     * @param token Claims 추출할 토큰
     * @return token의 Claims 객체
     */
    private Claims parseClaims(String token) {
        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        return jwtParser.parseClaimsJws(token).getBody();
    }

    /**
     * 토큰 유효성 검증
     * 토큰 복호화 시도 -> 예외 발생 여부 확인
     * 토큰 구조/형식, 서명 무결성, 유효기간 검증
     *
     * @param token 검증할 토큰
     * @throws JwtAuthException 검증 시도 중 발생하는 예외를 커스텀 예외로 변환
     */
    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException e) {
            if (e instanceof MalformedJwtException || e instanceof IllegalArgumentException) {
                throw new JwtAuthException(ErrorCode.INVALID_AUTH_TOKEN, e);
            } else if (e instanceof ExpiredJwtException) {
                throw new JwtAuthException(ErrorCode.EXPIRED_AUTH_TOKEN, e);
            } else if (e instanceof UnsupportedJwtException) {
                throw new JwtAuthException(ErrorCode.UNSUPPORTED_AUTH_TOKEN, e);
            } else if (e instanceof SignatureException) {
                throw new JwtAuthException(ErrorCode.INVALID_TOKEN_SIGNATURE, e);
            } else {
                throw new JwtAuthException(ErrorCode.INTERNAL_SERVER_ERROR, e);
            }
        }
    }

    /**
     * JWT Claim에서 인증 정보(username) 추출
     * 토큰의 sub Claims 에 저장한 username 반환
     * 존재하지 않는다면 null 반환
     *
     * @param token username을 추출할 JWT
     * @return username or null
     */
    public String extractUsername(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }
}
