package kinggora.portal.domain;

import kinggora.portal.domain.type.MemberRole;
import lombok.*;

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
    private List<MemberRole> role;

    public Member(Integer id) {
        this.id = id;
    }

    public void encodePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void removePassword() {
        this.password = null;
    }

}
