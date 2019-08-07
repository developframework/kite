package com.github.developframework.kite.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.element.ArrayKiteElement;
import com.github.developframework.kite.core.element.Template;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.processor.json.ArrayJsonProcessor;
import com.github.developframework.kite.core.processor.json.ArrayTemplateJsonProcessor;
import com.github.developframework.kite.core.processor.json.JsonProcessContext;
import com.github.developframework.kite.core.processor.json.TemplateJsonProcessor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 默认的Json生成器
 * @author qiuzhenhao
 */
class DefaultJsonProducer implements JsonProducer {

    private KiteConfiguration kiteConfiguration;

    DefaultJsonProducer(KiteConfiguration kiteConfiguration) {
        this.kiteConfiguration = kiteConfiguration;
    }

    @Override
    public String produce(DataModel dataModel, String namespace, String templateId) {
        return produce(dataModel, namespace, templateId, false);
    }

    @Override
    public String produce(DataModel dataModel, String namespace, String templateId, boolean isPretty) {
        JsonNode root = constructRootTree(dataModel, namespace, templateId);
        try {
            if (isPretty) {
                return kiteConfiguration.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(root);
            } else {
                return kiteConfiguration.getObjectMapper().writeValueAsString(root);
            }
        } catch (JsonProcessingException e) {
            throw new KiteException("produce json string failed.");
        }
    }

    @Override
    public void outputJson(JsonGenerator jsonGenerator, DataModel dataModel, String namespace, String templateId, boolean isPretty) {
        JsonNode root = constructRootTree(dataModel, namespace, templateId);
        try {
            if(isPretty) {
                kiteConfiguration.getObjectMapper().writerWithDefaultPrettyPrinter().writeValue(jsonGenerator, root);
            } else {
                kiteConfiguration.getObjectMapper().writer().writeValue(jsonGenerator, root);
            }
        } catch (Exception e) {
            throw new KiteException("produce json string failed.");
        }
    }

    /**
     * 构造json树对象
     * @param dataModel
     * @param namespace
     * @param id
     * @return
     */
    private JsonNode constructRootTree(DataModel dataModel, String namespace, String id) {
        Template template = kiteConfiguration.extractTemplate(namespace, id);
        JsonProcessContext jsonProcessContext = new JsonProcessContext();
        jsonProcessContext.setDataModel(dataModel);
        jsonProcessContext.setConfiguration(kiteConfiguration);

        DataDefinition templateDataDefinition = template.getDataDefinition();

        if(templateDataDefinition != null) {
            Optional<Object> rootObjectOptional = dataModel.getData(templateDataDefinition.getExpression());
            if (rootObjectOptional.isPresent()) {
                Object rootObject = rootObjectOptional.get();
                if (rootObject.getClass().isArray() || rootObject instanceof List || rootObject instanceof Set) {
                    // 视为数组模板
                    return constructRootArrayNodeTree(jsonProcessContext, template, rootObject);
                } else {
                    // 视为对象模板
                    return constructRootObjectNodeTree(jsonProcessContext, template, rootObject);
                }
            } else {
                throw new KiteException("Root data must not null.");
            }
        } else {
            // 视为对象模板
            return constructRootObjectNodeTree(jsonProcessContext, template, null);
        }
    }

    private ObjectNode constructRootObjectNodeTree(JsonProcessContext jsonProcessContext, Template template, Object value) {
        ObjectNode rootNode = kiteConfiguration.getObjectMapper().createObjectNode();
        TemplateJsonProcessor templateProcessor = new TemplateJsonProcessor(jsonProcessContext, template);
        templateProcessor.setValue(value);
        templateProcessor.setNode(rootNode);
        templateProcessor.process(null);
        return rootNode;
    }

    private ArrayNode constructRootArrayNodeTree(JsonProcessContext jsonProcessContext, Template template, Object value) {
        ArrayNode rootNode = kiteConfiguration.getObjectMapper().createArrayNode();
        ArrayKiteElement arrayElement = new ArrayKiteElement(kiteConfiguration, template.getTemplateLocation(), template.getDataDefinition(), null);
        arrayElement.setMapFunctionValue(template.getMapFunctionValue());
        ArrayJsonProcessor arrayProcessor = new ArrayTemplateJsonProcessor(jsonProcessContext, template, arrayElement);
        arrayProcessor.setValue(value);
        arrayProcessor.setNode(rootNode);
        arrayProcessor.process(null);
        return rootNode;
    }

}
