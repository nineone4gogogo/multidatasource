package com.aswald.common.model;

import lombok.Data;

import java.util.Date;

/**
 * @Author Ethan
 * @Date 2019-08-09 14:27
 * @Description
 **/
@Data
public class SysUser {
    private Long id;

    private String name;

    private String nickName;

    private String avatar;

    private String password;

    private String salt;

    private String email;

    private String mobile;

    private Byte status;

    private Long deptId;

    private String createBy;

    private Date createTime;

    private String lastUpdateBy;

    private Date lastUpdateTime;

    private Byte delFlag;

}