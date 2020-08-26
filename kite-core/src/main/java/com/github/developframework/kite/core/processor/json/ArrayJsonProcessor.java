package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.element.ArrayKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.utils.KiteUtils;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

/**
 * 数组节点处理器
 *
 * @author qiuzhenhao
 */
@Slf4j
public class ArrayJsonProcessor extends ContainerJsonProcessor<ArrayKiteElement, ArrayNode> {

    public ArrayJsonProcessor(JsonProcessContext jsonProcessContext, ArrayKiteElement element) {
        super(jsonProcessContext, element);
    }

    @Override
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        Optional<Object> valueOptional = getDataValue(parentProcessor);
        if (valueOptional.isPresent()) {
            this.value = KiteUtils.objectToArray(valueOptional.get(), element);
            this.node = ((ObjectNode) parentProcessor.node).putArray(showName(parentProcessor));
            return true;
        }
        if (!element.isNullHidden()) {
            if (element.isNullEmpty()) {
                ((ObjectNode) parentProcessor.node).putArray(showName(parentProcessor));
            } else {
                ((ObjectNode) parentProcessor.node).putNull(showName(parentProcessor));
            }
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        Object[] array = (Object[]) value;
        // 处理comparator功能
        if (element.getComparatorValue() != null) {
            Comparator<Object> comparator = KiteUtils.getComponentInstance(jsonProcessContext.getDataModel(), element.getComparatorValue(), Comparator.class, "comparator");
            Arrays.sort(array, comparator);
        }
        // 处理limit功能
        int length = element.getLimit() != null && element.getLimit() < array.length ? element.getLimit() : array.length;
        for (int i = 0; i < length; i++) {
            if (element.getMapFunctionValue() != null) {
                // 处理mapFunction功能
                if (!element.isChildElementEmpty()) {
                    log.warn("The child element invalid, because you use \"map\" attribute.");
                }
                empty(KiteUtils.handleMapFunction(jsonProcessContext.getDataModel(), element.getMapFunctionValue(), array[i], i));
            } else if (element.isChildElementEmpty()) {
                empty(array[i]);
            } else {
                final ObjectInArrayJsonProcessor childProcessor = new ObjectInArrayJsonProcessor(jsonProcessContext, element.getItemObjectElement(), i, length);
                childProcessor.setValue(array[i]);
                childProcessor.process(this);
                node.add(childProcessor.node);
            }
        }
    }

    /**
     * 空子标签处理
     * @param itemValue 数组元素值
     */
    private void empty(Object itemValue) {
        if (itemValue == null) {
            node.addNull();
            return;
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
}
