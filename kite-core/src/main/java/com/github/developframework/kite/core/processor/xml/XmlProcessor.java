package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.processor.Processor;
import lombok.Getter;
import lombok.Setter;
import org.dom4j.Element;


/**
 * Xml处理器基类
 * @author qiuzhenhao
 * @param <ELEMENT> 描述处理哪种节点
 * @param <NODE> JsonNode
 */
@Getter
public abstract class XmlProcessor<ELEMENT extends KiteElement, NODE extends Element> extends Processor {

    /* 处理过程上下文 */
    protected XmlProcessContext xmlProcessContext;

    /* 节点 */
    protected ELEMENT element;

    /* 节点树 */
    @Setter
    protected NODE node;

    @Setter
    protected Object value;

    public XmlProcessor(XmlProcessContext xmlProcessContext, ELEMENT element, NODE node) {
        this.xmlProcessContext = xmlProcessContext;
        this.element = element;
        this.node = node;
    }

    /**
     * 准备操作
     * @param parentProcessor 上层处理器
     * @return 是否继续执行处理逻辑
     */
    protected abstract boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor);

    /**
     * 处理核心逻辑
     * @param parentProcessor 上层处理器
     */
    protected abstract void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor);

    /**
     * 处理过程
     * @param parentProcessor 上层处理器
     */
    public final void process(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        if(prepare(parentProcessor)) {
            handleCoreLogic(parentProcessor);
        }
    }
}
