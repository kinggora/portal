package kinggora.portal.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_INPUT_VALUE(-401, "사용자 입력 유효성 검증 실패"),
    INVALID_PASSWORD(-402, "잘못된 비밀번호"),
    NOT_FOUND_POST(-404, "요청 게시물을 찾을 수 없음"),
    INTERNAL_SERVER_ERROR(-500, "내부 서버 오류"),
    DB_ERROR(-600, "데이터베이스 오류");

    private final int code;
    private final String message;
}
