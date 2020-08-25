package com.github.developframework.kite.core.saxparser;

import org.xml.sax.Attributes;

/**
 * xml节点解析器基接口
 *
 * @author qiuzhenhao on 2017-05-07
 */
interface ElementSaxParser {

    /**
     * 获得qName
     * @return qName
     */
    String qName();

    /**
     * SAX开始节点时的处理操作
     * @param parseContext 上下文
     * @param attributes 属性
     */
    void handleAtStartElement(ParseContext parseContext, Attributes attributes);

    /**
     * SAX结束节点时的处理操作
     * @param parseContext 上下文
     */
    void handleAtEndElement(ParseContext parseContext);
}
