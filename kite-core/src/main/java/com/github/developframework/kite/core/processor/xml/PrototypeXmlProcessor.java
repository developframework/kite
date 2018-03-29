package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.PrototypeKiteElement;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.Optional;

/**
 * 原型节点处理器
 *
 * @author qiuzhenhao
 */
public class PrototypeXmlProcessor extends ContentXmlProcessor<PrototypeKiteElement, Node>{

    public PrototypeXmlProcessor(XmlProcessContext xmlProcessContext, PrototypeKiteElement element, Expression parentExpression) {
        super(xmlProcessContext, element, parentExpression);
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Node> parentProcessor) {
        Optional<Object> valueOptional = xmlProcessContext.getDataModel().getData(expression);
        if(valueOptional.isPresent()) {
            this.value = valueOptional.get();
            return true;
        }
        if (!element.isNullHidden()) {
            ((Element)node).addElement(element.showNameXML());
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Node> parentProcessor) {
//        // 经过converter转化后的值
//        Optional<Object> convertValueOptional = element.getConverterValue().map(converterValue -> {
//            Optional<Object> converterOptional = xmlProcessContext.getDataModel().getData(converterValue);
//            Object obj = converterOptional.orElseGet(() -> {
//                try {
//                    return Class.forName(converterValue).newInstance();
//                } catch (ClassNotFoundException e) {
//                    throw new InvalidArgumentsException("converter", converterValue, "Class not found, and it's also not a expression.");
//                } catch (IllegalAccessException | InstantiationException e) {
//                    throw new KiteException("Can't new converter instance.");
//                }
//            });
//            if (obj instanceof PropertyConverter) {
//                return ((PropertyConverter) obj).convert(value);
//            } else {
//                throw new InvalidArgumentsException("converter", converterValue, "It's not a PropertyConverter instance.");
//            }
//        });
//        final Object convertValue = convertValueOptional.orElse(value);
//
//        ObjectMapper objectMapper = xmlProcessContext.getConfiguration().getObjectMapper();
//        JsonNode jsonNode = objectMapper.valueToTree(convertValue);
//        node.set(element.showNameXML(), jsonNode);
    }
}
