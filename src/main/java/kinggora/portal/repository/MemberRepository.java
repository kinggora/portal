package kinggora.portal.repository;

import kinggora.portal.domain.Member;
import kinggora.portal.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final MemberMapper mapper;

    /**
     * 회원 저장
     * @param member 저장할 회원 정보
     * @return 회원 id
     */
    public Integer saveMember(Member member) {
        mapper.saveMember(member);
        return member.getId();
    }

    /**
     * 회원 단건 조회 (id)
     * @param id 회원 id
     * @return 회원
     */
    public Optional<Member> findById(Integer id) {
        return mapper.findById(id);
    }

    /**
     * 회원 단건 조회 (username)
     * @param username 회원 로그인 id
     * @return 회원
     */
    public Optional<Member> findByUsername(String username) {
        return mapper.findByUsername(username);
    }

    /**
     * 동일한 username 을 가진 회원 존재 여부 확인
     * @param username 회원 로그인 id
     * @return true: 중복O / false: 중복X
     */
    public boolean checkDuplicateUsername(String username) {
        return mapper.checkDuplicateUsername(username);
    }

    /**
     * 로그인
     * @param member 로그인할 회원 정보 (username, password)
     * @return 로그인 사용자
     */
    public Optional<Member> signIn(Member member) {
        return mapper.signIn(member);
    }
}
