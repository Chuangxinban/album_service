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

    void createAlbum(String name, Long userId, String type, boolean judge);

    /**
     * 删除相册
     * @param albumId 相册ID
     */
    void deleteAlbum(Long albumId);

    /**
     * 修改相册名
     * @param albumId
     * @param newName
     */
    void updateAlbumName(Long albumId,Long userId, String newName);

    void createNormalAlbum(String name, Long userId);
}