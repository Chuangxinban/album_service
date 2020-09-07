package com.pst.picture.entity.vo;

import com.pst.picture.converter.OutPutConverter;
import com.pst.picture.entity.User;
import lombok.Data;

/**
 * @author RETURN
 * @date 2020/9/6 13:45
 */
@Data
public class AuthUserVO implements OutPutConverter<AuthUserVO, User> {

    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户头像地址
     */
    private String avatar;
    /**
     * token
     */
    private String token;
}
