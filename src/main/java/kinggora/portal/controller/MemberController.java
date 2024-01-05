package kinggora.portal.controller;

import kinggora.portal.api.DataResponse;
import kinggora.portal.domain.Member;
import kinggora.portal.domain.dto.request.MemberDto;
import kinggora.portal.domain.dto.request.PasswordDto;
import kinggora.portal.domain.dto.response.MemberResponse;
import kinggora.portal.security.CustomUserDetails;
import kinggora.portal.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public DataResponse<Void> createMember(@Valid MemberDto dto) {
        memberService.createMember(dto);
        return DataResponse.empty();
    }

    @GetMapping
    public DataResponse<MemberResponse> getMember(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Member member = userDetails.getMember();
        log.info("loginMember={}", member.getUsername());
        return DataResponse.of(MemberResponse.of(member));
    }

    @PatchMapping
    public DataResponse<Void> updateMember(@Valid MemberDto dto,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.updateMember(userDetails.getId(), dto);
        return DataResponse.empty();
    }

    @PatchMapping("/password")
    public DataResponse<Void> updatePassword(@Valid PasswordDto dto,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.updatePassword(userDetails.getMember(), dto);
        return DataResponse.empty();
    }

    @DeleteMapping
    public DataResponse<Void> deleteMember(@AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.deleteMember(userDetails.getId());
        return DataResponse.empty();
    }

}
