package krafton.bookmark.common.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private String message;
    private String errorCode;
    private String errorMessage;

    public ErrorResponse(String message, ErrorCode errorCode) {
        this.message = message;
        this.errorCode = errorCode.getCode();
        this.errorMessage = errorCode.getMessage();
    }

    public ErrorResponse(ErrorCode errorCode) {
        this.errorCode = errorCode.getCode();
        this.errorMessage = errorCode.getMessage();
    }

}
