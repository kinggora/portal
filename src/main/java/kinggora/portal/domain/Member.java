package kinggora.portal.domain;

import kinggora.portal.domain.dto.MemberRole;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    private Integer id;
    private String loginId;
    private String password;
    private String name;
    private MemberRole role;

}
