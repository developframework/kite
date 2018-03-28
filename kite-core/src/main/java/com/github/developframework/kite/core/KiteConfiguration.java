package com.github.developframework.kite.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.developframework.kite.core.element.Template;
import com.github.developframework.kite.core.element.TemplatePackage;
import com.github.developframework.kite.core.exception.ResourceNotUniqueException;
import com.github.developframework.kite.core.exception.TemplatePackageUndefinedException;
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
    private Map<String, TemplatePackage> templatePackages;

    public KiteConfiguration() {
        this.templatePackages = new HashMap<>();
    }

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
     * @param namespace 命名空间
     * @param templateId 模板ID
     * @return 模板
     */
    public Template extractTemplate(String namespace, String templateId) {
        return getTemplatePackageByNamespace(namespace).getTemplateById(templateId);
    }
}
