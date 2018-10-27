package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.XmlAttributeElement;
import org.dom4j.Element;

import java.util.Optional;

/**
 * @author qiuzhenhao
 */
public class XmlAttributeXmlProcessor extends ContentXmlProcessor<XmlAttributeElement, Element>{


    public XmlAttributeXmlProcessor(XmlProcessContext xmlProcessContext, XmlAttributeElement element) {
        super(xmlProcessContext, element);
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        Optional<Object> valueOptional = getDataValue(parentProcessor);
        if (valueOptional.isPresent()) {
            this.value = valueOptional.get();
            this.node = parentProcessor.getNode();
            return true;
        }
        return !element.isNullHidden();
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        this.node.addAttribute(element.showNameXML(), value.toString());
    }
}
