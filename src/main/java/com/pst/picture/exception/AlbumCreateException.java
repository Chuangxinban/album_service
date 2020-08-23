package com.pst.picture.exception;

/**
 * @author dongjinggezi
 * @date 2020/8/23
 */
public class AlbumCreateException extends RuntimeException {
    public AlbumCreateException(String message) {
        super(message);
    }

    public AlbumCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
