package kinggora.portal.domain.type;

import lombok.RequiredArgsConstructor;

/**
 * 게시글 첨부 파일 타입 정의
 * - ATTACHMENT: 첨부 파일
 * - CONTENT: 게시물 내용에 포함된 이미지 파일
 * - THUMBNAIL: 썸네일 이미지 파일
 */
@RequiredArgsConstructor
public enum FileType implements CodeEnum {
    ATTACHMENT("A"),
    CONTENT("C"),
    THUMBNAIL("T");

    private final String code;

    @Override
    public String getCode() {
        return this.code;
    }
}
