package com.pst.picture.enums;

import com.pst.picture.exception.PictureException;

/**
 * 定义所允许的图片格式
 * @author RETURN
 * @date 2020/8/15 23:05
 */
public enum AllowPictureType {

    PNG("png"),JPG("jpg"),GIF("gif");

    private final String type;

    AllowPictureType(String type) {
        this.type = type;
    }

    public static boolean checkType(String type){
        AllowPictureType[] types = AllowPictureType.values();
        for (AllowPictureType t:types) {
            if (t.type.equals(type)){
                return true;
            }
        }
        throw new PictureException("文件格式错误");
    }
}
