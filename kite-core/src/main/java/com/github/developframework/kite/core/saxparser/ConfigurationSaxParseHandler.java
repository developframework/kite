package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置解析Handler
 * @author qiuzhenhao
 */
class ConfigurationSaxParseHandler extends DefaultHandler{

    /* 解析器链 */
    private List<ElementSaxParser> elementSaxParserChain;
    /* 上下文 */
    private ParseContext parseContext;
    /* 配置 */
    private KiteConfiguration configuration;

    public ConfigurationSaxParseHandler(KiteConfiguration configuration) {
        this.configuration = configuration;
        this.elementSaxParserChain = new ArrayList<>(22);
        this.parseContext = new ParseContext(configuration);
        registerDefaultElementSaxParser();
    }

    /**
     * 注册默认的解析器
     */
    private void registerDefaultElementSaxParser() {
        registerElementSaxParser(new PropertyElementSaxParser(configuration));
        registerElementSaxParser(new DatePropertyElementSaxParser(configuration));
        registerElementSaxParser(new ObjectElementSaxParser(configuration));
        registerElementSaxParser(new ArrayElementSaxParser(configuration));
        registerElementSaxParser(new IncludeElementSaxParser(configuration));
        registerElementSaxParser(new VirtualObjectElementSaxParser(configuration));
        registerElementSaxParser(new UnixTimestampPropertyElementSaxParser(configuration));
        registerElementSaxParser(new BooleanPropertyElementSaxParser(configuration));
        registerElementSaxParser(new XmlAttributeElementSaxParser(configuration));
        registerElementSaxParser(new ExtendPortElementSaxParser(configuration));
        registerElementSaxParser(new IgnorePropertyElementSaxParser(configuration));
        registerElementSaxParser(new ThisElementSaxParser(configuration));
        registerElementSaxParser(new JsonElementSaxParser(configuration));
        registerElementSaxParser(new PrototypeElementSaxParser(configuration));
        registerElementSaxParser(new IfElementSaxParser(configuration));
        registerElementSaxParser(new ElseElementSaxParser(configuration));
        registerElementSaxParser(new CaseElementSaxParser(configuration));
        registerElementSaxParser(new DefaultCaseElementSaxParser(configuration));
        registerElementSaxParser(new SwitchElementSaxParser(configuration));
        registerElementSaxParser(new RelevanceElementParser(configuration));
        registerElementSaxParser(new LinkElementSaxParser(configuration));
        registerElementSaxParser(new TemplateElementSaxParser(configuration));
        registerElementSaxParser(new TemplatePackageElementSaxParser(configuration));
    }

    /**
     * 注册解析器
     * @param parser 解析器
     */
    private void registerElementSaxParser(ElementSaxParser parser) {
        elementSaxParserChain.add(parser);
    }

    /**
     * SAX开始文档处理
     * @throws SAXException
     */
    @Override
    public void startDocument() throws SAXException {
        parseContext.getStack().clear();
    }

    /**
     * SAX开始节点处理
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     * @throws SAXException
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        for (ElementSaxParser parser : elementSaxParserChain) {
            if (parser.qName().equals(qName)) {
                parser.handleAtStartElement(parseContext, attributes);
            }
        }
    }

    /**
     * SAX结束节点处理
     * @param uri
     * @param localName
     * @param qName
     * @throws SAXException
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        for (ElementSaxParser parser : elementSaxParserChain) {
            if (parser.qName().equals(qName)) {
                parser.handleAtEndElement(parseContext);
            }
        }
    }
}
