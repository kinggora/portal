package kinggora.portal.service;

import kinggora.portal.domain.Member;
import kinggora.portal.domain.dto.TokenInfo;
import kinggora.portal.domain.type.MemberRole;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    void 회원가입_후_로그인() {
        String id = UUID.randomUUID().toString().substring(0, 8);
        String password = UUID.randomUUID().toString().substring(0, 8);

        Member newMember = Member.builder()
                .username(id)
                .password(password)
                .name(UUID.randomUUID().toString().substring(0, 5))
                .role(List.of(MemberRole.USER))
                .build();
        memberService.register(newMember);

        Member signInMember = Member.builder()
                .username(id)
                .password(password)
                .build();
        TokenInfo tokenInfo = memberService.signIn(signInMember);
        log.info("token={}", tokenInfo);
    }

    @Test
    void 중복_아이디_가입_시도() {
        String id = UUID.randomUUID().toString().substring(0, 8);
        Member member = Member.builder()
                .username(id)
                .password(UUID.randomUUID().toString().substring(0, 8))
                .name(UUID.randomUUID().toString().substring(0, 5))
                .build();
        memberService.register(member);

        Member duplicateMember = Member.builder()
                .username(id)
                .password(UUID.randomUUID().toString().substring(0, 8))
                .name(UUID.randomUUID().toString().substring(0, 5))
                .build();
        Assertions.assertThrows(RuntimeException.class,
                () -> memberService.register(duplicateMember));
    }
}
