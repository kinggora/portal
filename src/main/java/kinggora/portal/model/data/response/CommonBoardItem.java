package kinggora.portal.model.data.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 게시글 목록 Response Object
 * boardType: L
 */
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommonBoardItem extends BoardItem {

    private int commentCount;
    private boolean attached;
    private boolean imaged;
    private String thumbUrl;

}
