package com.pst.picture.exception;

/**
 * @author RETURN
 * @date 2020/8/16 22:09
 */
public class UserGetException extends RuntimeException {
    public UserGetException(String message) {
        super(message);
    }

    public UserGetException(String message, Throwable cause) {
        super(message, cause);
    }
}
