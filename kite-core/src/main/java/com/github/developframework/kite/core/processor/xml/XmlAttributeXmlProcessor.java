package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.XmlAttributeElement;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.Optional;

/**
 * @author qiuzhenhao
 */
public class XmlAttributeXmlProcessor extends ContentXmlProcessor<XmlAttributeElement, Element>{


    public XmlAttributeXmlProcessor(XmlProcessContext xmlProcessContext, XmlAttributeElement element, Expression parentExpression) {
        super(xmlProcessContext, element, parentExpression);
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Node> parentProcessor) {
        Optional<Object> valueOptional = xmlProcessContext.getDataModel().getData(expression);
        if (valueOptional.isPresent()) {
            this.value = valueOptional.get();
            this.node = (Element) parentProcessor.getNode();
            return true;
        }
        return !element.isNullHidden();
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Node> parentProcessor) {
        this.node.addAttribute(element.showNameXML(), value.toString());
    }
}
