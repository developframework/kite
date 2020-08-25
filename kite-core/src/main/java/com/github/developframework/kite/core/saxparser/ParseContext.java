package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.TemplateLocation;
import com.github.developframework.kite.core.element.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Stack;

/**
 * 解析过程上下文
 * @author qiuzhenhao
 */
@Getter
@AllArgsConstructor
class ParseContext {

    /* 配置 */
    private final KiteConfiguration configuration;
    /* 当前模板包 */
    @Setter
    private TemplatePackage currentTemplatePackage;
    @Setter
    private Template currentTemplate;
    @Setter
    private IfKiteElement currentIfElement;
    @Setter
    private SwitchKiteElement currentSwitchElement;
    /* 节点栈 */
    private final Stack<KiteElement> stack = new Stack<>();

    public TemplateLocation getCurrentTemplateLocation() {
        return currentTemplate.getTemplateLocation();
    }
}
