package com.aswald.common;

import com.aswald.common.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class MultidataApplicationTest {

    @Autowired
    SysUserService service;
    @Test
    public void testMultiDatasource(){
        log.info("{}",service.findAll());
    }
}