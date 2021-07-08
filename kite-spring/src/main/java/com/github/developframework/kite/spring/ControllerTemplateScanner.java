package com.github.developframework.kite.spring;

import com.github.developframework.kite.core.KiteFactory;
import com.github.developframework.kite.core.element.Template;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.source.StringConfigurationSource;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.core.structs.TemplatePackage;
import com.github.developframework.kite.core.structs.TemplatePackageRegistry;
import com.github.developframework.kite.spring.mvc.annotation.KiteNamespace;
import com.github.developframework.kite.spring.mvc.annotation.TemplateKTL;
import lombok.RequiredArgsConstructor;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller上@Template注解的扫描器
 *
 * @author qiushui on 2021-07-08.
 */
@RequiredArgsConstructor
public final class ControllerTemplateScanner {

    private final KiteFactory kiteFactory;

    private final RequestMappingHandlerMapping handlerMapping;

    public Map<Method, FragmentLocation> scan() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        Map<Method, FragmentLocation> fragmentLocationMap = new HashMap<>();
        handlerMethods.forEach((info, handlerMethod) -> {
            final TemplateKTL templateKTL = handlerMethod.getMethodAnnotation(TemplateKTL.class);
            if (templateKTL == null) {
                return;
            }
            final String ktl = templateKTL.value();
            if (ktl.isEmpty()) {
                return;
            }
            final List<TemplatePackage> ktlTemplatePackages;
            try {
                ktlTemplatePackages = kiteFactory.getKtlParser().read(new StringConfigurationSource(ktl, StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new KiteException("KTL解析失败");
            }
            final String namespace = getNamespace(handlerMethod);
            final TemplatePackageRegistry registry = kiteFactory.getConfiguration().getTemplatePackageRegistry();
            final TemplatePackage ktlTemplatePackage = ktlTemplatePackages.get(0);
            // 把默认模板包设置成当前namespace并注册入模板包注册器
            if (ktlTemplatePackage.getNamespace().equals(TemplatePackage.DEFAULT_NAMESPACE)) {
                ktlTemplatePackage.setNamespace(namespace);
                registry.mergeTemplatePackage(ktlTemplatePackage);
            }
            final Template uniqueTemplate = ktlTemplatePackage.getUniqueTemplate();
            fragmentLocationMap.put(handlerMethod.getMethod(), uniqueTemplate.getFragmentLocation());
        });
        return fragmentLocationMap;
    }

    /**
     * 获取方法或类上标注的namespace
     *
     * @param handlerMethod 方法
     * @return namespace
     */
    private String getNamespace(HandlerMethod handlerMethod) {
        KiteNamespace kiteNamespace = handlerMethod.getMethodAnnotation(KiteNamespace.class);
        if (kiteNamespace == null) {
            final Class<?> controllerClass = handlerMethod.getBeanType();
            kiteNamespace = controllerClass.getAnnotation(KiteNamespace.class);
            if (kiteNamespace == null) {
                throw new KiteException("在“%s”方法“%s”上没标注@KiteNamespace，或者在Controller类上标注全局@KiteNamespace", controllerClass.getName(), handlerMethod.getMethod().getName());
            }
        }
        return kiteNamespace.value();
    }
}
