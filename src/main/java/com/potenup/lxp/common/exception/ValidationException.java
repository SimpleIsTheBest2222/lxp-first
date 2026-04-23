package com.potenup.lxp.common.exception;

public class ValidationException extends LxpException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
