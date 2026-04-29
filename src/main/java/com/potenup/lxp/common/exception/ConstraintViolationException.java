package com.potenup.lxp.common.exception;

public class ConstraintViolationException extends LxpException {
    public ConstraintViolationException(String message) {
        super(message);
    }

    public ConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
