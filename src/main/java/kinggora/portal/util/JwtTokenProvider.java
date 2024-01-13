package kinggora.portal.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kinggora.portal.domain.dto.response.TokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@PropertySource("classpath:/application-jwt.properties")
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    @Value("${jwt.access-token-expire-time}")
    private long ACCESS_TOKEN_EXPIRE_TIME; //30분
    @Value("${jwt.refresh-token-expire-time}")
    private long REFRESH_TOKEN_EXPIRE_TIME; //14일
    private final Key key;

    /**
     * 토큰 암호화 키, 유효 시간 초기화
     *
     * @param secretKey
     */
    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * accessToken, refreshToken 생성
     *
     * @param username
     * @param authorities
     * @return
     */
    public TokenInfo generateToken(String username, String authorities) {
        long now = (new Date()).getTime();
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(username)
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpiresIn)     //만료 시간
                .signWith(key, SignatureAlgorithm.HS256) //비밀 키, 해싱 알고리즘
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenInfo.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * JWT 토큰을 복호화하여 인증 정보(username) 추출
     *
     * @param accessToken
     * @return
     */
    public String extractUsername(String accessToken) {
        Claims claims = parseClaims(accessToken);
        return claims.getSubject();
    }

    /**
     * 토큰 복호화
     *
     * @param accessToken
     * @return 복호화된 정보
     */
    private Claims parseClaims(String accessToken) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
    }

    /**
     * 토큰 복호화
     *
     * @param accessToken
     * @return 복호화된 정보
     */
    public boolean validateToken(String accessToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JwtTokenProvider.validateToken", e);
            return false;
        }
        return true;
    }
}
