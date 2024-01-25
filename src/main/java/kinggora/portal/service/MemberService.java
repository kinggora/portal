package kinggora.portal.service;

import kinggora.portal.controller.api.error.ErrorCode;
import kinggora.portal.domain.Member;
import kinggora.portal.exception.BizException;
import kinggora.portal.model.request.MemberDto;
import kinggora.portal.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 회원 서비스
 * 회원 등록, 수정, 삭제 등 회원 관련 서비스
 */
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    /**
     * 회원 등록
     * 1. username 중복 여부 확인
     * 2. dto를 도메인 객체로 변환 -> 리포지토리 호출
     *
     * @param dto 회원 등록 요청 데이터
     * @return 회원 id
     * @throws BizException 이미 존재하는 username인 경우 발생
     */
    public Integer createMember(MemberDto.Create dto) {
        if (checkDuplicateUsername(dto.getUsername())) {
            throw new BizException(ErrorCode.DUPLICATE_USERNAME);
        }
        Member member = dto.toUser(passwordEncoder);
        return memberRepository.save(member);
    }

    /**
     * 회원 정보 수정
     * 1. 수정할 회원 유효성 검증
     * 2. dto를 도메인 객체로 변환 -> 리포지토리 호출
     *
     * @param member 수정할 회원
     * @param dto    회원 업데이트 요청 데이터
     */
    public void updateMember(Member member, MemberDto.Update dto) {
        validateMember(member);
        memberRepository.update(dto.toMember(member.getId()));
    }

    /**
     * 회원 비밀번호 수정
     * 1. 수정할 회원 유효성 검증
     * 2. 기존 비밀번호가 입력과 동일한지 확인
     * 3. 새 비밀번호가 기존 비밀번호와 동일한지 확인
     * 4. dto를 도메인 객체로 변환 -> 리포지토리 호출
     *
     * @param member 비밀번호를 수정할 회원
     * @param dto    비밀번호 업데이트 요청 데이터
     * @throws BizException 기존 비밀번호가 입력과 동일하지 않거나, 새 비밀번호가 기존 비밀번호와 동일한 경우 발생
     */
    public void updatePassword(Member member, MemberDto.PasswordUpdate dto) {
        validateMember(member);
        if (!checkPassword(dto.getCurrentPassword(), member.getPassword())) {
            throw new BizException(ErrorCode.INVALID_PASSWORD);
        } else if (checkPassword(dto.getNewPassword(), member.getPassword())) {
            throw new BizException(ErrorCode.DUPLICATE_PASSWORD);
        }
        memberRepository.update(dto.toMember(member.getId(), passwordEncoder));
    }

    /**
     * 회원 탈퇴
     * 1. 회원 유효성 검증
     * 2. 리포지토리 호출
     *
     * @param member 탈퇴할 회원
     */
    public void deleteMember(Member member) {
        validateMember(member);
        memberRepository.deleteById(member.getId());
    }

    /**
     * 로그인 아이디 중복 검사
     * 동일한 username이 존재하는지 확인
     *
     * @param username 회원 로그인 아이디
     * @return true: 중복O, false: 중복X
     */
    private boolean checkDuplicateUsername(String username) {
        return memberRepository.existsByUsername(username);
    }

    /**
     * 암호화되지 않은 비밀번호가 암호화된 비밀번호와 일치하는지 확인
     * 실제 검증은 PasswordEncoder.matches()에 위임
     *
     * @param rawPassword     암호화 되지 않은 비밀번호
     * @param encodedPassword 암호화된 비밀번호
     * @return true: 비밀번호 일치, false: 비밀번호 미일치
     */
    private boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 회원 유효성 검증
     *
     * @param member 검증할 회원
     * @throws BizException 탈퇴한 회원인 경우 발생
     */
    private void validateMember(Member member) {
        if (member.isDeleted()) {
            throw new BizException(ErrorCode.ALREADY_DELETED_MEMBER);
        }
    }

}
