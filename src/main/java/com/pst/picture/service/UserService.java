package com.pst.picture.service;

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
     * @param email 用户邮箱
     * @param password 密码
     * @param verifyCode 验证码
     */
    void updatePassword(String email,String password,String verifyCode);
}