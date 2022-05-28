package com.github.developframework.kite.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
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
 * fastjson实现
 *
 * @author qiushui on 2021-07-15.
 */
public final class FastjsonProducer extends AbstractProducer {

    public FastjsonProducer(KiteConfiguration configuration, DataModel dataModel, String namespace, String templateId) {
        super(configuration, dataModel, namespace, templateId);
    }

    public FastjsonProducer(KiteConfiguration configuration, DataModel dataModel, List<TemplatePackage> templatePackages) {
        super(configuration, dataModel, templatePackages);
    }

    @Override
    protected AssembleContext buildAssembleContext(DataModel dataModel) {
        return new FastjsonAssembleContext(configuration, dataModel);
    }

    @Override
    public String produce(boolean pretty) {
        final NodeProxy rootNodeProxy = buildRootNodeProxy();
        if (rootNodeProxy == null) {
            return "";
        } else {
            JSON node = (JSON) rootNodeProxy.getNode();
            return pretty ? node.toString(SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue) : node.toString(SerializerFeature.WriteMapNullValue);
        }

    }

    @Override
    public void output(OutputStream outputStream, Charset charset, boolean pretty) {
        final String json = produce(pretty);
        if (!json.isEmpty()) {
            try {
                outputStream.write(json.getBytes(charset));
                outputStream.flush();
            } catch (IOException e) {
                throw new KiteException("构建json失败：", e.getMessage());
            }
        }
    }
}
