# kite-spring-boot-starter

## 1. 引入

```xml
<dependency>
  <groupId>com.github.developframework</groupId>
  <artifactId>kite-spring-boot-starter</artifactId>
  <version>${version.kite}</version>
</dependency>
```

## 2. 配置

使用`@Enablekite`注解开启Kite功能

```java
@SpringBootApplication
@EnableKite
public class MyApplication {
	
  	public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

使用注解`@EnableKite`自动化完成对`KiteResponseReturnValueHandler`和`DataModelReturnValueHandler`的注册。在spring-boot的application.properties总配置项中可以使用

```properties
kite.locations=classpath*:kite/*.xml #指定扫描路径
kite.json.usedefault=true #采用默认的ObjectMapper对象
kite.json.namingStrategy= #xml命名策略
kite.xml.namingStrategy= #xml命名策略
kite.xml.suppressDeclaration= #默认true 是否去除xml的头信息（<?xml version="1.0" encoding="UTF-8"?>）
```
