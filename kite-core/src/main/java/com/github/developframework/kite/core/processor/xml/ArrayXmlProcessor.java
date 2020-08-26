package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.element.ArrayKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.utils.KiteUtils;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

/**
 * 数组节点处理器
 *
 * @author qiuzhenhao
 */
@Slf4j
public class ArrayXmlProcessor extends ContainerXmlProcessor<ArrayKiteElement, Element> {

    public ArrayXmlProcessor(XmlProcessContext xmlProcessContext, ArrayKiteElement element) {
        super(xmlProcessContext, element);
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        Optional<Object> valueOptional = getDataValue(parentProcessor);
        if (valueOptional.isPresent()) {
            this.value = KiteUtils.objectToArray(valueOptional.get(), element);
            this.node = parentProcessor.node.addElement(showName(parentProcessor));
            return true;
        }
        if (!element.isNullHidden()) {
            parentProcessor.node.addElement(showName(parentProcessor));
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        Object[] array = (Object[]) value;
        // 处理comparator功能
        if (element.getComparatorValue() != null) {
            Comparator<Object> comparator = KiteUtils.getComponentInstance(xmlProcessContext.getDataModel(), element.getComparatorValue(), Comparator.class, "comparator");
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
                empty(KiteUtils.handleMapFunction(xmlProcessContext.getDataModel(), element.getMapFunctionValue(), array[i], i));
            } else if (element.isChildElementEmpty()) {
                empty(array[i]);
            } else {
                final ObjectInArrayXmlProcessor childProcessor = new ObjectInArrayXmlProcessor(xmlProcessContext, element.getItemObjectElement(), i, length);
                childProcessor.setValue(array[i]);
                childProcessor.process(this);
            }
        }
    }

    /**
     * 空子标签处理
     * @param itemValue 数组元素值
     */
    @SuppressWarnings("unchecked")
    private void empty(Object itemValue) {
        if (itemValue == null) {
            node.addElement(element.getXmlItemName());
            return;
        }
        node.addElement(element.getXmlItemName()).addText(itemValue.toString());
    }
}
