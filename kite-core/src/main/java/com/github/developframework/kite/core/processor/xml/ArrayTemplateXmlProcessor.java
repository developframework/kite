package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.element.ArrayKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.Template;
import com.github.developframework.kite.core.utils.KiteUtils;
import org.dom4j.Element;

import java.util.Optional;

/**
 * 数组模板处理器
 *
 * @author qiuzhenhao
 */
public class ArrayTemplateXmlProcessor extends ArrayXmlProcessor {


    public ArrayTemplateXmlProcessor(XmlProcessContext processContext, Template template, ArrayKiteElement arrayElement) {
        super(processContext, arrayElement);
        element.copyChildElement(template);
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        Optional<Object> valueOptional = getDataValue(parentProcessor);
        if (valueOptional.isPresent()) {
            this.value = KiteUtils.objectToArray(valueOptional.get(), element);
            return true;
        }
        return false;
    }
}
