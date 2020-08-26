package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.expression.ExpressionUtils;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.data.FunctionSign;
import com.github.developframework.kite.core.dynamic.CaseTestFunction;
import com.github.developframework.kite.core.element.CaseKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.SwitchKiteElement;
import com.github.developframework.kite.core.utils.KiteUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * switch节点Json处理器
 *
 * @author qiushui on 2018-10-29.
 */
public class SwitchJsonProcessor extends FunctionalJsonProcessor<SwitchKiteElement, ObjectNode> {

    public SwitchJsonProcessor(JsonProcessContext jsonProcessContext, SwitchKiteElement element, ObjectNode parentNode) {
        super(jsonProcessContext, element, parentNode);
    }

    @Override
    protected boolean prepare(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        Optional<Object> dataValue = getDataValue(parentProcessor);
        if (dataValue.isPresent()) {
            value = dataValue.get();
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void handleCoreLogic(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        Map<String, CaseKiteElement> caseKiteElementMap = element.getCaseKiteElementMap();
        for (String testValue : caseKiteElementMap.keySet()) {
            CaseTestFunction caseTestFunction = KiteUtils.getComponentInstance(jsonProcessContext.getDataModel(), testValue, CaseTestFunction.class, "test");
            if (caseTestFunction.test(value)) {
                CaseKiteElement caseKiteElement = caseKiteElementMap.get(testValue);
                JsonProcessor<? extends KiteElement, ? extends JsonNode> nextProcessor = caseKiteElement.createJsonProcessor(jsonProcessContext, (ObjectNode) parentProcessor.node);
                nextProcessor.process(parentProcessor);
                return;
            }
        }
        element.getDefaultCaseKiteElement().ifPresent(defaultCase -> defaultCase.createJsonProcessor(jsonProcessContext, (ObjectNode) parentProcessor.node).process(parentProcessor));
    }

    private Optional<Object> getDataValue(ContentJsonProcessor<? extends KiteElement, ? extends JsonNode> parentProcessor) {
        if (Objects.nonNull(value)) {
            return Optional.of(value);
        }
        DataDefinition checkDataDefinition = element.getCheckDataDefinition();
        if (checkDataDefinition == DataDefinition.EMPTY_DATA_DEFINITION) {
            return Optional.ofNullable(parentProcessor.value);
        } else if (checkDataDefinition.getFunctionSign() == FunctionSign.ROOT || Objects.isNull(parentProcessor.value)) {
            return jsonProcessContext.getDataModel().getData(checkDataDefinition.getExpression());
        } else {
            return Optional.ofNullable(ExpressionUtils.getValue(parentProcessor.value, checkDataDefinition.getExpression()));
        }
    }
}
