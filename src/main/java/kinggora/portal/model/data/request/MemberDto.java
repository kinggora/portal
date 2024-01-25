package kinggora.portal.model.data.request;

import kinggora.portal.domain.Member;
import kinggora.portal.domain.type.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 회원 DTO
 * - 컨트롤러 단에서 @ModelAttribute 로 바인딩되어 Bean Validation 수행
 * - 회원 도메인 객체(Member)로 변환
 * - 요청(CREATE, UPDATE)에 따라 필요한 입력에 대해 inner class로 DTO 정의
 * - 필드 validation에 필요한 정규식을 static으로 정의
 */
public class MemberDto {

    private final static String USERNAME_REGEXP = "^[a-z]{1}[a-z0-9]{5,9}+$";
    private final static String PASSWORD_REGEXP = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$";
    private final static String NAME_REGEXP = "^[0-9a-zA-Zㄱ-ㅎ가-힣]{2,10}+$";

    private MemberDto() {
    }

    /**
     * 회원 등록 DTO
     */
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Create {
        @NotNull(message = "{require.member.username}")
        @Pattern(regexp = USERNAME_REGEXP, message = "{pattern.member.username}")
        private String username;
        @NotNull(message = "{require.member.name}")
        @Pattern(regexp = NAME_REGEXP, message = "{pattern.member.name}")
        private String name;
        @NotNull(message = "{require.member.password}")
        @Pattern(regexp = PASSWORD_REGEXP, message = "{pattern.member.password}")
        private String password;

        /**
         * 회원 Create DTO -> User 회원 도메인 변환
         * User 권한: USER
         * raw password를 암호화한 후 Member 생성
         *
         * @param encoder 비밀번호 암호화기
         * @return 등록할 회원
         */
        public Member toUser(PasswordEncoder encoder) {
            return Member.builder()
                    .username(username)
                    .name(name)
                    .password(encoder.encode(password))
                    .roles(List.of(MemberRole.USER))
                    .deleted(false)
                    .build();
        }

        /**
         * 회원 Create DTO -> Admin 회원 도메인 변환
         * Admin 권한: ADMIN, USER
         * password를 암호화한 후 Member 생성
         *
         * @param encoder 비밀번호 암호화기
         * @return 등록할 회원 도메인
         */
        public Member toAdmin(PasswordEncoder encoder) {
            return Member.builder()
                    .username(username)
                    .name(name)
                    .password(encoder.encode(password))
                    .roles(List.of(MemberRole.ADMIN, MemberRole.USER))
                    .deleted(false)
                    .build();
        }

    }

    /**
     * 회원 수정 DTO
     */
    @Getter
    @AllArgsConstructor
    public static class Update extends MemberDto {
        @NotNull(message = "{require.member.name}")
        @Pattern(regexp = NAME_REGEXP, message = "{pattern.member.name}")
        private String name;

        /**
         * 회원 Update DTO -> 회원 도메인 변환
         *
         * @param id 수정할 회원 id
         * @return 수정할 회원 도메인
         */
        public Member toMember(int id) {
            return Member.builder()
                    .id(id)
                    .name(name)
                    .build();
        }
    }

    /**
     * 회원 비밀번호 수정 DTO
     */
    @Getter
    @AllArgsConstructor
    public static class PasswordUpdate {
        @NotNull(message = "{require.member.current-password}")
        private String currentPassword;
        @NotNull(message = "{require.member.new-password}")
        @Pattern(regexp = PASSWORD_REGEXP, message = "{pattern.member.password}")
        private String newPassword;

        /**
         * 회원 Password Update DTO -> 회원 도메인 변환
         * newPassword를 암호화한 후 Member 생성
         *
         * @param id      수정할 회원 id
         * @param encoder 비밀번호 암호화기
         * @return 수정할 회원 도메인
         */
        public Member toMember(int id, PasswordEncoder encoder) {
            return Member.builder()
                    .id(id)
                    .password(encoder.encode(newPassword))
                    .build();
        }
    }

}
