package kinggora.portal.service;

import kinggora.portal.domain.Member;
import kinggora.portal.model.data.request.MemberDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

        MemberDto.Create newMember = MemberDto.Create.builder()
                .username(id)
                .password(password)
                .name(UUID.randomUUID().toString().substring(0, 5))
                .build();
        memberService.createMember(newMember);

        Member signInMember = Member.builder()
                .username(id)
                .password(password)
                .build();
        //TokenInfo tokenInfo = authService.signIn(signInMember);
        //log.info("token={}", tokenInfo);
    }

    @Test
    void 중복_아이디_가입_시도() {
        String id = UUID.randomUUID().toString().substring(0, 8);

        MemberDto.Create member = MemberDto.Create.builder()
                .username(id)
                .password(UUID.randomUUID().toString().substring(0, 8))
                .name(UUID.randomUUID().toString().substring(0, 5))
                .build();
        memberService.createMember(member);

        MemberDto.Create duplicateMember = MemberDto.Create.builder()
                .username(id)
                .password(UUID.randomUUID().toString().substring(0, 8))
                .name(UUID.randomUUID().toString().substring(0, 5))
                .build();
        Assertions.assertThrows(RuntimeException.class,
                () -> memberService.createMember(duplicateMember));
    }
}
