package kinggora.portal.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * API 요청에 대한 ErrorCode 정의
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, -401, "리프레시 토큰이 유효하지 않습니다"),
    MISMATCH_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, -402, "리프레시 토큰의 유저 정보가 일치하지 않습니다"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, -403, "기존 비밀번호가 일치하지 않습니다"),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, -404, "입력 값이 올바르지 않습니다"),
    INVALID_FILE_FORMAT(HttpStatus.BAD_REQUEST, -405, "잘못된 형식의 파일입니다"),
    ANSWER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, -406, "답변이 이미 존재합니다"),
    OVER_MAXIMUM_COMMENT_DEPTH(HttpStatus.BAD_REQUEST, -407, "댓글 최대 depth를 초과했습니다"),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    AUTHENTICATION_ERROR(HttpStatus.UNAUTHORIZED, -410, "인증을 실패했습니다"),
    INVALID_CREDENTIAL(HttpStatus.UNAUTHORIZED, -411, "아이디 또는 비밀번호를 잘못 입력했습니다"),
    EXPIRED_USER(HttpStatus.UNAUTHORIZED, -412, "만료된 계정입니다"),
    DISABLED_USER(HttpStatus.UNAUTHORIZED, -413, "탈퇴된 계정입니다"),
    LOCKED_USER(HttpStatus.UNAUTHORIZED, -414, "잠긴 계정입니다"),
    EMPTY_AUTH_PARAMETER(HttpStatus.UNAUTHORIZED, -415, "인증 파라미터는 빈 값이 될 수 없습니다."),
    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, -416, "유효하지 않은 토큰입니다"),
    EXPIRED_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, -417, "기한이 만료된 토큰입니다"),
    UNSUPPORTED_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, -418, "지원하지 않는 형식의 토큰입니다"),
    INVALID_TOKEN_SIGNATURE(HttpStatus.UNAUTHORIZED, -419, "유효하지 않은 토큰 시그니처입니다"),

    /* 403 FORBIDDEN : 사용자가 권한이 없어 요청이 거부됨 */
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, -430, "접근할 권한이 없습니다"),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, -440, "요청 유저 정보를 찾을 수 없습니다"),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, -441, "로그아웃 된 사용자입니다"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, -442, "존재하지 않는 게시글입니다"),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, -443, "존재하지 않는 파일입니다"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, -444, "존재하지 않는 댓글입니다"),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, -445, "존재하지 않는 게시판입니다"),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, -446, "존재하지 않는 카테고리입니다"),
    ALREADY_DELETED_MEMBER(HttpStatus.NOT_FOUND, -447, "탈퇴한 회원입니다"),
    ALREADY_DELETED_POST(HttpStatus.NOT_FOUND, -448, "삭제된 게시글입니다"),
    ALREADY_DELETED_FILE(HttpStatus.NOT_FOUND, -449, "삭제된 파일입니다"),
    ALREADY_DELETED_COMMENT(HttpStatus.NOT_FOUND, -450, "삭제된 댓글입니다"),

    /* 405 METHOD_NOT_ALLOWED : 허용하지 않는 method 요청 */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, -455, "허용되지 않은 method 입니다"),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, -460, "이미 존재하는 아이디 입니다"),
    DUPLICATE_PASSWORD(HttpStatus.CONFLICT, -461, "새 비밀번호가 기존 비밀번호와 동일합니다"),

    /* 415 UNSUPPORTED_MEDIA_TYPE : 지원하지 않는 media type 요청 */
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, -472, "지원하지 않는 미디어 타입입니다."),

    /* 500 INTERNAL_SERVER_ERROR : 서버 내부 오류 */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, -500, "서버 내부 오류"),
    DB_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, -600, "데이터베이스 오류"),
    S3_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, -601, "AWS A3 서비스 오류");

    private final HttpStatus status;
    private final int code;
    private final String defaultMessage;
}
