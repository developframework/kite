package com.github.developframework.kite.spring.mvc;

import com.github.developframework.kite.core.KiteFactory;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.spring.KiteResponseBodyProcessor;
import com.github.developframework.kite.spring.mvc.annotation.TemplateType;
import com.github.developframework.kite.spring.mvc.response.KiteResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 处理KiteResponse的ReturnValueHandler
 *
 * @author qiuzhenhao
 */
public final class KiteResponseReturnValueHandler extends AnnotationKiteReturnValueHandler<KiteResponse> {

    public KiteResponseReturnValueHandler(KiteFactory kiteFactory, KiteResponseBodyProcessor kiteResponseBodyProcessor, Map<Method, FragmentLocation> fragmentLocationMap) {
        super(kiteFactory, kiteResponseBodyProcessor, fragmentLocationMap);
    }

    @Override
    protected Class<KiteResponse> returnType() {
        return KiteResponse.class;
    }

    @Override
    protected String namespace(KiteResponse returnValue, MethodParameter methodParameter) {
        String tempNamespace = returnValue.getNamespace();
        return StringUtils.isNotEmpty(tempNamespace) ? tempNamespace : super.namespace(returnValue, methodParameter);
    }

    @Override
    protected String templateId(KiteResponse returnValue, MethodParameter methodParameter) {
        String tempTemplateId = returnValue.getTemplateId();
        return StringUtils.isNotEmpty(tempTemplateId) ? tempTemplateId : super.templateId(returnValue, methodParameter);
    }

    @Override
    protected TemplateType templateType(KiteResponse returnValue, MethodParameter methodParameter) {
        TemplateType tempTemplateType = returnValue.getTemplateType();
        return tempTemplateType != null ? tempTemplateType : super.templateType(returnValue, methodParameter);
    }

    @Override
    protected DataModel dataModel(KiteResponse returnValue, MethodParameter methodParameter) {
        return returnValue.getDataModel();
    }
}
