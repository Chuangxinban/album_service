package com.pst.picture.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pst.picture.entity.Picture;
import com.pst.picture.entity.vo.PictureDetail;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * (Picture)表服务接口
 *
 * @author RETURN
 * @since 2020-08-13 22:00:59
 */
public interface PictureService {

    /**
     * 返回图片列表
     * @param pageSize 每页数量
     * @param pageNum 页数
     * @param albumId 相册id
     * @return 图片列表
     */
    IPage<Picture> listPicture(Integer pageNum, Integer pageSize, Long albumId);

    /**
     * 图片上传
     * @param pictures 图片
     * @param albumId 相册id
     * @param userId 用户id
     */
    void uploadPicture(MultipartFile[] pictures, Long albumId, Long userId);

    /**
     * 移动图片
     * @param ids 图片的id
     * @param albumId 相册id
     */
    void movePicture(List<String> ids, Long albumId);

    /**
     * 获取图片详情
     * @param pictureId 图片id
     * @return 图片详情
     */
    PictureDetail getPicture(Long pictureId);

    /**
     * 更新图片的描述
     * @param description 新描述
     * @param pictureId 图片Id
     */
    void updatePictureDesc(String description, Long pictureId);
}