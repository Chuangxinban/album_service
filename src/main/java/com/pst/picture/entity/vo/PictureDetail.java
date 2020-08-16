package com.pst.picture.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author RETURN
 * @date 2020/8/16 21:16
 */
@Data
public class PictureDetail implements Serializable {
    private Long id;
    private String path;
    private Date uploadTime;
    private String describe;
    private String height;
    private String width;
    private String format;
    private Long size;
}
