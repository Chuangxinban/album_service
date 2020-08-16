package com.pst.picture.entity.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 通用响应
 * @author RETURN
 * @date 2020/8/14 0:24
 */
@Data
@Builder
public class Response {
    private Object data;
    private String msg;
    private String result;
}
