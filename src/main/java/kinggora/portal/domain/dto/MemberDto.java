package kinggora.portal.domain.dto;

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
