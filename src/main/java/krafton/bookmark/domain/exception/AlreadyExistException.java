package krafton.bookmark.domain.exception;

import krafton.bookmark.common.exception.BusinessException;
import krafton.bookmark.common.exception.ErrorCode;

public class AlreadyExistException extends BusinessException {

    public AlreadyExistException(String message) {
        super(ErrorCode.ALREADY_EXIST_ERROR, message);
    }
}
