package kinggora.portal.repository;

import kinggora.portal.domain.Member;
import kinggora.portal.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final MemberMapper mapper;

    /**
     * 회원 저장
     *
     * @param member 저장할 회원 정보
     * @return 회원 id
     */
    public Integer saveMember(Member member) {
        mapper.saveMember(member);
        return member.getId();
    }

    /**
     * 회원 단건 조회 (id)
     *
     * @param id 회원 id
     * @return 회원
     */
    public Optional<Member> findMemberById(Integer id) {
        return mapper.findMemberById(id);
    }

    /**
     * 회원 단건 조회 (username)
     *
     * @param username 회원 로그인 id
     * @return 회원
     */
    public Optional<Member> findMemberByUsername(String username) {
        return mapper.findMemberByUsername(username);
    }

    /**
     * 회원 정보 수정
     *
     * @param member
     */
    public void updateMember(Member member) {
        if (mapper.updateMember(member) != 1) {
            log.error("fail MemberRepository.updateMember");
        }
    }

    public void updatePassword(Member member) {
        if (mapper.updatePassword(member) != 1) {
            log.error("fail MemberRepository.updatePassword");
        }
    }

    /**
     * 동일한 username 을 가진 회원 존재 여부 확인
     *
     * @param username 회원 로그인 id
     * @return true: 중복O / false: 중복X
     */
    public boolean existsUsername(String username) {
        return mapper.existsUsername(username);
    }
}
