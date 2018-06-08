package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.exception.KiteParseXmlException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 容器节点
 * @author qiuzhenhao
 */
public abstract class ContainerKiteElement extends ContentKiteElement implements ContainChildElementable{

    /* 子节点列表 */
    protected List<KiteElement> childKiteElements = new ArrayList<>();

    /* for-class定义的类型 */
    @Getter
    protected Class<?> forClass;

    /* 忽略的属性名称列表 */
    protected List<String> ignorePropertyNames = new ArrayList<>();

    public ContainerKiteElement(KiteConfiguration configuration, String namespace, String templateId, DataDefinition dataDefinition, String alias) {
        super(configuration, namespace, templateId, dataDefinition, alias);
    }

    /**
     * 设置for-class
     * @param className 类名
     */
    public void setForClass(String className) {
        if (StringUtils.isNotBlank(className)) {
            try {
                forClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new KiteParseXmlException("Class \"%s\" is not found, please check configuration file.", className);
            }
        }
    }

    /**
     * 增加忽略属性
     * @param propertyName 属性名称
     */
    public final void addIgnoreProperty(String propertyName) {
        ignorePropertyNames.add(propertyName);
    }

    /**
     * 加载for-class的全部属性
     */
    public final void loadForClassAllProperty() {
        if (forClass != null) {
            Field[] fields = forClass.getDeclaredFields();
            for (Field field : fields) {
                if (!ignorePropertyNames.contains(field.getName())) {
                    DataDefinition dataDefinition = new DataDefinition(field.getName());
                    PropertyKiteElement propertyElement = new NormalPropertyKiteElement(configuration, this.getNamespace(), this.getTemplateId(), dataDefinition, null);
                    if (!childKiteElements.contains(propertyElement)) {
                        addChildElement(propertyElement);
                    }
                }
            }
        }
    }

    public void copyChildElement(ContainerKiteElement otherContainerElement) {
        this.childKiteElements.addAll(otherContainerElement.getChildKiteElements());
        this.ignorePropertyNames.addAll(otherContainerElement.ignorePropertyNames);
        this.alias = otherContainerElement.alias;
        this.forClass = otherContainerElement.forClass;
        this.nullHidden = otherContainerElement.nullHidden;
        this.namespace = otherContainerElement.namespace;
        this.templateId = otherContainerElement.templateId;
    }

    @Override
    public void addChildElement(KiteElement kiteElement) {
        childKiteElements.add(kiteElement);
    }


    @Override
    public final void copyChildElement(ContainChildElementable otherContainer) {
        this.childKiteElements.addAll(otherContainer.getChildKiteElements());
    }

    @Override
    public final Iterator<KiteElement> childElementIterator() {
        return childKiteElements.iterator();
    }

    @Override
    public final boolean isChildElementEmpty() {
        return childKiteElements.isEmpty();
    }


    @Override
    public final List<KiteElement> getChildKiteElements() {
        return childKiteElements;
    }
}
