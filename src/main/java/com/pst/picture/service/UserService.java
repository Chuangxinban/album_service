package com.pst.picture.service;

import com.pst.picture.entity.vo.AuthUserVO;
import com.pst.picture.entity.vo.UserInfoVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * (User)表服务接口
 *
 * @author RETURN
 * @since 2020-08-13 22:01:00
 */
public interface UserService {

    /**
     * 用户修改头像
     * @param picture 图片
     * @param userId 用户id
     */
    void uploadAvatar(MultipartFile picture, Long userId);

    /**
     * 用户更新昵称
     * @param nickname 昵称
     * @param userId 用户id
     */
    void updateNickname(String nickname,Long userId);

    /**
     * 用户修改密码
     * @param userId 用户id
     * @param password 密码
     */
    void updatePassword(Long userId,String password);

    /**
     * 获取验证码
     * @param email 用户邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    void sendVerifyCode(String email,String subject,String content);

    /**
     * 邮箱验证码登录
     * @param email 用户邮箱
     * @return
     */
    AuthUserVO loginEmail(String email, String verifyCode);

    /**
     * 验证邮箱是否已注册
     * @param email
     */
    Boolean checkEmail(String email);

    /**
     * 注册邮箱
     * @param email
     */
    void userRegister(String email);

    /**
     * 邮箱密码登录
     * @param email
     * @param password
     * @return
     */
    AuthUserVO loginPwd(String email, String password);

    /**
     * @param email sadasd
     * @return
     */
    String checkPassword(String email);

    UserInfoVO getUserInfo(Long userId);
}
