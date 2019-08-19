package com.aswald.common.config;

import lombok.Data;

/**
 * @Author Ethan
 * @Date 2019-08-12 16:33
 * @Description
 **/
@Data
public class DruidContext {
    private int initialSize;

    private int minIdle;

    private int maxActive;

    private int maxWait;

    private int timeBetweenEvictionRunsMillis;

    private int minEvictableIdleTimeMillis;

    private boolean testWhileIdle;

    private boolean testOnBorrow;

    private boolean testOnReturn;

    private boolean poolPreparedStatements;

    private int maxPoolPreparedStatementPerConnectionSize;

    private String filters;

    private String connectionProperties;
}
