package kinggora.portal.mapper;

import kinggora.portal.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

/**
 * MyBatis Mapper Interface
 * MemberMapper.xml에 정의된 SQL과 메서드를 매핑
 */
@Mapper
public interface MemberMapper {

    void save(Member member);

    Optional<Member> findById(int id);

    Optional<Member> findByUsername(String username);

    int update(Member member);

    int deleteById(int id);

    boolean existsByUsername(String username);

}
