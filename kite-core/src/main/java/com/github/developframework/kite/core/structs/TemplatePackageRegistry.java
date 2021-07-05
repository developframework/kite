package com.github.developframework.kite.core.structs;

import com.github.developframework.kite.core.element.Fragment;
import com.github.developframework.kite.core.element.Template;
import com.github.developframework.kite.core.exception.ResourceNotUniqueException;
import com.github.developframework.kite.core.exception.TemplatePackageUndefinedException;

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
     * 提取片段
     *
     * @param fragmentLocation 模板位置
     * @return 片段
     */
    public Fragment extractFragment(FragmentLocation fragmentLocation) {
        return getTemplatePackageByNamespace(fragmentLocation.getNamespace()).getFragmentById(fragmentLocation.getFragmentId());
    }
}
