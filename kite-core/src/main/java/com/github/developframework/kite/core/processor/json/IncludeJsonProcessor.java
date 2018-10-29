package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.element.IncludeKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.Template;

import java.util.Iterator;

/**
 * 包含功能节点处理器
 * @author qiuzhenhao
 */
public class IncludeJsonProcessor extends FunctionalJsonProcessor<IncludeKiteElement, ObjectNode>{
    public IncludeJsonProcessor(JsonProcessContext context, IncludeKiteElement element, ObjectNode node) {
        super(context, element, node);
    }

    @Override
    protected void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        KiteConfiguration kiteConfiguration = jsonProcessContext.getConfiguration();
        Template template = kiteConfiguration.extractTemplate(element.getTargetTemplateLocation().getNamespace(), element.getTargetTemplateLocation().getTemplateId());
        for (Iterator<KiteElement> iterator = template.childElementIterator(); iterator.hasNext();) {
            final KiteElement childElement = iterator.next();
            final JsonProcessor<? extends KiteElement, ? extends JsonNode> childProcessor = childElement.createJsonProcessor(jsonProcessContext, node);
            childProcessor.process(parentProcessor);
        }
    }
}
