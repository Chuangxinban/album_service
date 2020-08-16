package com.pst.picture.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pst.picture.dao.AlbumMapper;
import com.pst.picture.dao.PictureMapper;
import com.pst.picture.entity.Album;
import com.pst.picture.entity.Picture;
import com.pst.picture.entity.vo.PictureDetail;
import com.pst.picture.exception.PictureException;
import com.pst.picture.exception.UploadException;
import com.pst.picture.service.PictureService;
import com.pst.picture.utils.OssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * (Picture)表服务实现类
 *
 * @author RETURN
 * @since 2020-08-13 22:00:59
 */
@Service
@Slf4j
public class PictureServiceImpl implements PictureService {
    @Resource
    private PictureMapper pictureMapper;
    @Resource
    private AlbumMapper albumMapper;

    @Override
    public IPage<Picture> listPicture(Integer pageNum, Integer pageSize, Long albumId) {
        QueryWrapper<Picture> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("album_id",albumId);
        IPage<Picture> page = new Page<>(pageNum,pageSize);
        page = pictureMapper.selectPage(page,queryWrapper);
        return page;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class})
    public void uploadPicture(MultipartFile[] pictures, Long albumId,String userId) {
        Picture pictureEntity = new Picture();
        pictureEntity.setAlbumId(albumId);
        for (MultipartFile picture:pictures) {
            log.info("图片{}正在上传...",picture.getOriginalFilename());
            String path = OssUtil.uploadPicture(picture, userId);
            String pathMini = OssUtil.getMiniPath(path);
            pictureEntity.setUploadTime(new Date());
            pictureEntity.setPath(path);
            pictureEntity.setPathMini(pathMini);
            pictureEntity.setStatus(0);
            int row = pictureMapper.insert(pictureEntity);
            if (row <= 0){
                throw new UploadException("图片上传失败");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class})
    public void movePicture(List<String> ids, Long albumId) {
        Optional<Album> albumOptional = Optional.ofNullable(albumMapper.selectById(albumId));
        if (!albumOptional.isPresent()){
            throw new PictureException("相册不存在");
        }
        Picture picture = new Picture();
        picture.setAlbumId(albumId);
        for (String id : ids) {
            picture.setId(Long.valueOf(id));
            int row = pictureMapper.updateById(picture);
            if(row <= 0){
                throw new PictureException("图片移动失败");
            }
        }
    }

    @Override
    public PictureDetail getPicture(Long pictureId) {
        Picture picture = pictureMapper.selectById(pictureId);
        String path = picture.getPath();
        JSONObject info = OssUtil.getInfo(path);
        PictureDetail detail = JSONUtil.toBean(info, PictureDetail.class);
        detail.setDescribe(picture.getDescribe());
        detail.setUploadTime(picture.getUploadTime());
        detail.setPath(path);
        detail.setId(picture.getId());
        log.info("图片详情信息包括:{}",detail);
        return detail;
    }

    @Override
    public void updatePictureDesc(String description, Long pictureId) {
        Picture picture = new Picture();
        picture.setId(pictureId);
        picture.setDescribe(description);
        int row = pictureMapper.updateById(picture);
        if (row <= 0){
            throw new PictureException("修改图片描述失败");
        }
    }
}