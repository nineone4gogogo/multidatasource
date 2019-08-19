package com.aswald.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author Ethan
 * @Date 2019-08-09 16:29
 * @Description
 **/
@Data
public class DataSourceContext {
    String driverClassName;
    String type;
    String url;
    String username;
    String password;
}
