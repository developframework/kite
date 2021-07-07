package com.github.developframework.kite.core.structs;

import com.github.developframework.kite.core.exception.ResourceNotUniqueException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qiushui on 2021-06-24.
 */
public class TemplatePackageRegistry {

    private final Map<String, TemplatePackage> templatePackages = new HashMap<>();

    /**
     * 增加模板包
     *
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
     *
     * @param namespace 命名空间
     * @return 模板包
     */
    public TemplatePackage getTemplatePackageByNamespace(String namespace) {
        return templatePackages.get(namespace);
    }
}
