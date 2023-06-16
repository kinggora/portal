package kinggora.portal.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, -401, "리프레시 토큰이 유효하지 않습니다"),
    MISMATCH_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, -402, "리프레시 토큰의 유저 정보가 일치하지 않습니다"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, -403, "비밀번호가 일치하지 않습니다"),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, -404, "입력이 올바르지 않습니다."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, -410, "권한이 없는 요청입니다."),
    UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, -411, "현재 내 계정 정보가 존재하지 않습니다"),
    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, -412, "권한 정보가 없는 토큰입니다"),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, -420, "해당 유저 정보를 찾을 수 없습니다"),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, -421, "로그아웃 된 사용자입니다"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, -422, "해당 게시글을 찾을 수 없습니다"),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, -423, "해당 파일을 찾을 수 없습니다"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, -424, "해당 댓글을 찾을 수 없습니다"),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, -425, "해당 게시판을 찾을 수 없습니다"),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, -430, "이미 존재하는 아이디 입니다."),
    DUPLICATE_PASSWORD(HttpStatus.CONFLICT, -431, "기존 비밀번호와 동일합니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, -500,"내부 서버 오류"),
    DB_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, -600, "데이터베이스 오류");

    private final HttpStatus status;
    private final int code;
    private final String defaultMessage;
}
