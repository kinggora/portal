package kinggora.portal.domain.dto.request;

import kinggora.portal.domain.Member;
import kinggora.portal.domain.type.MemberRole;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Builder
public class MemberDto {
    @NotNull(message = "{require.member.username}")
    @Pattern(regexp = "^[a-z]{1}[a-z0-9]{5,9}+$", message = "{pattern.member.username}")
    private String username;
    @NotNull(message = "{require.member.name}")
    @Pattern(regexp = "^[0-9a-zA-Zㄱ-ㅎ가-힣]{2,10}+$", message = "{pattern.member.name}")
    private String name;
    @NotNull(message = "{require.member.password}")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$", message = "{pattern.member.password}")
    private String password;

    public MemberDto(String username, String name, String password) {
        this.username = username;
        this.name = name;
        this.password = password;
    }

    public Member toUser() {
        return Member.builder()
                .username(username)
                .name(name)
                .password(password)
                .roles(List.of(MemberRole.USER))
                .deleted(false)
                .build();
    }

    public Member toUpdateMember(int id) {
        return Member.builder()
                .id(id)
                .name(name)
                .build();
    }

    public Member toAdmin() {
        return Member.builder()
                .username(username)
                .name(name)
                .password(password)
                .roles(List.of(MemberRole.ADMIN, MemberRole.USER))
                .deleted(false)
                .build();
    }

}
