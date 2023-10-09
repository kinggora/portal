package kinggora.portal.controller;

import kinggora.portal.api.DataResponse;
import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.Member;
import kinggora.portal.domain.dto.PasswordDto;
import kinggora.portal.domain.dto.TokenInfo;
import kinggora.portal.exception.BizException;
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
    public DataResponse<Void> createMember(Member member) {
        memberService.register(member);
        return DataResponse.empty();
    }

    @GetMapping
    public DataResponse<Member> getMember() {
        Member member = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        member.setPassword(null);
        log.info("loginMember={}", member.getUsername());
        return DataResponse.of(member);
    }

    @PutMapping
    public DataResponse<Void> updateMember(Member member) {
        Member signInMember = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        member.setId(signInMember.getId());
        memberService.updateMember(member);
        return DataResponse.empty();
    }

    @PutMapping("/password")
    public DataResponse<Void> updatePassword(PasswordDto dto) {
        Member signInMember = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        if (!memberService.checkPassword(dto.getCurrentPassword(), signInMember)) {
            throw new BizException(ErrorCode.INVALID_PASSWORD);
        } else if (!memberService.checkPassword(dto.getNewPassword(), signInMember)) {
            throw new BizException(ErrorCode.DUPLICATE_PASSWORD);
        }
        memberService.updatePassword(dto.getNewPassword(), signInMember);
        return DataResponse.empty();
    }

    @PostMapping("/signin")
    public DataResponse<TokenInfo> signIn(Member member) {
        TokenInfo tokenInfo = memberService.signIn(member);
        log.info("access token={}", tokenInfo.getAccessToken());
        return DataResponse.of(tokenInfo);
    }
}
