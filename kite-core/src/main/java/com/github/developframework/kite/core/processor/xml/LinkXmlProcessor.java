package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.expression.ArrayExpression;
import com.github.developframework.expression.Expression;
import com.github.developframework.expression.ObjectExpression;
import com.github.developframework.kite.core.element.ContentKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.LinkKiteElement;
import com.github.developframework.kite.core.exception.InvalidArgumentsException;
import com.github.developframework.kite.core.exception.LinkSizeNotEqualException;
import org.dom4j.Node;

import java.util.Collection;
import java.util.Optional;

/**
 * 一对一链接节点处理器
 *
 * @author qiuzhenhao
 */
public class LinkXmlProcessor extends ObjectXmlProcessor {

    public LinkXmlProcessor(XmlProcessContext xmlProcessContext, LinkKiteElement element, Expression parentExpression) {
        super(xmlProcessContext, element, parentExpression);
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Node> parentProcessor) {
        final ObjectInArrayXmlProcessor objectInArrayProcessor = (ObjectInArrayXmlProcessor) parentProcessor;
        Optional<Object> valueOptional = xmlProcessContext.getDataModel().getData(expression);
        if (valueOptional.isPresent()) {
            this.value = valueOptional.get();
            int size;
            if (value.getClass().isArray()) {
                size = ((Object[]) value).length;
            } else if (value instanceof Collection<?>) {
                size = ((Collection<?>) value).size();
            } else {
                throw new InvalidArgumentsException("data", expression.toString(), "Data must be array or List type.");
            }

            if (size != objectInArrayProcessor.getSize()) {
                throw new LinkSizeNotEqualException(element.getNamespace(), element.getTemplateId());
            }
            return true;
        }
        if (!element.isNullHidden()) {
            node.addElement(element.showNameXML());
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Node> parentProcessor) {
        final ObjectInArrayXmlProcessor objectInArrayProcessor = (ObjectInArrayXmlProcessor) parentProcessor;
        final ArrayExpression arrayExpression = (ArrayExpression) objectInArrayProcessor.getExpression();
        ArrayExpression targetExpression = new ArrayExpression(((ObjectExpression) expression).getPropertyName(), arrayExpression.getIndex());
        ContentKiteElement contentElement = ((LinkKiteElement) element).createProxyContentElement();
        XmlProcessor<? extends KiteElement, ? extends Node> nextProcessor = contentElement.createXmlProcessor(xmlProcessContext, objectInArrayProcessor.node, targetExpression);
        nextProcessor.process(parentProcessor);
    }
}
