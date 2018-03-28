package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.expression.ArrayExpression;
import com.github.developframework.expression.Expression;
import com.github.developframework.expression.ObjectExpression;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.dynamic.MapFunction;
import com.github.developframework.kite.core.element.ArrayKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.exception.InvalidArgumentsException;
import com.github.developframework.kite.core.exception.KiteException;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.List;
import java.util.Optional;

/**
 * 数组节点处理器
 *
 * @author qiuzhenhao
 */
@Slf4j
public class ArrayXmlProcessor extends ContainerXmlProcessor<ArrayKiteElement, Node> {

    private Optional<MapFunction> mapFunctionOptional;

    public ArrayXmlProcessor(XmlProcessContext xmlProcessContext, ArrayKiteElement element, Expression parentExpression) {
        super(xmlProcessContext, element, parentExpression);
        this.mapFunctionOptional = mapFunction(element.getMapFunctionValueOptional(), xmlProcessContext.getDataModel());
        if (mapFunctionOptional.isPresent()) {
            if (!element.isChildElementEmpty()) {
                log.warn("The child element invalid, because you use \"map-function\" attribute.");
            }
        }
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Node> parentProcessor) {
        Optional<Object> valueOptional = xmlProcessContext.getDataModel().getData(expression);
        if (valueOptional.isPresent()) {
            this.value = valueOptional.get();
            this.node = ((Element) parentProcessor.getNode()).addElement(element.showName());
            return true;
        }
        if (!element.isNullHidden()) {
            ((Element) parentProcessor.getNode()).addElement(element.showName());
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Node> parentProcessor) {
        int size;
        if (value.getClass().isArray()) {
            size = ((Object[]) value).length;
        } else if (value instanceof List<?>) {
            size = ((List<?>) value).size();
        } else {
            throw new InvalidArgumentsException("data", expression.toString(), "Data must be array or List type.");
        }
        for (int i = 0; i < size; i++) {
            single(ArrayExpression.fromObject((ObjectExpression) expression, i), size);
        }
    }

    /**
     * 处理单一元素
     * @param arrayExpression 表达式
     * @param size 总数量
     */
    protected final void single(ArrayExpression arrayExpression, int size) {
        if (element.isChildElementEmpty() || mapFunctionOptional.isPresent()) {
            empty(arrayExpression.getIndex());
        } else {
            final ObjectInArrayXmlProcessor childProcessor = new ObjectInArrayXmlProcessor(xmlProcessContext, element.getItemObjectElement(), arrayExpression, size);
            childProcessor.process(this);
//            ((Element)node).add(childProcessor.getNode());
        }
    }

    /**
     * 空子标签处理
     * @param index 索引
     */
    private void empty(final int index) {
        final Optional<Object> objectOptional = xmlProcessContext.getDataModel().getData(ArrayExpression.fromObject((ObjectExpression) expression, index));
        if (!objectOptional.isPresent()) {
//            node.addNull();
            return;
        }
        Object object = objectOptional.get();

        if (mapFunctionOptional.isPresent()) {
            object = mapFunctionOptional.get().apply(object, index);
        }

        ((Element)node).addElement(element.getXmlItemName()).addText(object.toString());

//        if (object instanceof String) {
//            node.add((String) object);
//        } else if (object instanceof Integer) {
//            node.add((Integer) object);
//        } else if (object instanceof Long) {
//            node.add((Long) object);
//        } else if (object instanceof Short) {
//            node.add((Short) object);
//        } else if (object instanceof Boolean) {
//            node.add((Boolean) object);
//        } else if (object instanceof Float) {
//            node.add((Float) object);
//        } else if (object instanceof Double) {
//            node.add((Double) object);
//        } else if (object instanceof BigDecimal) {
//            node.add((BigDecimal) object);
//        } else if (object instanceof Character) {
//            node.add((Character) object);
//        } else if (object instanceof Byte) {
//            node.add((Byte) object);
//        } else {
//            node.add(object.toString());
//        }
    }

    /**
     * 获得mapFunction
     * @param mapFunctionValueOptional mapFunctionValueOptional
     * @param dataModel 数据模型
     * @return mapFunction
     */
    private Optional<MapFunction> mapFunction(Optional<String> mapFunctionValueOptional, DataModel dataModel) {
        if (mapFunctionValueOptional.isPresent()) {
            final String mapFunctionValue = mapFunctionValueOptional.get();
            Optional<Object> mapFunctionOptional = dataModel.getData(mapFunctionValue);
            Object obj = mapFunctionOptional.orElseGet(() -> {
                try {
                    return Class.forName(mapFunctionValue).newInstance();
                } catch (ClassNotFoundException e) {
                    throw new InvalidArgumentsException("map-function", mapFunctionValue, "Class not found, and it's also not a expression.");
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new KiteException("Can't new mapFunction instance.");
                }
            });
            if (obj instanceof MapFunction) {
                return Optional.of(((MapFunction) obj));
            } else {
                throw new InvalidArgumentsException("map-function", mapFunctionValue, "It's not a MapFunction instance.");
            }
        }
        return Optional.empty();
    }
}
