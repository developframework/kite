package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.element.ArrayKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.ObjectKiteElement;
import lombok.Getter;
import org.dom4j.Element;

/**
 * 数组元素处理器
 *
 * @author qiuzhenhao
 */
@Getter
public class ObjectInArrayXmlProcessor extends ObjectXmlProcessor {

    protected int index;

    protected int size;

    public ObjectInArrayXmlProcessor(XmlProcessContext context, ObjectKiteElement element, int index, int size) {
        super(context, element);
        this.index = index;
        this.size = size;
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        if (value != null) {
            this.node = parentProcessor.node.addElement(((ArrayKiteElement) (parentProcessor.element)).getXmlItemName());
            return true;
        }
        return false;
    }
}
