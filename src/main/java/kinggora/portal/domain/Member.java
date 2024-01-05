package kinggora.portal.domain;

import kinggora.portal.domain.type.MemberRole;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    private Integer id;
    private String username;
    private String password;
    private String name;
    private List<MemberRole> roles;
    private boolean deleted;

    public Member(Integer id) {
        this.id = id;
    }

    public void encodedPassword(PasswordEncoder encoder) {
        this.password = encoder.encode(this.password);
    }

}
