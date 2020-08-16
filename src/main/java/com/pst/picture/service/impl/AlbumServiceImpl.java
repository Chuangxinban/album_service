package com.pst.picture.service.impl;

import com.pst.picture.dao.AlbumMapper;
import com.pst.picture.service.AlbumService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

}