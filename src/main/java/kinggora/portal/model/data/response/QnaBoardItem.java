package kinggora.portal.model.data.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 게시글 목록 Response Object
 * boardType: Q
 */
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class QnaBoardItem extends BoardItem {
    private LocalDateTime modDate;
    private boolean secret;
    private boolean childExists;

}
