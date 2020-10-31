package com.pst.picture.controller;

import com.pst.picture.annotation.PassToken;
import com.pst.picture.entity.vo.AuthUserVO;
import com.pst.picture.entity.vo.Response;
import com.pst.picture.entity.vo.UserInfoVO;
import com.pst.picture.exception.UserException;
import com.pst.picture.exception.VerifyCodeException;
import com.pst.picture.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
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
@Slf4j
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource(name = "verifyCode")
    private Cache<String, String> verifyCodeCache;

    @PostMapping("changeAvatar")
    public Response changeAvatar(MultipartFile picture, HttpServletRequest request) {
        Assert.notNull(picture, "上传图片不能为空");

        Long userId = request.getAttribute("userId") == null ? null : Long.valueOf(request.getAttribute("userId").toString());
        if (null != userId) {
            userService.uploadAvatar(picture, userId);
        } else {
            throw new UserException("获取用户id失败");
        }
        UserInfoVO userInfoVO = userService.getUserInfo(userId);
        log.info("返回的用户信息{}",userInfoVO);
        return Response.builder().data(userInfoVO).result("ok").msg("修改头像成功").build();
    }

    @PostMapping("changeNickname")
    public Response changeNickname(String nickname, HttpServletRequest request) {
        log.info("新昵称:{}", nickname);
        Assert.notNull(nickname, "用户昵称不能为空");

        Long userId = request.getAttribute("userId") == null ? null : Long.valueOf(request.getAttribute("userId").toString());
        if (null != userId) {
            userService.updateNickname(nickname, userId);
        } else {
            throw new UserException("获取用户id失败");
        }
        UserInfoVO userInfoVO = userService.getUserInfo(userId);
        return Response.builder().data(userInfoVO).result("ok").msg("修改昵称成功").build();
    }

    @PostMapping("changePwd")
    public Response changePwd(String email, String password, String verifyCode, HttpServletRequest request) {
        log.info("邮箱:{},密码:{},验证码:{}", email, password, verifyCode);
        Assert.notNull(email, "用户邮箱不能为空");

        Long userId = request.getAttribute("userId") == null ? null : Long.valueOf(request.getAttribute("userId").toString());
        String matchVerifyCode = verifyCodeCache.get(email);

        if (!matchVerifyCode.equals(verifyCode)) {
            throw new VerifyCodeException("验证码错误");
        }
        if (null != userId) {
            userService.updatePassword(userId, password);
        } else {
            throw new UserException("获取用户id失败");
        }

        return Response.builder().result("ok").msg("修改密码成功").build();
    }

    @PostMapping("verifyCode")
    @PassToken
    public Response testSendTextMail(String email) {
        log.info("发送验证码 -> 邮箱:{}", email);
        Integer v = (int) ((Math.random() * 9 + 1) * 100000);
        String verifyCode = String.valueOf(v);
        verifyCodeCache.put(email, verifyCode);
        log.info("验证码 -> {}", verifyCode);
        userService.sendVerifyCode(email, "您的验证码", verifyCode);

        return Response.builder().result("ok").msg("验证码发送成功").build();

    }

    @PostMapping("loginEmail")
    @PassToken
    public Response loginEmail(String email, String verifyCode) {
        log.info("邮箱:{},验证码:{}", email, verifyCode);
        Assert.notNull(email, "用户邮箱不能为空");

        String matchVerifyCode = verifyCodeCache.get(email);
        if (matchVerifyCode == null) {
            throw new VerifyCodeException("验证码错误");
        }
        if (!matchVerifyCode.equals(verifyCode)) {
            throw new VerifyCodeException("验证码错误");
        }

        AuthUserVO userDetail = userService.loginEmail(email, verifyCode);
        if (verifyCodeCache.containsKey(email)) {
            verifyCodeCache.remove(email);
        }
        return Response.builder().result("ok").data(userDetail).msg("登录成功").build();
    }

    @PassToken
    @PostMapping("loginPwd")
    public Response loginPwd(String email, String password) {
        log.info("邮箱:{},密码:{}", email, password);
        Assert.notNull(email, "用户邮箱不能为空");
        userService.loginPwd(email, password);
        AuthUserVO userDetail = userService.loginPwd(email, password);
        return Response.builder().result("ok").data(userDetail).msg("登录成功").build();
    }


}
