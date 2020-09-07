package com.pst.picture.entity.vo;

import com.pst.picture.converter.OutPutConverter;
import com.pst.picture.entity.Picture;
import lombok.Data;

import java.util.Date;

/**
 * @author RETURN
 * @date 2020/8/16 21:16
 */
@Data
public class PictureDetailVO implements OutPutConverter<PictureDetailVO, Picture> {
    private Long id;
    private String path;
    private Date uploadTime;
    private String describe;
    private String height;
    private String width;
    private String format;
    private Long size;
}
