# Kite

Kite框架实现通过XML文件配置来自定义json和xml格式，大大提升了java生成json和xml字符串的自由性，让开发模块化更加便捷快速。

Kite是一款面向接口编程的框架，目前支持如下主流序列化框架作为核心技术：

+ 以**Jackson**作为核心序列化json
+ 以**Fastjson**作为核心序列化json
+ 以**Gson**作为核心序列化json
+ 以**Dom4j**作为核心序列化xml

### 运行环境

JDK11及以上

### 引入方式

```xml
<dependency>
  <groupId>com.github.developframework</groupId>
  <artifactId>kite-jackson</artifactId>
  <version>${version.kite}</version>
</dependency>
<dependency>
  <groupId>com.github.developframework</groupId>
  <artifactId>kite-fastjson</artifactId>
  <version>${version.kite}</version>
</dependency>
<dependency>
  <groupId>com.github.developframework</groupId>
  <artifactId>kite-gson</artifactId>
  <version>${version.kite}</version>
</dependency>
<dependency>
  <groupId>com.github.developframework</groupId>
  <artifactId>kite-dom4j</artifactId>
  <version>${version.kite}</version>
</dependency>
```

json或xml选用一种底层实现技术就行

## 0. 文档传送

- 为什么使用Kite?
- [基础教程](https://github.com/developframework/kite/blob/master/README-basic.md)
- [高级功能](https://github.com/developframework/kite/blob/master/README-pro.md)
- [spring-boot顺滑接入 kite-spring-boot-starter](https://github.com/developframework/kite/blob/master/README-boot.md)
- [支持JDK15 text block特性——KTL语法](https://github.com/developframework/kite/blob/master/README-ktl.md)

## 1. 为什么使用Kite?


>  场景1：最原始的场景，从依靠持久层框架（这里是spring-data-jpa为例）从数据库中查询一条记录并使用Jackson序列化成json响应

```java
@RestController
@RequestMapping("users")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("{id}")
    public UserPO findUserDetail(@PathVariable int id) {
        return userRepository.findById(id);
    }
}
```

这里的痛点：

+ Jackson序列化了UserPO这个和数据库交互的实体类**PO**（Persist Object），把表的所有字段都做了输出，类似password这种敏感字段都出去了
+ 不能重命名字段的名称
+ 不能可选的设置null值不参与序列化
+ 不能对字段的值进行处理

>  场景2：这时候你会说那么Jackson、Fastjson框架可以在UserPO里加注解来定义序列化的结果。那么看场景2，UserPO是如下定义的：

```java
@Getter
@Setter
@Entity
@Table(name="user")
@JsonInclude(Include.NON_NULL)   // 此处加了Jackson的注解用于不序列化null值的字段
public class UserPO {
    
    @Id
    private Integer id;
    
    @JsonSerialize(using = MobileEncryptSerializer.class) // 此处加了Jackson的注解用于加密手机号
    @Column(nullable=false, length=11, unique=true)
    private String mobile;
    
    @JsonProperty("username")	// 此处加了Jackson的注解用于重命名字段名称
    @Column(nullable=false, length=20)
    private String name;
    
    @JsonIgnore  // 此处加了Jackson的注解用于忽略该字段的序列化
    @Column(nullable=false, length=32)
    private String password;
    
    @CreateDate
    private String createTime;
}
```

```java
// 手机号的加密器，把中间4位转为*
public class MobileEncryptSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String mobile, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(mobile.substring(0, 3) + "****" + mobile.substring(7));
    }
}
```

这里的痛点：

+ 很明显，持久层框架JPA的注解和Jackson的注解混在一起，代码非常混乱
+ `@JsonInclude(Include.NON_NULL)`只能设置在类上，代表全部字段的null策略，不能精细控制到某个字段
+ `@JsonSerialize`、` @JsonProperty`和` @JsonIgnore`之类的注解一旦加上了就适用在任何场景的序列化上。做不到有时候需要显示，有时候不需要显示

> 场景3：针对场景2，你会说那么把UserPO的数据导入到多个DTO类，或者使用`@JsonView`注解适用于不同的场景下的响应需求，那么看场景3：

```java
@Data
public class UserDTO {
    
    // 平台管理查询
    public interface ForManage {}
    
    private int id;
    
    @JsonProperty("username")
    private String name;
    
    @JsonView(ForManage.class) 	// 平台管理查询的时候需要显示password字段
    private String password;
    
    private String createTime;
}
```

```java
@RestController
@RequestMapping("users")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("{id}")
    @JsonView(UserDTO.ForManage.class)  // 这里选择了使用什么视图
    public UserDTO findUserDetail(@PathVariable int id) {
        UserPO userPO = userRepository.findById(id);
        // 这里采用mapstruct这种搬数据的框架，如果自己写set语句那么又是一长串的样板代码
        return userMapper.toDTO(userPO)
    }
}
```

这里的痛点：

+ 需要新建一个传输对象做数据迁移操作
+ 新建了两个不必要的类UserDTO和UserDTO.ForManage，如果不同的场景更多，那么需要建更多的类

> 场景4：多个数组数据无法插接到一起：

```java
@RestController
@RequestMapping("users")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    
    @GetMapping("list")
    public List<UserDTO> findUserAndAddressList() {
        List<UserPO> users = userRepository.findAll();
        List<AddresPO> addresses = addressRepository.findAll();
        // 下面想把每个user的多个address收货地址插接到一起，那么得自己来干这件事，并输出到List<UserDTO>
        return null;
    }
}
```

最终想得到类似下面结构的json

```json
[
    {
        "id": 1,
        "mobile": "177****7777",
        "name": "小张",
        "addresses": [
            {
                "userId": 1,
            	"county": "北京-北京市-朝阳区",
                "location": "xxxxxx"
        	},
            {
                "userId": 1,
            	"county": "北京-北京市-海淀区",
                "location": "yyyyyy"
        	}
        ]
    },
    {
        "id": 2,
        "mobile": "188****8888",
        "name": "小李",
        "addresses": [
            {
                "userId": 2,
            	"county": "上海-上海市-黄浦区",
                "location": "zzzzzz"
        	}
        ]
    }
]
```

> 场景5：如果固定的json格式则需要一个通用的实体类来封装，比如最常见的类似下面的实体类：

```java
@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Result<T> {
    
    private final boolean success;
    
    private final String message;
    
    private final T data;
    
    public static <T> Result<T> ok(T data) {
        return new Result(true, null, data);
    }
    
    public static <T> Result<T> fail(String message) {
        return new Result(false, message, null);
    }
}
```

```java
@RestController
@RequestMapping("users")
public class UserController {
    
    @GetMapping
    public Result<UserPO> find() {
        try {
           UserPO user = // 干点查询
           return Result.ok(dto);
        } catch(Exception e) {
            return Result.fail(e.getMessage);
        }
    }
}
```

输出下面的格式：

```json
{
    "success": true,
    "message": null,
    "data": {
        "user": {
            ...
        }
    }
}
```

------

使用Kite都能解决以上痛点

> 场景1、场景2和场景3的解决

```java
@Controller
@KiteNamespace("kite-user")
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @TemplateId("user-detail")
    @GetMapping("{id}")
    public DataModel findUserDetail(@PathVariable int id) {
        return DataModel
                .singleton("user", userRepository.findById(id))
                // 可以有条件控制分支
                .putData("needPassword", true);
                // 声明手机号的加密逻辑
			    .putConverter("mobileEncryptConverter", (KiteConverter<String, String>) mobile -> mobile.substring(0, 3) + "****" + mobile.substring(7));
    }
}
```

```xml
<template id="user-detail" data="user">
    <property data="id"/>
    <!-- 可以对字段处理 -->
    <property data="mobile" converter="mobileEncryptConverter"/>
    <!-- 可以重命名字段 -->
    <property data="name" alias="username"/>
    <!-- 可以有条件选择是否需要哪些字段 -->
    <if condition="needPassword">
        <property data="password"/>
    </if>
    <!-- 可以精细控制某个字段null是不是显示 -->
    <property-date data="createTime" null-hidden="true"/>
</template>
```

完全没有新建任何类，也没有给UserPO加入任何注解

> 场景4的解决

```java
@Controller
@KiteNamespace("kite-user")
@RequestMapping("users")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    
    @TemplateId("user-list")
    @GetMapping("list")
    public DataModel findUserAndAddressList() {
        List<UserPO> users = userRepository.findAll();
        List<AddresPO> addresses = addressRepository.findAll();
        // 这里啥都不用干，直接把数据放入即可
        return DataModel
            .singleton("users", users)
            .putData("addresses", addresses)
            // 声明如何关联
            .putRelFunction("rel", (RelFunction<UserPO, AddressPO>)(u, ui, a, ai) -> u.getId().equals(a.getUserId()))
    }
}
```

```xml
<template id="user-list" data="users">
    <!-- 可以直接引用之前声明过的片段，无需重复定义 -->
    <include id="user-detail"/>
    <!-- 一对多的关联，自动匹配插接数据 -->
    <relevance data="#addresses" rel="rel">
        <property data="userId"/>
        <property data="county"/>
        <property data="location"/>
    </relevance>
</template>
```

> 场景5的解决，不需要建类

```java
@RestController
@KiteNamespace("kite-user")
@RequestMapping("users")
public class UserController {
    
    @TemplateId("user-extend")
    @GetMapping
    public DataModel find() {
        try {
           UserPO user = // 干点查询
           return DataModel.singleton("success", true).putData("user", user);
        } catch(Exception e) {
           return DataModel.singleton("success", false).putData("message", e.getMessage());
        }
    }
}
```

```xml
<!-- 公共的父级格式 -->
<fragment id="common-parent">
    <property data="success"/>
    <property data="message" null-hidden="true"/>
    <!-- 虚拟层结构，并没有data这个数据 -->
    <object-virtual alias="data">
        <!-- 锚点，所有的子片段插接于此 -->
        <slot/>
    </object-virtual>
</fragment>

<!-- 子片段声明继承自哪个父片段 -->
<template id="user-extend" data="user" extend="common-parent">
    <include id="user-detail"/>
</template>
```

