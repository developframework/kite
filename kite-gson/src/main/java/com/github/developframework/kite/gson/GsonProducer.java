package com.github.developframework.kite.gson;

import com.github.developframework.kite.core.AbstractProducer;
import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.element.Template;
import com.github.developframework.kite.core.node.ArrayNodeProxy;
import com.github.developframework.kite.core.node.NodeProxy;
import com.github.developframework.kite.core.node.ObjectNodeProxy;
import com.github.developframework.kite.core.structs.TemplatePackage;
import com.github.developframework.kite.core.utils.KiteUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

/**
 * @author qiushui on 2021-07-15.
 */
public final class GsonProducer extends AbstractProducer {

    private final Gson gson;

    public GsonProducer(KiteConfiguration configuration, DataModel dataModel, String namespace, String templateId) {
        super(configuration, dataModel, namespace, templateId);
        gson = (Gson) configuration.getJsonFramework().getCore();
    }

    public GsonProducer(KiteConfiguration configuration, DataModel dataModel, List<TemplatePackage> templatePackages) {
        super(configuration, dataModel, templatePackages);
        gson = (Gson) configuration.getJsonFramework().getCore();
    }

    @Override
    protected AssembleContext buildAssembleContext() {
        return new GsonAssembleContext(configuration);
    }

    @Override
    public String produce(boolean pretty) {
        final JsonElement node = buildJsonElement();
        if (node == null) return "";
        return gson.toJson(node);
    }

    @Override
    public void output(OutputStream outputStream, Charset charset, boolean pretty) {
        final JsonElement node = buildJsonElement();
        if (node == null) return;
        final JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(outputStream, charset));
        gson.toJson(node, jsonWriter);
    }

    private JsonElement buildJsonElement() {
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
            rootNodeProxy = new GsonArrayNodeProxy(new JsonArray());
            context.pushValue(rootValue);
            template.getInnerArrayKiteElement().assembleArrayItems(context, rootValue, (ArrayNodeProxy) rootNodeProxy);
        } else {
            // 以对象为根
            rootNodeProxy = new GsonObjectNodeProxy(new JsonObject());
            context.pushNodeProxy((ObjectNodeProxy) rootNodeProxy);
            template.assemble(context);
        }
        return (JsonElement) rootNodeProxy.getNode();
    }
}
