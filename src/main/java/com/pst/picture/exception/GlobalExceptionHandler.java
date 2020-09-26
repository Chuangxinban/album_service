package com.pst.picture.exception;

import com.pst.picture.entity.vo.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常捕获
 * @author RETURN
 * @date 2020/8/14 0:21
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response runtimeExceptionHandle(Exception e){
        return Response.builder().result("failed").msg(e.getMessage()).build();
    }

    @ExceptionHandler(EmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response emailExceptionHandle(Exception e){
        return Response.builder().result("failed").msg(e.getMessage()).build();
    }

    @ExceptionHandler(PictureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response pictureExceptionHandle(Exception e){
        return Response.builder().result("failed").msg(e.getMessage()).build();
    }

    @ExceptionHandler(UploadException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response uploadExceptionHandle(Exception e){
        return Response.builder().result("failed").msg(e.getMessage()).build();
    }

    @ExceptionHandler(UserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response userExceptionHandle(Exception e){
        return Response.builder().result("failed").msg(e.getMessage()).build();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response authExceptionHandle(AuthenticationException e){
        return Response.builder().result("failed").msg(e.getMessage()).build();
    }
}
