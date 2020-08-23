package com.pst.picture.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pst.picture.annotation.PassToken;
import com.pst.picture.entity.Album;
import com.pst.picture.entity.vo.Response;
import com.pst.picture.exception.UserGetException;
import com.pst.picture.service.AlbumService;
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
@RestController
@RequestMapping("albums")
public class AlbumController {

    @Resource
    private AlbumService albumService;

    @PostMapping
    public Response pictures(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "1") Integer pageSize, HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");
        if (null!= userId){
            IPage<Album> list = albumService.listAlbum(pageNum, pageSize, userId);
            return Response.builder().result("ok").msg("获取图片列表成功").data(list).build();
        }else{
            throw new UserGetException("获取用户id失败");
        }

    }

    @PostMapping("create")
    public Response createAlbum(String name, HttpServletRequest request){
        Assert.notNull(name,"相册名不能为空");

        Long userId = (Long) request.getAttribute("userId");
        if (null != userId){
            albumService.createNormalAlbum(name,userId);
        }else {
            throw new UserGetException("获取用户id失败");
        }
        return Response.builder().result("ok").msg("创建相册成功").build();
    }
}