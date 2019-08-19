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

    private static final ThreadLocal<DbNames> contextHolder=new ThreadLocal<>();


    /**
     * 数据源的 key集合，用于切换时判断数据源是否存在
     */
    public static List<DbNames> dataSourceKeys = new ArrayList<>();

    /**
     * 切换数据源
     * @param key
     */
    public static void setDataSource(DbNames key) {
        contextHolder.set(key);
    }

    /**
     * 获取数据源
     * @return
     */
    public static DbNames getDataSource() {
        return contextHolder.get();
    }

    /**
     * 重置数据源
     */
    public static void removeDataSource() {
        contextHolder.remove();
    }

    /**
     * 判断是否包含数据源
     * @param key
     * @return
     */
    public static boolean existDataSource(DbNames key) {
        return dataSourceKeys.contains(key);
    }

    /**
     * 添加数据源keys
     * @param keys
     * @return
     */
    public static boolean addDataSourceKeys(Collection<? extends Object> keys) {
        return dataSourceKeys.addAll((Collection<? extends DbNames>) keys);
    }
}