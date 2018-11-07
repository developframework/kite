package com.github.developframework.kite.spring.mvc;

import com.github.developframework.kite.core.KiteFactory;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.spring.mvc.annotation.KiteNamespace;
import com.github.developframework.kite.spring.mvc.annotation.TemplateId;
import com.github.developframework.kite.spring.mvc.annotation.TemplateType;
import com.github.developframework.kite.spring.mvc.response.KiteResponse;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;

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
        if (methodParameter.hasMethodAnnotation(KiteNamespace.class)) {
            return methodParameter.getMethodAnnotation(KiteNamespace.class).value();
        } else {
            final KiteNamespace annotation = AnnotationUtils.findAnnotation(methodParameter.getContainingClass(), KiteNamespace.class);
            if (annotation != null) {
                return annotation.value();
            } else {
                return returnValue.getNamespace();
            }
        }
    }

    @Override
    protected String templateId(KiteResponse returnValue, MethodParameter methodParameter) {
        if (methodParameter.hasMethodAnnotation(TemplateId.class)) {
            return methodParameter.getMethodAnnotation(TemplateId.class).value();
        } else {
            return returnValue.getTemplateId();
        }
    }

    @Override
    protected TemplateType templateType(KiteResponse returnValue, MethodParameter methodParameter) {
        if (methodParameter.hasMethodAnnotation(TemplateId.class)) {
            return methodParameter.getMethodAnnotation(TemplateId.class).type();
        } else {
            return returnValue.getTemplateType();
        }
    }

    @Override
    protected DataModel dataModel(KiteResponse returnValue, MethodParameter methodParameter) {
        return returnValue.getDataModel();
    }
}
