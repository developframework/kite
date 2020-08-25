package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.ExpressionUtils;
import com.github.developframework.kite.core.dynamic.KiteConverter;
import com.github.developframework.kite.core.dynamic.RelFunction;
import com.github.developframework.kite.core.element.ContentKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.RelevanceKiteElement;
import com.github.developframework.kite.core.utils.KiteUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 关联节点处理器
 *
 * @author qiuzhenhao
 */
@Slf4j
public class RelevanceJsonProcessor extends ArrayJsonProcessor {

    @Setter
    private int index;

    public RelevanceJsonProcessor(JsonProcessContext jsonProcessContext, RelevanceKiteElement element) {
        super(jsonProcessContext, element);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        index = ((ObjectInArrayJsonProcessor) parentProcessor).getIndex();
        Optional<Object> valueOptional = getDataValue(parentProcessor);
        if (valueOptional.isPresent()) {
            ObjectInArrayJsonProcessor objectInArrayProcessor = (ObjectInArrayJsonProcessor) parentProcessor;

            RelFunction relFunction = ((RelevanceKiteElement) element).getRelFunctionValue()
                    .map(relFunctionValue -> KiteUtils.getComponentInstance(jsonProcessContext.getDataModel(), relFunctionValue, RelFunction.class, "rel"))
                    .orElseThrow();
            Object[] targets = KiteUtils.objectToArray(valueOptional.get(), element);
            List<Integer> indexList = new LinkedList<>();
            for (int i = 0; i < targets.length; i++) {
                if (relFunction.relevant(objectInArrayProcessor.value, index, targets[i], i)) {
                    indexList.add(i);
                }
            }
            value = indexList.stream().map(i -> targets[i]).toArray();
            return true;
        }
        if (!element.isNullHidden()) {
            if (element.isNullEmpty()) {
                ((ObjectNode) parentProcessor.node).putArray(element.showNameJSON());
            } else {
                ((ObjectNode) parentProcessor.node).putNull(element.showNameJSON());
            }
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        Object[] valueArray = forInnerConverter((Object[]) value);
        // 类型策略
        switch (((RelevanceKiteElement) element).getRelevanceType()) {
            case AUTO: {
                if (valueArray.length == 1) {
                    generateObjectStructure(parentProcessor, valueArray);
                } else {
                    generateArrayStructure(parentProcessor, valueArray);
                }
            }
            break;
            case SINGLE: {
                if (valueArray.length > 1) {
                    log.warn("You have more items, and you choose relevance type \"SINGLE\".");
                }
                generateObjectStructure(parentProcessor, valueArray);
            }
            break;
            case MULTIPLE: {
                generateArrayStructure(parentProcessor, valueArray);
            }
            break;
        }
    }

    /**
     * 生成对象结构
     *
     * @param parentProcessor  上层处理器
     * @param matchItems 匹配的元素
     */
    private void generateObjectStructure(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor, Object[] matchItems) {
        if (matchItems.length > 0) {
            ContentKiteElement contentElement;
            if (KiteUtils.isArrayOrCollection(matchItems[0])) {
                contentElement = ((RelevanceKiteElement) element).createProxyArrayElement();
            } else {
                contentElement = ((RelevanceKiteElement) element).createProxyObjectElement();
            }
            JsonProcessor<? extends KiteElement, ? extends JsonNode> nextProcessor = contentElement.createJsonProcessor(jsonProcessContext, null);
            nextProcessor.value = matchItems[0];
            nextProcessor.process(parentProcessor);
        } else {
            if (!element.isNullHidden()) {
                ((ObjectNode) parentProcessor.node).putNull(element.showNameJSON());
            }
        }
    }

    /**
     * 生成数组结构
     *
     * @param parentProcessor  上层处理器
     * @param matchItems 匹配的元素
     */
    private void generateArrayStructure(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor, Object[] matchItems) {
        ContentKiteElement contentElement = ((RelevanceKiteElement) element).createProxyArrayElement();
        JsonProcessor<? extends KiteElement, ? extends JsonNode> nextProcessor = contentElement.createJsonProcessor(jsonProcessContext, ((ObjectInArrayJsonProcessor) parentProcessor).node);
        nextProcessor.value = matchItems;
        nextProcessor.process(parentProcessor);
    }

    /**
     * 内嵌转换器
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private Object[] forInnerConverter(Object[] valueArray) {
        Optional<String> innerConverterValue = ((RelevanceKiteElement) element).getInnerConverterValue();
        // 处理转换器
        if (innerConverterValue.isPresent()) {
            String converterValue = innerConverterValue.get();
            if (converterValue.startsWith("this.")) {
                // 简单表达式
                return Stream.of(valueArray).map(value -> ExpressionUtils.getValue(value, converterValue.substring(5))).toArray(Object[]::new);
            } else {
                KiteConverter converter = KiteUtils.getComponentInstance(jsonProcessContext.getDataModel(), converterValue, KiteConverter.class, "inner-converter");
                return Stream.of(valueArray).map((Function<Object, Object>) converter::convert).toArray(Object[]::new);
            }
        }
        return valueArray;
    }
}
