package com.aswald.common.annotation;

import com.aswald.common.config.DbNames;

import java.lang.annotation.*;
/**
 * 动态数据源注解
 * @author Louis
 * @date Jun 17, 2019
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    
    /**
     * 数据源key值
     * @return
     */
    DbNames value();
    
}