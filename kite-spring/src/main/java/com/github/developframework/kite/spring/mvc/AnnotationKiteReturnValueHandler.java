package com.github.developframework.kite.spring.mvc;

import com.github.developframework.kite.core.KiteFactory;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.spring.mvc.annotation.KiteNamespace;
import com.github.developframework.kite.spring.mvc.annotation.TemplateId;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * 基于注解的ReturnValueHandler
 * @author qiuzhenhao
 */
public abstract class AnnotationKiteReturnValueHandler<T> extends AbstractKiteReturnValueHandler <T>{


    public AnnotationKiteReturnValueHandler(KiteFactory kiteFactory) {
        super(kiteFactory);
    }

    @Override
    protected String namespace(T returnValue, MethodParameter methodParameter) {
        if (methodParameter.hasMethodAnnotation(KiteNamespace.class)) {
            return methodParameter.getMethodAnnotation(KiteNamespace.class).value();
        } else {
            final KiteNamespace annotation = AnnotationUtils.findAnnotation(methodParameter.getContainingClass(), KiteNamespace.class);
            if (annotation != null) {
                return annotation.value();
            } else {
                throw new KiteException("@KiteNamespace is not found in Class \"%s\" with Method \"%s\".", methodParameter.getContainingClass(), methodParameter.getMethod().getName());
            }
        }
    }

    @Override
    protected String templateId(T returnValue, MethodParameter methodParameter) {

        if (methodParameter.hasMethodAnnotation(TemplateId.class)) {
            return methodParameter.getMethodAnnotation(TemplateId.class).value();
        } else {
            throw new KiteException("@TemplateId is not found in Class \"%s\" with Method \"%s\".", methodParameter.getContainingClass(), methodParameter.getMethod().getName());
        }
    }
}
