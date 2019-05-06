package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.expression.ExpressionUtils;
import com.github.developframework.kite.core.dynamic.KiteConverter;
import com.github.developframework.kite.core.element.ContentKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.ThisKiteElement;
import com.github.developframework.kite.core.utils.KiteUtils;
import org.dom4j.Element;

/**
 * 指代自身的Xml处理器
 *
 * @author qiushui on 2018-10-28.
 */
public class ThisXmlProcessor extends ContainerXmlProcessor<ThisKiteElement, Element> {


    public ThisXmlProcessor(XmlProcessContext xmlProcessContext, ThisKiteElement element) {
        super(xmlProcessContext, element);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        if (element.getConverterValue().isPresent()) {
            String converterValue = element.getConverterValue().get();
            if (converterValue.startsWith("this.")) {
                // 简单表达式
                value = ExpressionUtils.getValue(parentProcessor.value, converterValue.substring(5));
            } else {
                KiteConverter converter = KiteUtils.getComponentInstance(xmlProcessContext.getDataModel(), converterValue, KiteConverter.class, "converter");
                value = converter.convert(parentProcessor.value);
            }
        } else {
            value = parentProcessor.value;
        }
        if (value == null && !element.isNullHidden()) {
            parentProcessor.node.addElement(element.showNameXML());
            return false;
        }
        return true;
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        ContentKiteElement contentElement = KiteUtils.isArrayOrCollection(value) ? element.createProxyArrayElement() : element.createProxyObjectElement();
        XmlProcessor<? extends KiteElement, ? extends Element> nextProcessor = contentElement.createXmlProcessor(xmlProcessContext, parentProcessor.node);
        nextProcessor.value = value;
        nextProcessor.process(parentProcessor);
    }
}
