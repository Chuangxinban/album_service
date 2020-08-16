package com.pst.picture.controller;

import com.pst.picture.service.AlbumService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

}