package com.github.developframework.kite.spring.mvc;

import com.github.developframework.kite.core.KiteFactory;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.spring.mvc.annotation.TemplateType;
import com.github.developframework.kite.spring.mvc.response.KiteResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;

/**
 * 处理KiteResponse的ReturnValueHandler
 *
 * @author qiuzhenhao
 */
public final class KiteResponseReturnValueHandler extends AnnotationKiteReturnValueHandler<KiteResponse> {

    public KiteResponseReturnValueHandler(KiteFactory kiteFactory) {
        super(kiteFactory);
    }

    @Override
    protected Class<KiteResponse> returnType() {
        return KiteResponse.class;
    }

    @Override
    protected String namespace(KiteResponse returnValue, MethodParameter methodParameter) {
        String tempNamespace = super.namespace(returnValue, methodParameter);
        if (StringUtils.isNotBlank(tempNamespace)) {
            return tempNamespace;
        } else {
            return returnValue.getNamespace();
        }
    }

    @Override
    protected String templateId(KiteResponse returnValue, MethodParameter methodParameter) {
        String tempTemplateId = super.templateId(returnValue, methodParameter);
        if (StringUtils.isNotBlank(tempTemplateId)) {
            return tempTemplateId;
        } else {
            return returnValue.getTemplateId();
        }
    }

    @Override
    protected TemplateType templateType(KiteResponse returnValue, MethodParameter methodParameter) {
        TemplateType tempTemplateType = super.templateType(returnValue, methodParameter);
        if (tempTemplateType != null) {
            return tempTemplateType;
        } else {
            return returnValue.getTemplateType();
        }
    }

    @Override
    protected DataModel dataModel(KiteResponse returnValue, MethodParameter methodParameter) {
        return returnValue.getDataModel();
    }
}
