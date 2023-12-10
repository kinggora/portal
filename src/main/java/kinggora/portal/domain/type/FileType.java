package kinggora.portal.domain.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FileType implements CodeEnum {
    ATTACHMENT("A"), // 첨부 파일
    CONTENT("C"),  // 게시물 내용에 포함된 이미지 파일
    THUMBNAIL("T");  // 썸네일 이미지 파일

    private final String code;

    @Override
    public String getCode() {
        return this.code;
    }
}
