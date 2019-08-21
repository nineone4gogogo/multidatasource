package com.aswald.common.aspect;

import com.aswald.common.annotation.DataSource;
import com.aswald.common.datasource.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
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
        for (Class<?> clazz : target.getInterfaces()) {
            resolveDataSource(clazz, signature.getMethod());
        }
        Object result = point.proceed();
        DynamicDataSourceContextHolder.removeDataSource();
        return result;
    }

    /**
     * 获取目标对象方法注解和类型注解中的注解
     */
    private void resolveDataSource(Class<?> clazz, Method method) {
        try {
            Class<?>[] types = method.getParameterTypes();

            // 方法上注解
            Method m = clazz.getMethod(method.getName(), types);
            if (m != null && m.isAnnotationPresent(DataSource.class)) {
                DataSource ds = m.getAnnotation(DataSource.class);
                String key = ds.value().name().toLowerCase();
                DynamicDataSourceContextHolder.setDataSource(key);
                log.info("Switch DataSource to [" + DynamicDataSourceContextHolder.getDataSource()
                        + "] in Method [" + method + "]");
                return;
            }
            // 类上注解
            if (clazz.isAnnotationPresent(DataSource.class)) {
                DataSource ds = clazz.getAnnotation(DataSource.class);
                String key = ds.value().name().toLowerCase();
                DynamicDataSourceContextHolder.setDataSource(key);
                log.info("Switch DataSource to [" + DynamicDataSourceContextHolder.getDataSource()
                        + "] in Method [" + method + "]");
                return;
            }
        } catch (Exception e) {
            log.error(clazz + ":" + e.getMessage());
        }
    }
}
