package com.pst.picture.controller;

import com.pst.picture.annotation.PassToken;
import com.pst.picture.entity.vo.Response;
import com.pst.picture.exception.UserGetException;
import com.pst.picture.service.UserService;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * (User)表控制层
 *
 * @author RETURN
 * @since 2020-08-13 22:01:00
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("changeAvatar")
    public Response changeAvatar(MultipartFile picture, HttpServletRequest request){

        Assert.notNull(picture, "上传图片不能为空");

        Long userId = (Long) request.getAttribute("userId");
        if (null != userId){
            userService.uploadAvatar(picture,userId);
        }else {
            throw new UserGetException("获取用户id失败");
        }
        return Response.builder().result("ok").msg("修改头像成功").build();
    }

    @PostMapping("changeNickname")
    public Response changeNickname(String nickname, HttpServletRequest request){

        Assert.notNull(nickname,"用户昵称不能为空");

        Long userId = (Long) request.getAttribute("userId");
        if (null != userId){
            userService.updateNickname(nickname,userId);
        }else{
            throw new UserGetException("获取用户id失败");
        }
        return Response.builder().result("ok").msg("修改昵称成功").build();
    }

    @PostMapping("changePwd")
    @PassToken
    public  Response changePwd(String email, String password, String verifyCode){
        Assert.notNull(email,"用户邮箱不能为空");
        //验证码发送功能暂无

        return Response.builder().result("ok").msg("修改密码成功").build();
    }
}