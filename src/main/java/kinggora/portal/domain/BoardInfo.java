package kinggora.portal.domain;

import kinggora.portal.domain.type.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * board_info 테이블 Domain Class
 */
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
    private AccessLevel accessReplyRead;
    private AccessLevel accessReplyWrite;
    private AccessLevel accessComment;
    private AccessLevel accessFile;
    private String boardType;
    private boolean allowSecret;

}
