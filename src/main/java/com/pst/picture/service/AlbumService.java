package com.pst.picture.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pst.picture.entity.Album;

/**
 * (Album)表服务接口
 *
 * @author RETURN
 * @since 2020-08-13 22:00:59
 */
public interface AlbumService {

    /**
     * 获取所有相册
     * @param pageNum 每页相册数量
     * @param pageSize 页数
     * @param userId 用户ID
     * @return 相册列表
     */
    IPage<Album> listAlbum(Integer pageNum, Integer pageSize, Long userId);


    /**
     * 创建默认相册和回收站
     * @param userId 用户ID
     */
    void createDefaultAndRecycledAlbum(Long userId);

    /**
     * 创建相册
     * @param name 相册名字
     * @param userId 用户ID
     */
    void createNormalAlbum(String name, Long userId);


}