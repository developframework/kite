package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.element.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Stack;

/**
 * 解析过程上下文
 * @author qiuzhenhao
 */
@Getter
class ParseContext {

    /* 配置 */
    private KiteConfiguration configuration;
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
    private Stack<KiteElement> stack;

    public ParseContext(KiteConfiguration configuration) {
        this.configuration = configuration;
        this.stack = new Stack<>();
    }
}
