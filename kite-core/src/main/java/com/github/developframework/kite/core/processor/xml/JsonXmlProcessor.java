package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.element.JsonKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import org.dom4j.Element;

/**
 * json xml节点处理器
 *
 * @author qiushui on 2018-12-28.
 */
public class JsonXmlProcessor extends ContentXmlProcessor<JsonKiteElement, Element> {

    public JsonXmlProcessor(XmlProcessContext xmlProcessContext, JsonKiteElement element) {
        super(xmlProcessContext, element);
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        // 不处理
    }
}
