package com.pst.picture.service.impl;

import com.pst.picture.dao.UserMapper;
import com.pst.picture.entity.User;
import com.pst.picture.exception.UploadException;
import com.pst.picture.service.UserService;
import com.pst.picture.utils.OssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * (User)表服务实现类
 *
 * @author RETURN
 * @since 2020-08-13 22:01:00
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;


    @Override
    @Transactional(rollbackFor = {RuntimeException.class})
    public void uploadAvatar(MultipartFile picture, Long userId){
        User user = new User();
        log.info("头像{}正在上传...",picture.getOriginalFilename());
        String path = OssUtil.uploadPicture(picture, userId);
        String pathMini = OssUtil.getMiniPath(path);
        user.setId(userId);
        user.setAvatar(pathMini);
        int row = userMapper.updateById(user);
        if (row <= 0){
            throw new UploadException("头像修改失败");
        }

    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class})
    public void updateNickname(String nickname, Long userId) {

        String reg="^[\u4E00-\u9FA5A-Za-z\\s+-]{2,64}$";
        if (nickname.matches(reg)){
            User user = new User();
            user.setId(userId);
            user.setNickname(nickname);
            int row = userMapper.updateById(user);
            if (row <= 0) {
                throw new UploadException("昵称修改失败");
            }
        }else {
            throw new UploadException("昵称格式错误");
        }
    }

    @Override
    public void updatePassword(String email, String password, String verifyCode) {

    }
}