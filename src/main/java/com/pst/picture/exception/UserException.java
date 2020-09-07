package com.pst.picture.exception;

/**
 * @author RETURN
 * @date 2020/8/16 22:09
 */
public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

}
