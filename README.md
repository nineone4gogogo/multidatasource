springboot2.1.X + mybatis + mysql + druid多数据源的配置
===

* 首先,项目建设需要用到多数据源随时切换
* 同一种或多种数据库(MySQL,Oracle,Sql Server)
* 本案例支持同种数据库多数据源动态切换(不同种数据库扩展也是可以的)
******
环境介绍
====
* web框架：SpringBoot2.1.6
* ORM框架：Mybatis 2.1.0
* 数据库连接池：Druid 1.1.19
* 数据源：MySQL
* 运行平台：Jdk8
* 日志配置：Logback
* Maven依赖
```
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-aop</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId> 
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>                        
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
    </dependency>
</dependencies>
```



*****
实现思路
===
* 自定义多个数据源，并指定切换规则
* 引入ThreadLocal来保存和管理数据源上下文标识
* 使用AOP切面编程，根据某些自定义条件，动态切换数据源(反射)
* 访问接口测试效果
*****
具体实现
===

### 步骤1 

参照单数据源构建方式,构建单个DruidDataSource数据源

* application.yml
```
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://192.168.2.53:3306/vip_main
    username: root
    password: 123456
```

* DataSourceConfig.java
```java
@Bean("vipmain")
@ConfigurationProperties(prefix = "spring.datasource")
public DataSource createDataSource()
{
     return DruidDataSourceBuilder.create().build();
}
```

### 步骤2
多数据源构建

* application.yml
```
spring:
  datasource:
    vipmain:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://192.168.2.53:3306/vip_main
      username: root
      password: 123456
    vipfinancial:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://192.168.2.53:3306/vip_financial
      username: root
      password: 123456
```

* DataSourceConfig.java
```java
@Bean("vipmain")
@ConfigurationProperties(prefix = "spring.datasource.vipmain")
public DataSource createDataSource()
{
     return DruidDataSourceBuilder.create().build();
}
@Bean("vipfinancial")
@ConfigurationProperties(prefix = "spring.datasource.vipfinancial")
public DataSource createDataSource()
{
     return DruidDataSourceBuilder.create().build();
}
```
*************
### 步骤3
##### 如果采用上述多数据源配置，优点是代码简洁，缺点是硬编码，10个数据源就需要配置10次，不能动态构建数据源，无法满足我们的需求。 #####

所以我们需要创建以下对象

* 动态数据源对象DynamicDataSource，继承AbstractRoutingDataSource，spring多数据源实现

* 存储当前DataSource上下文信息的DynamicDataSourceContextHolder，实现数据源切换
* 数据源枚举DbNames，数据源统一管理

*************

###### 存储当前DataSource上下文
```java
public class DynamicDataSourceContextHolder {
    /*
     * 使用ThreadLocal维护变量，ThreadLocal为每个使用该变量的线程提供独立的变量副本，
     * 所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
     */
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
```
###### 数据源枚举
```java
public enum DbNames {
    MAIN,
    LTCUSDT,
    BTCUSDT,
    ETHUSDT,
    ETCUSDT,
    EOSUSDT,
    QTUMUSDT,
    SNTUSDT,
    ELFUSDT,
    KNCUSDT,
    LTCBTC,
    ETHBTC,
    EOSBTC,
    ETCBTC,
    DASHBTC,
    LINKBTC,
    OMGBTC,
    ZRXBTC,
    OTC,
    ODS,
    FUTURES,
    BRUSH,
    MANABTC,
    MCOBTC,
    LRCBTC,
    DGDBTC,
    VDSBTC,
    VDSUSDT,
    FINANCIAL
    ;
}
```
* 创建动态数据源类，接管springboot的数据源配置
```java
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 如果希望所有数据源在启动配置时就加载好，这里通过设置数据源Key值来切换数据，定制这个方法
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSource();
    }

}
```

###### AbstractRoutingDataSource源码
```
public abstract class AbstractRoutingDataSource extends AbstractDataSource implements InitializingBean {
    @Nullable
    private Map<Object, Object> targetDataSources;
    @Nullable
    private Object defaultTargetDataSource;
    private boolean lenientFallback = true;
    private DataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
    @Nullable
    private Map<Object, DataSource> resolvedDataSources;
    @Nullable
    private DataSource resolvedDefaultDataSource;

    public AbstractRoutingDataSource() {
    }

    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        this.targetDataSources = targetDataSources;
    }

    public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
        this.defaultTargetDataSource = defaultTargetDataSource;
    }

    public void setLenientFallback(boolean lenientFallback) {
        this.lenientFallback = lenientFallback;
    }

    public void setDataSourceLookup(@Nullable DataSourceLookup dataSourceLookup) {
        this.dataSourceLookup = (DataSourceLookup)(dataSourceLookup != null ? dataSourceLookup : new JndiDataSourceLookup());
    }

    public void afterPropertiesSet() {
        if (this.targetDataSources == null) {
            throw new IllegalArgumentException("Property 'targetDataSources' is required");
        } else {
            this.resolvedDataSources = new HashMap(this.targetDataSources.size());
            this.targetDataSources.forEach((key, value) -> {
                Object lookupKey = this.resolveSpecifiedLookupKey(key);
                DataSource dataSource = this.resolveSpecifiedDataSource(value);
                this.resolvedDataSources.put(lookupKey, dataSource);
            });
            if (this.defaultTargetDataSource != null) {
                this.resolvedDefaultDataSource = this.resolveSpecifiedDataSource(this.defaultTargetDataSource);
            }

        }
    }

    protected Object resolveSpecifiedLookupKey(Object lookupKey) {
        return lookupKey;
    }

    protected DataSource resolveSpecifiedDataSource(Object dataSource) throws IllegalArgumentException {
        if (dataSource instanceof DataSource) {
            return (DataSource)dataSource;
        } else if (dataSource instanceof String) {
            return this.dataSourceLookup.getDataSource((String)dataSource);
        } else {
            throw new IllegalArgumentException("Illegal data source value - only [javax.sql.DataSource] and String supported: " + dataSource);
        }
    }

    public Connection getConnection() throws SQLException {
        return this.determineTargetDataSource().getConnection();
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return this.determineTargetDataSource().getConnection(username, password);
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return iface.isInstance(this) ? this : this.determineTargetDataSource().unwrap(iface);
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this) || this.determineTargetDataSource().isWrapperFor(iface);
    }

    protected DataSource determineTargetDataSource() {
        Assert.notNull(this.resolvedDataSources, "DataSource router not initialized");
        Object lookupKey = this.determineCurrentLookupKey();
        DataSource dataSource = (DataSource)this.resolvedDataSources.get(lookupKey);
        if (dataSource == null && (this.lenientFallback || lookupKey == null)) {
            dataSource = this.resolvedDefaultDataSource;
        }

        if (dataSource == null) {
            throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + lookupKey + "]");
        } else {
            return dataSource;
        }
    }

    @Nullable
    protected abstract Object determineCurrentLookupKey();
}

```

### 步骤4

* 自定义动态数据源切换注解


```
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    
    /**
     * 数据源key值
     * @return
     */
    DbNames value() default DbNames.MAIN;
    
}
```

* 实现切换DataSource的AOP


```

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

```

最终application.yml配置实例

```
spring:
  datasource:
    driver:
      main:
        driver-class-name: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://127.0.0.1:3306/master?useUnicode=true&zeroDateTimeBehavior=convertToNull&autoReconnect=true&characterEncoding=utf-8
        username: root
        password: rootroot
      financial:
        driver-class-name: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://127.0.0.1:3306/slave?useUnicode=true&zeroDateTimeBehavior=convertToNull&autoReconnect=true&characterEncoding=utf-8
        username: root
        password: rootroot
    druid:
      # 初始化大小，最小，最大
      initialSize: 5
      minIdle: 5
      maxActive: 20
      # 配置获取连接等待超时的时间
      maxWait: 60000
      # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
      timeBetweenEvictionRunsMillis: 60000
      # 配置一个连接在池中最小生存的时间，单位是毫秒
      minEvictableIdleTimeMillis: 300000
      validationQuery: SELECT 1 FROM DUAL
      testWhileIdle: true
      testOnBorrow: false
      testOnReturn: false
      # 打开PSCache，并且指定每个连接上PSCache的大小
      poolPreparedStatements: true
      maxPoolPreparedStatementPerConnectionSize: 20
      # 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
      filters: stat,wall,slf4j
      # 通过connectProperties属性来打开mergeSql功能；慢SQL记录
      connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
```

读取配置类

```java
@Data
@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceProperties {
    Map<String,DataSourceContext> driver;
    DruidContext druid;
}

//数据源连接基本信息
@Data
public class DataSourceContext {
    String driverClassName;
    String url;
    String username;
    String password;
}

//druid通用配置信息
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

```



测试样例
===

1.SysUserMapper
```
@Mapper
@DataSource(DbNames.MAIN)
public interface SysUserMapper {

    /**
     * 查询全部用户
     * @return
     */
    @DataSource(DbNames.MAIN)
    List<SysUser> selectAll();

    @DataSource(DbNames.FINANCIAL)
    List<SysUser> selectAll2();
}

/**
*1动态数据源注解使用于Mapper
*
*2支持类注解
*
*3支持方法注解
*
*4方法注解会覆盖类注解
*/

```

2. SysUserService

```
public interface SysUserService {
    List<SysUser> findAll();

    List<SysUser> findAll2();
}

```

3. SysUserServiceImpl

```
@Service
public class SysUserServiceImpl implements SysUserService {
    
    @Autowired
    private SysUserMapper sysUserMapper;
    
    @Override
    public List<SysUser> findAll() {
        return sysUserMapper.selectAll();
    }

    @Override
    public List<SysUser> findAll2() {
        return sysUserMapper.selectAll2();
    }
}
```

4. SysUserController
```
@RestController
@RequestMapping("user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @PostMapping(value="/findAll")
    public List<SysUser> findAll() {
        return sysUserService.findAll();
    }
    @PostMapping(value="/findAll2")
    public List<SysUser> findAll2() {
        return sysUserService.findAll2();
    }

}

```
5. SpringBootApplication

> 注：exclude = DataSourceAutoConfiguration.class 屏蔽spring数据源自动配置
```
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class RpcVdsApp {
    public static void main(String[] args) {
        SpringApplication.run(RpcVdsApp.class, args);
    }
}

```

