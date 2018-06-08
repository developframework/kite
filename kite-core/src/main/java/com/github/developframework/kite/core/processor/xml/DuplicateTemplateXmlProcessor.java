package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.ObjectKiteElement;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.Optional;

/**
 * 副本模板节点处理器
 * @author qiuzhenhao
 */
public class DuplicateTemplateXmlProcessor extends ObjectXmlProcessor{

    public DuplicateTemplateXmlProcessor(XmlProcessContext jsonProcessContext, ObjectKiteElement element, Expression parentExpression) {
        super(jsonProcessContext, element, parentExpression);
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Node> parentProcessor) {
        // 始终为true
        return true;
    }

    @Override
    public void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Node> parentProcessor) {
        if (parentProcessor instanceof ObjectInArrayXmlProcessor) {
            ObjectInArrayXmlProcessor processor = new ObjectInArrayXmlProcessor(xmlProcessContext, element, parentProcessor.expression, ((ObjectInArrayXmlProcessor) parentProcessor).size) {

                @Override
                protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Node> parentProcessor) {
                    Optional<Object> valueOptional = xmlProcessContext.getDataModel().getData(expression);
                    if (valueOptional.isPresent()) {
                        this.value = valueOptional.get();
                        this.node = (Element) parentProcessor.node;
                        return true;
                    }
                    return false;
                }
            };
            processor.setNode((Element) parentProcessor.node);
            processor.process(parentProcessor);
        } else {
            super.handleCoreLogic(parentProcessor);
        }
    }
}
