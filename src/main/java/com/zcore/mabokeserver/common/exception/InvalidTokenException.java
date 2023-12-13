package com.zcore.mabokeserver.common.exception;

public class InvalidTokenException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String message;
    
    public InvalidTokenException(String message) {
        super(message);
    }
}
