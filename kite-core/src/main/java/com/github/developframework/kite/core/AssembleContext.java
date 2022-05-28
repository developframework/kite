package com.github.developframework.kite.core;

import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.element.Fragment;
import com.github.developframework.kite.core.element.Template;
import com.github.developframework.kite.core.exception.TemplateException;
import com.github.developframework.kite.core.exception.TemplatePackageUndefinedException;
import com.github.developframework.kite.core.node.ArrayNodeProxy;
import com.github.developframework.kite.core.node.ObjectNodeProxy;
import com.github.developframework.kite.core.strategy.NamingStrategy;
import com.github.developframework.kite.core.structs.TemplatePackage;
import com.github.developframework.kite.core.structs.TemplatePackageRegistry;
import lombok.Getter;

import java.util.Stack;

/**
 * 组装过程上下文
 *
 * @author qiushui on 2021-06-23.
 */
public abstract class AssembleContext {

    private final Stack<Object> valueStack = new Stack<>();

    private final Stack<ObjectNodeProxy> nodeStack = new Stack<>();

    @Getter
    protected final KiteConfiguration configuration;

    // true 组装json | false 组装xml
    private final boolean assembleJson;

    public DataModel dataModel;

    public int arrayIndex;

    public int arrayLength;

    // 插槽片段 用于extend遍历后跳回原模板
    public Stack<Fragment> slotStack = new Stack<>();

    public TemplatePackageRegistry extraTemplatePackages = new TemplatePackageRegistry();

    public AssembleContext(KiteConfiguration configuration, boolean assembleJson) {
        this.configuration = configuration;
        this.assembleJson = assembleJson;
    }

    /**
     * 创建对象节点
     */
    public abstract ObjectNodeProxy createObjectNodeProxy(Object node);

    /**
     * 创建数组节点
     */
    public abstract ArrayNodeProxy createArrayNodeProxy(Object node);

    /**
     * 选择实现框架
     */
    public Framework<?> switchFramework() {
        return assembleJson ? configuration.getJsonFramework() : configuration.getXmlFramework();
    }

    public NamingStrategy getOptionNamingStrategy() {
        final KiteOptions options = configuration.getOptions();
        return assembleJson ? options.getJson().getNamingStrategy() : options.getXml().getNamingStrategy();
    }

    public void pushNodeProxy(ObjectNodeProxy nodeProxy) {
        this.nodeStack.push(nodeProxy);
    }

    /**
     * 父节点拼接一个对象节点并入栈
     *
     * @param name
     */
    public void parentPutNodeProxyAndPush(String name) {
        this.nodeStack.push(nodeStack.peek().putObjectNode(name));
    }

    /**
     * 取栈顶节点代理
     *
     * @return 节点代理
     */
    public ObjectNodeProxy peekNodeProxy() {
        return nodeStack.peek();
    }

    public void pushValue(Object value) {
        this.valueStack.push(value);
    }

    public void popNodeProxy() {
        this.nodeStack.pop();
    }

    public void popValue() {
        this.valueStack.pop();
    }

    /**
     * 取栈顶值
     *
     * @return 栈顶值
     */
    public Object peekValue() {
        return valueStack.isEmpty() ? null : valueStack.peek();
    }

    public void pop() {
        this.nodeStack.pop();
        this.valueStack.pop();
    }

    /**
     * 提取模板
     */
    public Template extractTemplate(String namespace, String id) {
        Template template = findTemplatePackage(namespace).getTemplateById(id);
        if (template == null) {
            throw new TemplateException("未定义模板“%s.%s”", namespace, id);
        }
        return template;
    }

    /**
     * 提取片段
     */
    public Fragment extractFragment(String namespace, String id) {
        Fragment fragment = findTemplatePackage(namespace).get(id);
        if (fragment == null) {
            throw new TemplateException("未定义片段“%s.%s”", namespace, id);
        }
        return fragment;
    }

    private TemplatePackage findTemplatePackage(String namespace) {
        final TemplatePackageRegistry registry = configuration.getTemplatePackageRegistry();
        TemplatePackage templatePackage = registry.getTemplatePackageByNamespace(namespace);
        if (templatePackage == null) {
            templatePackage = extraTemplatePackages.getTemplatePackageByNamespace(namespace);
            if (templatePackage == null) {
                throw new TemplatePackageUndefinedException(namespace);
            }
        }
        return templatePackage;
    }
}
