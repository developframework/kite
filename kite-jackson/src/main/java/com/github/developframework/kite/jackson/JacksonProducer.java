package com.github.developframework.kite.jackson;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.Producer;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.element.Template;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.node.ArrayNodeProxy;
import com.github.developframework.kite.core.node.NodeProxy;
import com.github.developframework.kite.core.node.ObjectNodeProxy;
import com.github.developframework.kite.core.structs.TemplatePackage;
import com.github.developframework.kite.core.utils.KiteUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

/**
 * Jackson的json生成器
 *
 * @author qiushui on 2021-06-23.
 */
public class JacksonProducer implements Producer {

    private final KiteConfiguration configuration;

    private final String namespace;

    private final String templateId;

    private final AssembleContext context;

    private final ObjectMapper objectMapper;

    public JacksonProducer(KiteConfiguration configuration, DataModel dataModel, String namespace, String templateId) {
        this.configuration = configuration;
        this.objectMapper = (ObjectMapper) configuration.getJsonFramework().getCore();
        this.context = new AssembleContext(configuration, true);
        this.context.dataModel = dataModel;
        this.namespace = namespace;
        this.templateId = templateId;
    }

    public JacksonProducer(KiteConfiguration configuration, DataModel dataModel, List<TemplatePackage> templatePackages) {
        this.configuration = configuration;
        this.objectMapper = (ObjectMapper) configuration.getJsonFramework().getCore();
        this.context = new AssembleContext(configuration, true);
        this.context.dataModel = dataModel;
        templatePackages.forEach(this.context.extraTemplatePackages::putTemplatePackage);
        final TemplatePackage templatePackage = templatePackages.get(0);
        this.namespace = templatePackage.getNamespace();
        this.templateId = templatePackage.getUniqueTemplate().getId();
    }


    @Override
    public String produce(boolean pretty) {
        final JsonNode node = buildJsonNode();
        if (node == null) return "";
        try {
            return pretty ? objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node) : objectMapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            throw new KiteException("produce json string failed.");
        }
    }

    @Override
    public void output(OutputStream outputStream, Charset charset, boolean pretty) {
        final JsonNode node = buildJsonNode();
        if (node == null) return;
        final ObjectWriter writer = pretty ? objectMapper.writerWithDefaultPrettyPrinter() : objectMapper.writer();
        try {
            final JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, getJsonEncoding(charset));
            writer.writeValue(generator, node);
        } catch (IOException e) {
            throw new KiteException("produce json string failed.");
        }
    }

    private JsonNode buildJsonNode() {
        final Template template = context.extractTemplate(namespace, templateId);
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
        final NodeProxy rootNodeProxy;
        context.pushValue(context.dataModel);
        if (KiteUtils.objectIsArray(rootValue)) {
            // 以数组为根
            rootNodeProxy = new JacksonArrayNodeProxy(objectMapper.createArrayNode());
            context.pushValue(rootValue);
            template.getInnerArrayKiteElement().assembleArrayItems(context, rootValue, (ArrayNodeProxy) rootNodeProxy);
        } else {
            // 以对象为根
            rootNodeProxy = new JacksonObjectNodeProxy(objectMapper.createObjectNode());
            context.pushNodeProxy((ObjectNodeProxy) rootNodeProxy);
            template.assemble(context);
        }
        return (JsonNode) rootNodeProxy.getNode();
    }

    private JsonEncoding getJsonEncoding(Charset charset) {
        if (charset != null) {
            for (JsonEncoding encoding : JsonEncoding.values()) {
                if (charset.name().equals(encoding.getJavaName())) {
                    return encoding;
                }
            }
        }
        return JsonEncoding.UTF8;
    }
}
