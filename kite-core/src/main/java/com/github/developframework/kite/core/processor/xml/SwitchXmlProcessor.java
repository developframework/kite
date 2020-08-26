package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.expression.ExpressionUtils;
import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.data.FunctionSign;
import com.github.developframework.kite.core.dynamic.CaseTestFunction;
import com.github.developframework.kite.core.element.CaseKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.SwitchKiteElement;
import com.github.developframework.kite.core.utils.KiteUtils;
import org.dom4j.Element;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * switch节点Xml处理器
 *
 * @author qiushui on 2018-10-29.
 */
public class SwitchXmlProcessor extends FunctionalXmlProcessor<SwitchKiteElement, Element> {

    public SwitchXmlProcessor(XmlProcessContext xmlProcessContext, SwitchKiteElement element, Element parentNode) {
        super(xmlProcessContext, element, parentNode);
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        Optional<Object> dataValue = getDataValue(parentProcessor);
        if (dataValue.isPresent()) {
            value = dataValue.get();
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        Map<String, CaseKiteElement> caseKiteElementMap = element.getCaseKiteElementMap();
        for (String testValue : caseKiteElementMap.keySet()) {
            CaseTestFunction caseTestFunction = KiteUtils.getComponentInstance(xmlProcessContext.getDataModel(), testValue, CaseTestFunction.class, "test");
            if (caseTestFunction.test(value)) {
                CaseKiteElement caseKiteElement = caseKiteElementMap.get(testValue);
                XmlProcessor<? extends KiteElement, ? extends Element> nextProcessor = caseKiteElement.createXmlProcessor(xmlProcessContext, parentProcessor.node);
                nextProcessor.process(parentProcessor);
                return;
            }
        }
        element.getDefaultCaseKiteElement().ifPresent(defaultCase -> defaultCase.createXmlProcessor(xmlProcessContext, parentProcessor.node).process(parentProcessor));
    }

    private Optional<Object> getDataValue(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        if (Objects.nonNull(value)) {
            return Optional.of(value);
        }
        DataDefinition checkDataDefinition = element.getCheckDataDefinition();
        if (checkDataDefinition == DataDefinition.EMPTY_DATA_DEFINITION) {
            return Optional.ofNullable(parentProcessor.value);
        } else if (checkDataDefinition.getFunctionSign() == FunctionSign.ROOT || Objects.isNull(parentProcessor.value)) {
            return xmlProcessContext.getDataModel().getData(checkDataDefinition.getExpression());
        } else {
            return Optional.ofNullable(ExpressionUtils.getValue(parentProcessor.value, checkDataDefinition.getExpression()));
        }
    }
}
