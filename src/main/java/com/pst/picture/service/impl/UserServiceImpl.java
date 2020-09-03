package com.pst.picture.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pst.picture.dao.UserMapper;
import com.pst.picture.entity.User;
import com.pst.picture.exception.EmailException;
import com.pst.picture.exception.LoginException;
import com.pst.picture.exception.UploadException;
import com.pst.picture.exception.VerifyCodeException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    @Override
    @Transactional(rollbackFor = {RuntimeException.class})
    public void uploadAvatar(MultipartFile picture, Long userId) {
        User user = new User();
        log.info("头像{}正在上传...", picture.getOriginalFilename());
        String path = OssUtil.uploadPicture(picture, userId);
        String pathMini = OssUtil.getMiniPath(path);
        user.setId(userId);
        user.setAvatar(pathMini);
        int row = userMapper.updateById(user);
        if (row <= 0) {
            throw new UploadException("头像修改失败");
        }

    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class})
    public void updateNickname(String nickname, Long userId) {

        String reg = "^[\u4E00-\u9FA5A-Za-z\\s+-]{2,64}$";
        if (nickname.matches(reg)) {
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
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class})
    public void updatePassword(Long userId, String password) {
        String reg = ".{6,64}$";
        if (password.matches(reg)) {
            User user = new User();
            user.setId(userId);
            user.setPassword(password);
            int row = userMapper.updateById(user);
            if (row <= 0) {
                throw new UploadException("密码修改错误");
            }
        } else {
            throw new UploadException("密码格式错误");
        }
    }

    @Override
    public void sendTextMailService(String email, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2728662673@qq.com");
        message.setTo(email);
        message.setSubject(subject);
        message.setText(content);
        try {
            sender.send(message);
            System.out.println("验证码发送成功");
        } catch (Exception e) {
            throw new VerifyCodeException("验证码发送失败");
        }
    }

    @Override
    public JSONObject emailVerifyCodeLogin(String email, String verifyCode) {
        String matchVerifyCode = verifyCodeCache.get(email);
        System.out.println(matchVerifyCode);
        if (verifyCode.equals(matchVerifyCode)) {
            if (selectEmail(email)) {
                QueryWrapper<User> query = new QueryWrapper<>();
                query.eq("email", email);
                User user = userMapper.selectOne(query);
                JSONObject jsonObject = new JSONObject(user);
                System.out.println(jsonObject);
                String token = JwtUtil.signUser(user.getId());
                jsonObject.putOpt("token", token);
                tokenCache.put(String.valueOf(user.getId()), token);
                return jsonObject;
            }
            emailRegister(email);
            return emailVerifyCodeLogin(email, matchVerifyCode);
        }
        throw new VerifyCodeException("验证码错误");
    }

    @Override
    public Boolean selectEmail(String email) {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("email", email);
        Integer count = userMapper.selectCount(query);
        return count != 0;
    }

    @Override
    public void emailRegister(String email) {
        User user = new User();
        user.setEmail(email);
        user.setNickname("匿名用户");

        userMapper.insert(user);
    }

    @Override
    public JSONObject loginPwd(String email, String password) {

        String pwd = selectPwd(email);
        if (!password.equals(pwd)) {
            throw new LoginException("密码不匹配");
        }
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("email", email);
        User user = userMapper.selectOne(query);
        JSONObject jsonObject = new JSONObject(user);
        System.out.println(jsonObject);
        String token = tokenCache.get(String.valueOf(user.getId()));
        if (null == token) {
            token = JwtUtil.signUser(user.getId());
        }
        jsonObject.putOpt("token", token);
        tokenCache.put(String.valueOf(user.getId()), token);
        return jsonObject;
    }

    @Override
    public String selectPwd(String email) {
        Map<String, Object> columnMap = new HashMap<>(16);
        columnMap.put("email", email);
        System.out.println(email);
        List<User> users = userMapper.selectByMap(columnMap);
        if (users.size() != 0) {
            System.out.println(users.get(0).getPassword());
            return users.get(0).getPassword();
        } else {
            throw new EmailException("该邮箱未注册");
        }
    }
}