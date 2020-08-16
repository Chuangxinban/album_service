package com.pst.picture.exception;

/**
 * @author RETURN
 * @date 2020/8/15 17:13
 */
public class PictureException extends RuntimeException  {
    public PictureException(String message) {
        super(message);
    }

    public PictureException(String message, Throwable cause) {
        super(message, cause);
    }
}
