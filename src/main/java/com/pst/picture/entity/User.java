package com.pst.picture.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * (User)实体类
 *
 * @author RETURN
 * @since 2020-08-13 22:01:00
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 779866878072284592L;
    /**
     * 用户id
     */
    @TableId(type= IdType.AUTO)
    private Long id;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户头像地址
     */
    private String avatar;

}