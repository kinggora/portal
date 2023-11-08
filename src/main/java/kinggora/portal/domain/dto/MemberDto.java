package kinggora.portal.domain.dto;

import kinggora.portal.domain.type.MemberRole;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {

    private Integer id;
    private String username;
    private String name;
    private MemberRole role;

}
