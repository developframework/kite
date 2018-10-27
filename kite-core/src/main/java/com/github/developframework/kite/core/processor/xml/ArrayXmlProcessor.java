package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.dynamic.MapFunction;
import com.github.developframework.kite.core.element.ArrayKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.exception.InvalidArgumentsException;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.utils.KiteUtils;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Element;

import java.util.Optional;

/**
 * 数组节点处理器
 *
 * @author qiuzhenhao
 */
@Slf4j
public class ArrayXmlProcessor extends ContainerXmlProcessor<ArrayKiteElement, Element> {

    private Optional<MapFunction> mapFunctionOptional;

    public ArrayXmlProcessor(XmlProcessContext xmlProcessContext, ArrayKiteElement element) {
        super(xmlProcessContext, element);
        this.mapFunctionOptional = mapFunction(element.getMapFunctionValueOptional(), xmlProcessContext.getDataModel());
        if (mapFunctionOptional.isPresent()) {
            if (!element.isChildElementEmpty()) {
                log.warn("The child element invalid, because you use \"map-function\" attribute.");
            }
        }
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        Optional<Object> valueOptional = getDataValue(parentProcessor);
        if (valueOptional.isPresent()) {
            this.value = KiteUtils.objectToArray(valueOptional.get(), element);
            this.node = parentProcessor.getNode().addElement(element.showNameXML());
            return true;
        }
        if (!element.isNullHidden()) {
            parentProcessor.getNode().addElement(element.showNameXML());
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        Object[] array = (Object[]) value;
        for (int i = 0; i < array.length; i++) {
            if (element.isChildElementEmpty() || mapFunctionOptional.isPresent()) {
                empty(array[i], i);
            } else {
                final ObjectInArrayXmlProcessor childProcessor = new ObjectInArrayXmlProcessor(xmlProcessContext, element.getItemObjectElement(), i, array.length);
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
    private void empty(Object itemValue, int index) {
        if (itemValue == null) {
            node.addElement(element.getXmlItemName());
            return;
        }

        if (mapFunctionOptional.isPresent()) {
            itemValue = mapFunctionOptional.get().apply(itemValue, index);
        }

        node.addElement(element.getXmlItemName()).addText(itemValue.toString());
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
