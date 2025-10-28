package krafton.bookmark.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INVALID_INPUT_ERROR("C001","입력값이 잘못되었습니다."),
    ENTITY_NOT_FOUND_ERROR("C002","엔티티를 찾을 수 없습니다."),
    ALREADY_EXIST_ERROR("C003","중복된 엔티티 저장이 감지되었습니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
