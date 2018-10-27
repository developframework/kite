package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.developframework.kite.core.element.ArrayKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.Template;

/**
 * 数组模板处理器
 *
 * @author qiuzhenhao
 */
public class ArrayTemplateJsonProcessor extends ArrayJsonProcessor {


    public ArrayTemplateJsonProcessor(JsonProcessContext processContext, Template template, ArrayKiteElement arrayElement) {
        super(processContext, arrayElement);
        element.copyChildElement(template);
    }

    @Override
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        // 始终为true
        return true;
    }
}
