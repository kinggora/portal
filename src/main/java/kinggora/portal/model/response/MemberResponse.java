package kinggora.portal.model.response;

import kinggora.portal.domain.Member;
import kinggora.portal.domain.type.MemberRole;
import lombok.*;

import java.util.List;

/**
 * 회원 Response Object
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberResponse {
    private String username;
    private String name;
    private List<MemberRole> roles;

    /**
     * 도메인 객체로 MemberResponse 객체 생성
     *
     * @param member 회원 도메인 객체
     * @return MemberResponse
     */
    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .username(member.getUsername())
                .name(member.getName())
                .roles(member.getRoles())
                .build();
    }

}
