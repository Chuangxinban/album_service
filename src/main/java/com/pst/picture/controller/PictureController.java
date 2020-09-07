package com.pst.picture.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pst.picture.entity.Picture;
import com.pst.picture.entity.vo.PictureDetailVO;
import com.pst.picture.entity.vo.Response;
import com.pst.picture.exception.UserException;
import com.pst.picture.service.PictureService;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * (Picture)表控制层
 *
 * @author RETURN
 * @since 2020-08-13 22:00:59
 */
@RestController
@RequestMapping("pictures")
public class PictureController {

    @Resource
    private PictureService pictureService;

    @PostMapping
    public Response pictures(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "20") Integer pageSize, Long albumId) {
        Assert.notNull(albumId, "相册id不能为空");

        IPage<Picture> list = pictureService.listPicture(pageNum, pageSize, albumId);
        return Response.builder().result("ok").msg("成功").data(list).build();
    }

    @PostMapping("upload")
    public Response upload(MultipartFile[] pictures, Long albumId, HttpServletRequest request) {
        Assert.notEmpty(pictures, "上传图片不能为空");
        Assert.notNull(albumId, "相册id不能为空");

        Long userId = request.getAttribute("userId") == null ? null : Long.valueOf(request.getAttribute("userId").toString());
        if (null != userId){
            pictureService.uploadPicture(pictures, albumId, userId);
        }else {
            throw new UserException("获取用户id失败");
        }
        return Response.builder().result("ok").msg("成功").build();
    }

    @PostMapping("move")
    public Response move(String ids, Long albumId) {
        Assert.notNull(ids, "图片id列表不能为空");
        Assert.notNull(albumId, "相册id不能为空");

        List<String> splitId = StrUtil.split(ids, ',');
        pictureService.movePicture(splitId, albumId);
        return Response.builder().result("ok").msg("成功").build();
    }

    @PostMapping("detailed")
    public Response detailed(Long pictureId) {
        Assert.notNull(pictureId, "图片id不能为空");

        PictureDetailVO picture = pictureService.getPicture(pictureId);
        return Response.builder().result("ok").msg("成功").data(picture).build();
    }

    @PostMapping("descUpdate")
    public Response descUpdate(@RequestParam("newDescription") String description, Long pictureId) {
        Assert.notNull(pictureId, "图片id不能为空");

        pictureService.updatePictureDesc(description, pictureId);
        return Response.builder().result("ok").msg("成功").build();
    }
}