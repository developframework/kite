package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.expression.ExpressionUtils;
import com.github.developframework.kite.core.dynamic.KiteConverter;
import com.github.developframework.kite.core.dynamic.RelFunction;
import com.github.developframework.kite.core.element.ContentKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.RelevanceKiteElement;
import com.github.developframework.kite.core.utils.KiteUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;

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
public class RelevanceXmlProcessor extends ArrayXmlProcessor {

    @Setter
    private int index;

    public RelevanceXmlProcessor(XmlProcessContext xmlProcessContext, RelevanceKiteElement element) {
        super(xmlProcessContext, element);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        index = ((ObjectInArrayXmlProcessor) parentProcessor).getIndex();
        Optional<Object> valueOptional = getDataValue(parentProcessor);
        if (valueOptional.isPresent()) {
            ObjectInArrayXmlProcessor objectInArrayProcessor = (ObjectInArrayXmlProcessor) parentProcessor;

            RelFunction relFunction = ((RelevanceKiteElement) element).getRelFunctionValue()
                    .map(relFunctionValue -> KiteUtils.getComponentInstance(xmlProcessContext.getDataModel(), relFunctionValue, RelFunction.class, "rel"))
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
            parentProcessor.getNode().addElement(showName(parentProcessor));
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
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
    private void generateObjectStructure(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor, Object[] matchItems) {
        if (matchItems.length > 0) {
            ContentKiteElement contentElement;
            if (KiteUtils.isArrayOrCollection(matchItems[0])) {
                contentElement = ((RelevanceKiteElement) element).createProxyArrayElement();
            } else {
                contentElement = ((RelevanceKiteElement) element).createProxyObjectElement();
            }
            XmlProcessor<? extends KiteElement, ? extends Element> nextProcessor = contentElement.createXmlProcessor(xmlProcessContext, null);
            nextProcessor.value = matchItems[0];
            nextProcessor.process(parentProcessor);
        } else {
            if (!element.isNullHidden()) {
                parentProcessor.getNode().addElement(showName(parentProcessor));
            }
        }
    }

    /**
     * 生成数组结构
     *
     * @param parentProcessor  上层处理器
     * @param matchItems 匹配的元素
     */
    private void generateArrayStructure(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor, Object[] matchItems) {
        ContentKiteElement contentElement = ((RelevanceKiteElement) element).createProxyArrayElement();
        XmlProcessor<? extends KiteElement, ? extends Element> nextProcessor = contentElement.createXmlProcessor(xmlProcessContext, ((ObjectInArrayXmlProcessor) parentProcessor).node);
        nextProcessor.value = matchItems;
        nextProcessor.process(parentProcessor);
    }

    /**
     * 内嵌转换器
     *
     * @param valueArray
     * @return
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
                KiteConverter converter = KiteUtils.getComponentInstance(xmlProcessContext.getDataModel(), converterValue, KiteConverter.class, "inner-converter");
                return Stream.of(valueArray).map((Function<Object, Object>) converter::convert).toArray(Object[]::new);
            }
        }
        return valueArray;
    }
}
