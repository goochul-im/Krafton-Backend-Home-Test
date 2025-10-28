package krafton.bookmark.domain.exception;

import krafton.bookmark.common.exception.BusinessException;
import krafton.bookmark.common.exception.ErrorCode;

public class NotFoundEntityException extends BusinessException {

    public NotFoundEntityException(String message) {
        super(ErrorCode.ENTITY_NOT_FOUND_ERROR, message);
    }

}
