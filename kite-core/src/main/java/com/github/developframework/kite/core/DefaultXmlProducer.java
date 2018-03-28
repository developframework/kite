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
            format.setSuppressDeclaration(true);
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
            format.setSuppressDeclaration(true);
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

        if (templateDataDefinition != null) {
            Optional<Object> rootObjectOptional = dataModel.getData(templateDataDefinition.getExpression());
            if (rootObjectOptional.isPresent()) {
                Object rootObject = rootObjectOptional.get();
                if (rootObject.getClass().isArray() || rootObject instanceof List) {
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
        Element rootNode = document.addElement(template.getXmlRootName());
        TemplateXmlProcessor templateProcessor = new TemplateXmlProcessor(xmlProcessContext, template, template.getDataDefinition().getExpression());
        templateProcessor.setValue(value);
        templateProcessor.setNode(rootNode);
        templateProcessor.process(null);
        return document;
    }

    private Document constructRootArrayNodeTree(XmlProcessContext xmlProcessContext, Template template, Object value) {
        Document document = DocumentHelper.createDocument();
        Element rootNode = document.addElement(template.getXmlRootName());
        Element arrayNode = rootNode.addElement(template.getDataDefinition().getExpression().toString());
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
