## 6. 高级功能

### 6.1.处理器

#### 6.1.1. 转换器 converter

> com.github.developframework.kite.core.dynamic.KiteConverter

接口可以对表达式选取的属性值进行自定义转换。具体地，是去处理了`data` 属性找到的值。所以`converter`和`data` 一般同时可用。

```java
@FunctionalInterface
public interface KiteConverter<SOURCE, TARGET> {

    /**
     * 转化方法
     * @param source 源对象
     * @return 目标对象
     */
    TARGET convert(SOURCE source);
}
```

- 泛型SOURCE为原数据的类型。
- 泛型TARGET为转化后的类型。

例如将peter的name处理后输出：

```xml
<template id="company-info" data="company">
    <property data="companyId"/>
    <property data="companyName" converter="nameConverter"/>
</template>
```

```java
DataModel dataModel = DataModel
    .singleton("company", DemoDataMock.mockCompanies()[0])
    .putConverter("nameConverter", (KiteConverter<String, String>) s -> "name: " + s);
```

```json
{
  "company_id" : 1,
  "company_name" : "name: XX科技公司"
}
```

`converter` 属性可以填写Expression表达式，还可以填写`KiteConverter` 的接口实现类全类名。

当转换器用于获取当前data值对象内部属性值时有一种快捷用法：

```xml

<template id="company-info">
    <property data="company" alias="company_name" converter="this.companyName"/>
</template>
```

`<property>` 的取值为company，经过converter的转换选取了company对象的companyName属性作为该节点的值。

#### 6.1.2. `<array>` 的元素映射器 map

在`<array>`节点属性`map`用于指定对每个元素的映射函数。`map`的值可以为`Expression`
表达式或一个实现`com.github.developframework.kite.core.dynamic.KiteConverter`
接口的完全类名。其中使用表达式方式，其获取的实例必须是`com.github.developframework.kite.core.dynamic.KiteConverter`
的实现类。具体示例：实现功能：对数组的每个元素进行映射处理，处理结果作为生成Json的数据。以下示例在字符串数组的每项元素以`name: {item}` 形式输出。

```java
DataModel dataModel = DataModel
    .singleton("companies", DemoDataMock.mockCompanies()[0])
    .putConverter("nameConverter", (KiteConverter<Company, String>) s -> "name: " + s.getCompanyName());
```

```xml

<template id="company-info" data="companies" map="nameConverter">

</template>
```

```json
[ "name: XX科技公司", "name: YY网络公司" ]
```

### 6.2. 虚拟结构

使用`<object-virtual>`可以虚拟一个对象结构。 利用仅有的属性值，构造一个对象结构：

```xml

<template id="staff-info" data="staff">
    <property data="staffId"/>
    <property data="departmentId"/>
    <property data="staffName"/>
    <!-- 这里可以虚构出一层把员工的其他个人信息包装起来 -->
    <object-virtual alias="other">
        <property data="gender"/>
        <property data="birthday" />
    </object-virtual>
</template>
```

```java
DataModel dataModel = DataModel.singleton("companyName", "AA公司");
```

```json
{
  "staff_id" : 111,
  "department_id" : 11,
  "staff_name" : "小张",
  "other" : {
    "gender" : "MALE",
    "birthday" : "1988-02-03 00:00:00"
  }
}
```

### 6.3. 模块化

#### 6.3.1. include引用

使用`<include>`标签引用其它`<template>`模板，从而可实现模块化设计，避免重复定义视图模板。

```xml
<!-- 子模板 -->
<template id="company-info" data="company">
    <property data="companyId"/>
    <property data="companyName"/>
</template>

<template id="company-list">
<array data="companies">
        <!-- 引用子模板 -->
        <include id="company-info"/>
</array>
</template>
```

```json
{
  "companies" : [ {
    "company_id" : 1,
    "company_name" : "XX科技公司"
  }, {
    "company_id" : 2,
    "company_name" : "YY网络公司"
  } ]
}
```

#### 6.3.2. extend继承和slot插槽

Kite框架的继承的概念，在`<template>`标签可以添加属性`extend`
指定继承的template和继承的端口。继承的概念可以理解为反向include，调用子template视图，会优先从父template开始构造结构，当遇到`<slot`标签时才会构造子template视图。

**注意：**

+ 假如单独调用了有`<slot>`标签的父template视图或者端口没有与之对应的子template实现，则`<slot>`标签被忽略
+ 一个父template中只能声明一个`<slot>`标签

```xml
<!-- 父模板 -->
<template id="parent">
    <property data="success"/>
    <property data="message"/>
    <object-virtual alias="data">
        <!-- 插槽 所有继承此模板的子模板内容会插入在此 -->
        <slot/>
    </object-virtual>
</template>

        <!-- 子模板 -->
<template id="company-info" extend="parent">
<object data="company">
        <property data="companyId"/>
        <property data="companyName"/>
</object>
</template>
```

```java
DataModel dataModel = DataModel
        .singleton("company", DemoDataMock.mockCompanies()[0])
        .putData("success", true)
        .putData("message", "ok");
// 这里调用了子模板
final String json = kiteFactory.getJsonProducer(dataModel, "kite-demo", "company-info").produce(true);
```

```json
{
  "success" : true,
  "message" : "ok",
  "data" : {
    "company" : {
      "company_id" : 1,
      "company_name" : "XX科技公司"
    }
  }
}
```

`extend` 属性的写法为  **namespace.templateId**  其中namespace可以省略，默认为当前命名空间下。

### 6.4. 链接与关联

#### 6.4.1. link一对一数组链接

使用`<link>` 标签可以在数组间一对一链接对象。 **该标签仅能在`<array>`下使用。** 当`<link>` 的data属性所指的数组和父`<array>`
数组个数不相同时将会抛出`LinkSizeNotEqualException`。 例子： 假如每个员工都有一个考核评分，数据源为员工数组和评分数组（这里使用`limit`限制输出前3条数据）

```xml

<template id="staff-info" data="staffs" limit="3">
    <property data="staffId"/>
    <property data="departmentId"/>
    <property data="staffName"/>
    <!-- 因为scores属性不在Staff中所以要加#从dataModel取值 -->
    <link data="#scores" limit="3"/>
</template>
```

```java
DataModel dataModel = DataModel
    .singleton("staffs", DemoDataMock.mockStaffs())
    .putData("scores", DemoDataMock.mockScores());
```

```json
[ {
  "staff_id" : 111,
  "department_id" : 11,
  "staff_name" : "小张",
  "scores" : 70
}, {
  "staff_id" : 112,
  "department_id" : 12,
  "staff_name" : "小王",
  "scores" : 85
}, {
  "staff_id" : 113,
  "department_id" : 13,
  "staff_name" : "小李",
  "scores" : 93
} ]
```

#### 6.4.2. relevance根据条件关联

假如A数组有2个元素，B数组有3个元素。其中A[0] 需要关联B[0]和B[1]， A[1] 需要关联B[2]。这种需求下就可以使用`<relevance>`标签，实现在数组间一对多关联。属性`rel` 指定判定条件，需要实现一个接口：

```java
public interface RelFunction<S, T> {

    boolean relevant(S sourceItem, int sourceIndex, T target, int targetIndex);
}
```

其中泛型S指代A数组类型，T指代B数组类型。

sourceItem是迭代了A数组的每一项，sourceIndex是它的索引。每一项的A元素都会去迭代B数组的每一项target，targetIndex是索引，`relevant` 方法返回true表示需要关联。

具体看示例：

```xml

<template id="test-rel" data="companies">
    <include id="company-info"/>
    <relevance data="#departments" rel="companyDepartmentRel">
        <include id="department-info"/>
        <relevance data="#staffs" rel="departmentStaffRel">
            <include id="staff-info"/>
        </relevance>
    </relevance>
</template>
```

```java
DataModel dataModel = DataModel
    .singleton("staffs", DemoDataMock.mockStaffs())
    .putData("departments", DemoDataMock.mockDepartments())
    .putData("companies", DemoDataMock.mockCompanies())
    .putData("scores", DemoDataMock.mockScores())
    // 声明公司和部门关联关系
    .putRelFunction("companyDepartmentRel", (RelFunction<Company, Department>) (company, ci, department, di) -> company.getCompanyId().equals(department.getCompanyId()))
    // 声明部门和员工关联关系
    .putRelFunction("departmentStaffRel", (RelFunction<Department, Staff>) (department, di, staff, si) -> department.getDepartmentId().equals(staff.getDepartmentId()));
```

```json
[ {
  "company_id" : 1,
  "company_name" : "XX科技公司",
  "departments" : [ {
    "department_id" : 11,
    "company_id" : 1,
    "department_name" : "技术部",
    "staffs" : {
      "staff_id" : 111,
      "department_id" : 11,
      "staff_name" : "小张"
    }
  }, {
    "department_id" : 13,
    "company_id" : 1,
    "department_name" : "财务部",
    "staffs" : [ {
      "staff_id" : 113,
      "department_id" : 13,
      "staff_name" : "小李"
    }, {
      "staff_id" : 116,
      "department_id" : 13,
      "staff_name" : "小钱"
    } ]
  } ]
}, {
  "company_id" : 2,
  "company_name" : "YY网络公司",
  "departments" : [ {
    "department_id" : 12,
    "company_id" : 2,
    "department_name" : "运营部",
    "staffs" : [ {
      "staff_id" : 112,
      "department_id" : 12,
      "staff_name" : "小王"
    }, {
      "staff_id" : 115,
      "department_id" : 12,
      "staff_name" : "小郑"
    } ]
  }, {
    "department_id" : 14,
    "company_id" : 2,
    "department_name" : "事业部",
    "staffs" : [ {
      "staff_id" : 114,
      "department_id" : 14,
      "staff_name" : "小孙"
    }, {
      "staff_id" : 117,
      "department_id" : 14,
      "staff_name" : "小潘"
    } ]
  } ]
} ]
```

可以看到输出的结果公司-部门-员工被按照关系拼接到了一个数组里

`<relevance>`的属性`type`

+ `single`如果匹配中多个就取第一个元素，作为对象结构拼接到父节点
+ `multiple`作为数组结构拼接到父节点
+ `auto` 如果匹配到一个结果就用对象结构，多个结果就用数组结构

`<relevance>`的`unique`，默认为false，true的话强制只匹配一个就结束匹配，如果明确只有一个对应的结果那么使用unique能提高性能

`<relevance>`的`merge-parent`，默认为false，true的话如果只匹配中一个结果时将会把属性拼接到父节点

例子：强制只取第一个员工并拼接到部门对象

```xml

<template id="test-merge-parent" data="departments">
    <include id="department-info"/>
    <relevance data="#staffs" rel="departmentStaffRel" unique="true" merge-parent="true">
        <include id="staff-info"/>
    </relevance>
</template>
```

```json
[ {
  "department_id" : 11,
  "company_id" : 1,
  "department_name" : "技术部",
  "staff_id" : 111,
  "staff_name" : "小张"
}, {
  "department_id" : 12,
  "company_id" : 2,
  "department_name" : "运营部",
  "staff_id" : 112,
  "staff_name" : "小王"
}, {
  "department_id" : 13,
  "company_id" : 1,
  "department_name" : "财务部",
  "staff_id" : 113,
  "staff_name" : "小李"
}, {
  "department_id" : 14,
  "company_id" : 2,
  "department_name" : "事业部",
  "staff_id" : 114,
  "staff_name" : "小孙"
} ]
```

### 6.5. 分支结构

#### 6.5.1. `<if>` `<else>`

可以使用`<if>` `<else>` 标签进行模块内容的取舍。`<else>` 标签可以不写，但必须紧跟`<if>` 后出现。

`<if>` 标签的`condition` 属性内容为接口`com.github.developframework.kite.core.dynamic.KiteCondition` 的实现类。

```java
@FunctionalInterface
public interface Condition<T> {

    /**
     * 判断条件
     * @param dataModel 数据模型
     * @param currentValue 当前值
     * @return 判断结果
     */
    boolean verify(DataModel dataModel, T currentValue);
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

#### 6.5.2. `<switch>` `<case>` `<default>`

可以使用`<switch>` `<case>` `<default>`标签进行模块内容的选择。`<case>` 标签可以出现多个，`<default>`只能出现一次，并且只能是`<switch>` 标签的最后一个子节点。

`<case>` 标签的`test` 属性内容为接口`com.github.developframework.kite.core.dynamic.CaseTestFunction` 的实现类。

```java
@FunctionalInterface
public interface CaseTestFunction<T> {

    /**
     * 测试选择
     * 
     * @param data switch传入的值
     * @return 是否选中该分支
     */
    boolean test(T data);
}
```

最简范例：

```xml

<template id="first-view">
    <switch check-data="switchData">
    <case test="testCase1">
        <property data="sayHello"/>
    </case>
    <case test="testCase2">
        <property data="sayThanks"/>
    </case>
    <default>
        <property data="sayBye"/>
    </default>
    </switch>
</template>
```

```java
dataModel.putData("switchData", 1);
dataModel.putData("sayHello", "Hello");
dataModel.putData("sayThanks", "Thanks");
dataModel.putData("sayBye", "Bye");
dataModel.putData("testCase1", (CaseTestFunction<Integer>) value -> value == 1);
dataModel.putData("testCase2", (CaseTestFunction<Integer>) value -> value == 2);
```

当`switchData`等于1时输出

```json
{"sayHello" : "Hello"}
```

当`switchData`等于2时输出

```json
{"sayThanks" : "Thanks"}
```

当`switchData`等于3时输出

```json
{"sayBye" : "Bye"}
```

