package com.malt.commission.web.error.exception;

import com.malt.commission.web.error.ErrorCode;

public class ExistingEntityException extends ApiException {

    public ExistingEntityException(Class className, String identifier) {
        super(ErrorCode.EXISTING_ENTITY, String.format(ErrorCode.EXISTING_ENTITY.getMessage(), className.getSimpleName(), identifier));
    }
}
