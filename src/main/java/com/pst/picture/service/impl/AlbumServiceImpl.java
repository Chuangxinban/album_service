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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (Album)表服务实现类
 *
 * @author RETURN
 * @since 2020-08-13 22:00:59
 */
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
        IPage<Album> page = new Page<>(pageNum,pageSize);
        page = albumMapper.selectPage(page,queryWrapper);
        return page;
    }


    private void createAlbum(String name,Long userId,String type,boolean judge) {

        QueryWrapper<Album> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name).lt("user_id",userId).last("limit 1");
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

    public void deleteAlbum(Long albumId){
        Boolean judge = selectAlbum(albumId);
        if (judge){
            QueryWrapper<Picture> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("album_id", albumId);
            Integer pictureCount = pictureMapper.selectCount(queryWrapper);
            if (pictureCount == 0){
                LocalDateTime localDateTime = LocalDateTime.now();
                Album album = new Album();
                album.setId(albumId);
                album.setModifyTime(localDateTime);
                album.setType("recycled");
                albumMapper.updateById(album);
            }else {
                throw new AlbumDeleteException("删除相册失败");
            }
        }
            throw new AlbumDeleteException("删除相册失败");
    }

    public void updateAlbumName(Long albumId, String newName){
        Boolean judge = selectAlbum(albumId);
        if (judge){
            Map<String, Object> columnMap = new HashMap<>();
            columnMap.put("name", newName);
            System.out.println(newName);
            List<Album> albums = albumMapper.selectByMap(columnMap);
            if (albums.size() == 0){
                LocalDateTime localDateTime = LocalDateTime.now();
                Album album = new Album();
                album.setId(albumId);
                album.setName(newName);
                System.out.println(album.getName());
                album.setModifyTime(localDateTime);
                albumMapper.updateById(album);
            }else {
                throw new AlbumUpdateException("相册名已存在");
            }
        }else {
            throw new AlbumUpdateException("相册不存在");
        }
    }

    public Boolean selectAlbum(Long albumId){
        return albumMapper.selectById(albumId) != null;
    }

    @Override
    public void createDefaultAndRecycledAlbum(Long userId) {
        createAlbum("默认相册",userId,"default",false);
        createAlbum("回收站",userId,"recycled",false);
    }

    @Override
    public void createNormalAlbum(String name, Long userId) {
        createAlbum(name,userId,"default",true);
    }

    @Override
    public void deleteNormalAlbum(Long albumId) {
        deleteAlbum(albumId);
    }

    @Override
    public void updateNormalAlbum(Long albumId, String newName) {
        updateAlbumName(albumId, newName);
    }

}