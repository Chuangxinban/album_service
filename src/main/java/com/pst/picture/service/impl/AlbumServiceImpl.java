package com.pst.picture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pst.picture.dao.AlbumMapper;
import com.pst.picture.dao.PictureMapper;
import com.pst.picture.entity.Album;
import com.pst.picture.entity.Picture;
import com.pst.picture.exception.AlbumCreateException;
import com.pst.picture.exception.AlbumDeleteException;
import com.pst.picture.exception.AlbumUpdateException;
import com.pst.picture.service.AlbumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (Album)表服务实现类
 *
 * @author RETURN
 * @since 2020-08-13 22:00:59
 */
@Slf4j
@Service
public class AlbumServiceImpl implements AlbumService {
    @Resource
    private AlbumMapper albumMapper;
    @Resource
    private PictureMapper pictureMapper;

    @Override
    public IPage<Album> listAlbum(Integer pageNum, Integer pageSize, Long userId) {
        QueryWrapper<Album> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<Album> albums = albumMapper.selectList(queryWrapper).stream().filter(album -> !"默认相册".equals(album.getName())).collect(Collectors.toList());
        log.debug("相册:{}",albums);
        if (albums.isEmpty()){
            createDefaultAndRecycledAlbum(userId);
        }
        IPage<Album> page = new Page<>(pageNum,pageSize);
        page = albumMapper.selectPage(page,queryWrapper);
        return page;
    }

    @Override
    public void createAlbum(String name, Long userId, String type, boolean judge) {
        log.debug("给用户{} 创建相册:{} --> {}",userId,name,type);
        QueryWrapper<Album> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name).lt("user_id",userId);
        Integer integer = albumMapper.selectCount(queryWrapper);
        if (integer!=1){
            LocalDateTime nowTime = LocalDateTime.now();
            Album album = new Album();
            album.setName(name);
            album.setModifyTime(nowTime);
            album.setType(type);
            album.setUserId(userId);
            int row = albumMapper.insert(album);
            if (row<=0){
                throw new AlbumCreateException("创建相册失败");
            }
        }else {
            if (judge){
                throw new AlbumCreateException("相册名重复");
            }
        }
    }


    @Override
    public void deleteAlbum(Long albumId){
        boolean judge = albumMapper.selectById(albumId) != null;
        if (judge){
            QueryWrapper<Picture> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("album_id", albumId);
            Integer pictureCount = pictureMapper.selectCount(queryWrapper);
            if (pictureCount == 0){
                Album album = new Album();
                album.setId(albumId);
                albumMapper.deleteById(album);
                return;
            }else {
                throw new AlbumDeleteException("删除相册失败");
            }
        }
            throw new AlbumDeleteException("删除相册失败");
    }

    @Override
    public void updateAlbumName(Long albumId,Long userId, String newName){
        boolean judge = albumMapper.selectById(albumId) != null;
        if (judge){
            QueryWrapper<Album> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", newName);
            queryWrapper.eq("user_id",userId);
            List<Album> albums = albumMapper.selectList(queryWrapper);
            if (albums.isEmpty()){
                LocalDateTime localDateTime = LocalDateTime.now();
                Album album = new Album();
                album.setId(albumId);
                album.setName(newName);
                album.setModifyTime(localDateTime);
                albumMapper.updateById(album);
            }else {
                throw new AlbumUpdateException("相册名已存在");
            }
        }else {
            throw new AlbumUpdateException("相册不存在");
        }
    }

    @Override
    public void createNormalAlbum(String name, Long userId) {
        createAlbum(name,userId,"default",true);
    }

    public void createDefaultAndRecycledAlbum(Long userId) {
        createAlbum("默认相册",userId,"default",false);
        createAlbum("回收站",userId,"recycled",false);
    }

}