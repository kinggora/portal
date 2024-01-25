package kinggora.portal.domain;

import kinggora.portal.model.request.MemberDto;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
class MemberDtoValidationTest {

    CustomValidator<MemberDto.Create> validator;
    final String requireUsername = "{require.member.username}";
    final String requireName = "{require.member.name}";
    final String requirePassword = "{require.member.password}";
    final String patternUsername = "{pattern.member.username}";
    final String patternName = "{pattern.member.name}";
    final String patternPassword = "{pattern.member.password}";

    @BeforeEach
    void init() {
        validator = new CustomValidator<>();
    }

    @Test
    @DisplayName("@NotNull")
    void validateNotNull() {
        MemberDto.Create memberDto = MemberDto.Create.builder()
                .username(null)
                .name(null)
                .password(null)
                .build();
        List<String> messages = validator.getErrorMessage(memberDto);
        Assertions.assertThat(messages).containsOnly(
                requireUsername
                , requireName
                , requirePassword
        );
    }

    /**
     * username
     * 영문으로 시작, 영문 소문자 or 숫자
     * 길이: 6 이상 10 이하
     * 정규식: ^[a-z]{1}[a-z0-9]{5,9}+$
     */
    @Test
    @DisplayName("아이디 패턴 검증: invalid")
    void validateInvalidUsername() {
        String[] invalidUsername = {
                "abc45", // 5자
                "abc45678910", // 11자
                "0abcde", // 숫자로 시작
                "abc45가", // 한글 포함
                "Abc456", // 대문자 포함 (첫 글자)
                "aBc456", // 대문자 포함
        };
        for (String username : invalidUsername) {
            MemberDto.Create memberDto = MemberDto.Create.builder()
                    .username(username)
                    .build();
            List<String> messages = validator.getErrorMessage(memberDto);
            Assertions.assertThat(messages).contains(patternUsername);
        }
    }

    @Test
    @DisplayName("아이디 패턴 검증: valid")
    void validateValidUsername() {
        String[] validUsername = {
                "abc456", // 6자, 영문 + 숫자
                "abcdef", // 6자, 영문
                "abc4567891", // 10자, 영문 + 숫자
                "abcdefghij" // 10자, 영문
        };
        for (String username : validUsername) {
            MemberDto.Create memberDto = MemberDto.Create.builder()
                    .username(username)
                    .build();
            List<String> messages = validator.getErrorMessage(memberDto);
            Assertions.assertThat(messages).containsOnly(
                    requireName
                    , requirePassword
            );
        }
    }

    /**
     * name
     * 한글 or 영문 대/소문자 or 숫자
     * 길이: 2 이상 10 이하
     * 정규식: ^[0-9a-zA-Zㄱ-ㅎ가-힣]{2,10}+$
     */
    @Test
    @DisplayName("닉네임 패턴 검증: invalid")
    void validateInvalidName() {
        String[] invalidName = {
                "a", // 1자
                "ㄱ", // 1자
                "0", // 1자
                "abcdefghijk", // 11자
                "가나다라마바사아자차타", // 11자
                "12345678910", // 11자
                "a@", // 특수문자 포함
        };
        for (String name : invalidName) {
            MemberDto.Create memberDto = MemberDto.Create.builder()
                    .name(name)
                    .build();
            List<String> messages = validator.getErrorMessage(memberDto);
            Assertions.assertThat(messages).contains(patternName);
        }
    }

    @Test
    @DisplayName("닉네임 패턴 검증: valid")
    void validateValidName() {
        String[] validName = {
                "ab", // 2자, 영문
                "abcdefghij", // 10자, 영문
                "가나", // 2자, 한글
                "가나다라마바사아자차", // 10자, 한글
                "12", // 2자, 숫자
                "1234567891", // 10자, 숫자
                "a가", // 영문 + 한글
                "a1", // 영문 + 숫자
                "가1", // 한글 + 숫자
                "1가a", // 숫자 + 한글 + 영문
        };
        for (String name : validName) {
            MemberDto.Create memberDto = MemberDto.Create.builder()
                    .name(name)
                    .build();
            List<String> messages = validator.getErrorMessage(memberDto);
            Assertions.assertThat(messages).containsOnly(
                    requireUsername
                    , requirePassword
            );
        }
    }

    /**
     * password
     * 영문 대소문자 and 숫자 or 특수문자(~!@#$%^&*()+|=)
     * 길이: 8 이상 20 이하
     * 정규식: ^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d~!@#$%^&*()+|=]{8,20}$
     */
    @Test
    @DisplayName("비밀번호 패턴 검증: invalid")
    void validateInvalidPassword() {
        String[] invalidPassword = {
                "abcd567", // 7자
                "abcdefghij12345678901", // 21자
                "abcdefge", // 영문
                "12345678", // 숫자
                "abcd456가", // 한글 포함
                "!@#$%^&*", // 특수문자
                "abcd!@#$", // 영문 + 특수문자
                "1234!@#$", // 숫자 + 특수문자
                "abcd456?", // 정의되지 않은 특수문자 포함
        };
        for (String password : invalidPassword) {
            MemberDto.Create memberDto = MemberDto.Create.builder()
                    .password(password)
                    .build();
            List<String> messages = validator.getErrorMessage(memberDto);
            Assertions.assertThat(messages).contains(patternPassword);
        }
    }

    @Test
    @DisplayName("비밀번호 패턴 검증: valid")
    void validateValidPassword() {
        String[] validPassword = {
                "abcd5678", // 8자
                "abcdefghij1234567890", // 20자
                "abcd567@", // 영문 + 숫자 + 특수문자
                "1234abc!", // 숫자 + 영문 + 특수문자
                "@1234abc", // 특수문자 + 숫자 + 영문
                "abc@1234", // 영문 + 특수문자 + 숫자
                "ABC@1234", // 대문자 + 특수문자 + 숫자
        };
        for (String password : validPassword) {
            MemberDto.Create memberDto = MemberDto.Create.builder()
                    .password(password)
                    .build();
            List<String> messages = validator.getErrorMessage(memberDto);
            Assertions.assertThat(messages).containsOnly(
                    requireUsername
                    , requireName
            );
        }
    }
}
