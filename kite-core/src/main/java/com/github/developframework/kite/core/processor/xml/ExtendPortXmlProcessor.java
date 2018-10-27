package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.element.ExtendPortKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import org.dom4j.Element;

/**
 * 扩展端口处理器
 * @author qiuzhenhao
 */
public class ExtendPortXmlProcessor extends FunctionalXmlProcessor<ExtendPortKiteElement, Element> {

    public ExtendPortXmlProcessor(XmlProcessContext xmlProcessContext, ExtendPortKiteElement element, Element node) {
        super(xmlProcessContext, element, node);
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        xmlProcessContext.getExtendCallback(element.getPortName()).ifPresent(extendCallback -> extendCallback.call(parentProcessor));
    }

    /**
     * 扩展口回调接口
     *
     * @author qiuzhenhao
     */
    public interface ExtendCallback {

        void call(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor);
    }
}
