package kinggora.portal.repository;

import kinggora.portal.domain.Member;
import kinggora.portal.exception.BizException;
import kinggora.portal.exception.ErrorCode;
import kinggora.portal.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 회원 리포지토리
 * member 테이블에 대한 CRUD 수행
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final MemberMapper mapper;

    /**
     * 회원 저장
     *
     * @param member 저장할 회원 정보
     * @return 저장한 회원 id
     */
    public Integer save(Member member) {
        mapper.save(member);
        return member.getId();
    }

    /**
     * 회원 단건 조회 (id)
     *
     * @param id 회원 id
     * @return 회원
     */
    public Optional<Member> findById(Integer id) {
        return mapper.findById(id);
    }

    /**
     * 회원 단건 조회 (username)
     *
     * @param username 회원 username
     * @return 회원
     */
    public Optional<Member> findByUsername(String username) {
        return mapper.findByUsername(username);
    }

    /**
     * 회원 정보 수정
     *
     * @param member 수정할 회원 정보
     */
    public void update(Member member) {
        if (mapper.update(member) == 0) {
            log.error("fail MemberRepository.update");
            throw new BizException(ErrorCode.DB_ERROR, "회원 수정 실패");
        }
    }

    /**
     * 회원 삭제
     * update deleted=true
     *
     * @param id 삭제할 회원
     */
    public void deleteById(int id) {
        if (mapper.deleteById(id) == 0) {
            log.error("fail MemberRepository.deleteById");
            throw new BizException(ErrorCode.DB_ERROR, "회원 삭제 실패");
        }
    }

    /**
     * 동일한 username 을 가진 회원 존재 여부 확인
     *
     * @param username 회원 로그인 id
     * @return true: 중복O / false: 중복X
     */
    public boolean existsByUsername(String username) {
        return mapper.existsByUsername(username);
    }
}
