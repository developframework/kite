package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.dynamic.MapFunction;
import com.github.developframework.kite.core.element.ArrayKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.exception.InvalidArgumentsException;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.utils.KiteUtils;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * 数组节点处理器
 *
 * @author qiuzhenhao
 */
@Slf4j
public class ArrayJsonProcessor extends ContainerJsonProcessor<ArrayKiteElement, ArrayNode> {

    protected Optional<MapFunction> mapFunctionOptional;

    public ArrayJsonProcessor(JsonProcessContext jsonProcessContext, ArrayKiteElement element) {
        super(jsonProcessContext, element);
        this.mapFunctionOptional = mapFunction(element.getMapFunctionValueOptional(), jsonProcessContext.getDataModel());
        if (mapFunctionOptional.isPresent()) {
            if (!element.isChildElementEmpty()) {
                log.warn("The child element invalid, because you use \"map-function\" attribute.");
            }
        }
    }

    @Override
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        Optional<Object> valueOptional = getDataValue(parentProcessor);
        if (valueOptional.isPresent()) {
            this.value = KiteUtils.objectToArray(valueOptional.get(), element);
            this.node = ((ObjectNode) parentProcessor.getNode()).putArray(element.showNameJSON());
            return true;
        }
        if (!element.isNullHidden()) {
            ((ObjectNode) parentProcessor.getNode()).putNull(element.showNameJSON());
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        Object[] array = (Object[]) value;
        for (int i = 0; i < array.length; i++) {
            if (element.isChildElementEmpty() || mapFunctionOptional.isPresent()) {
                empty(array[i], i);
            } else {
                final ObjectInArrayJsonProcessor childProcessor = new ObjectInArrayJsonProcessor(jsonProcessContext, element.getItemObjectElement(), i, array.length);
                childProcessor.setValue(array[i]);
                childProcessor.process(this);
                node.add(childProcessor.getNode());
            }
        }
    }

    /**
     * 空子标签处理
     * @param itemValue 数组元素值
     */
    @SuppressWarnings("unchecked")
    private void empty(Object itemValue, int index) {
        if (itemValue == null) {
            node.addNull();
            return;
        }

        if (mapFunctionOptional.isPresent()) {
            itemValue = mapFunctionOptional.get().apply(itemValue, index);
        }

        if (itemValue instanceof String) {
            node.add((String) itemValue);
        } else if (itemValue instanceof Integer) {
            node.add((Integer) itemValue);
        } else if (itemValue instanceof Long) {
            node.add((Long) itemValue);
        } else if (itemValue instanceof Short) {
            node.add((Short) itemValue);
        } else if (itemValue instanceof Boolean) {
            node.add((Boolean) itemValue);
        } else if (itemValue instanceof Float) {
            node.add((Float) itemValue);
        } else if (itemValue instanceof Double) {
            node.add((Double) itemValue);
        } else if (itemValue instanceof BigDecimal) {
            node.add((BigDecimal) itemValue);
        } else if (itemValue instanceof Character) {
            node.add((Character) itemValue);
        } else if (itemValue instanceof Byte) {
            node.add((Byte) itemValue);
        } else {
            node.add(itemValue.toString());
        }
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
