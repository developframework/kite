package com.github.developframework.kite.gson;

import com.github.developframework.kite.core.AbstractProducer;
import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.node.NodeProxy;
import com.github.developframework.kite.core.structs.TemplatePackage;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

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
    protected AssembleContext buildAssembleContext(DataModel dataModel) {
        return new GsonAssembleContext(configuration, dataModel);
    }

    @Override
    public String produce(boolean pretty) {
        final NodeProxy rootNodeProxy = buildRootNodeProxy();
        if (rootNodeProxy == null) {
            return "";
        } else {
            return gson.toJson((JsonElement) rootNodeProxy.getNode());
        }
    }

    @Override
    public void output(OutputStream outputStream, Charset charset, boolean pretty) {
        final NodeProxy rootNodeProxy = buildRootNodeProxy();
        if (rootNodeProxy != null) {
            final JsonWriter jsonWriter = new JsonWriter(new OutputStreamWriter(outputStream, charset));
            gson.toJson((JsonElement) rootNodeProxy.getNode(), jsonWriter);
        }
    }
}
