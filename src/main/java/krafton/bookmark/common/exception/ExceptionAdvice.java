package krafton.bookmark.common.exception;

import krafton.bookmark.application.exception.AlreadyExistException;
import krafton.bookmark.application.exception.NotFoundEntityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleException(AlreadyExistException e) {

        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), e.getErrorCode());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundEntityException.class)
    public ResponseEntity<ErrorResponse> handleException(NotFoundEntityException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), e.getErrorCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {

        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("유효성 검사에 실패했습니다.");

        log.error("요청 필드가 잘못되었습니다. : {}", e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(errorMessage, ErrorCode.INVALID_INPUT_ERROR);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleException(HttpMessageNotReadableException e) {

        log.error("HttpMessageNotReadableException : {}", e.getMessage());
        String message = "요청 JSON의 형식이 잘못되었습니다.";

        ErrorResponse errorResponse = new ErrorResponse(message, ErrorCode.INVALID_INPUT_ERROR);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentTypeMismatchException e) {

        log.error("MethodArgumentTypeMismatchException : {}", e.getMessage());

        String requiredType = (e.getRequiredType() != null) ? e.getRequiredType().getSimpleName() : "알 수 없음";
        String message = String.format("'%s' 파라미터는 '%s' 타입이어야 합니다. (입력된 값: '%s')",
                e.getName(),
                requiredType,
                e.getValue()
        );

        ErrorResponse errorResponse = new ErrorResponse(message, ErrorCode.INVALID_INPUT_ERROR);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
