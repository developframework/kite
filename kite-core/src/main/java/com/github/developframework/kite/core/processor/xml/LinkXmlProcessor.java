package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.element.ContentKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.LinkKiteElement;
import com.github.developframework.kite.core.exception.LinkSizeNotEqualException;
import com.github.developframework.kite.core.utils.KiteUtils;
import lombok.Setter;
import org.dom4j.Element;

import java.util.Optional;

/**
 * 一对一链接节点处理器
 *
 * @author qiuzhenhao
 */
public class LinkXmlProcessor extends ObjectXmlProcessor {

    @Setter
    private int index;

    public LinkXmlProcessor(XmlProcessContext xmlProcessContext, LinkKiteElement element) {
        super(xmlProcessContext, element);
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        index = ((ObjectInArrayXmlProcessor) parentProcessor).getIndex();
        Optional<Object> valueOptional = getDataValue(parentProcessor);
        if (valueOptional.isPresent()) {
            Object[] array = KiteUtils.objectToArray(valueOptional.get(), element);
            if (array.length != ((ObjectInArrayXmlProcessor) parentProcessor).getSize()) {
                throw new LinkSizeNotEqualException(element.getTemplateLocation());
            }
            value = array[index];
            return true;
        }
        if (!element.isNullHidden()) {
            node.addElement(showName(parentProcessor));
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        ContentKiteElement contentElement = ((LinkKiteElement) element).createProxyContentElement(value.getClass());
        XmlProcessor<? extends KiteElement, ? extends Element> nextProcessor = contentElement.createXmlProcessor(xmlProcessContext, ((ObjectInArrayXmlProcessor) parentProcessor).node);
        nextProcessor.setValue(value);
        nextProcessor.process(parentProcessor);
    }
}
