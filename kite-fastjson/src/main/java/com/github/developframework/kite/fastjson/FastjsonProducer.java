package com.github.developframework.kite.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.developframework.kite.core.AbstractProducer;
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
 * fastjson实现
 *
 * @author qiushui on 2021-07-15.
 */
public final class FastjsonProducer extends AbstractProducer {

    public FastjsonProducer(KiteConfiguration configuration, DataModel dataModel, String namespace, String templateId) {
        super(configuration, dataModel, namespace, templateId, true);
    }

    public FastjsonProducer(KiteConfiguration configuration, DataModel dataModel, List<TemplatePackage> templatePackages) {
        super(configuration, dataModel, templatePackages, true);
    }

    @Override
    public String produce(boolean pretty) {
        final JSON node = buildJSON();
        if (node == null) return "";
        return pretty ? node.toString(SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue) : node.toString(SerializerFeature.WriteMapNullValue);
    }

    @Override
    public void output(OutputStream outputStream, Charset charset, boolean pretty) {
        final String json = produce(pretty);
        try {
            outputStream.write(json.getBytes(charset));
            outputStream.flush();
        } catch (IOException e) {
            throw new KiteException("构建json失败：", e.getMessage());
        }
    }

    private JSON buildJSON() {
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
            rootNodeProxy = new FastjsonArrayNodeProxy(new JSONArray());
            context.pushValue(rootValue);
            template.getInnerArrayKiteElement().assembleArrayItems(context, rootValue, (ArrayNodeProxy) rootNodeProxy);
        } else {
            // 以对象为根
            rootNodeProxy = new FastjsonObjectNodeProxy(new JSONObject(true));
            context.pushNodeProxy((ObjectNodeProxy) rootNodeProxy);
            template.assemble(context);
        }
        return (JSON) rootNodeProxy.getNode();
    }
}
