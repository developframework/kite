package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.element.IncludeKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.Template;
import org.dom4j.Element;

import java.util.Iterator;

/**
 * 包含功能节点处理器
 * @author qiuzhenhao
 */
public class IncludeXmlProcessor extends FunctionalXmlProcessor<IncludeKiteElement, Element> {
    public IncludeXmlProcessor(XmlProcessContext context, IncludeKiteElement element, Element node) {
        super(context, element, node);
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        KiteConfiguration kiteConfiguration = xmlProcessContext.getConfiguration();
        Template template = kiteConfiguration.extractTemplate(element.getTargetTemplateLocation());
        for (Iterator<KiteElement> iterator = template.childElementIterator(); iterator.hasNext();) {
            final KiteElement childElement = iterator.next();
            final XmlProcessor<? extends KiteElement, ? extends Element> childProcessor = childElement.createXmlProcessor(xmlProcessContext, node);
            childProcessor.process(parentProcessor);
        }
    }
}
