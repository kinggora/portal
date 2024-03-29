package kinggora.portal.repository;

import kinggora.portal.domain.Member;
import kinggora.portal.domain.type.MemberRole;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository repository;

    @Test
    void 회원등록() {
        Member member = Member.builder()
                .username(UUID.randomUUID().toString().substring(0, 8))
                .password(UUID.randomUUID().toString().substring(0, 8))
                .name(UUID.randomUUID().toString().substring(0, 5))
                .roles(List.of(MemberRole.ADMIN, MemberRole.USER))
                .build();
        Integer id = repository.save(member);
        Member findMember = repository.findById(id).get();
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember.getName()).isEqualTo(member.getName());
        Assertions.assertThat(findMember.getPassword()).isEqualTo(member.getPassword());
        Assertions.assertThat(findMember.getRoles()).contains(MemberRole.ADMIN, MemberRole.USER);
    }

}
