package com.github.developframework.kite.core.element;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.JsonProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import com.github.developframework.kite.core.processor.xml.XmlProcessor;
import lombok.Getter;
import org.dom4j.Element;

/**
 * 节点顶级基类
 * @author qiuzhenhao
 */
@Getter
public abstract class KiteElement {

    /* 配置 */
    protected KiteConfiguration configuration;
    /* 所在命名空间 */
    protected String namespace;
    /* 所在模板 */
    protected String templateId;

    public KiteElement(KiteConfiguration configuration, String namespace, String templateId) {
        this.configuration = configuration;
        this.namespace = namespace;
        this.templateId = templateId;
    }

    /**
     * 创建处理该节点的Json处理器
     * @param jsonProcessContext 处理过程上下文
     * @param parentNode 父json树节点
     * @return 处理器
     */
    public abstract JsonProcessor<? extends KiteElement, ? extends JsonNode> createJsonProcessor(JsonProcessContext jsonProcessContext, ObjectNode parentNode);


    /**
     * 创建处理该节点的Xml处理器
     * @param xmlProcessContext 处理过程上下文
     * @param parentNode 父json树节点
     * @return 处理器
     */
    public abstract XmlProcessor<? extends KiteElement, ? extends Element> createXmlProcessor(XmlProcessContext xmlProcessContext, Element parentNode);
}
