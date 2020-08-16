package com.pst.picture.exception;

/**
 * @author RETURN
 * @date 2020/8/14 0:00
 */
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
