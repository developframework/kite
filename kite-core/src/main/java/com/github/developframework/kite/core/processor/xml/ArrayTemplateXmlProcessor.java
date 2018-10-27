package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.element.ArrayKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.Template;
import org.dom4j.Element;

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
        // 始终为true
        return true;
    }
}
