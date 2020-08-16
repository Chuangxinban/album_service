package com.pst.picture.exception;

import com.pst.picture.entity.vo.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常捕获
 * @author RETURN
 * @date 2020/8/14 0:21
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PictureException.class)
    public Response pictureExceptionHandle(Exception e){
        return Response.builder().result("failed").msg(e.getMessage()).build();
    }

    @ExceptionHandler(UploadException.class)
    public Response uploadExceptionHandle(Exception e){
        return Response.builder().result("failed").msg(e.getMessage()).build();
    }

    @ExceptionHandler(UserGetException.class)
    public Response userGetExceptionHandle(Exception e){
        return Response.builder().result("failed").msg(e.getMessage()).build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public Response authExceptionHandle(AuthenticationException e){
        return Response.builder().result("failed").msg(e.getMessage()).build();
    }
}
