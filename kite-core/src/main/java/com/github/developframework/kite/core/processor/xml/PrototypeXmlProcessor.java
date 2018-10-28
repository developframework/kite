package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.PrototypeKiteElement;
import com.github.developframework.kite.core.exception.KiteException;
import org.dom4j.Element;

import java.util.Optional;

/**
 * 原型节点处理器
 *
 * @author qiuzhenhao
 */
public class PrototypeXmlProcessor extends ContentXmlProcessor<PrototypeKiteElement, Element> {

    public PrototypeXmlProcessor(XmlProcessContext xmlProcessContext, PrototypeKiteElement element) {
        super(xmlProcessContext, element);
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        Optional<Object> valueOptional = getDataValue(parentProcessor);
        if(valueOptional.isPresent()) {
            value = valueOptional.get();
            return true;
        }
        if (!element.isNullHidden()) {
            node.addElement(element.showNameXML());
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        throw new KiteException("xml not support prototype");
    }
}
