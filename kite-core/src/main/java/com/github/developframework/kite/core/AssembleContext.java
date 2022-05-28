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

import java.util.Stack;
import java.util.function.Consumer;

/**
 * 组装过程上下文
 *
 * @author qiushui on 2021-06-23.
 */
public abstract class AssembleContext {

    public final Stack<Object> valueStack = new Stack<>();

    public final Stack<ObjectNodeProxy> nodeStack = new Stack<>();

    // 插槽片段 用于extend遍历后跳回原模板
    public final Stack<Fragment> slotStack = new Stack<>();

    public final KiteConfiguration configuration;

    public final DataModel dataModel;

    public final Framework<?> framework;

    public final NamingStrategy optionNamingStrategy;

    public int arrayIndex;

    public int arrayLength;

    public TemplatePackageRegistry extraTemplatePackages = new TemplatePackageRegistry();

    public AssembleContext(KiteConfiguration configuration, DataModel dataModel, boolean assembleJson) {
        this.configuration = configuration;
        this.dataModel = dataModel;
        this.framework = assembleJson ? configuration.getJsonFramework() : configuration.getXmlFramework();
        this.optionNamingStrategy = assembleJson ? configuration.getOptions().getJson().getNamingStrategy() : configuration.getOptions().getXml().getNamingStrategy();
    }

    /**
     * 创建对象节点
     */
    public abstract ObjectNodeProxy createObjectNodeProxy();

    /**
     * 创建数组节点
     */
    public abstract ArrayNodeProxy createArrayNodeProxy();

    public void prepareNext(ObjectNodeProxy nodeProxy, Object value, Consumer<AssembleContext> consumer) {
        valueStack.push(value);
        nodeStack.push(nodeProxy);
        consumer.accept(this);
        nodeStack.pop();
        valueStack.pop();
    }

    public void prepareNextOnlyNode(ObjectNodeProxy nodeProxy, Consumer<AssembleContext> consumer) {
        nodeStack.push(nodeProxy);
        consumer.accept(this);
        nodeStack.pop();
    }

    public void prepareNextOnlyValue(Object value, Consumer<AssembleContext> consumer) {
        valueStack.push(value);
        consumer.accept(this);
        valueStack.pop();
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
