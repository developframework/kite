package com.github.developframework.kite.jackson;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.developframework.kite.core.AbstractProducer;
import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.KiteConfiguration;
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
public final class JacksonProducer extends AbstractProducer {

    private final ObjectMapper objectMapper;

    public JacksonProducer(KiteConfiguration configuration, DataModel dataModel, String namespace, String templateId) {
        super(configuration, dataModel, namespace, templateId);
        this.objectMapper = (ObjectMapper) configuration.getJsonFramework().getCore();
    }

    public JacksonProducer(KiteConfiguration configuration, DataModel dataModel, List<TemplatePackage> templatePackages) {
        super(configuration, dataModel, templatePackages);
        this.objectMapper = (ObjectMapper) configuration.getJsonFramework().getCore();
    }

    @Override
    protected AssembleContext buildAssembleContext() {
        return new JacksonAssembleContext(configuration);
    }


    @Override
    public String produce(boolean pretty) {
        final JsonNode node = buildJsonNode();
        if (node == null) return "";
        try {
            return pretty ? objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node) : objectMapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            throw new KiteException("构建json失败");
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
            throw new KiteException("构建json失败");
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
            rootNodeProxy = context.createArrayNodeProxy();
            context.pushValue(rootValue);
            template.getInnerArrayKiteElement().assembleArrayItems(context, rootValue, (ArrayNodeProxy) rootNodeProxy);
        } else {
            // 以对象为根
            rootNodeProxy = context.createObjectNodeProxy();
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
