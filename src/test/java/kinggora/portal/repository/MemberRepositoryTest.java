package kinggora.portal.repository;

import kinggora.portal.domain.Member;
import kinggora.portal.domain.dto.MemberRole;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
                .role(MemberRole.USER)
                .build();
        Integer id = repository.saveMember(member);
        Member findMember = repository.findMemberById(id).get();
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember.getName()).isEqualTo(member.getName());
        Assertions.assertThat(findMember.getPassword()).isEqualTo(member.getPassword());
    }

}
