package kinggora.portal.domain.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class TokenInfo {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
