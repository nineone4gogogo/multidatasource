package com.aswald.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author Ethan
 * @Date 2019-08-09 16:30
 * @Description
 **/
@Data
@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceProperties {
    Map<String,DataSourceContext> driver;
    DruidContext druid;}
