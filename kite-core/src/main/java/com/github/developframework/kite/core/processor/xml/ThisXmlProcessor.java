package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.expression.ExpressionUtils;
import com.github.developframework.kite.core.dynamic.KiteConverter;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.ThisKiteElement;
import com.github.developframework.kite.core.utils.KiteUtils;
import org.dom4j.Element;

import java.util.Iterator;

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
        if (value != null) {
            node = parentProcessor.node.addElement(element.showNameXML());
            return true;
        }
        if (!element.isNullHidden()) {
            parentProcessor.node.addElement(element.showNameXML());
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        for (Iterator<KiteElement> iterator = element.childElementIterator(); iterator.hasNext(); ) {
            final KiteElement childKiteElement = iterator.next();
            final XmlProcessor<? extends KiteElement, ? extends Element> nextProcessor = childKiteElement.createXmlProcessor(xmlProcessContext, node);
            nextProcessor.process(this);
        }
    }
}
