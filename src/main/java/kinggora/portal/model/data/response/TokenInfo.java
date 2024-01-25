package kinggora.portal.model.data.response;

import lombok.*;

/**
 * JWT Response Object
 * 로그인 요청 성공 시 반환
 */
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TokenInfo {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
