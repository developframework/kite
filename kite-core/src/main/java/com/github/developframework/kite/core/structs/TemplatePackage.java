package com.github.developframework.kite.core.structs;

import com.github.developframework.kite.core.element.Fragment;
import com.github.developframework.kite.core.element.Template;
import com.github.developframework.kite.core.exception.ResourceNotUniqueException;
import com.github.developframework.kite.core.exception.TemplateException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;

/**
 * 模板包
 *
 * @author qiushui on 2021-06-23.
 */
@Getter
@RequiredArgsConstructor
public class TemplatePackage extends HashMap<String, Fragment> {


    /* 命名空间 */
    @Getter
    private final String namespace;

    /**
     * 根据id获取片段
     *
     * @param fragmentId 模板ID
     * @return 模板
     */
    public Fragment getFragmentById(String fragmentId) {
        Fragment fragment = super.get(fragmentId);
        if (fragment == null) {
            throw new TemplateException("The template \"%s\" is undefined in template-package \"%s\".", fragmentId, namespace);
        }
        return fragment;
    }

    /**
     * 根据id获取模板
     *
     * @param templateId 模板ID
     * @return 模板
     */
    public Template getTemplateById(String templateId) {
        Fragment fragment = getFragmentById(templateId);
        if (fragment instanceof Template) {
            return (Template) fragment;
        }
        throw new TemplateException("The template \"%s\" is a fragment in template-package \"%s\".", templateId, namespace);
    }

    /**
     * 放入模板
     *
     * @param fragment 模板
     */
    public void push(Fragment fragment) {
        String templateId = fragment.getFragmentLocation().getFragmentId();
        if (super.containsKey(templateId)) {
            throw new ResourceNotUniqueException("Kite template", templateId);
        }
        super.put(templateId, fragment);
    }

}
