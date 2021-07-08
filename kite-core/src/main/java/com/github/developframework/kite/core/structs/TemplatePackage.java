package com.github.developframework.kite.core.structs;

import com.github.developframework.kite.core.element.Fragment;
import com.github.developframework.kite.core.element.Template;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.exception.ResourceNotUniqueException;
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

    public static final String DEFAULT_NAMESPACE = "_default";

    /* 命名空间 */
    @Getter
    private final String namespace;

    public TemplatePackage() {
        this.namespace = DEFAULT_NAMESPACE;
    }

    /**
     * 根据id获取模板
     *
     * @param templateId 模板ID
     * @return 模板
     */
    public Template getTemplateById(String templateId) {
        Fragment fragment = super.get(templateId);
        if (fragment instanceof Template) {
            return (Template) fragment;
        }
        return null;
    }

    /**
     * 放入模板
     *
     * @param fragment 模板
     */
    public void push(Fragment fragment) {
        String templateId = fragment.getFragmentLocation().getFragmentId();
        if (super.containsKey(templateId)) {
            throw new ResourceNotUniqueException("template id", templateId);
        }
        super.put(templateId, fragment);
    }

    /**
     * 获取唯一的模板
     */
    public Template getUniqueTemplate() {
        final Template[] templates = values()
                .stream()
                .filter(f -> f instanceof Template)
                .map(f -> (Template) f)
                .toArray(Template[]::new);
        if (templates.length != 1) {
            throw new KiteException("使用ktl模式下template-package有且只能有一个template");
        } else {
            return templates[0];
        }
    }
}
