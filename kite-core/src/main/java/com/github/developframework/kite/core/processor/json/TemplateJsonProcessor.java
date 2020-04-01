package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.element.DuplicateTemplateKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.Template;

import java.util.Optional;

/**
 * 模板处理器
 *
 * @author qiuzhenhao
 */
public class TemplateJsonProcessor extends ObjectJsonProcessor {

    public TemplateJsonProcessor(JsonProcessContext jsonProcessContext, Template template) {
        super(jsonProcessContext, template);
    }

    @Override
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        // 始终为true
        return true;
    }

    @Override
    protected void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        Optional<Template.Extend> extendOptional = ((Template) element).getExtend();
        if (extendOptional.isPresent()) {
            Template.Extend extend = extendOptional.get();
            // 提取模板
            Template extendTemplate = jsonProcessContext.getConfiguration().extractTemplate(extend.getExtendTemplateLocation());
            // 定义扩展口回调接口实现
            final ExtendPortJsonProcessor.ExtendCallback callback = parentProcessorInCallback -> {
                // 复制一个副本节点进行回调处理
                DuplicateTemplateKiteElement duplicateTemplateElement = ((Template) this.element).createDuplicateTemplateKiteElement();
                JsonProcessor<? extends KiteElement, ? extends JsonNode> processor = duplicateTemplateElement.createJsonProcessor(jsonProcessContext, (ObjectNode) parentProcessorInCallback.node);
                processor.value = parentProcessorInCallback.value;
                processor.process(parentProcessorInCallback);
            };
            jsonProcessContext.pushExtendCallback(extend.getPort(), callback);
            JsonProcessor<? extends KiteElement, ? extends JsonNode> nextProcessor = extendTemplate.createJsonProcessor(jsonProcessContext, node);
            nextProcessor.process(parentProcessor);
        } else {
            super.handleCoreLogic(parentProcessor);
        }
    }
}
