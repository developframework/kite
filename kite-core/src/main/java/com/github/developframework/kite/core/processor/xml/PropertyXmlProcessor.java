package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.dynamic.PropertyConverter;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.PropertyKiteElement;
import com.github.developframework.kite.core.utils.KiteUtils;
import org.dom4j.Element;

import java.util.Iterator;
import java.util.Optional;

/**
 * 抽象的属性节点处理器
 *
 * @author qiuzhenhao
 */
public abstract class PropertyXmlProcessor extends ContainerXmlProcessor<PropertyKiteElement, Element> {

    public PropertyXmlProcessor(XmlProcessContext xmlProcessContext, PropertyKiteElement element) {
        super(xmlProcessContext, element);
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        Optional<Object> valueOptional = getDataValue(parentProcessor);
        if (valueOptional.isPresent()) {
            value = valueOptional.get();
            if (element.getConverterValue().isPresent()) {
                PropertyConverter converter = KiteUtils.getComponentInstance(xmlProcessContext.getDataModel(), element.getConverterValue().get(), PropertyConverter.class, "converter");
                value = converter.convert(value);
            }
            this.node = parentProcessor.getNode().addElement(element.showNameXML());
            return true;
        }
        if (!element.isNullHidden()) {
            this.node = parentProcessor.getNode().addElement(element.showNameXML());
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        Class<?> valueClass = value.getClass();
        if (support(valueClass)) {
            handle(valueClass, value);
        }
    }

    /**
     * 判断是否支持sourceClass类型
     *
     * @param sourceClass 源类型
     * @return 是否支持
     */
    protected abstract boolean support(Class<?> sourceClass);

    /**
     * 属性具体处理
     *
     * @param sourceClass sourceClass
     * @param value       值
     */
    protected abstract void handle(Class<?> sourceClass, Object value);

    /**
     * 给节点增加内容
     * @param value 值
     */
    protected void elementAddContent(String value) {
        if (element.isXmlCdata()) {
            node.addCDATA(value);
        } else {
            node.addText(value);
        }
        for (Iterator<KiteElement> iterator = element.childElementIterator(); iterator.hasNext();) {
            final KiteElement childKiteElement = iterator.next();
            final XmlProcessor<? extends KiteElement, ? extends Element> childXmlProcessor = childKiteElement.createXmlProcessor(xmlProcessContext, node);
            childXmlProcessor.process(this);
        }
    }
}
