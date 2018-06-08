package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.expression.Expression;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.element.IncludeKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.Template;
import org.dom4j.Node;

import java.util.Iterator;

/**
 * 包含功能节点处理器
 * @author qiuzhenhao
 */
public class IncludeXmlProcessor extends FunctionalXmlProcessor<IncludeKiteElement, Node>{
    public IncludeXmlProcessor(XmlProcessContext context, IncludeKiteElement element, Node node, Expression parentExpression) {
        super(context, element, node, parentExpression);
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Node> parentProcessor) {
        KiteConfiguration kiteConfiguration = xmlProcessContext.getConfiguration();
        Template template = kiteConfiguration.extractTemplate(element.getTargetNamespace(), element.getTargetTemplateId());
        for (Iterator<KiteElement> iterator = template.childElementIterator(); iterator.hasNext();) {
            final KiteElement childElement = iterator.next();
            final XmlProcessor<? extends KiteElement, ? extends Node> childProcessor = childElement.createXmlProcessor(xmlProcessContext, node, expression);
            childProcessor.process(parentProcessor);
        }
    }
}
