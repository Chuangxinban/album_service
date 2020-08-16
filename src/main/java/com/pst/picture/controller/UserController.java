package com.pst.picture.controller;

import com.pst.picture.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * (User)表控制层
 *
 * @author RETURN
 * @since 2020-08-13 22:01:00
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;


}