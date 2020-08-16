package com.pst.picture.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (Album)实体类
 *
 * @author RETURN
 * @since 2020-08-13 22:00:59
 */
@Data
public class Album implements Serializable {
    private static final long serialVersionUID = 184808527455604831L;
    /**
     * 相册id
     */
    @TableId(type= IdType.AUTO)
    private Long id;
    /**
     * 相册名
     */
    private String name;
    /**
     * 修改时间
     */
    private Date modifyTime;
    /**
     * 相册类型
     */
    private String type;
    /**
     * 用户id
     */
    private Long userId;

}