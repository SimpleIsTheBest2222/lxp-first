package com.potenup.lxp.common.exception;

public class DataAccessException extends LxpException {
    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
