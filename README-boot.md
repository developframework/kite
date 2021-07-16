# spring-boot顺滑接入 kite-spring-boot-starter

## 1. 引入

```xml
<dependency>
  <groupId>com.github.developframework</groupId>
  <artifactId>kite-spring-boot-starter</artifactId>
  <version>${version.kite}</version>
</dependency>
```

## 2. 配置

导入包就能自动化完成对`KiteResponseReturnValueHandler`和`DataModelReturnValueHandler`的注册。在spring-boot的application.properties总配置项中可以使用

```properties
kite.locations=classpath*:kite/*.xml #指定扫描路径
kite.json.namingStrategy=FRAMEWORK #json命名策略
kite.xml.namingStrategy=FRAMEWORK #xml命名策略
kite.xml.suppressDeclaration=true #是否去除xml的头信息（<?xml version="1.0" encoding="UTF-8"?>）
```

## 3.优化controller的返回值

#### 3.1. KiteResponseReturnValueHandler

Controller方法以`KiteResponse`对象返回将会被交由` KiteResponseReturnValueHandler` 处理。

```java
@Controller
public class HelloController {
    
    @GetMapping("/hello")
    public KiteResponse hello() {
        KiteResponse res = new EmptyKiteResponse("kite-demo", "hello-view");
        res.putData("sayHello", "Hello kite-spring!");
        return res;
    }
}
```

`KiteResponse`提供以下实现：

| 实现                  | 说明                              |
| --------------------- | --------------------------------- |
| EmptyKiteResponse     | 提供空的内置DataModel             |
| SingletonKiteResponse | 提供可优先包含一个数据的DataModel |

可以继续继承方便自己的使用。

#### 3.2. DataModelReturnValueHandler

Controller方法以`DataModel`对象返回将会被交由` DataModelReturnValueHandler` 处理。

```java
@Controller
public class HelloController {

    @KiteNamespace("kite-demo")
    @TemplateId("hello-view")
    @GetMapping("/hello")
    public DataModel hello() {
        return DataModel.singleton("sayHello", "Hello kite-spring!");
    }
}
```

`@KiteNamespace` 用于申明使用哪个命名空间，该注解也可以加在类上，对类的所有方法起作用。

`@TemplateId` 用于申明使用哪个模板ID。

**在使用返回`DataModel` 模式时，这两个注解必填。**
