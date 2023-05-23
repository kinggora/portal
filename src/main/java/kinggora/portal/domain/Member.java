package kinggora.portal.domain;

import kinggora.portal.domain.dto.MemberRole;
import lombok.*;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    private Integer id;
    private String username;
    private String password;
    private String name;
    private MemberRole role;

    public Member(Integer id) {
        this.id = id;
    }

}
