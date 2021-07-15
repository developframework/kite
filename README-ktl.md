## KTL语法

KTL(Kite Template Language) 也能替代xml来声明片段模板，支持所有xml配置的所有功能

比如以下复杂配置：

```
fragment: id = parent
    property: data = success
    array: data = users
        slot

# 此为注释行
template: id = user-list, extend = parent
    property: data = id
    property: data = name
    # 也可以加单引号
    property-date: data = 'createTime'
```

等价于xml

```xml
<fragment id="parent">
    <property data="success"/>
    <array data="users">
    	<slot/>
    </array>
</fragment>

<template id="user-list" extend="parent">
	<property data="id"/>
    <property data="name"/>
    <property-date data="createTime"/>
</template>
```

KTL主要适用于java代码中的小型模板声明，结合JDK15的text block特性会非常趁手

```java
DataModel dataModel = DataModel
        .singleton("users", DataMock.users())
        .putData("success", true);

final String json = kiteFactory
        .getJsonProducer(dataModel, """
                fragment: id = parent
                    property: data = success
                    array: data = users
                        slot
                template: id = user-list, extend = parent
                    property: data = id
                    property: data = name
                    property-date: data = createTime
        """)
        .produce(true);
System.out.println(json);
```

```json
{
  "success" : true,
  "users" : [ {
    "id" : 1,
    "name" : "user1",
    "create_time" : "2021-01-01 12:11:13"
  }, {
    "id" : 2,
    "name" : "user2",
    "create_time" : "2021-01-02 12:11:13"
  } ]
}
```

结合springmvc使用有更好的效果

```java
@Controller
@KiteNamespace("kite-user")
@RequestMapping("user")
public class UserController {

    @TemplateKTL("""
            template: id = user-list-ktl, data = users
                property: data = id
                property: data = name
    """)
    @GetMapping
    public DataModel userList() {
        return DataModel.singleton("users", DataMock.users());
    }
}
```

在controller中声明的片段或模板，是注册入全局片段库的，也就是在xml里也能引用controller中声明的片段。反过来在controller中的ktl也能引用到xml声明的片段