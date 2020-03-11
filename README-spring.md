# Kite-spring

## 1. 引入

```xml
<dependency>
    <groupId>com.github.developframework</groupId>
    <artifactId>kite-spring</artifactId>
    <version>${version.kite}</version>
</dependency>
```

## 2. 配置

### 2.1. 加载KiteFactory

```xml
<bean id="kiteFactory" class="com.github.developframework.kite.spring.KiteFactoryFactoryBean">
    <property name="configs">
        <set>
            <value>/kite/kite-student.xml</value>
            <value>/kite/kite-account.xml</value>
            <value>/kite/kite-class.xml</value>
        </set>
    </property>
</bean>
```

在配置文件较多时，上述方法比较麻烦，可以采用以下扫描配置文件包的方法缩减代码量。

使用`<kite:scan>`扫描文件包加载Kite configuration文件。

```xml
kitekite<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:kite="https://github.com/developframework/kite/schema"
       xsi:schemaLocation="
		http://www.springframework.org/schema/beans
		http://www.springframework.org/schema/beans/spring-beans-4.1.xsd
		https://github.com/developframework/kite/schema
		https://github.com/developframework/kite/schema/kite-spring.xsd">

    <kite:scan id="kiteFactory" locations="classpath:kite/*.xml" />
</beans>
```

- 此方法需要添加命名空间`xmlns:kite="https://github.com/developframework/kite/schema"`
- `classpath:kite/*.xml`为通配加载kite文件夹下的所有配置文件。

### 2.2. 支持springmvc

当前版本下提供两个`HandlerMethodReturnValueHandler` 实现类，分别对应于Controller方法返回`DataModel` 和`KiteResponse`的处理。

```xml
<mvc:annotation-driven>
    <mvc:return-value-handlers>
        <bean class="com.github.developframework.kite.spring.mvc.DataModelReturnValueHandler" />
        <bean class="com.github.developframework.kite.spring.mvc.KiteResponseReturnValueHandler" />
    </mvc:return-value-handlers>
</mvc:annotation-driven>
```

#### 2.2.1. KiteResponseReturnValueHandler

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

#### 2.2.2. DataModelReturnValueHandler

Controller方法以`DataModel`对象返回将会被交由` DataModelReturnValueHandler` 处理。

```java
@Controller
public class HelloController {

    @JsonviewNamespace("kite-demo")
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

