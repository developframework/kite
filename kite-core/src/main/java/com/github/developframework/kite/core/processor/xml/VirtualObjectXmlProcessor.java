package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.ObjectKiteElement;
import org.dom4j.Element;

/**
 * 虚拟对象节点处理器
 *
 * @author qiuzhenhao
 */
public class VirtualObjectXmlProcessor extends ObjectXmlProcessor {

    public VirtualObjectXmlProcessor(XmlProcessContext context, ObjectKiteElement element) {
        super(context, element);
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        this.node = parentProcessor.getNode().addElement(element.showNameXML());
        // 始终为true
        return true;
    }
}
