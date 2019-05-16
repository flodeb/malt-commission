package com.malt.commission.web.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    BAD_REQUEST(1, "Bad request"),

    EXISTING_ENTITY(2, "Entity %s with identifier '%s' already exists");

    private final Integer code;

    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
