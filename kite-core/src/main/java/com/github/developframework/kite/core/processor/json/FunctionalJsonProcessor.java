package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.developframework.kite.core.element.FunctionalKiteElement;
import com.github.developframework.kite.core.element.KiteElement;

/**
 * 功能型节点处理器
 * @author qiuzhenhao
 */
public abstract class FunctionalJsonProcessor<ELEMENT extends FunctionalKiteElement, NODE extends JsonNode> extends JsonProcessor<ELEMENT, NODE> {


    public FunctionalJsonProcessor(JsonProcessContext jsonProcessContext, ELEMENT element, NODE node) {
        super(jsonProcessContext, element, node);
    }

    @Override
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        return true;
    }

}
