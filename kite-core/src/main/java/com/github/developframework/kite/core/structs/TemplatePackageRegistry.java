package com.github.developframework.kite.core.structs;

import com.github.developframework.kite.core.exception.ResourceNotUniqueException;

import java.util.HashMap;
import java.util.Map;

/**
 * 模板包注册器
 *
 * @author qiushui on 2021-06-24.
 */
public final class TemplatePackageRegistry {

    private final Map<String, TemplatePackage> templatePackages = new HashMap<>();

    /**
     * 增加模板包
     *
     * @param templatePackage 模板包
     */
    public void putTemplatePackage(TemplatePackage templatePackage) {
        String namespace = templatePackage.getNamespace();
        if (this.templatePackages.containsKey(namespace)) {
            throw new ResourceNotUniqueException("kite-package namespace", namespace);
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

    /**
     * 合并模板包
     *
     * @param templatePackage 模板包
     */
    public void mergeTemplatePackage(TemplatePackage templatePackage) {
        final TemplatePackage tp = templatePackages.get(templatePackage.getNamespace());
        if (tp == null) {
            templatePackages.put(templatePackage.getNamespace(), templatePackage);
        } else {
            templatePackage.values().forEach(tp::push);
        }
    }
}
