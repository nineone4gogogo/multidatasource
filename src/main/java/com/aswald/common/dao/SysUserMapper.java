package com.aswald.common.dao;


import com.aswald.common.annotation.DataSource;
import com.aswald.common.config.DbNames;
import com.aswald.common.model.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author Ethan
 * @Date 2019-08-09 14:27
 * @Description
 **/
@Mapper
@DataSource(DbNames.FINANCIAL)
public interface SysUserMapper {
    /**
     * 查询全部用户
     *
     * @return
     */
//    @DataSource(DbNames.FINANCIAL)
    List<SysUser> selectAll();
}