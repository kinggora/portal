package kinggora.portal.domain;

import kinggora.portal.domain.type.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class BoardInfo {

    private Integer id;
    private String name;
    private String subject;
    private AccessLevel accessList;
    private AccessLevel accessWrite;
    private AccessLevel accessRead;
    private AccessLevel accessReply;

    public BoardInfo(int id) {
        this.id = id;
    }
}
