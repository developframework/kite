package com.github.developframework.kite.core.structs;

import com.github.developframework.kite.core.element.Template;
import com.github.developframework.kite.core.exception.ResourceNotUniqueException;
import com.github.developframework.kite.core.exception.TemplateUndefinedException;
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
public class TemplatePackage extends HashMap<String, Template> {


    /* 命名空间 */
    @Getter
    private final String namespace;

    /**
     * 根据id获取模板
     *
     * @param templateId 模板ID
     * @return 模板
     */
    public Template getTemplateById(String templateId) {
        Template template = super.get(templateId);
        if (template == null) {
            throw new TemplateUndefinedException(namespace, templateId);
        }
        return template;
    }

    /**
     * 放入模板
     *
     * @param template 模板
     */
    public void push(Template template) {
        String templateId = template.getTemplateLocation().getTemplateId();
        if (super.containsKey(templateId)) {
            throw new ResourceNotUniqueException("Kite template", templateId);
        }
        super.put(templateId, template);
    }

}
