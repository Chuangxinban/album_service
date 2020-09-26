package com.pst.picture.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pst.picture.dao.UserMapper;
import com.pst.picture.entity.User;
import com.pst.picture.entity.vo.AuthUserVO;
import com.pst.picture.exception.*;
import com.pst.picture.service.UserService;
import com.pst.picture.utils.JwtUtil;
import com.pst.picture.utils.OssUtil;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    @Resource(name = "verifyCode")
    private Cache<String, String> verifyCodeCache;
    @Resource(name = "tokenCache")
    private Cache<String, String> tokenCache;
    @Resource
    private JavaMailSender sender;

    private static final String NICKNAME_REG = "^[\u4E00-\u9FA5A-Za-z\\s+-]{2,64}$";


    @Override
    @Transactional(rollbackFor = {RuntimeException.class})
    public void uploadAvatar(MultipartFile picture, Long userId) {
        User user = new User();
        log.debug("头像{}正在上传...", picture.getOriginalFilename());
        String path = OssUtil.uploadPicture(picture, userId);
        String pathMini = OssUtil.getMiniPath(path);
        user.setId(userId);
        user.setAvatar(pathMini);
        int row = userMapper.updateById(user);
        if (row <= 0) {
            throw new UploadException("头像修改失败");
        }
        log.debug("头像上传成功");
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class})
    public void updateNickname(String nickname, Long userId) {
        log.debug("用户{}昵称修改为{}",userId,nickname);
        if (nickname.matches(NICKNAME_REG)) {
            User user = new User();
            user.setId(userId);
            user.setNickname(nickname);
            int row = userMapper.updateById(user);
            if (row <= 0) {
                throw new UploadException("昵称修改失败");
            }
        } else {
            throw new UploadException("昵称格式错误");
        }
        log.info("昵称修改成功");
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class})
    public void updatePassword(Long userId, String password) {
        log.debug("用户{}修改密码为{}",userId,password);
        User user = new User();
        user.setId(userId);
        user.setPassword(DigestUtil.md5Hex(password));
        int row = userMapper.updateById(user);
        if (row <= 0) {
            throw new UserException("密码修改错误");
        }
        log.debug("用户密码修改成功");
    }

    @Override
    public void sendVerifyCode(String email, String subject, String content) {
        log.debug("开始发送验证码");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2728662673@qq.com");
        message.setTo(email);
        message.setSubject(subject);
        message.setText(content);
        try {
            sender.send(message);
            log.debug("验证码发送成功{}:",message);
        } catch (Exception e) {
            throw new VerifyCodeException("验证码发送失败");
        }
    }

    @Override
    public AuthUserVO loginEmail(String email, String verifyCode) {
        log.debug("邮箱验证码登录开始");
        String matchVerifyCode = verifyCodeCache.get(email);
        if (verifyCode.equals(matchVerifyCode)) {
            if (checkEmail(email)) {
                log.debug("邮箱验证码登录成功");
                return getAuthUser(email);
            }
            log.debug("用户不存在,开始创建用户");
            userRegister(email);
            log.debug("用户创建成功");
            return loginEmail(email, matchVerifyCode);
        }
        throw new VerifyCodeException("验证码错误");
    }

    @Override
    public AuthUserVO loginPwd(String email, String password) {

        String pwd = checkPassword(email);
        if (!DigestUtil.md5Hex(password).equals(pwd)) {
            throw new LoginException("密码不匹配");
        }
        return getAuthUser(email);
    }

    @Override
    public void userRegister(String email) {
        User user = new User();
        user.setEmail(email);
        user.setNickname("匿名用户");
        userMapper.insert(user);
    }

    @Override
    public Boolean checkEmail(String email) {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("email", email);
        Integer count = userMapper.selectCount(query);
        return count != 0;
    }

    @Override
    public String checkPassword(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",email);
        User user = userMapper.selectOne(queryWrapper);
        if (null != user) {
            log.info("密码登录的用户:{}",user);
            return user.getPassword();
        } else {
            throw new EmailException("该邮箱未注册");
        }
    }

    private AuthUserVO getAuthUser(String email) {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("email", email);
        User user = userMapper.selectOne(query);
        AuthUserVO authUser = new AuthUserVO().convertFrom(user);
        String token = tokenCache.get(String.valueOf(user.getId()));
        if (null == token) {
            token = JwtUtil.signUser(user.getId());
        }
        authUser.setToken(token);
        tokenCache.put(String.valueOf(user.getId()), token);
        return authUser;
    }
}