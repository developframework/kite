package com.github.developframework.kite.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.developframework.kite.core.element.Template;
import com.github.developframework.kite.core.element.TemplatePackage;
import com.github.developframework.kite.core.exception.ResourceNotUniqueException;
import com.github.developframework.kite.core.exception.TemplatePackageUndefinedException;
import com.github.developframework.kite.core.strategy.DefaultXmlKitePropertyNamingStrategy;
import com.github.developframework.kite.core.strategy.JacksonKitePropertyNamingStrategy;
import com.github.developframework.kite.core.strategy.KitePropertyNamingStrategy;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Kite配置
 * @author qiuzhenhao
 */
public class KiteConfiguration {

    @Getter
    @Setter
    private ObjectMapper objectMapper;

    /* 模板包Map */
    private final Map<String, TemplatePackage> templatePackages = new HashMap<>();

    /* json节点的命名策略 */
    @Getter
    private KitePropertyNamingStrategy forJsonStrategy = new JacksonKitePropertyNamingStrategy();

    /* xml节点的命名策略 */
    @Getter
    private KitePropertyNamingStrategy forXmlStrategy = new DefaultXmlKitePropertyNamingStrategy();

    @Getter
    @Setter
    private boolean xmlSuppressDeclaration = true;

    /**
     * 增加模板包
     * @param templatePackage 模板包
     */
    public void putTemplatePackage(TemplatePackage templatePackage) {
        String namespace = templatePackage.getNamespace();
        if (this.templatePackages.containsKey(namespace)) {
            throw new ResourceNotUniqueException("Kite package", namespace);
        }
        this.templatePackages.put(namespace, templatePackage);
    }

    /**
     * 获取模板包
     * @param namespace 命名空间
     * @return 模板包
     */
    public TemplatePackage getTemplatePackageByNamespace(String namespace) {
        TemplatePackage templatePackage = templatePackages.get(namespace);
        if (templatePackage == null) {
            throw new TemplatePackageUndefinedException(namespace);
        }
        return templatePackage;
    }

    /**
     * 提取模板
     *
     * @param namespace  命名空间
     * @param templateId 模板ID
     * @return 模板
     */
    public Template extractTemplate(String namespace, String templateId) {
        return getTemplatePackageByNamespace(namespace).getTemplateById(templateId);
    }

    /**
     * 提取模板
     *
     * @param templateLocation 模板位置
     * @return 模板
     */
    public Template extractTemplate(TemplateLocation templateLocation) {
        return getTemplatePackageByNamespace(templateLocation.getNamespace()).getTemplateById(templateLocation.getTemplateId());
    }

    public void setForJsonStrategy(KitePropertyNamingStrategy forJsonStrategy) {
        if (forJsonStrategy != null) {
            this.forJsonStrategy = forJsonStrategy;
        }
    }

    public void setForXmlStrategy(KitePropertyNamingStrategy forXmlStrategy) {
        if (forXmlStrategy != null) {
            this.forXmlStrategy = forXmlStrategy;
        }
    }
}
