package kinggora.portal.mapper;

import kinggora.portal.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface MemberMapper {

    /**
     * 회원 저장
     *
     * @param member 회원 정보
     */
    void saveMember(Member member);

    /**
     * 회원 단건 조회 (id)
     *
     * @param id 회원 id
     * @return 회원
     */
    Optional<Member> findMemberById(Integer id);

    /**
     * 회원 단건 조회 (username)
     *
     * @param username 회원 로그인 id
     * @return 회원
     */
    Optional<Member> findMemberByUsername(String username);

    /**
     * 동일한 username 을 가진 회원 존재 여부 확인
     *
     * @param username 회원 로그인 id
     * @return true: 중복O / false: 중복X
     */
    boolean existsUsername(String username);

    /**
     * 회원 정보 수정
     *
     * @param member 수정할 회원 정보
     * @return 변경된 row 수
     */
    int updateMember(Member member);

    /**
     * 회원 비밀번호 수정
     *
     * @param id       수정할 회원 id
     * @param password 암호화된 새 비밀번호
     * @return 변경된 row 수
     */
    int updatePassword(@Param("id") int id, @Param("password") String password);
}
