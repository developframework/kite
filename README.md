# Kite

## 0. 文档传送

- [kite-spring](https://github.com/developframework/kite/blob/master/README-spring.md)
- 
  [kite-spring-boot-starter](https://github.com/developframework/kite/blob/master/README-boot.md)

## <a name="chapter1">**1. 简介**</a>

Kite框架构建于jackson和dom4j框架之上，实现通过XML文件配置来自定义json和xml格式，大大提升了java生成json和xml字符串的自由性，让开发模块化更加便捷快速。

### **1.1. 运行环境**

JDK11及以上

### **1.2. 使用方式**

maven

```xml
<dependency>
  <groupId>com.github.developframework</groupId>
  <artifactId>kite-core</artifactId>
  <version>${version.kite}</version>
</dependency>
```

## 2. HelloWorld

一个最简单的kite使用示例：

```java
KiteOptions options=new KiteOptions();
        KiteFactory kiteFactory=KiteFactoryBuilder.buildFromClasspathXml(options,"/kite/kite-demo.xml");
        ObjectMapper objectMapper=new ObjectMapper();
        kiteFactory.useJsonFramework(new JacksonFramework(objectMapper));
        kiteFactory.useXmlFramework(new Dom4jFramework());

        DataModel dataModel=DataModel.singleton("sayHello","Hello Kite!");
// 生成json
        String json=kiteFactory.getJsonProducer(dataModel,"kite-demo","first-view").produce(false);
        System.out.println(json);
// 生成xml
        String xml=kiteFactory.getXmlProducer(dataModel,"kite-demo","first-view").produce(false);
        System.out.println(xml);
```

你需要一份Kite XML配置，位置在上述声明的/kite/kite-demo.xml：

```xml

<kite-configuration xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xmlns="https://github.com/developframework/kite/schema"
                    xsi:schemaLocation="
	https://github.com/developframework/kite/schema kite-configuration.xsd">

  <template-package namespace="kite-demo">

    <template id="first-view">
      <property data="sayHello"/>
    </template>

  </template-package>

</kite-configuration>
```

运行结果：

```json
{"sayHello":"Hello Kite!"}
```

```xml

<xml>
  <say-hello>Hello Kite!</say-hello>
</xml>
```

## <a name="chapter3">**3. 概览**</a>

### **3.1. java概览**

#### **3.1.1. DataModel**

> com.github.developframework.kite.data.DataModel

Kite框架的数据模型。用于装载需要在视图中渲染的数据或函数接口实现，数据由键值对构成。接口提供存入和取出数据的方法，支持链式写法。

```java
DataModel dataModel=DataModel.singleton("sayHello","Hello Kite!");
```

#### **3.1.2. Expression**

> com.github.developframework.expression.Expression

是kite框架从DataModel中提取数据的表达式。不论dataModel存的是java实体类还是Map对象都可以使用表达式取值。 范例：

- `student` 你可以从DataModel对象内取得名为student的对象
- `#student` 你可以从DataModel对象内 **强制从根路径** 取得名为student的对象
- `student.name` 你可以从DataModel对象内取得名为student的对象的name属性值
- `students[0]` 你可以从DataModel对象内取得名为students的数组内的第1个元素
- `student[0].name` 你可以从DataModel对象内取得名为students的数组内的第1个元素的name属性值

`Expression` 的详细使用请查看独立项目[expression](https://github.com/developframework/expression)

#### 3.1.3. KiteOptions

> com.github.developframework.kite.core.KiteOptions

Kite框架的配置类。

```java
KiteOptions options=new KiteOptions();
        options.getJson().setNamingStrategy(NamingStrategy.LOWER_CASE);
```

#### 3.1.4. KiteFactory

> com.github.developframework.kite.core.KiteFactory

类是Kite框架的构建工厂。使用Kite框架的第一步就是建立该对象。 建立该对象需要提供配置文件路径的字符串，多份配置文件可以采用字符串数组。

```java
final String[]xmlFiles={"config1.xml","config2.xml"};
        KiteFactory kiteFactory=KiteFactoryBuilder.buildFromClasspathXml(options,xmlFiles);
```

#### 3.1.5. Framework

#### 3.1.6. Producer

> com.github.developframework.kite.core.Producer

接口是json字符串建造类，根据`kiteFactory.useFramework()`方法传入的`Framework`
对象不同可以得出不同实现的Producer。

```java
// 直接生成字符串结果
String produce(boolean pretty);

// 向输出流输出结果
        void output(OutputStream outputStream,Charset charset,boolean pretty);
```

#### **3.1.8. 异常**

Kite框架的所有异常类。

| 异常                              | 说明                             |
| --------------------------------- | -------------------------------- |
| KiteException                     | kite顶级异常                     |
| ConfigurationSourceException      | 配置源异常                       |
| TemplateUndefinedException        | template未定义异常               |
| TemplatePackageUndefinedException | templatePackage未定义异常        |
| KiteParseXmlException             | 配置文件解析错误异常             |
| LinkSizeNotEqualException         | 使用link功能时数组大小不相等异常 |
| ResourceNotUniqueException        | 资源定义不唯一异常               |
| InvalidArgumentsException         | 无效的参数异常                   |
| DataUndefinedException            | data没有定义在DataModel异常      |

### **3.2. XML概览**

#### **3.2.1. 结构**

Kite configuration 文档的结构如下： 

```xml
<kite-configuration>
    <template-package namespace="">
    	<template id="">
    		<!-- 定义视图内容 -->
    	</template>
    	<template id="">
    		<!-- 定义视图内容 -->
    	</template>
    	<!-- 其它template -->
    </template-package>
    <!-- 其它template-package -->
</kite-configuration>
```

在`<kite-configuration>`节点中你可以配置任意数量的`<template-package>`，代表不同的模板包，在`<template-package>`节点上你必须声明命名空间`namespace`属性，并且`namespace`是唯一的，不然会抛出`ResourceNotUniqueException`。

在每个`<template-package>`节点中你可以配置任意数量的`<template>`。每个`<template>`即代表了某一种json格式的视图，在`<template>`节点你必须声明id属性，并且id必须是唯一的，不然会抛出`ResourceNotUniqueException`。

Kite configuration文档不是唯一的，Kite框架允许你拥有多份的Kite configuration配置文档，文档的加载顺序不分先后。

#### **3.2.2. 标签**

基本型标签

- `<template>`
- `<object>`
- `<array>`
- `<property>`
- `<this>`
- `<prototype>`
- `raw`
- `<xml-attribute>`

功能型标签

- `<include>`
- `<link>`
- `<relevance>`
- `<object-virtual>`
- `<slot>`
- `<if>`、 `<else>`
- `<switch>`、`<case>`、`<default>`

拓展型标签

- `<property-date>`

- `<property-unixtimestamp>`

- `<property-boolean>`
- `<property-enum>` `enum`

##### **3.2.2.1. 基本型标签**

###### a) template

当你需要声明一个模板时，你将会使用到`<template>`标签。

  ```xml
<template id="">

</template>
  ```

| 属性         | 功能                                                         | 是否必须 |
| ------------ | ------------------------------------------------------------ | -------- |
| id           | 声明模板编号，在命名空间中唯一                               | 是       |
| data         | 取值表达式                                                   | 否       |
| extend       | 声明继承的kite和端口，格式为**namespace.id**（namespace不填时默认为当前template所在的namespace） | 否       |
| xml-root     | 生成xml时的根节点名称                                        | 否       |

以及包含`<array>`节点的所有属性

###### b) object

当你需要构建一个对象结构时，你将会使用到`<object>`标签。详见[4.1.节](#chapter41)

```xml

<object data="">

</object>
```

| 属性            | 功能                                                         | 是否必须 |
| --------------- | ------------------------------------------------------------ | -------- |
| data            | 取值表达式                                                   | 是       |
| alias           | 别名，你可以重新定义显示名                                   | 否       |
| naming-strategy | 命名策略，默认FRAMEWORK                                      | 否       |
| null-hidden     | true时表示表达式取的值为null时隐藏该节点，默认为false        | 否       |
| converter       | 类型转换器全限定类名或expression表达式。详见[5.1.1节](#chapter511) | 否       |

###### c) array

当你需要构建一个数组结构时，你将会使用到`<array>`标签。详见[4.6.节](#chapter46)

```xml
<array data="">

</array>
```

| 属性         | 功能                                                         | 是否必须 |
| ------------ | ------------------------------------------------------------ | -------- |
| data         | 取值表达式                                                   | 是       |
| alias        | 别名，你可以重新定义显示名                                   | 否       |
| naming-strategy | 命名策略，默认FRAMEWORK | 否 |
| null-hidden  | true时表示表达式取的值为null时隐藏该节点，默认为false        | 否       |
| converter | 类型转换器全限定类名或expression表达式。详见[5.1.1节](#chapter511) | 否 |
| map | MapFunction的实现类全名或Expression表达式。详见[5.1.2节](#chapter512) | 否       |
| xml-item     | 生成xml时，子节点数组项的节点名称                            | 否       |
| limit | 取前若干个元素 | 否 |
| comparator | Comparator比较器接口实现类表达式 | 否 |
| null-empty | true时表示表达式取的值为null时设为空数组，默认为false | 否 |

`<array>`标签可以没有子标签，这时表示数组为基本类型数组，如果是对象将会调用它的`toString()`方法。

###### d) property

当你需要构建一个普通属性结构时， 你将会使用到`<property>`标签。

```xml

<property data=""/>
```

| 属性            | 功能                                                         | 是否必须 |
| --------------- | ------------------------------------------------------------ | -------- |
| data            | 取值表达式                                                   | 是       |
| alias           | 别名，你可以重新定义显示名                                   | 否       |
| naming-strategy | 命名策略，默认FRAMEWORK                                      | 否       |
| converter       | 类型转换器全限定类名或expression表达式。详见[5.1.1节](#chapter511) | 否       |
| null-hidden     | true时表示表达式取的值为null时隐藏该节点，默认为false        | 否       |
| xml-cdata       | 生成xml时是否使用`<![CDATA[  ]]>`，默认false                 | 否       |

###### e) this

指代节点本身值

```xml

<this alias="">

</this>
```

| 属性            | 功能                                                         | 是否必须 |
| --------------- | ------------------------------------------------------------ | -------- |
| alias           | 别名，你可以重新定义显示名                                   | 是       |
| naming-strategy | 命名策略，默认FRAMEWORK                                      | 否       |
| null-hidden     | true时表示表达式取的值为null时隐藏该节点，默认为false        | 否       |
| converter       | 类型转换器全限定类名或expression表达式。详见[5.1.1节](#chapter511) | 否       |
| xml-cdata       | 生成xml时是否使用`<![CDATA[  ]]>`，默认false                 | 否       |

###### f) prototype

原型实体构建结构，即使用实现框架自己的序列化功能， 你将会使用到`<prototype>`标签。详见[4.7.节](#chapter47)

```xml

<prototype data=""/>
```

| 属性            | 功能                                                         | 是否必须 |
| --------------- | ------------------------------------------------------------ | -------- |
| data            | 取值表达式                                                   | 是       |
| alias           | 别名，你可以重新定义显示名                                   | 否       |
| naming-strategy | 命名策略，默认FRAMEWORK                                      | 否       |
| converter       | 类型转换器全限定类名或expression表达式。详见[5.1.1节](#chapter511) | 否       |
| null-hidden     | true时表示表达式取的值为null时隐藏该节点，默认为false        | 否       |

###### g) raw

使用raw字符串构建结构， 你将会使用到`<raw>`标签。

```xml

<raw data=""/>
```

| 属性            | 功能                                                         | 是否必须 |
| --------------- | ------------------------------------------------------------ | -------- |
| data            | 取值表达式                                                   | 是       |
| alias           | 别名，你可以重新定义显示名                                   | 否       |
| naming-strategy | 命名策略，默认FRAMEWORK                                      | 否       |
| converter       | 类型转换器全限定类名或expression表达式。详见[5.1.1节](#chapter511) | 否       |
| null-hidden     | true时表示表达式取的值为null时隐藏该节点，默认为false        | 否       |

###### h) xml-attribute

在输出xml时，提供配置xml节点的属性值。

```xml

<xml-attribute data=""/>
```

| 属性            | 功能                                                  | 是否必须 |
| --------------- | ----------------------------------------------------- | -------- |
| data            | 取值表达式                                            | 是       |
| alias           | 别名，你可以重新定义显示名                            | 否       |
| naming-strategy | 命名策略，默认FRAMEWORK                               | 否       |
| null-hidden     | true时表示表达式取的值为null时隐藏该节点，默认为false | 否       |

##### **3.2.2.2. 功能型标签**

###### a) include

Kite框架提供模块化设计json结构视图的功能。在一个`<template>`标签中你可以采用`<include>`标签来导入其它的`<template>`的结构内容，从而实现模块化单元分解。详见[5.3.1.节](#chapter531)

```xml
<include id="" namespace=""/>
```

| 属性      | 功能                                       | 是否必须 |
| --------- | ------------------------------------------ | -------- |
| id        | 需要导入的template id                      | 是       |
| namespace | template的命名空间，不填默认为当前命名空间 | 否       |

###### b) link

该标签用于实现一对一链接对象功能。详见[5.4.1.节](#chapter541)。

```xml

<link data="">

</link>
```

| 属性            | 功能                                                  | 是否必须 |
| --------------- | ----------------------------------------------------- | -------- |
| data            | 取值表达式，**data必须指代一个List或array类型的对象** | 是       |
| alias           | 别名，你可以重新定义显示名                            | 否       |
| naming-strategy | 命名策略，默认FRAMEWORK                               | 否       |
| null-hidden     | true时表示表达式取的值为null时隐藏该节点，默认为false | 否       |

###### c) relevance
该标签用于实现一对多关联功能。详见[5.4.2.节](#chapter542)。

```xml

<relevance data="" rel="">

</relevance>
```

| 属性           | 功能                                       | 是否必须 |
| ------------ | ---------------------------------------- | ---- |
| data         | 取值表达式，**data必须指代一个List或array类型的对象**      | 是    |
| alias        | 别名，你可以重新定义显示名                            | 否    |
| naming-strategy | 命名策略，默认FRAMEWORK | 否 |
| rel | 关联判定器全限定类名或expression表达式                 | 是    |
| null-hidden  | true时表示表达式取的值为null时隐藏该节点，默认为false        | 否    |
| map | KiteConverter的实现类全名或Expression表达式。详见[5.1.2节](#chapter512) | 否    |
| null-empty | true时表示表达式取的值为null时设为空数组，默认为false | 否 |

###### d) object-virtual

该标签用于虚拟对象结构。详见[5.2.节](#chapter52)

```xml

<object-virtual alias="">

</object-virtual>
```

| 属性    | 功能   | 是否必须 |
| ----- | ---- | ---- |
| alias | 别名   | 是    |

###### f) slot

此标签位置作为子`<template>`的插槽位置。和template的`extend`结合使用。详见[5.3.2节](#chapter532)

```xml

<template id="" extend="">
  <slot/>
</template>
```

###### g) if  else

分支结构标签。详见[5.5.1节](#chapter551)

```xml
<if condition="">

</if>
<else>

</else>
```

| 属性        | 功能                      | 是否必须 |
| --------- | ----------------------- | ---- |
| condition | 条件接口实现类全名或Expression表达式 | 是    |

###### h) switch  case  default

分支结构标签。详见[5.5.2节](#chapter552)

```xml
<switch check-data="">
	<case test=""></case>
	<case test=""></case>
	<default></default>
</switch>
```

| 属性       | 功能                             | 是否必须 |
| ---------- | -------------------------------- | -------- |
| check-data | 取值表达式                       | 否       |
| test       | CaseTestFunction接口实现类表达式 | 是       |


##### **3.2.2.3. 拓展型标签**

###### a) property-date

该标签拓展于`<property>`，针对时间日期类型，使用`pattern`属性来格式化日期时间。详见[4.3.1.节](#chapter431)

| 拓展属性    | 功能                                  | 是否必须 |
| ------- | ----------------------------------- | ---- |
| pattern | 格式化模板字符串，不填默认为"yyyy-MM-dd HH:mm:ss" | 否    |

支持的时间日期类型：

- java.util.Date
- java.sql.Date
- java.sql.Time
- java.sql.Timestamp
- (JDK8) java.time.LocalDate
- (JDK8) java.time.LocalDateTime
- (JDK8) java.time.LocalTime
- (JDK8) java.time.Instant

###### b) property-unixtimestamp

该标签拓展于`<property>`，针对时间日期类型，可以将时间日期类型转化为unix时间戳格式显示。可支持的时间日期类型同`<property-date>`。详见[4.3.2.节](#chapter432)

###### c) property-boolean

该标签拓展于`<property>`，可以将数字类型（short、int、long）变为boolean型，非0值为true，0值为false。详见[4.3.3节](#chapter433)

###### d) property-enum

该标签拓展于`<property>`，可以将值映射成另一个固定值。详见[4.3.4节](#chapter434)

## <a name="chapter4">**4. 基本使用**</a>

模型声明（以下各小节示例代码均使用这些模型实体类）：

```java
// 公司类 Company.java
@Data
@RequiredArgsConstructor
public class Company {
  // 公司ID
  private final Integer companyId;
  // 公司名称
  private final String companyName;
}
```

```java
// 部门 Department.java   一个公司多个部门
@Data
@RequiredArgsConstructor
public class Department {
  // 部门ID
  private final Integer departmentId;
  // 部门名称
  private final String departmentName;
}
```

```java
// 员工 Staff.java   一个部门多个
@Data
@RequiredArgsConstructor
public class Staff {
  // 员工ID
  private final Integer staffId;
  // 部门ID
  private final Integer departmentId;
  // 员工姓名
  private final String staffName;
  // 性别
  private final Gender gender;
  // 生日
  private final LocalDate birthday;

  public enum Gender {
    MALE, FEMALE, UNKNOWN
  }
}
```

对应的kite configuration文件配置

```xml
<!-- 忽略kite-configuration -->
<template-package namespace="kite-demo">

  <template id="company-info" data="company">
    <property data="companyId"/>
    <property data="companyName"/>
  </template>

  <template id="department-info" data="department">
    <property data="departmentId"/>
    <property data="companyId"/>
    <property data="departmentName"/>
  </template>

  <template id="staff-info" data="staff">
    <property data="staffId"/>
    <property data="departmentId"/>
    <property data="staffName"/>
    <property data="gender"/>
    <property-date data="birthday"/>
  </template>

</template-package>
```

伪造数据

```java
public final class DemoDataMock {

  public static Company[] mockCompanies() {
    return new Company[]{
            new Company(1, "XX科技公司"),
            new Company(2, "YY网络公司")
    };
  }

  public static Department[] mockDepartments() {
    return new Department[]{
            new Department(11, 1, "技术部"),
            new Department(12, 2, "运营部"),
            new Department(13, 1, "财务部"),
            new Department(14, 2, "事业部")
    };
  }

  public static Staff[] mockStaffs() {
    return new Staff[]{
            new Staff(111, 11, "小张", Staff.Gender.MALE, LocalDate.of(1988, 2, 3)),
            new Staff(112, 12, "小王", Staff.Gender.FEMALE, LocalDate.of(1992, 12, 6)),
            new Staff(113, 13, "小李", Staff.Gender.FEMALE, LocalDate.of(1995, 6, 15)),
            new Staff(114, 14, "小孙", Staff.Gender.UNKNOWN, LocalDate.of(1983, 11, 8)),
            new Staff(115, 12, "小郑", Staff.Gender.FEMALE, LocalDate.of(1986, 9, 5)),
            new Staff(116, 13, "小钱", Staff.Gender.MALE, LocalDate.of(1984, 10, 1)),
            new Staff(117, 14, "小潘", Staff.Gender.FEMALE, LocalDate.of(1990, 12, 12)),
    };
  }

  public static Integer[] mockScores() {
    return new Integer[]{70, 85, 93, 87, 81, 94, 97, 86};
  }
}
```

### 4.1. 简单输出模型对象

```java
// 初始化kiteFactory 后续不再赘述
final ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
final JavaTimeModule javaTimeModule=new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class,new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        objectMapper.registerModules(javaTimeModule);
final KiteFactory kiteFactory=KiteFactoryBuilder.buildFromClasspathXml(new KiteOptions(),"kite/kite-demo.xml");
        kiteFactory.useJsonFramework(new JacksonFramework(objectMapper));
        kiteFactory.useXmlFramework(new Dom4jFramework());

        DataModel dataModel=DataModel.singleton("company",DemoDataMock.mockCompanies()[0]);
        String json=kiteFactory.getJsonProducer(dataModel,"kite-demo","company-info").produce(false);
        String xml=kiteFactory.getXmlProducer(dataModel,"kite-demo","company-info").produce(false);
        System.out.println(json);
        System.out.println(xml);
```

执行结果：

```json
{
  "company_id": 1,
  "company_name": "XX科技公司"
}
```

```xml

<xml>
  <company-id>1</company-id>
  <company-name>XX科技公司</company-name>
</xml>
```

### 4.2. 使用alias修改显示名称

```xml

<template id="company-info" data="company">
  <property data="companyId" alias="id"/>
  <property data="companyName" alias="name"/>
</template>
```

```json
{
  "id": 1,
  "name": "XX科技公司"
}
```

```xml

<xml>
  <id>1</id>
  <name>XX科技公司</name>
</xml>
```

### 4.3. naming-strategy命名策略

> com.github.developframework.kite.core.strategy.KitePropertyNamingStrategy

json节点和xml节点名称的命名策略扩展，继承接口

```java
public interface KitePropertyNamingStrategy {

  /**
   属性最终显示名称
   */
  String propertyDisplayName(Framework<?> framework, String name);
}
```

传入的name是data的变量名称，通过接口方法改写得出最终要显示的名称。

Kite内置接口实现：

+ **FRAMEWORK** JacksonKitePropertyNamingStrategy 用实现框架配置的策略命名，比如Jackson ObjectMapper设置的PropertyNamingStrategy
+ **MIDDLE_LINE** MiddleLineKitePropertyNamingStrategy 中划线命名策略，AbCd => ab-cd
+ **UNDERLINE** UnderlineXmlKitePropertyNamingStrategy 下划线xml命名策略，AbCd => ab_cd
+ **LOWDER_CASE** LowerCaseKitePropertyNamingStrategy 全小写命名策略， AbCd => abcd
+ **ORIGINAL** OriginalKitePropertyNamingStrategy 什么都不做，使用原名

可在`KiteOptions`里分别给json和xml的序列化设置全局命名策略

```java
final KiteOptions options=new KiteOptions();
        options.getJson().setNamingStrategy(NamingStrategy.UNDERLINE);
        options.getXml().setNamingStrategy(NamingStrategy.MIDDLE_LINE);
```

也可以在实际的模板里某个内容节点上配置`naming-strategy` 属性强制使用某个策略

```xml

<template id="test-naming-strategy">
  <object data="myCompany" naming-strategy="ORIGINAL">
    <property data="companyId" naming-strategy="MIDDLE_LINE"/>
    <property data="companyName" naming-strategy="UNDERLINE"/>
  </object>
</template>
```

```json
{
  "myCompany": {
    "company-id": 1,
    "company_name": "XX科技公司"
  }
}
```

```xml

<xml>
  <myCompany>
    <company-id>1</company-id>
    <company_name>XX科技公司</company_name>
  </myCompany>
</xml>
```

### 4.4. property扩展

#### 4.4.1 使用property-date格式化日期时间

该标签可以格式化时间

```xml

<property data="birthday"/>
```

替换为

```xml

<property-date data="birthday" pattern="yyyy年MM月dd日"/>
```

运行结果：

```json
{
  "birthday": "1995年01月01日"
}
```

#### 4.4.2 使用property-unixtimestamp输出时间戳

该标签可以使日期时间类型转化成时间戳形式输出。

```java
dataModel.putData("datetime", LocalDateTime.of(2016, 1, 1, 0, 0, 0));
```

```json
{"datetime" : 1451577600}
```

#### 4.4.3 使用property-boolean转换

该标签可以把非0数字转换成true，0转换成false

```java
DataModel dataModel = new DataModel();
dataModel.putData("number1", 1);
dataModel.putData("number2", 0);
```

```json
{"number1" : true, "number2" : false}
```

#### 4.4.4 使用property-enum映射

该标签可以把值映射成另一个固定值，该标签不仅可以处理枚举类型，字符串或者基本类型都可以处理

```xml

<property-enum data="gender">
  <enum value="MALE" text="男"/>
    <enum value="FEMALE" text="女"/>
</property-enum>
```

```java
DataModel dataModel=DataModel.singleton("gender",Staff.Gender.MALE);
```

```json
{
  "gender": "男"
}
```

### 4.5. null-hidden和null-empty

所有的内容节点可以用`null-hidden`来隐藏data指代对象值为null的节点

`<array>`标签可以用`null-empty`属性把data指代对象值为null的节点设为空数组

```xml

<template id="test-array-null">
  <array data="array" alias="null-array"/>
  <array data="array" alias="empty-array" null-empty="true"/>
  <array data="array" alias="null-hidden-array" null-hidden="true"/>
</template>
```

```java
DataModel dataModel=DataModel.singleton("array",null);
```

```json
{
  "null-array": null,
  "empty-array": []
}
```

```xml

<xml>
  <null-array/>
  <empty-array/>
</xml>
```

### 4.6. object array template

```xml

<template id="company-info">
  <object data="company">
    <property data="companyId"/>
    <property data="companyName"/>
  </object>
</template>
```

利用`<object>`标签构造一个对象结构

```java
DataModel dataModel=DataModel.singleton("company",DemoDataMock.mockCompanies()[0]);
```

```json
{
  "company": {
    "company_id": 1,
    "company_name": "XX科技公司"
  }
}
```

```xml

<xml>
  <company>
    <company-id>1</company-id>
    <company-name>XX科技公司</company-name>
  </company>
</xml>
```

利用`array` 标签构造一个数组结构：

```xml

<template id="company-info">
  <array data="companies" xml-item="company">
    <property data="companyId"/>
    <property data="companyName"/>
  </array>
</template>
```

```java
DataModel dataModel=DataModel.singleton("company",DemoDataMock.mockCompanies());
```

```json
{
  "companies": [
    {
      "company_id": 1,
      "company_name": "XX科技公司"
    },
    {
      "company_id": 2,
      "company_name": "YY网络公司"
    }
  ]
}
```

```xml

<xml>
  <companies>
    <company>
      <company-id>1</company-id>
      <company-name>XX科技公司</company-name>
    </company>
    <company>
      <company-id>2</company-id>
      <company-name>YY网络公司</company-name>
    </company>
  </companies>
</xml>
```

或者直接把data设定在`<template>` 标签上，Kite框架会自动识别data对应的数据是否是数组或List。

```xml

<template id="company-info" data="company" xml-item="company" xml-root="companies">
  <property data="companyId"/>
  <property data="companyName"/>
</template>
```

当data指代的对象是Array或Collection时生成数组结构

```json
[
  {
    "company_id": 1,
    "company_name": "XX科技公司"
  },
  {
    "company_id": 2,
    "company_name": "YY网络公司"
  }
]
```

```xml

<companies>
  <company>
    <company-id>1</company-id>
    <company-name>XX科技公司</company-name>
  </company>
  <company>
    <company-id>2</company-id>
    <company-name>YY网络公司</company-name>
  </company>
</companies>
```

当data指代的对象是单个对象时生成对象结构

```json
{
  "company_id": 1,
  "company_name": "XX科技公司"
}
```

```xml

<companies>
  <company-id>1</company-id>
  <company-name>XX科技公司</company-name>
</companies>
```

### **4.7. prototype使用原型实体**

使用`<prototype>` 标签可以使用原生的框架转换成json，以下展示使用Jackson作为实现框架

```xml

<template id="staff-info">
  <prototype data="staff"/>
</template>
```

以下加入Jackson的注解达到序列化效果

```java

@Data
@RequiredArgsConstructor
public class Staff {

  // 员工ID
  private final Integer staffId;
  // 部门ID  忽略该属性
  @JsonIgnore
  private final Integer departmentId;
  // 员工姓名  改写属性名
  @JsonProperty("name")
  private final String staffName;
  // 性别
  private final Gender gender;
  // 生日
  private final LocalDate birthday;

  public enum Gender {
    MALE, FEMALE, UNKNOWN
  }
}
```

```json
{
  "staff": {
    "staff_id": 111,
    "gender": "MALE",
    "birthday": "1988-02-03",
    "name": "小张"
  }
}
```

更多注解使用请参考jackson-annotations文档。

### 4.8. raw原文本

使用`<raw>`来添加原文本对象，内部使用具体实现框架来反序列化的。

```xml

<template id="test-raw">
  <raw data="companyJson" alias="company"/>
</template>
```

```json
String rawJson = "{\"company_id\":1,\"company_name\":\"XX科技公司\"}";
DataModel dataModel = DataModel.singleton("companyJson", rawJson);
```

```json
{
  "company": {
    "company_id": 1,
    "company_name": "XX科技公司"
  }
}
```

## 5. 日志

Kite框架使用slf4j-api日志接口，提供内部日志打印功能。可以使用log4j或者logback打印日志。
以下示例使用logback

```xml
<configuration scan="true" scanPeriod="60 seconds" debug="false">
  <contextName>kite-log</contextName>
  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <pattern>%d{HH:mm:ss.SSS} %-5level - %msg%n</pattern>
    </encoder>
  </appender>
  <logger name="com.github.developframework.kite" level="INFO" additivity="false">
    <appender-ref ref="STDOUT" />
  </logger>
</configuration>
```

项目启动日志：

```
09:29:07.753 【Kite】loaded the configuration source "/kite/kite-demo.xml".
```

