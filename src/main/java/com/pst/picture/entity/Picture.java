package com.pst.picture.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (Picture)实体类
 *
 * @author RETURN
 * @since 2020-08-13 22:00:59
 */
@Data
public class Picture implements Serializable {
    private static final long serialVersionUID = 143987266524185557L;
    /**
     * 图片id
     */
    @TableId(type=IdType.AUTO)
    private Long id;
    /**
     * 图片地址
     */
    private String path;
    /**
     * 上传时间
     */
    private Date uploadTime;
    /**
     * 图片描述
     */
    @TableField("`describe`")
    private String describe;
    /**
     * 缩略图地址
     */
    private String pathMini;
    /**
     * 图片状态
     */
    @TableField("`status`")
    @JsonIgnore
    private Object status;
    /**
     * 相册id
     */
    private Long albumId;

}