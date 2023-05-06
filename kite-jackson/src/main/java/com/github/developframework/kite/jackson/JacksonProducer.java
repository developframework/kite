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
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.node.NodeProxy;
import com.github.developframework.kite.core.structs.TemplatePackage;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

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
    protected AssembleContext buildAssembleContext(DataModel dataModel) {
        return new JacksonAssembleContext(configuration, dataModel);
    }


    @Override
    public String produce(boolean pretty) {
        final NodeProxy rootNodeProxy = buildRootNodeProxy();
        if (rootNodeProxy == null) {
            return "";
        } else {
            final JsonNode rootNode = (JsonNode) rootNodeProxy.getNode();
            try {
                return pretty ? objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode) : objectMapper.writeValueAsString(rootNode);
            } catch (JsonProcessingException e) {
                throw new KiteException("produce json failed");
            }
        }
    }

    @Override
    public void output(OutputStream outputStream, Charset charset, boolean pretty) {
        final NodeProxy rootNodeProxy = buildRootNodeProxy();
        if (rootNodeProxy != null) {
            final ObjectWriter writer = pretty ? objectMapper.writerWithDefaultPrettyPrinter() : objectMapper.writer();
            try {
                final JsonGenerator generator = objectMapper.getFactory().createGenerator(outputStream, getJsonEncoding(charset));
                writer.writeValue(generator, rootNodeProxy.getNode());
            } catch (IOException e) {
                throw new KiteException("produce json failed");
            }
        }
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
