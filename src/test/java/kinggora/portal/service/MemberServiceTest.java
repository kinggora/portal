package kinggora.portal.service;

import kinggora.portal.domain.Member;
import kinggora.portal.domain.dto.MemberRole;
import kinggora.portal.domain.dto.TokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;

    @Test
    void 회원가입_후_로그인() {
        Member newMember = Member.builder()
                .username("member")
                .password("password")
                .name("회원A")
                .build();
        memberService.register(newMember);

        Member signInMember = Member.builder()
                .username("member")
                .password("password")
                .build();
        TokenInfo tokenInfo = memberService.signIn(signInMember);
        log.info("token={}", tokenInfo);
    }

    @Test
    void 중복_아이디_가입_시도() {
        Member member = Member.builder()
                .username("member")
                .password("password111")
                .name("회원A")
                .build();
        memberService.register(member);

        Member duplicateMember = Member.builder()
                .username("member")
                .password("password222")
                .name("회원B")
                .build();
        Assertions.assertThrows(RuntimeException.class,
                () -> memberService.register(duplicateMember));
    }
}