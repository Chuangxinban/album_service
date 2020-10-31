package com.pst.picture.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.pst.picture.enums.AlbumType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (Album)实体类
 *
 * @author RETURN
 * @since 2020-08-13 22:00:59
 */
@Data
public class Album implements Serializable {
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
    private LocalDateTime modifyTime;
    /**
     * 相册类型
     */
    private AlbumType type;
    /**
     * 用户id
     */
    private Long userId;

}