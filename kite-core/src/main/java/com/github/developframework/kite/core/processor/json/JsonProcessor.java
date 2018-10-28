package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.processor.Processor;
import lombok.Getter;
import lombok.Setter;

/**
 * Json处理器基类
 * @author qiuzhenhao
 * @param <ELEMENT> 描述处理哪种节点
 * @param <NODE> JsonNode
 */
@Getter
public abstract class JsonProcessor<ELEMENT extends KiteElement, NODE extends JsonNode> implements Processor {

    /* 处理过程上下文 */
    protected JsonProcessContext jsonProcessContext;

    /* 节点 */
    protected ELEMENT element;

    /* 节点树 */
    @Setter
    protected NODE node;

    @Setter
    protected Object value;

    public JsonProcessor(JsonProcessContext jsonProcessContext, ELEMENT element, NODE node) {
        this.jsonProcessContext = jsonProcessContext;
        this.element = element;
        this.node = node;
    }

    /**
     * 准备操作
     * @param parentProcessor 上层处理器
     * @return 是否继续执行处理逻辑
     */
    protected abstract boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor);

    /**
     * 处理核心逻辑
     * @param parentProcessor 上层处理器
     */
    protected abstract void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor);

    /**
     * 处理过程
     * @param parentProcessor 上层处理器
     */
    public final void process(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        if(prepare(parentProcessor)) {
            handleCoreLogic(parentProcessor);
        }
    }
}
