package com.potenup.lxp.common.exception;

public class NotFoundException extends LxpException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
