package kinggora.portal.service;

import kinggora.portal.api.ErrorCode;
import kinggora.portal.domain.Member;
import kinggora.portal.domain.dto.request.MemberDto;
import kinggora.portal.domain.dto.request.PasswordDto;
import kinggora.portal.exception.BizException;
import kinggora.portal.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    /**
     * 회원 등록
     * 로그인 아이디 중복 시 BizException 발생
     *
     * @param dto 회원 등록 요청 데이터
     * @return 회원 id
     */
    public Integer createMember(MemberDto dto) {
        if (checkDuplicateUsername(dto.getUsername())) {
            throw new BizException(ErrorCode.DUPLICATE_USERNAME);
        }
        Member member = dto.toUser();
        member.encodedPassword(passwordEncoder);
        return memberRepository.saveMember(member);
    }

    /**
     * 회원 정보 수정
     *
     * @param memberId 수정할 회원의 id
     * @param dto      회원 업데이트 요청 데이터
     */
    public void updateMember(int memberId, MemberDto dto) {
        memberRepository.updateMember(dto.toUpdateMember(memberId));
    }

    /**
     * 회원 비밀번호 수정
     *
     * @param member 비밀번호를 수정할 회원
     * @param dto    비밀번호 업데이트 요청 데이터
     */
    public void updatePassword(Member member, PasswordDto dto) {
        if (!checkPassword(dto.getCurrentPassword(), member.getPassword())) {
            throw new BizException(ErrorCode.INVALID_PASSWORD);
        } else if (checkPassword(dto.getNewPassword(), member.getPassword())) {
            throw new BizException(ErrorCode.DUPLICATE_PASSWORD);
        }
        String encodedNewPassword = passwordEncoder.encode(dto.getNewPassword());
        memberRepository.updatePassword(member.getId(), encodedNewPassword);
    }

    public void deleteMember(int id) {
        Member member = findMemberById(id);
        if (member.isDeleted()) {
            throw new BizException(ErrorCode.ALREADY_DELETED_MEMBER);
        }
        memberRepository.deleteMember(id);
    }

    /**
     * 회원 단건 조회 (id)
     *
     * @param id 회원 id
     * @return 회원
     */
    public Member findMemberById(int id) {
        return memberRepository.findMemberById(id)
                .orElseThrow(() -> new BizException(ErrorCode.MEMBER_NOT_FOUND));
    }

    /**
     * 로그인 아이디 중복 검사
     *
     * @param username 회원 로그인 id
     * @return true: 중복O / false: 중복X
     */
    private boolean checkDuplicateUsername(String username) {
        return memberRepository.existsUsername(username);
    }

    /**
     * 비밀번호 확인
     *
     * @param rawPassword     암호화 되지 않은 비밀번호
     * @param encodedPassword 암호화된 비밀번호
     * @return true: 비밀번호 일치, false: 비밀번호 미일치
     */
    private boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
