package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.dynamic.PropertyConverter;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.PropertyKiteElement;
import com.github.developframework.kite.core.exception.InvalidArgumentsException;
import com.github.developframework.kite.core.exception.KiteException;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.Iterator;
import java.util.Optional;

/**
 * 抽象的属性节点处理器
 *
 * @author qiuzhenhao
 */
public abstract class PropertyXmlProcessor extends ContainerXmlProcessor<PropertyKiteElement, Element> {

    public PropertyXmlProcessor(XmlProcessContext xmlProcessContext, PropertyKiteElement element, Expression parentExpression) {
        super(xmlProcessContext, element, parentExpression);
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Node> parentProcessor) {
        Optional<Object> valueOptional = xmlProcessContext.getDataModel().getData(expression);
        if (valueOptional.isPresent()) {
            this.value = valueOptional.get();
            this.node = ((Element) parentProcessor.getNode()).addElement(element.showNameXML());
            return true;
        }
        if (!element.isNullHidden()) {
            this.node = ((Element) parentProcessor.getNode()).addElement(element.showNameXML());
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Node> parentProcessor) {
        // 经过converter转化后的值
        Optional<Object> convertValueOptional = element.getConverterValue().map(converterValue -> {
            Optional<Object> converterOptional = xmlProcessContext.getDataModel().getData(converterValue);
            Object obj = converterOptional.orElseGet(() -> {
                try {
                    return Class.forName(converterValue).newInstance();
                } catch (ClassNotFoundException e) {
                    throw new InvalidArgumentsException("converter", converterValue, "Class not found, and it's also not a expression.");
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new KiteException("Can't new converter instance.");
                }
            });
            if (obj instanceof PropertyConverter) {
                return ((PropertyConverter) obj).convert(value);
            } else {
                throw new InvalidArgumentsException("converter", converterValue, "It's not a PropertyConverter instance.");
            }
        });
        final Object convertValue = convertValueOptional.orElse(value);
        Class<?> convertValueClass = convertValue.getClass();
        if (support(convertValueClass)) {
            handle(convertValueClass, convertValue);
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
            final XmlProcessor<? extends KiteElement, ? extends Node> childXmlProcessor = childKiteElement.createXmlProcessor(xmlProcessContext, node, expression);
            childXmlProcessor.process(this);
        }
    }
}
