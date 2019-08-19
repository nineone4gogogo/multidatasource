package com.aswald.common.controller;

import com.aswald.common.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Ethan
 * @Date 2019-08-09 14:27
 * @Description
 **/
@RestController
@RequestMapping("user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @PostMapping(value = "/findAll")
    public Object findAll() {
        return sysUserService.findAll();
    }

    @PostMapping(value = "/findAll2")
    public Object findAll2() {
        return sysUserService.findAll();
    }

}
