package kinggora.portal.domain;

import kinggora.portal.domain.type.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * member 테이블 Domain Class
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    private Integer id;
    private String username;
    private String password;
    private String name;
    private List<MemberRole> roles;
    private boolean deleted;

}
