package com.github.developframework.kite.core;

import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.element.ArrayKiteElement;
import com.github.developframework.kite.core.element.Template;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.processor.xml.ArrayTemplateXmlProcessor;
import com.github.developframework.kite.core.processor.xml.ArrayXmlProcessor;
import com.github.developframework.kite.core.processor.xml.TemplateXmlProcessor;
import com.github.developframework.kite.core.processor.xml.XmlProcessContext;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 默认的Json生成器
 *
 * @author qiuzhenhao
 */
class DefaultXmlProducer implements XmlProducer {

    private KiteConfiguration kiteConfiguration;

    DefaultXmlProducer(KiteConfiguration kiteConfiguration) {
        this.kiteConfiguration = kiteConfiguration;
    }

    @Override
    public String produce(DataModel dataModel, String namespace, String templateId) {
        return produce(dataModel, namespace, templateId, false);
    }

    @Override
    public String produce(DataModel dataModel, String namespace, String templateId, boolean isPretty) {
        Document document = constructDocument(dataModel, namespace, templateId);
        try {
            OutputFormat format = new OutputFormat();
            format.setIndent(isPretty);
            format.setNewlines(isPretty);
            format.setSuppressDeclaration(kiteConfiguration.isXmlSuppressDeclaration());
            Writer writer = new StringWriter();
            XMLWriter xmlWriter = new XMLWriter(writer, format);
            xmlWriter.write(document);
            xmlWriter.close();
            return writer.toString();
        } catch (IOException e) {
            throw new KiteException("produce xml string failed.");
        }
    }

    @Override
    public void outputXml(Writer writer, DataModel dataModel, String namespace, String templateId, boolean isPretty) {
        Document document = constructDocument(dataModel, namespace, templateId);
        try {
            OutputFormat format = new OutputFormat();
            format.setIndent(isPretty);
            format.setNewlines(isPretty);
            format.setSuppressDeclaration(kiteConfiguration.isXmlSuppressDeclaration());
            XMLWriter xmlWriter = new XMLWriter(writer, format);
            xmlWriter.write(document);
            xmlWriter.close();
        } catch (IOException e) {
            throw new KiteException("produce xml string failed.");
        }
    }

    private Document constructDocument(DataModel dataModel, String namespace, String templateId) {
        Template template = kiteConfiguration.extractTemplate(namespace, templateId);
        XmlProcessContext xmlProcessContext = new XmlProcessContext();
        xmlProcessContext.setDataModel(dataModel);
        xmlProcessContext.setConfiguration(kiteConfiguration);

        DataDefinition templateDataDefinition = template.getDataDefinition();

        if (templateDataDefinition != DataDefinition.EMPTY_DATA_DEFINITION) {
            Optional<Object> rootObjectOptional = dataModel.getData(templateDataDefinition.getExpression());
            if (rootObjectOptional.isPresent()) {
                Object rootObject = rootObjectOptional.get();
                if (rootObject.getClass().isArray() || rootObject instanceof List || rootObject instanceof Set) {
                    // 视为数组模板
                    return constructRootArrayNodeTree(xmlProcessContext, template, rootObject);
                } else {
                    // 视为对象模板
                    return constructRootObjectNodeTree(xmlProcessContext, template, rootObject);
                }
            } else {
                throw new KiteException("Root data must not null.");
            }
        } else {
            // 视为对象模板
            return constructRootObjectNodeTree(xmlProcessContext, template, null);
        }
    }

    private Document constructRootObjectNodeTree(XmlProcessContext xmlProcessContext, Template template, Object value) {
        Document document = DocumentHelper.createDocument();
        String xmlRootName = template.getXmlRootName();
        String objNodeName = kiteConfiguration.getForXmlStrategy().propertyShowName(kiteConfiguration, template.getDataDefinition().getExpression().toString());
        Element objNode;
        if(StringUtils.isBlank(xmlRootName)) {
            if (StringUtils.isBlank(objNodeName)) {
                throw new KiteException("\"data\" or \"xml-root\" is undefined in template \"%s : %s\".", template.getNamespace(), template.getTemplateId());
            } else {
                objNode = document.addElement(objNodeName);
            }
        } else {
            Element rootNode = document.addElement(xmlRootName);
            if(template.getDataDefinition() != DataDefinition.EMPTY_DATA_DEFINITION) {
                objNode = rootNode.addElement(objNodeName);
            } else {
                objNode = rootNode;
            }
        }
        TemplateXmlProcessor templateProcessor = new TemplateXmlProcessor(xmlProcessContext, template);
        templateProcessor.setValue(value);
        templateProcessor.setNode(objNode);
        templateProcessor.process(null);
        return document;
    }

    private Document constructRootArrayNodeTree(XmlProcessContext xmlProcessContext, Template template, Object value) {
        Document document = DocumentHelper.createDocument();
        String xmlRootName = template.getXmlRootName();
        String arrayNodeName = kiteConfiguration.getForXmlStrategy().propertyShowName(kiteConfiguration, template.getDataDefinition().getExpression().toString());
        Element arrayNode;
        if(StringUtils.isBlank(xmlRootName)) {
            if (StringUtils.isBlank(arrayNodeName)) {
                throw new KiteException("\"data\" or \"xml-root\" is undefined in template \"%s : %s\".", template.getNamespace(), template.getTemplateId());
            } else {
                arrayNode = document.addElement(arrayNodeName);
            }
        } else {
            Element rootNode = document.addElement(xmlRootName);
            arrayNode = rootNode.addElement(arrayNodeName);
        }
        ArrayKiteElement arrayElement = new ArrayKiteElement(kiteConfiguration, template.getNamespace(), template.getTemplateId(), template.getDataDefinition(), null);
        arrayElement.setXmlItemName(template.getXmlItemName());
        arrayElement.setMapFunctionValue(template.getMapFunctionValue());
        ArrayXmlProcessor arrayProcessor = new ArrayTemplateXmlProcessor(xmlProcessContext, template, arrayElement);
        arrayProcessor.setValue(value);
        arrayProcessor.setNode(arrayNode);
        arrayProcessor.process(null);
        return document;
    }
}
