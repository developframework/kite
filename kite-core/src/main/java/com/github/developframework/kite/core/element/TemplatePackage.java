package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.exception.ResourceNotUniqueException;
import com.github.developframework.kite.core.exception.TemplateUndefinedException;
import lombok.Getter;

import java.util.HashMap;

/**
 * Kite 模板包
 * @author qiuzhenhao
 */
public class TemplatePackage extends HashMap<String, Template>{

    /* 命名空间 */
    @Getter
    private String namespace;

    public TemplatePackage(String namespace) {
        super();
        this.namespace = namespace;
    }

    /**
     * 根据id获取模板
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
     * @param template 模板
     */
    public void push(Template template) {
        String templateId = template.getTemplateId();
        if (super.containsKey(templateId)) {
            throw new ResourceNotUniqueException("Kite template", templateId);
        }
        super.put(templateId, template);
    }
}
