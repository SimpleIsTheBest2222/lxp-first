package com.potenup.lxp.common.exception;

public class LxpException extends RuntimeException {
    public LxpException(String message) {
        super(message);
    }

    public LxpException(String message, Throwable cause) {
        super(message, cause);
    }
}
