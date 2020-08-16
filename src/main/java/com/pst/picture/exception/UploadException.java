package com.pst.picture.exception;

/**
 * @author RETURN
 * @date 2020/8/15 17:13
 */
public class UploadException extends RuntimeException {
    public UploadException(String message) {
        super(message);
    }

    public UploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
