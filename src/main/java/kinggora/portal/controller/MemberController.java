package kinggora.portal.controller;

import kinggora.portal.controller.api.data.DataResponse;
import kinggora.portal.domain.Member;
import kinggora.portal.model.request.MemberDto;
import kinggora.portal.model.response.MemberResponse;
import kinggora.portal.security.user.CustomUserDetails;
import kinggora.portal.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 회원 API 컨트롤러
 * 회원에 대한 API 요청의 응답을 JSON 형태로 반환
 * 응답 객체: DataResponse
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 등록 요청 처리
     *
     * @param dto 회원 등록 데이터
     * @return Empty API Response
     */
    @PostMapping
    public DataResponse<Void> createMember(@Valid MemberDto.Create dto) {
        memberService.createMember(dto);
        return DataResponse.empty();
    }

    /**
     * 회원 정보 조회 요청 처리
     * 인증 객체로부터 로그인한 회원 정보 조회
     *
     * @param userDetails 인증 객체
     * @return 회원 정보 API Response
     */
    @GetMapping
    public DataResponse<MemberResponse> getMember(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Member member = userDetails.getMember();
        return DataResponse.of(MemberResponse.from(member));
    }

    /**
     * 회원 정보 수정 요청 처리
     *
     * @param dto         회원 수정 데이터
     * @param userDetails 인증 객체
     * @return Empty API Response
     */
    @PatchMapping
    public DataResponse<Void> updateMember(@Valid MemberDto.Update dto,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.updateMember(userDetails.getMember(), dto);
        return DataResponse.empty();
    }

    /**
     * 비밀번호 수정 요청 처리
     *
     * @param dto         비밀번호 수정 데이터
     * @param userDetails 인증 객체
     * @return Empty API Response
     */
    @PatchMapping("/password")
    public DataResponse<Void> updatePassword(@Valid MemberDto.PasswordUpdate dto,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.updatePassword(userDetails.getMember(), dto);
        return DataResponse.empty();
    }

    /**
     * 회원 탈퇴 요청 처리
     *
     * @param userDetails 인증 객체
     * @return Empty API Response
     */
    @DeleteMapping
    public DataResponse<Void> deleteMember(@AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.deleteMember(userDetails.getMember());
        return DataResponse.empty();
    }

}
