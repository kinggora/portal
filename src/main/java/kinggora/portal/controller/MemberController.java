package kinggora.portal.controller;

import kinggora.portal.api.DataResponse;
import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.Member;
import kinggora.portal.domain.dto.request.MemberDto;
import kinggora.portal.domain.dto.request.PasswordDto;
import kinggora.portal.domain.dto.response.TokenInfo;
import kinggora.portal.exception.BizException;
import kinggora.portal.service.MemberService;
import kinggora.portal.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public DataResponse<Void> createMember(@Valid MemberDto member) {
        memberService.register(member.toUser());
        return DataResponse.empty();
    }

    @GetMapping
    public DataResponse<Member> getMember() {
        Member member = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        member.removePassword();
        log.info("loginMember={}", member.getUsername());
        return DataResponse.of(member);
    }

    @PutMapping
    public DataResponse<Void> updateMember(@Valid MemberDto member) {
        Member signInMember = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        memberService.updateMember(member.toUpdateMember(signInMember.getId()));
        return DataResponse.empty();
    }

    @PutMapping("/password")
    public DataResponse<Void> updatePassword(PasswordDto dto) {
        Member signInMember = memberService.findMemberByUsername(SecurityUtil.getCurrentUsername());
        if (!memberService.checkPassword(dto.getCurrentPassword(), signInMember.getPassword())) {
            throw new BizException(ErrorCode.INVALID_PASSWORD);
        } else if (memberService.checkPassword(dto.getNewPassword(), signInMember.getPassword())) {
            throw new BizException(ErrorCode.DUPLICATE_PASSWORD);
        }
        memberService.updatePassword(signInMember.getId(), dto.getNewPassword());
        return DataResponse.empty();
    }

    @PostMapping("/signin")
    public DataResponse<TokenInfo> signIn(Member member) {
        TokenInfo tokenInfo = memberService.signIn(member);
        log.info("access token={}", tokenInfo.getAccessToken());
        return DataResponse.of(tokenInfo);
    }
}
