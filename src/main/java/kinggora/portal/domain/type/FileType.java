package kinggora.portal.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileType {
    ATTACHMENT("A"), // 첨부 파일
    CONTENT("C"),  // 게시물 내용에 포함된 이미지 파일
    THUMBNAIL("T"),  // 썸네일 이미지 파일
    UNKNOWN("U");  // 알 수 없는 파일


    private final String value;
}
