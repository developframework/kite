package com.github.developframework.kite.spring.mvc;

import com.github.developframework.kite.core.KiteFactory;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.spring.KiteResponseBodyProcessor;
import com.github.developframework.kite.spring.mvc.annotation.KiteNamespace;
import com.github.developframework.kite.spring.mvc.annotation.TemplateId;
import com.github.developframework.kite.spring.mvc.annotation.TemplateKTL;
import com.github.developframework.kite.spring.mvc.annotation.TemplateType;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 基于注解的ReturnValueHandler
 *
 * @author qiuzhenhao
 */
public abstract class AnnotationKiteReturnValueHandler<T> extends AbstractKiteReturnValueHandler<T> {

    private final Map<Method, FragmentLocation> fragmentLocationMap;

    public AnnotationKiteReturnValueHandler(KiteFactory kiteFactory, KiteResponseBodyProcessor kiteResponseBodyProcessor, Map<Method, FragmentLocation> fragmentLocationMap) {
        super(kiteFactory, kiteResponseBodyProcessor);
        this.fragmentLocationMap = fragmentLocationMap;
    }

    @Override
    protected String namespace(T returnValue, MethodParameter methodParameter) {
        KiteNamespace annotation = methodParameter.getMethodAnnotation(KiteNamespace.class);
        if (annotation == null) {
            annotation = AnnotationUtils.findAnnotation(methodParameter.getContainingClass(), KiteNamespace.class);
            if (annotation == null) {
                throw new KiteException("在“%s”方法“%s”上没标注@KiteNamespace，或者在Controller类上标注全局@KiteNamespace", methodParameter.getContainingClass(), methodParameter.getMethod().getName());
            }
        }
        return annotation.value();
    }

    @Override
    protected String templateId(T returnValue, MethodParameter methodParameter) {
        final TemplateId templateId = methodParameter.getMethodAnnotation(TemplateId.class);
        if (templateId != null) {
            return templateId.value();
        }
        final TemplateKTL templateKTL = methodParameter.getMethodAnnotation(TemplateKTL.class);
        if (templateKTL == null) {
            throw new KiteException("在“%s”方法“%s”上没标注@TemplateId或@TemplateKTL", methodParameter.getContainingClass(), methodParameter.getMethod().getName());
        }
        final FragmentLocation fragmentLocation = fragmentLocationMap.get(methodParameter.getMethod());
        return fragmentLocation.getFragmentId();
    }

    @Override
    protected TemplateType templateType(T returnValue, MethodParameter methodParameter) {
        final TemplateId templateId = methodParameter.getMethodAnnotation(TemplateId.class);
        if (templateId != null) {
            return templateId.type();
        }
        final TemplateKTL templateKTL = methodParameter.getMethodAnnotation(TemplateKTL.class);
        if (templateKTL == null) {
            throw new KiteException("在“%s”方法“%s”上没标注@TemplateId或@TemplateKTL", methodParameter.getContainingClass(), methodParameter.getMethod().getName());
        }
        return templateKTL.type();
    }
}
