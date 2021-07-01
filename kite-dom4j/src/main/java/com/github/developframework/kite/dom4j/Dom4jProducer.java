package com.github.developframework.kite.dom4j;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.Producer;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.element.Template;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.node.ObjectNodeProxy;
import com.github.developframework.kite.core.utils.KiteUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Dom4j的xml生成器
 *
 * @author qiushui on 2021-06-23.
 */
public class Dom4jProducer implements Producer {

    private final KiteConfiguration configuration;

    private final String namespace;

    private final String templateId;

    private final AssembleContext context;

    public Dom4jProducer(KiteConfiguration configuration, DataModel dataModel, String namespace, String templateId) {
        this.configuration = configuration;
        this.namespace = namespace;
        this.templateId = templateId;
        this.context = new AssembleContext(configuration, false);
        this.context.dataModel = dataModel;
    }


    @Override
    public String produce(boolean pretty) {
        final Document document = buildXmlDocument();
        if (document == null) return "";
        try {
            OutputFormat format = OutputFormat.createCompactFormat();
            format.setSuppressDeclaration(configuration.getOptions().getXml().isSuppressDeclaration());
            format.setEncoding(StandardCharsets.UTF_8.name());
            format.setIndent(pretty);
            format.setNewlines(pretty);
            Writer writer = new StringWriter();
            XMLWriter xmlWriter = new XMLWriter(writer, format);
            xmlWriter.setEscapeText(true);
            xmlWriter.write(document);
            xmlWriter.close();
            return writer.toString();
        } catch (IOException e) {
            throw new KiteException("produce xml string failed: %s", e.getMessage());
        }
    }

    @Override
    public void output(OutputStream outputStream, Charset charset, boolean pretty) {
        final Document document = buildXmlDocument();
        if (document == null) return;
        try {
            OutputFormat format = OutputFormat.createCompactFormat();
            format.setSuppressDeclaration(configuration.getOptions().getXml().isSuppressDeclaration());
            format.setEncoding(charset == null ? StandardCharsets.UTF_8.name() : charset.name());
            format.setIndent(pretty);
            format.setNewlines(pretty);
            Writer writer = new OutputStreamWriter(outputStream);
            XMLWriter xmlWriter = new XMLWriter(writer, format);
            xmlWriter.setEscapeText(true);
            xmlWriter.write(document);
            xmlWriter.close();
        } catch (IOException e) {
            throw new KiteException("produce xml string failed: %s", e.getMessage());
        }
    }

    private Document buildXmlDocument() {
        final Template template = configuration.getTemplatePackageRegistry().extractTemplate(namespace, templateId);
        final DataDefinition dataDefinition = template.getContentAttributes().dataDefinition;
        Object rootValue = null;
        if (dataDefinition != DataDefinition.EMPTY) {
            Optional<Object> rootValueOptional = context.dataModel.getData(dataDefinition.getExpression());
            if (rootValueOptional.isEmpty()) {
                return null;
            } else {
                rootValue = rootValueOptional.get();
            }
        }
        final Document document = DocumentHelper.createDocument();
        final ObjectNodeProxy rootNodeProxy = new Dom4jObjectNodeProxy(document.addElement(template.getXmlRoot()));
        context.pushValue(context.dataModel);
        if (KiteUtils.objectIsArray(rootValue)) {
            // 以数组为根
            Dom4jArrayNodeProxy arrayNodeProxy = new Dom4jArrayNodeProxy((Element) rootNodeProxy.getNode());
            context.pushValue(rootValue);
            template.getInnerArrayKiteElement().assembleArrayItems(context, rootValue, arrayNodeProxy);
        } else {
            // 以对象为根
            context.pushNodeProxy(rootNodeProxy);
            template.assemble(context);
        }
        return document;
    }
}
