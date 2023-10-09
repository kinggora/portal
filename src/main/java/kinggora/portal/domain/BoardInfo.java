package kinggora.portal.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardInfo {

    private Integer id;
    private String name;
    private String subject;

    public BoardInfo(int id) {
        this.id = id;
    }
}
