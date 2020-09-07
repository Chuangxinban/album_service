package com.pst.picture.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;

/**
 * @author RETURN
 * @date 2020/9/7 13:29
 */
public interface OutPutConverter<DTO extends OutPutConverter<DTO,DOMAIN>, DOMAIN> {

    /**
     * 从domain转换成dto
     *
     * @param domain 需要转换的对象
     * @param <T> 转换后的对象
     * @return 转换后的对象
     */
    default <T extends DTO> T convertFrom(@NonNull DOMAIN domain){
        BeanUtils.copyProperties(domain,this);

        return (T) this;
    }
}
