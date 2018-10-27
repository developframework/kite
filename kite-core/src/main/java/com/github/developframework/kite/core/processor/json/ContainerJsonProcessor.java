package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.developframework.kite.core.element.ContainerKiteElement;

/**
 * 容器节点处理器
 *
 * @author qiuzhenhao
 */
public abstract class ContainerJsonProcessor<ELEMENT extends ContainerKiteElement, NODE extends JsonNode> extends ContentJsonProcessor<ELEMENT, NODE> {

    public ContainerJsonProcessor(JsonProcessContext jsonProcessContext, ELEMENT element) {
        super(jsonProcessContext, element);
    }
}
