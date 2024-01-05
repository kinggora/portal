package kinggora.portal.domain.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonBoardItem extends BoardItem {
    private int commentCount;
    private boolean attached;
    private boolean imaged;
    private String thumbUrl;
}
