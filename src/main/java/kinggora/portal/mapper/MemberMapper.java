package kinggora.portal.mapper;

import kinggora.portal.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {

    /**
     * 회원 저장
     * @param member 회원 정보
     */
    void saveMember(Member member);

    /**
     * 회원 단건 조회 (id)
     * @param id 회원 id
     * @return 회원
     */
    Optional<Member> findById(Integer id);

    /**
     * 회원 단건 조회 (username)
     * @param username 회원 로그인 id
     * @return 회원
     */
    Optional<Member> findByUsername(String username);

    /**
     * 동일한 username 을 가진 회원 존재 여부 확인
     * @param username 회원 로그인 id
     * @return true: 중복O / false: 중복X
     */
    boolean checkDuplicateUsername(String username);

    /**
     * 로그인
     * @param member 로그인할 회원 정보 (username, password)
     * @return 로그인 사용자
     */
    Optional<Member> signIn(Member member);
}
