# Kite

## 0. 文档传送

- [kite-spring](https://github.com/developframework/kite/blob/master/README-spring.md)
- 
  [kite-spring-boot-starter](https://github.com/developframework/kite/blob/master/README-boot.md)

## <a name="chapter1">**1. 简介**</a>

Kite框架构建于jackson和dom4j框架之上，实现通过XML文件配置来自定义json和xml格式，大大提升了java生成json和xml字符串的自由性，让开发模块化更加便捷快速。

### **1.1. 运行环境**

JDK8及以上

### **1.2. 使用方式**

maven

```xml
<dependency>
	<groupId>com.github.developframework</groupId>
	<artifactId>kite-core</artifactId>
	<version>${version.kite}</version>
</dependency>
```

## <a name="chapter2">**2. HelloWorld**</a>

一个最简单的kite使用示例：

```java
KiteFactory kiteFactory = new KiteFactory("/kite/kite-demo.xml");
DataModel dataModel = new HashDataModel();
dataModel.putData("sayHello", "Hello Kite!");
JsonProducer jsonProducer = kiteFactory.getJsonProducer();
// 生成json
String json = jsonProducer.produce(dataModel, "kite-demo", "first-view");
System.out.println(json);
// 生成xml
XmlProducer xmlProducer = kiteFactory.getXmlProducer();
String xml = xmlProducer.produce(dataModel, "kite-demo", "first-view");
System.out.println(xml);
```

你需要一份Kite XML配置，位置在上述声明的/kite/kite-demo.xml：

```xml
<kite-configuration xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                        xmlns="https://github.com/developframework/kite/schema"
                        xsi:schemaLocation="
	https://github.com/developframework/kite/schema kite-configuration.xsd">

    <template-package namespace="kite-demo">

        <template id="first-view" xml-root="xml">
            <property data="sayHello" />
        </template>

    </template-package>

</kite-configuration>
```

运行结果：

```json
{"sayHello":"Hello Kite!"}
```

```xml
<xml><say-hello>Hello Kite!</say-hello></xml>
```

## <a name="chapter3">**3. 概览**</a>

### **3.1. java概览**

#### **3.1.1. DataModel**

`com.github.developframework.kite.data.DataModel`接口是Kite框架的数据模型。用于装载需要在json视图中渲染的数据或函数接口实现，数据由键值对构成。接口提供存入和取出数据的方法。
目前实现类仅有`com.github.developframework.kite.data.HashDataModel`
存取数据范例：

```java
DataModel dataModel = new HashDataModel();
dataModel.putData("sayHello", "Hello Kite!");
Optional<Object> value = dataModel.getData("sayHello");
value.ifPresent(System.out::println);
```

#### **3.1.2. Expression**

`com.github.developframework.expression.Expression`类是kite框架从DataModel中提取数据的表达式。不论dataModel存的是java实体类还是Map对象都可以使用表达式取值。
范例：

- `student` 你可以从DataModel对象内取得名为student的对象
- `#student` 你可以从DataModel对象内 **强制从根路径** 取得名为student的对象
- `student.name` 你可以从DataModel对象内取得名为student的对象的name属性值
- `students[0]` 你可以从DataModel对象内取得名为students的数组内的第1个元素
- `student[0].name` 你可以从DataModel对象内取得名为students的数组内的第1个元素的name属性值

`Expression` 的详细使用请查看独立项目[expression](https://github.com/developframework/expression)

#### **3.1.3. KiteFactory**

`com.github.developframework.kite.core.KiteFactory`类是Kite框架的构建工厂。使用Kite框架的第一步就是建立该对象。
建立该对象需要提供配置文件路径的字符串，多份配置文件可以采用字符串数组。

```java
final String[] configs = {"config1.xml", "config2.xml"};
KiteFactory kiteFactory = new KiteFactory(configs);
```

#### **3.1.4. KiteConfiguration**

`com.github.developframework.kite.core.KiteConfiguration`类为Kite框架的总配置文件，可以从KiteFactory中得到该对象。

```java
KiteConfiguration kiteConfiguration = kiteFactory.getKiteConfiguration();
```

#### **3.1.5. Producer**

#### **3.1.5.1. JsonProducer**

`com.github.developframework.kite.core.JsonProducer`接口是json字符串建造类，执行一次生成json字符串的操作需要构建该对象。JsonProducer由KiteFactory生成。
该对象提供三个构建json字符串的方法：

```java
String produce(DataModel dataModel, String namespace, String templateId, boolean isPretty);
```

返回json字符串

- isPretty=true时可以美化json

```java
String produce(DataModel dataModel, String namespace, String templateId);
```

返回json字符串，不美化

```java
void outputJson(JsonGenerator jsonGenerator, DataModel dataModel, String namespace, String templateId, boolean isPretty);
```

将json输出到ObjectMapper的JsonGenerator对象

#### **3.1.5.1. XmlProducer**

`com.github.developframework.kite.core.XmlProducer`接口是xml字符串建造类，执行一次生成xml字符串的操作需要构建该对象。XmlProducer由KiteFactory生成。
该对象提供三个构建xml字符串的方法：

```java
String produce(DataModel dataModel, String namespace, String templateId, boolean isPretty);
```

返回json字符串

- isPretty=true时可以美化xml

```java
String produce(DataModel dataModel, String namespace, String templateId);
```

返回json字符串，不美化

```java
void outputXml(Writer writer, DataModel dataModel, String namespace, String templateId, boolean isPretty);
```

将xml输出到Writer

#### **3.1.6. Template**

`com.github.developframework.kite.core.element.Template`类，一个Template类的实例代表一种视图模板。它由`namespace`和`id`唯一确定。可以通过以下方法得到Template实例：

```java
Template template = kiteConfiguration.extractTemplate("namespace", "id");
```

#### **3.1.7. TemplatePackage**

`com.github.developframework.kite.core.element.TemplatePackage`类，一个TemplatePackage实例是一个命名空间，可以装载若干个Template实例。推荐将Template按功能有序存放于TemplatePackage。通过以下方法得TemplatePackage对象：

```java
TemplatePackage templatePackage = kiteConfiguration.getTemplatePackageByNamespace("namespace");
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
- `<prototype>`
- `<xml-attribute>`

功能型标签

- `<include>`
- `<link>`
- `<relevance>`
- `<object-virtual>`
- `<property-ignore>`
- `<extend-port>`
- `<if>`、 `<else>`

拓展型标签

- `<property-date>`

- `<property-unixtimestamp>`

- `<property-boolean>`

##### **3.2.2.1. 基本型标签**

###### a) template

  当你需要声明一个json格式模板时，你将会使用到`<template>`标签。

  ```xml
  <template id="" data="" for-class=""></template>
  ```

| 属性         | 功能                                                         | 是否必须 |
| ------------ | ------------------------------------------------------------ | -------- |
| id           | 声明模板编号，在命名空间中唯一                               | 是       |
| data         | 取值表达式                                                   | 否       |
| for-class    | 声明data表达式指向的对象类型                                 | 否       |
| extend       | 声明继承的kite和端口，格式为namespace.id:port（namespace不填时默认为当前namespace） | 否       |
| map-function | 仅当data指代的数据为数组或List时有效。MapFunction的实现类全名或Expression表达式。详见[5.1.2节](#chapter512) | 否       |
| xml-root     | 生成xml时的根节点名称                                        | 否       |
| xml-item     | 生成xml时，子节点数组项的节点名称                            | 否       |

###### b) object

当你需要在json中构建一个对象结构时，你将会使用到`<object>`标签。详见[4.1.节](#chapter41)

```xml
<object data="" alias="" for-class="" null-hidden="true"></object>
```

| 属性          | 功能                                | 是否必须 |
| ----------- | --------------------------------- | ---- |
| data        | 取值表达式                             | 是    |
| alias       | 别名，你可以重新定义显示名                     | 否    |
| for-class   | 声明data表达式指向的对象类型                  | 否    |
| null-hidden | true时表示表达式取的值为null时隐藏该节点，默认为false | 否    |

###### c) array

当你需要在json中构建一个数组结构时，你将会使用到`<array>`标签。详见[4.6.节](#chapter46)

```xml
<array data="" alias="" for-class="" null-hidden="true"></array>
```

| 属性         | 功能                                                         | 是否必须 |
| ------------ | ------------------------------------------------------------ | -------- |
| data         | 取值表达式                                                   | 是       |
| alias        | 别名，你可以重新定义显示名                                   | 否       |
| for-class    | 声明data表达式指向的对象类型                                 | 否       |
| null-hidden  | true时表示表达式取的值为null时隐藏该节点，默认为false        | 否       |
| map-function | MapFunction的实现类全名或Expression表达式。详见[5.1.2节](#chapter512) | 否       |
| xml-item     | 生成xml时，子节点数组项的节点名称                            | 否       |

`<array>`标签可以没有子标签，这时表示数组为基本类型数组。

###### d) property

当你需要在json中构建一个普通属性结构时， 你将会使用到`<property>`标签。

```xml
<property data="" alias="" converter="" null-hidden="true"/>
```

| 属性          | 功能                                       | 是否必须 |
| ----------- | ---------------------------------------- | ---- |
| data        | 取值表达式                                    | 是    |
| alias       | 别名，你可以重新定义显示名                            | 否    |
| converter   | 类型转换器全限定类名或expression表达式。详见[5.1.1节](#chapter511) | 否    |
| null-hidden | true时表示表达式取的值为null时隐藏该节点，默认为false        | 否    |

###### e) prototype

使用Jackson原型实体构建结构， 你将会使用到`<prototype>`标签。详见[4.7.节](#chapter47)

```xml
<prototype data="" alias="" converter="" null-hidden="true"/>
```

| 属性          | 功能                                       | 是否必须 |
| ----------- | ---------------------------------------- | ---- |
| data        | 取值表达式                                    | 是    |
| alias       | 别名，你可以重新定义显示名                            | 否    |
| converter   | 类型转换器全限定类名或expression表达式。详见[5.1.1节](#chapter511) | 否    |
| null-hidden | true时表示表达式取的值为null时隐藏该节点，默认为false        | 否    |

###### f) xml-attribute

在输出xml时，提供配置xml节点的属性。

| 属性        | 功能                                                  | 是否必须 |
| ----------- | ----------------------------------------------------- | -------- |
| data        | 取值表达式                                            | 是       |
| alias       | 别名，你可以重新定义显示名                            | 否       |
| null-hidden | true时表示表达式取的值为null时隐藏该节点，默认为false | 否       |

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
<link data="" alias="" for-class="" null-hidden="true"></link>
```

| 属性          | 功能                                  | 是否必须 |
| ----------- | ----------------------------------- | ---- |
| data        | 取值表达式，**data必须指代一个List或array类型的对象** | 是    |
| alias       | 别名，你可以重新定义显示名                       | 否    |
| for-class   | 声明data表达式指向的对象类型                    | 否    |
| null-hidden | true时表示表达式取的值为null时隐藏该节点，默认为false   | 否    |

###### c) relevance
该标签用于实现一对多关联功能。详见[5.4.2.节](#chapter542)。

```xml
<relevance data="" alias="" rel-function="" null-hidden="true"></relevance>
```

| 属性           | 功能                                       | 是否必须 |
| ------------ | ---------------------------------------- | ---- |
| data         | 取值表达式，**data必须指代一个List或array类型的对象**      | 是    |
| alias        | 别名，你可以重新定义显示名                            | 否    |
| rel-function | 关联判定器全限定类名或expression表达式                 | 是    |
| null-hidden  | true时表示表达式取的值为null时隐藏该节点，默认为false        | 否    |
| map-function | MapFunction的实现类全名或Expression表达式。详见[5.1.2节](#chapter512) | 否    |

###### d) object-virtual

该标签用于虚拟对象结构。详见[5.2.节](#chapter52)

```xml
<object-virtual alias=""></object-virtual>
```

| 属性    | 功能   | 是否必须 |
| ----- | ---- | ---- |
| alias | 别名   | 是    |

###### e) property-ignore

忽略属性，需结合`for-class`属性使用。详见[4.5.节](#chapter45)

```xml
<property-ignore name=""/>
```

| 属性   | 功能        | 是否必须 |
| ---- | --------- | ---- |
| name | 类中忽略的属性名称 | 是    |

###### f) extend-port

此标签位置作为子`<template>`的接入位置。详见[5.3.2节](#chapter532)

```xml
<extend-port port-name=""/>
```

| 属性        | 功能   | 是否必须 |
| --------- | ---- | ---- |
| port-name | 端口名称 | 是    |

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


##### **3.2.2.3. 拓展型标签**

###### a) property-date

该标签拓展于`<property>`，针对时间日期类型，使用`pattern`属性来格式化日期时间。详见[4.3.1.节](#chapter431)

| 拓展属性    | 功能                                  | 是否必须 |
| ------- | ----------------------------------- | ---- |
| pattern | 格式化模板字符串，不填默认为"yyyy-MM-dd HH:mm:ss" | 否    |

支持的时间日期类型：

- java.util.Date
- java.util.Calendar
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

## <a name="chapter4">**4. 基本使用**</a>

模型声明（以下各小节示例代码均使用这些模型实体类）：

```java
// 学生类 Student.java
@Data
public class Student {
    // 编号
    private int id;
    // 学生名称
    private String name;
    // 班级ID
    private int classId;
    // 出生日期
    private Date birthday;

    public Student(int id, String name, int classId, String birthday) {
        this.id = id;
        this.name = name;
        this.classId = classId;
        if(birthday != null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                this.birthday = dateFormat.parse(birthday);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
```

```java
// 账户类 Account.java   一个学生对应有一个账户
@Data
@AllArgsConstructor
public class Account {
    private String username;
    private String password;
}
```

```java
// 班级类 SchoolClass.java   一个班级对应多个学生
@Data
@AllArgsConstructor
public class SchoolClass {

    private int id;

    private String className;
}
```

### <a name="chapter41">**4.1. 简单输出模型对象**</a>

#### **4.1.1. json**
```xml
<!-- /kite/kite-student.xml --> 
<!-- 忽略kite-configuration -->
<template-package namespace="kite-student">
  <template id="student-detail" data="student">
    <property data="id"/>
    <property data="name"/>
    <property data="classId"/>
    <property data="birthday"/>
  </template>
</template-package>
```

```java
// Application.java    main()
KiteFactory factory = new KiteFactory("/kite/kite-student.xml");
JsonProducer jsonProducer = factory.getJsonProducer();
DataModel dataModel = new HashDataModel();
Student peter = new Student(1, "Peter", 1, "1995-01-01");
dataModel.putData("student", peter);
String json = jsonProducer.produce(dataModel, "kite-student", "student-detail");
System.out.println(json);
```

执行结果：

```json
{"id":1,"name":"Peter","classId":1,"birthday":"Sun Jan 01 00:00:00 CST 1995"}
```

#### **4.1.2. xml**

```xml
<template-package namespace="kite-student">
  <template id="student-detail" data="student" xml-root="student">
    <xml-attribute data="id"/>
    <property data="name"/>
    <property data="classId"/>
    <property data="birthday"/>
  </template>
</template-package>
```

+ `xml-root` 申明了xml的根节点名称
+ `<xml-attribute>` 设定了节点的属性

执行结果：

```xml
<student id="1">
  <name>Peter</name>
  <class-id>1</class-id>
  <birthday>Sun Jan 01 00:00:00 CST 1995</birthday>
</student>
```



### 4.2. 使用alias修改显示名称

```xml
<!-- /kite/kite-student.xml --> 
<!-- 忽略kite-configuration -->
<template-package namespace="kite-student">
  <template id="student-detail" data="student">
    <property data="id" alias="student_id"/>
    <property data="name" alias="student_name"/>
    <property data="classId" alias="student_classId"/>
    <property data="birthday" alias="student_birthday"/>
  </template>
</template-package>
```

```json
{"student_id":1,"student_name":"Peter","student_classId":1,"student_birthday":"Sun Jan 01 00:00:00 CST 1995"}
```

### **4.3. property扩展**

#### <a name="chapter431">**4.3.1 使用property-date格式化日期时间**</a>

该标签可以格式化时间。
把4.1.节代码

```xml
<property data="birthday"/>
```

替换为

```xml
<property-date data="birthday" pattern="yyyy-MM-dd"/>
```

运行结果：

```json
{"id":1,"name":"Peter","classId":1,"birthday":"1995-01-01"}
```

#### <a name="chapter432">**4.3.2 使用property-unixtimestamp输出时间戳**</a>

该标签可以使日期时间类型转化成时间戳形式输出。

```java
dataModel.putData("datetime", LocalDateTime.of(2016, 1, 1, 0, 0, 0));
```

```json
{"datetime" : 1451577600}
```

#### <a name="chapter433">**4.3.3 使用property-boolean转换**</a>

该标签可以把非0数字转换成true，0转换成false

```java
DataModel dataModel = new HashDataModel();
dataModel.putData("number1", 1);
dataModel.putData("number2", 0);
```

```json
{"number1" : true, "number2" : false}
```

### **4.4. 使用null-hidden隐藏值为null的属性**

把4.1.节代码

```xml
<property data="birthday"/>
```

替换为

```xml
<property data="birthday" null-hidden="true"/>
```

student实例传入null的birthday值

```java
Student student = new Student(1, "Peter", 1, null);
```

运行结果：

```json
{"id":1,"name":"Peter","classId":1}
```

### <a name="chapter45">**4.5. 使用for-class输出模型对象Json**</a>

```xml
<template id="student-detail" data="student" for-class="test.Student" />
```

运行结果和4.1.节相同。
使用`property-ignore`忽略不需要的属性：

```xml
<template id="student-detail" data="student" for-class="test.Student">
  <property-ignore name="birthday" />
</template>
```

运行结果：

```json
{"id":1,"name":"Peter","classId":1}
```

### <a name="chapter46">**4.6. 简单输出数组模型**</a>

#### **4.6.1. json**

利用`array` 标签构造一个数组结构：

```xml
<template id="student-list">
  <array data="students">
    <property data="name"/>
    <property data="classId"/>
    <property-date data="birthday" pattern="yyyy-MM-dd"/>
  </array>
</template>
```

```java
Student peter = new Student(1, "Peter", 1, "1995-01-01");
Student john = new Student(2, "John", 1, "1996-5-20");
Student[] students = {peter, john};
dataModel.putData("students", students);
// isPretty参数设为true，开启json美化
String json = jsonProducer.produce(dataModel, "kite-student", "student-list", true);
```

```json
{
  "students" : [ {
    "id" : 1,
    "name" : "Peter",
    "classId" : 1,
    "birthday" : "1995-01-01"
  }, {
    "id" : 2,
    "name" : "John",
    "classId" : 1,
    "birthday" : "1996-05-20"
  } ]
}
```

或者直接把data设定在`<template>` 标签上，Kite框架会自动识别data对应的数据是否是数组或List。

```xml
<template id="student-list" data="students">
  <property data="id"/>
  <property data="name"/>
  <property data="classId"/>
  <property-date data="birthday" pattern="yyyy-MM-dd"/>
</template>
```

```json
[ {
  "id" : 1,
  "name" : "Peter",
  "classId" : 1,
  "birthday" : "1995-01-01"
}, {
  "id" : 2,
  "name" : "John",
  "classId" : 1,
  "birthday" : "1996-05-20"
} ]
```

#### **4.6.2. xml**

```xml
<template id="student-list" data="students" xml-root="xml" xml-item="student">
    <xml-attribute data="id"/>
    <property data="name"/>
    <property data="classId"/>
    <property-date data="birthday" pattern="yyyy-MM-dd"/>
</template>
```

+ `xml-item` 设定数组每项元素的节点名称

```xml
<xml>
  <students>
    <student id="1">
      <name>Peter</name>
      <class-id>1</class-id>
      <birthday>1995-01-01</birthday>
    </student>
    <student id="2">
      <name>John</name>
      <class-id>1</class-id>
      <birthday>1996-05-20</birthday>
    </student>
  </students>
</xml>
```

### <a name="chapter47">**4.7. 使用Jackson原型实体**</a>

使用`<prototype>` 标签可以使用原生的Jackson方式转化实体成json。

```xml
<template id="student-detail">
  <prototype data="student" />
</template>
```

```java
@Data
public class Student {
    // 编号
    private int id;
    // 学生名称
    @JsonProperty("student_name")	// 通过注解@JsonProperty对属性重命名
    private String name;
    // 班级ID
    @JsonIgnore		// 通过注解@JsonIgnore忽略该属性
    private int classId;
    // 出生日期
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")//通过@JsonFormat格式化日期
    private Date birthday;
    //构造方法略
}
```

```json
{
  "student" : {
    "id" : 1,
    "birthday" : "1995-01-01",
    "student_name" : "Peter"
  }
}
```

更多注解使用请参考jackson-annotations文档。

## <a name="chapter5">**5. 高级功能**</a>

### **5.1.处理器**

#### <a name="chapter511">**5.1.1. `<property>` 的转换器 `converter`**</a>

`com.github.developframework.kite.core.dynamic.PropertyConverter`
接口可以对表达式选取的属性值进行自定义转换。

```java
@FunctionalInterface
public interface PropertyConverter<TARGET> {

    /**
     * 转化方法
     * @param source 源对象
     * @return 目标对象
     */
    TARGET convert(Object source);
}
```

- 泛型TARGET为转化后的类型。

例如将peter的name处理后输出：

```xml
<template id="student-detail" data="student">
  <property data="id" />
  <property data="name" converter="nameConverter"/>
  <property data="classId"/>
  <property-date data="birthday" pattern="yyyy-MM-dd"/>
</template>
```

```java
dataModel.putData("student", peter);
// 这里采用JDK8 lambda写法
dataModel.putData("nameConverter", (PropertyConverter<String>) source -> "My name is " + source);
```

```json
{
  "id" : 1,
  "name" : "My name is Peter",
  "classId" : 1,
  "birthday" : "1995-01-01"
}
```

`<property>` 系列标签的`converter` 属性可以填写Expression表达式，还可以填写`PropertyConverter` 的接口实现类全类名。

#### <a name="chapter512">**5.1.2. `<array>` 的元素映射器 `map-function`**</a>

在`<array>`节点属性`map-function`用于指定对每个元素的映射函数。`map-function`的值可以为`Expression`表达式或一个实现`com.github.developframework.kite.core.dynamic.MapFunction`接口的完全类名。其中使用表达式方式，其获取的实例必须是`com.github.developframework.kite.core.dynamic.MapFunction`的实现类。具体示例：实现功能：对数组的每个元素进行映射处理，处理结果作为生成Json的数据。以下示例在字符串数组的每项元素以`value_{item}_{i}` 形式输出。

```java
String[] strArray = new String[]{"aaa", "bbb", "ccc"};
dataModel.putData("strArray", strArray);
dataModel.putData("func", ((MapFunction<String, String>) (item, i) -> "value_" + item + "_" + i));
```

```xml
<template id="array-map-function-demo" >
  <array data="strArray" map-function="func"></array>
</template>
```

```json
{"str_array":["value_aaa_0","value_bbb_1","value_ccc_2"]}
```
**注意**

使用`map-function`会导致`<array>`标签的所有子节点失效，因为映射结果将会直接作为json数据。

如果你设置了子节点将会出现以下警告：

```
The child element invalid, because you use "map-function" attribute.
```
### <a name="chapter52">**5.2. 虚拟结构**</a>

使用`<object-virtual>`可以虚拟一个对象结构。
利用仅有的属性值，构造一个对象结构：

```xml
<template id="student-detail">
  <object-virtual alias="student">
    <property data="id"/>
    <property data="name"/>
    <property data="classId"/>
    <property-date data="birthday" pattern="yyyy-MM-dd"/>
  </object-virtual>
</template>
```

```java
dataModel.putData("id", 1);
dataModel.putData("name", "Peter");
dataModel.putData("classId", 1);
dataModel.putData("birthday", "1995-01-01");
```

```json
{
  "student" : {
    "id" : 1,
    "name" : "Peter",
    "classId" : 1,
    "birthday" : "1995-01-01"
  }
}
```

### **5.3. 模块化**

#### <a name="chapter531">**5.3.1 引用**</a>

使用`<include>`标签引用其它`<template>`模板，从而可实现模块化设计，避免重复定义视图模板。

```xml
<template-package namespace="kite-student">
  <template id="student-list" data="students">
    <include id="student-detail" />
  </template>

  <template id="student-detail">
    <property data="id"/>
    <property data="name"/>
    <property data="classId"/>
    <property-date data="birthday" pattern="yyyy-MM-dd"/>
  </template>
</template-package>
```

#### <a name="chapter532">**5.3.2 继承**</a>

Kite框架的继承的概念，在`<template>`标签可以添加属性`extend`指定继承的template和继承的端口。继承的概念可以理解为反向include，调用子template视图，会优先从父template开始构造结构，当遇到匹配端口名的`<extend-port>`标签时才会构造子template视图。

**注意：**

假如单独调用了有`<extend-port>`标签的父template视图或者端口没有与之对应的子template实现，则`<extend-port>`标签被忽略。

```xml
<template-package namespace="kite-student">
  <!-- 一个父视图模板  -->
  <template id="student-parent">
    <object-virtual alias="other">
      <property data="otherData" />
    </object-virtual>
    <!-- 子视图模板的内容将会插入到这个端口位置上 -->
    <extend-port port-name="my-port" />
  </template>

  <!-- 子视图模板  -->
  <template id="student-detail" extend="student-parent:my-port">
    <!-- 本模板内容将会插入到父视图模板的my-port端口位置上 -->
    <object data="student">
      <property data="id"/>
      <property data="name"/>
      <property data="classId"/>
      <property-date data="birthday" pattern="yyyy-MM-dd"/>
    </object>
  </template>
</template-package>
```

```java
Student peter = new Student(1, "Peter", 1, "1995-01-01");
dataModel.putData("student", peter);
dataModel.putData("otherData", "I'm other data.");
// 这里调用的子视图模板
String json = jsonProducer.produce(dataModel, "kite-student", "student-detail", true);
```

```json
{
  "other" : {
    "otherData" : "I'm other data."
  },
  "student" : {
    "id" : 1,
    "name" : "Peter",
    "classId" : 1,
    "birthday" : "1995-01-01"
  }
}
```

`extend` 属性的写法为  namespace.templateId:portName  其中namespace可以省略，默认为当前命名空间下。

### **5.4. 链接与关联**

#### <a name="chapter541">**5.4.1. 一对一数组链接**</a>

使用`<link>` 标签可以在数组间一对一链接对象。**该标签仅能在`<array>`下使用。**当`<link>` 的data属性所指的数组和父`<array>`数组个数不相同时将会抛出`LinkSizeNotEqualException`。
例子：
假如每个学生实例都有一个账户实例，并且又都一对一对应了一个成绩值。

```xml
<template-package namespace="kite-student">

  <template id="student-list" data="students">
    <property data="id"/>
    <property data="name"/>
    <property data="classId"/>
    <property-date data="birthday" pattern="yyyy-MM-dd"/>
    <!-- 一对一对应accounts数组每项 -->
    <link data="#accounts" alias="account">
      <!-- 引用另一个命名空间的模板 -->
      <include id="account-detail" namespace="kite-account"/>
    </link>
    <!-- 一对一对应scores数组每项 -->
    <link data="#scores" alias="score"/>
  </template>
</template-package>

<template-package namespace="kite-account">

  <template id="account-detail">
    <property data="username"/>
    <property data="password"/>
  </template>
</template-package>
```

```java
 Account peterAccount = new Account("peter's username", "peter's password");
Account johnAccount = new Account("john's username", "john's password");

Student[] students = {peter, john};
Account[] accounts = {peterAccount, johnAccount};
Integer[] scores = {95, 98};

dataModel.putData("students", students);
dataModel.putData("accounts", accounts);
dataModel.putData("scores", scores);
```

```json
[ {
  "id" : 1,
  "name" : "Peter",
  "classId" : 1,
  "birthday" : "1995-01-01",
  "account" : {
    "username" : "peter's username",
    "password" : "peter's password"
  },
  "score" : 95
}, {
  "id" : 2,
  "name" : "John",
  "classId" : 1,
  "birthday" : "1996-05-20",
  "account" : {
    "username" : "john's username",
    "password" : "john's password"
  },
  "score" : 98
} ]
```

#### <a name="chapter542">**5.4.2. 根据条件关联**</a>

假如A数组有2个元素，B数组有3个元素。其中A[0] 需要关联B[0]和B[1]， A[1] 需要关联B[2]。这种需求下就可以使用`<relevance>`标签，实现在数组间一对多关联。属性`rel-function` 指定判定条件，需要实现一个接口：

```java
public interface RelFunction<S, T> {

    boolean relevant(S sourceItem, int sourceIndex, T target, int targetIndex);
}
```

其中泛型S指代A数组类型，T指代B数组类型。

sourceItem是迭代了A数组的每一项，sourceIndex是它的索引。每一项的A元素都会去迭代B数组的每一项target，targetIndex是索引，`relevant` 方法返回true表示需要关联。

具体看示例，有如下数据结构关系：

```java
// 1班
SchoolClass schoolClass1 = new SchoolClass(1, "1班");
// 2班
SchoolClass schoolClass2 = new SchoolClass(2, "2班");
// 1班的学生
Student peter = new Student(1, "Peter", 1, "1995-01-01");
Student john = new Student(2, "John", 1, "1996-5-20");
// 2班的学生
Student bill = new Student(3, "Bill", 2, "1993-4-16");

Student[] students = {peter, john, bill};
SchoolClass[] schoolClasses = {schoolClass1, schoolClass2};
dataModel.putData("students", students);
dataModel.putData("schoolClasses", schoolClasses);
```



```xml
<template-package namespace="kite-student">
  <template id="student-detail">
    <property data="name"/>
    <property-date data="birthday" pattern="yyyy-MM-dd"/>
  </template>
</template-package>

<template-package namespace="kite-class">
  <template id="class-list" data="schoolClasses">
    <property data="id" />
    <property data="className" />
    <!-- 关联学生列表 -->
    <relevance data="#students" rel-function="rel-function">
      <include id="student-detail" namespace="kite-student" />
    </relevance>
  </template>
</template-package>
```

实现`RelFunction`

```java
dataModel.putData("rel-function", (RelFunction<SchoolClass, Student>) (sourceItem, sourceIndex, target, targetIndex) -> sourceItem.getId() == target.getClassId());
```

判定条件为当SchoolClass（sourceItem）的id与Student（target）的classId相等时，允许关联。

```json
[ {
  "id" : 1,
  "className" : "1班",
  "students" : [ {
    "name" : "Peter",
    "birthday" : "1995-01-01"
  }, {
    "name" : "John",
    "birthday" : "1996-05-20"
  } ]
}, {
  "id" : 2,
  "className" : "2班",
  "students" : [ {
    "name" : "Bill",
    "birthday" : "1993-04-16"
  } ]
} ]
```

### 5.5. 分支结构

#### <a name="chapter551">**5.5.1. `<if>` `<else>`**</a>

可以使用`<if>` `<else>` 标签进行模块内容的取舍。`<else>` 标签可以不写，但必须紧跟`<if>` 后出现。

`<if>` 标签的`condition` 属性内容为接口`com.github.developframework.kite.core.dynamic.Condition` 的实现类或直接使用Boolean类型。

```java
@FunctionalInterface
public interface Condition {

    /**
     * 判断条件
     * @param dataModel 数据模型
     * @param expression 当前位置的表达式
     * @return 判断结果
     */
    boolean verify(DataModel dataModel, Expression expression);
}
```

最简范例：

```xml
<template id="first-view">
  <if condition="myCondition">
    <property data="sayHello"/>
  </if>
  <else>
    <property data="sayBye"/>
  </else>
</template>
```

```java
dataModel.putData("sayHello", "Hello");
dataModel.putData("sayBye", "Bye");
dataModel.putData("myCondition", (Condition) (dm, expression) -> true);
// 或直接使用boolean
// dataModel.putData("myCondition", true);
```

```json
{"sayHello" : "Hello"}
```

当myCondition 接口返回false时

```json
{"sayBye" : "Bye"}
```

### 5.6 命名策略 

json节点和xml节点名称的命名策略扩展，继承接口`com.github.developframework.kite.core.strategy.KitePropertyNamingStrategy`

```java
public interface KitePropertyNamingStrategy {

    /**
    	属性展示名称
    */
    String propertyShowName(KiteConfiguration configuration, String expressionString);
}
```

通过接口方法得到生成的名称。

Kite内置接口实现：

+ JacksonKitePropertyNamingStrategy  用Jackson的策略命名
+ DefaultXmlKitePropertyNamingStrategy  默认的xml命名策略，AbCd => ab-cd
+ DoNothingKitePropertyNamingStrategy  什么都不做，使用原名


## <a name="chapter6">**6. 日志**</a>

Kite框架使用slf4j-api日志接口，提供内部日志打印功能。可以使用log4j或者logback打印日志。
以下示例使用logback

```xml
<configuration scan="true" scanPeriod="60 seconds" debug="false">
	<contextName>kite-log</contextName>
	<appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
		<encoder>
			<pattern>%d{HH:mm:ss.SSS} %-5level - %msg%n
			</pattern>
		</encoder>
	</appender>
	<logger name="com.github.developframework.kite" level="INFO" additivity="false">
		<appender-ref ref="STDOUT" />
	</logger>
</configuration>
```

项目启动日志：

```
09:29:07.753 INFO  - Kite framework loaded the configuration source "/kite/kite-demo.xml".

```

