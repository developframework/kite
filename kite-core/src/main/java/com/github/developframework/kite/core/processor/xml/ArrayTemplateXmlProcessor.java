package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.element.ArrayKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.Template;
import org.dom4j.Node;

/**
 * 数组模板处理器
 *
 * @author qiuzhenhao
 */
public class ArrayTemplateXmlProcessor extends ArrayXmlProcessor {


    public ArrayTemplateXmlProcessor(XmlProcessContext processContext, Template template, ArrayKiteElement arrayElement) {
        super(processContext, arrayElement, template.getDataDefinition().getExpression());
        element.copyChildElement(template);
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Node> parentProcessor) {
        // 始终为true
        return true;
    }
}
