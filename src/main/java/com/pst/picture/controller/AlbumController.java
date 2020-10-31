package com.pst.picture.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pst.picture.entity.Album;
import com.pst.picture.entity.vo.Response;
import com.pst.picture.exception.UserException;
import com.pst.picture.service.AlbumService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * (Album)表控制层
 *
 * @author RETURN
 * @since 2020-08-13 22:00:59
 */
@Slf4j
@RestController
@RequestMapping("albums")
public class AlbumController {

    @Resource
    private AlbumService albumService;

    @PostMapping
    public Response albums(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        Long userId = request.getAttribute("userId") == null ? null : Long.valueOf(request.getAttribute("userId").toString());
        log.info("查询用户:{},第{}页,每页{}个图片列表",userId,pageNum,pageSize);
        if (null != userId){
            IPage<Album> list = albumService.listAlbum(pageNum, pageSize, userId);
            log.info("获取用户:{}相册列表 -->{}",userId,list);
            return Response.builder().result("ok").msg("获取图片列表成功").data(list).build();
        }else{
            throw new UserException("获取用户id失败");
        }

    }

    @PostMapping("create")
    public Response createAlbum(String name, HttpServletRequest request){
        Assert.notNull(name,"相册名不能为空");

        Long userId = request.getAttribute("userId") == null ? null : Long.valueOf(request.getAttribute("userId").toString());
        log.info("用户:{},创建新相册:{}",userId,name);
        if (null != userId){
            albumService.createNormalAlbum(name,userId);
        }else {
            throw new UserException("获取用户id失败");
        }
        return Response.builder().result("ok").msg("创建相册成功").build();
    }

    @PostMapping("delete")
    public Response deleteAlbum(Long albumId, HttpServletRequest request){
        Assert.notNull(albumId,"相册id不能为空");

        Long userId = request.getAttribute("userId") == null ? null : Long.valueOf(request.getAttribute("userId").toString());
        log.info("用户:{},删除相册:{}",userId,albumId);
        if (null != userId){
            albumService.deleteAlbum(albumId);
        }else {
            throw new UserException("获取用户id失败");
        }
        return Response.builder().result("ok").msg("删除相册成功").build();
    }

    @PostMapping("update")
    public Response updateAlbum(Long albumId, String newName, HttpServletRequest request){
        Assert.notNull(albumId,"相册id不能为空");

        Long userId = request.getAttribute("userId") == null ? null : Long.valueOf(request.getAttribute("userId").toString());
        log.info("用户:{},更新相册:{},新相册名:{}",userId,albumId,newName);
        if (null != userId){
            albumService.updateAlbumName(albumId, userId,newName);
        }else {
            throw new UserException("获取用户id失败");
        }
        return Response.builder().result("ok").msg("修改相册名成功").build();
    }
}