package kinggora.portal.controller;

import kinggora.portal.api.DataResponse;
import kinggora.portal.domain.Member;
import kinggora.portal.domain.dto.TokenInfo;
import kinggora.portal.service.MemberService;
import kinggora.portal.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public DataResponse<Void> createMember(@RequestBody Member member) {
        memberService.register(member);
        return DataResponse.empty();
    }

    @GetMapping
    public DataResponse<Member> getMember() {
        Member member = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        member.setPassword(null);
        return DataResponse.of(member);
    }

    @PostMapping("/signin")
    public TokenInfo signIn(@RequestBody Member member) {
        TokenInfo tokenInfo = memberService.signIn(member);
        log.info("access token={}", tokenInfo.getAccessToken());
        return tokenInfo;
    }
}
