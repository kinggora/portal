package kinggora.portal.controller;

import kinggora.portal.api.DataResponse;
import kinggora.portal.domain.Member;
import kinggora.portal.domain.dto.response.TokenInfo;
import kinggora.portal.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public DataResponse<TokenInfo> signIn(Member member) {
        TokenInfo tokenInfo = authService.signIn(member);
        log.info("access token={}", tokenInfo.getAccessToken());
        return DataResponse.of(tokenInfo);
    }
}
