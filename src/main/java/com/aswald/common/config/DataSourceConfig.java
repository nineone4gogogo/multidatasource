package com.aswald.common.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.aswald.common.config.DataSourceContext;
import com.aswald.common.config.DataSourceProperties;
import com.aswald.common.datasource.DynamicDataSource;
import com.aswald.common.datasource.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Configuration
@Slf4j
public class DataSourceConfig {


    @Bean("dynamicDataSource")
    @ConditionalOnBean(value = DataSourceProperties.class)
    public AbstractRoutingDataSource dynamicDataSource(DataSourceProperties dataSourceProperties) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>();

        if (null == dataSourceProperties) {
            log.info("读取数据源配置信息失败,初始化数据源失败...");
        }
        dataSourceMap = getDataSourceMap(dataSourceProperties);
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        dynamicDataSource.afterPropertiesSet();
        return dynamicDataSource;
    }

    private Map<Object, Object> getDataSourceMap(DataSourceProperties dataSourceProperties) {
        Map<Object, Object> datasourceMap = new HashMap<>();
        if (dataSourceProperties != null && dataSourceProperties.getDriver().size() > 0) {
            Map<String, DataSourceContext> datasource = dataSourceProperties.getDriver();
            DruidContext druidContext = dataSourceProperties.getDruid();
            DbNames[] dbNames = DbNames.values();
            for (DbNames dbName : dbNames) {
                String key = dbName.name().toLowerCase();
                DataSourceContext dataSourceContext = datasource.get(key);
                if (dataSourceContext != null) {
                    DataSource dataSource = getDataSource(dataSourceContext, druidContext);
                    datasourceMap.put(dbName, dataSource);
                }
            }
        }
        DynamicDataSourceContextHolder.addDataSourceKeys(datasourceMap.keySet());
        return datasourceMap;
    }

    /**
     * 依据数据配置 获取datasource 对象
     *
     * @param params       Map 数据配置
     * @param druidContext
     * @return 返回datasource
     */
    public DataSource getDataSource(DataSourceContext params, DruidContext druidContext) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(params.getUrl());
        druidDataSource.setUsername(params.getUsername());
        druidDataSource.setPassword(params.getPassword());
        druidDataSource.setDriverClassName(params.getDriverClassName());
        druidDataSource.setInitialSize(druidContext.getInitialSize());
        druidDataSource.setMinIdle(druidContext.getMinIdle());
        druidDataSource.setMaxActive(druidContext.getMaxActive());
        druidDataSource.setMaxWait(druidContext.getMaxWait());
        druidDataSource.setTimeBetweenEvictionRunsMillis(druidContext.getTimeBetweenEvictionRunsMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(druidContext.getMinEvictableIdleTimeMillis());
        druidDataSource.setTestWhileIdle(druidContext.isTestWhileIdle());
        druidDataSource.setTestOnBorrow(druidContext.isTestOnBorrow());
        druidDataSource.setTestOnReturn(druidContext.isTestOnReturn());
        druidDataSource.setPoolPreparedStatements(druidContext.isPoolPreparedStatements());
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(druidContext.getMaxPoolPreparedStatementPerConnectionSize());
        try {
            druidDataSource.setFilters(druidContext.getFilters());
        } catch (SQLException e) {
            log.error("druid configuration initialization filter Exception", e);
        }
        druidDataSource.setConnectionProperties(druidContext.getConnectionProperties());
        return druidDataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean(DataSource dynamicDataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        // 配置数据源，此处配置为关键配置，如果没有将 dynamicDataSource作为数据源则不能实现切换
        sessionFactory.setDataSource(dynamicDataSource);
        sessionFactory.setTypeAliasesPackage("com.aswald.**.model");    // 扫描Model
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath*:mapper/**/*.xml"));    // 扫描映射文件
        return sessionFactory.getObject();
    }

    /**
     * 事务管理, 使用事务时在方法头部添加@Transactional注解即可
     *
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dynamicDataSource) {
        return new DataSourceTransactionManager(dynamicDataSource);
    }

    @Bean(name = "druidServlet")
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        reg.addInitParameter("allow", ""); // 白名单
        return reg;
    }

    @Bean(name = "filterRegistrationBean")
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        filterRegistrationBean.addInitParameter("principalCookieName", "USER_COOKIE");
        filterRegistrationBean.addInitParameter("principalSessionName", "USER_SESSION");
        filterRegistrationBean.addInitParameter("DruidWebStatFilter", "/*");
        return filterRegistrationBean;
    }

}