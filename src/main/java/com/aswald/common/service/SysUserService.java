package com.aswald.common.service;


import com.aswald.common.annotation.DataSource;
import com.aswald.common.dao.SysUserMapper;
import com.aswald.common.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserService {

    @Autowired
    SysUserMapper sysUserMapper;
    /**
     * 查找所有用户
     * @return
     */
    public List<SysUser> findAll(){
        return sysUserMapper.selectAll();
    }

}