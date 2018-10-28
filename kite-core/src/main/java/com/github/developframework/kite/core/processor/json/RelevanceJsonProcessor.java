package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
    @SuppressWarnings("unchecked")
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        index = ((ObjectInArrayJsonProcessor) parentProcessor).getIndex();
        Optional<Object> valueOptional = getDataValue(parentProcessor);
        if (valueOptional.isPresent()) {
            ObjectInArrayJsonProcessor objectInArrayProcessor = (ObjectInArrayJsonProcessor) parentProcessor;

            RelFunction relFunction = ((RelevanceKiteElement) element).getRelFunctionValue()
                    .map(relFunctionValue -> KiteUtils.getComponentInstance(jsonProcessContext.getDataModel(), relFunctionValue, RelFunction.class, "rel"))
                    .get();
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
            ((ObjectNode) parentProcessor.getNode()).putNull(element.showNameJSON());
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        Object[] valueArray = (Object[]) value;
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
        ContentKiteElement contentElement = ((RelevanceKiteElement) element).createProxyObjectElement();
        JsonProcessor<? extends KiteElement, ? extends JsonNode> nextProcessor = contentElement.createJsonProcessor(jsonProcessContext, null);
        nextProcessor.value = matchItems[0];
        nextProcessor.process(parentProcessor);
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
}
