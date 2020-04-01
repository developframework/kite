package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.element.DuplicateTemplateKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.Template;
import org.dom4j.Element;

import java.util.Optional;

/**
 * 模板处理器
 * @author qiuzhenhao
 */
public class TemplateXmlProcessor extends ObjectXmlProcessor {

    public TemplateXmlProcessor(XmlProcessContext xmlProcessContext, Template template) {
        super(xmlProcessContext, template);
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        // 始终为true
        return true;
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        Optional<Template.Extend> extendOptional = ((Template) element).getExtend();
        if (extendOptional.isPresent()) {
            Template.Extend extend = extendOptional.get();
            // 提取模板
            Template extendTemplate = xmlProcessContext.getConfiguration().extractTemplate(extend.getExtendTemplateLocation());
            // 定义扩展口回调接口实现
            final ExtendPortXmlProcessor.ExtendCallback callback = parentProcessorInCallback -> {
                // 复制一个副本节点进行回调处理
                DuplicateTemplateKiteElement duplicateTemplateElement = ((Template) this.element).createDuplicateTemplateKiteElement();
                XmlProcessor<? extends KiteElement, ? extends Element> processor = duplicateTemplateElement.createXmlProcessor(xmlProcessContext, parentProcessorInCallback.node);
                processor.value = parentProcessorInCallback.value;
                processor.process(parentProcessorInCallback);
            };
            xmlProcessContext.pushExtendCallback(extend.getPort(), callback);
            XmlProcessor<? extends KiteElement, ? extends Element> extendTemplateXmlProcessor = extendTemplate.createXmlProcessor(xmlProcessContext, node);
            extendTemplateXmlProcessor.process(parentProcessor);
        } else {
            super.handleCoreLogic(parentProcessor);
        }
    }
}
