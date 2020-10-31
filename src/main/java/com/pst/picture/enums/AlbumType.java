package com.pst.picture.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @Author: RETURN
 * @Date: 2020/10/31 16:19
 */
@Getter
public enum AlbumType {
    //默认相册
    DEFAULT("default"),
    //回收站
    RECYCLED("recycled");

    @EnumValue
    private String type;

    AlbumType(String type){
        this.type = type;
    }
    @Override
    public String toString() {
        return this.type;
    }


}
