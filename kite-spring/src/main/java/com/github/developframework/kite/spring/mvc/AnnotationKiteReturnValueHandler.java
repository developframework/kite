package com.github.developframework.kite.spring.mvc;

import com.github.developframework.kite.core.KiteFactory;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.spring.mvc.annotation.KiteNamespace;
import com.github.developframework.kite.spring.mvc.annotation.TemplateId;
import com.github.developframework.kite.spring.mvc.annotation.TemplateType;
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
        KiteNamespace annotation = methodParameter.getMethodAnnotation(KiteNamespace.class);
        if (annotation == null) {
            annotation = AnnotationUtils.findAnnotation(methodParameter.getContainingClass(), KiteNamespace.class);
            if (annotation == null) {
                throw new KiteException("@KiteNamespace is not found in Class \"%s\" with Method \"%s\".", methodParameter.getContainingClass(), methodParameter.getMethod().getName());
            }
        }
        return annotation.value();
    }

    @Override
    protected String templateId(T returnValue, MethodParameter methodParameter) {
        final TemplateId annotation = methodParameter.getMethodAnnotation(TemplateId.class);
        if (annotation == null) {
            throw new KiteException("@TemplateId is not found in Class \"%s\" with Method \"%s\".", methodParameter.getContainingClass(), methodParameter.getMethod().getName());
        }
        return annotation.value();
    }

    @Override
    protected TemplateType templateType(T returnValue, MethodParameter methodParameter) {
        final TemplateId annotation = methodParameter.getMethodAnnotation(TemplateId.class);
        if (annotation == null) {
            throw new KiteException("@TemplateId is not found in Class \"%s\" with Method \"%s\".", methodParameter.getContainingClass(), methodParameter.getMethod().getName());
        }
        return annotation.type();
    }
}
