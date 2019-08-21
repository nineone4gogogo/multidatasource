package com.aswald.common.datasource;
import com.aswald.common.config.DbNames;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author Ethan
 * @Date 2019-08-09 14:27
 * @Description 动态数据源上下文
 **/

public class DynamicDataSourceContextHolder {

    private static final ThreadLocal<String> contextHolder=new ThreadLocal<>();

    /**
     * 切换数据源
     * @param key
     */
    public static void setDataSource(String key) {
        contextHolder.set(key);
    }

    /**
     * 获取数据源
     * @return
     */
    public static String getDataSource() {
        return contextHolder.get();
    }

    /**
     * 重置数据源
     */
    public static void removeDataSource() {
        contextHolder.remove();
    }

}