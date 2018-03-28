package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.element.ArrayKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.ObjectKiteElement;
import lombok.Getter;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.Optional;

/**
 * 数组元素处理器
 *
 * @author qiuzhenhao
 */
public class ObjectInArrayXmlProcessor extends ObjectXmlProcessor {

    @Getter
    protected int size;

    public ObjectInArrayXmlProcessor(XmlProcessContext context, ObjectKiteElement element, Expression parentExpression, int size) {
        super(context, element, parentExpression);
        this.size = size;
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Node> parentProcessor) {
        Optional<Object> valueOptional = xmlProcessContext.getDataModel().getData(expression);
        if (valueOptional.isPresent()) {
            this.value = valueOptional.get();
            this.node = ((Element) parentProcessor.node).addElement(((ArrayKiteElement)(parentProcessor.element)).getXmlItemName());
            return true;
        }
        return false;
    }
}
