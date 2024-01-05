package kinggora.portal.domain.dto.response;

import kinggora.portal.domain.Member;
import kinggora.portal.domain.type.MemberRole;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberResponse {
    private String username;
    private String name;
    private List<MemberRole> roles;

    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
                .username(member.getUsername())
                .name(member.getName())
                .roles(member.getRoles())
                .build();
    }

}
