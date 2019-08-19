package com.aswald.common.aspect;

import com.aswald.common.annotation.DataSource;
import com.aswald.common.config.DbNames;
import com.aswald.common.datasource.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Author Ethan
 * @Date 2019-08-09 14:23
 * @Description
 **/

@Aspect
@Order(-1)
@Component
@Slf4j
public class DynamicDataSourceAspect {
    @Around("@within(org.apache.ibatis.annotations.Mapper)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Class<?> target = point.getTarget().getClass();
        MethodSignature signature = (MethodSignature) point.getSignature();
        boolean resolve = false;
        //1. 首先使用springbean代理对象的实现类查找数据源
        for (Class<?> clazz : target.getInterfaces()) {
            resolve = resolveDataSource(clazz, signature.getMethod());
        }
        //2. 使用springbean代理对象查找数据源
        if (!resolve) {
            resolve=resolveDataSource(target, signature.getMethod());
        }
        if (!resolve){
            //未发现定制数据源，使用默认配置
            DynamicDataSourceContextHolder.setDataSource(DbNames.MAIN);
        }
        Object result = point.proceed();
        DynamicDataSourceContextHolder.removeDataSource();
        return result;
    }

    /**
     * 获取目标对象方法注解和类型注解中的注解
     */
    private boolean resolveDataSource(Class<?> clazz, Method method) {
        boolean flag = false;
        try {
            Class<?>[] types = method.getParameterTypes();

            // 默认使用类型注解
            if (clazz.isAnnotationPresent(DataSource.class)) {
                DataSource cds = clazz.getAnnotation(DataSource.class);
                if (!DynamicDataSourceContextHolder.existDataSource(cds.value())) {
                    log.info("DataSource [{}] doesn't exist,use default DataSource [{}]", cds.value(), DynamicDataSourceContextHolder.getDataSource());
                } else {
                    DynamicDataSourceContextHolder.setDataSource(cds.value());
                    flag = true;
                }
            }
            // 方法注解覆盖，以方法注解为最后值
            Method m = clazz.getMethod(method.getName(), types);
            if (m != null && m.isAnnotationPresent(DataSource.class)) {

                DataSource cds = m.getAnnotation(DataSource.class);
                if (!DynamicDataSourceContextHolder.existDataSource(cds.value())) {
                    log.info("DataSource [{}] doesn't exist,use default DataSource [{}]", cds.value(), DynamicDataSourceContextHolder.getDataSource());
                } else {
                    DynamicDataSourceContextHolder.setDataSource(cds.value());
                    flag = true;
                }
            }
            if (flag){
                log.info("Switch DataSource to [" + DynamicDataSourceContextHolder.getDataSource()
                        + "] in Method [" + method + "]");
            }
        } catch (Exception e) {
            log.error(clazz + ":" + e.getMessage());
        }
        return flag;
    }
}
