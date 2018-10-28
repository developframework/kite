package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.KiteConfiguration;
import lombok.Getter;

/**
 * 抽象节点
 *
 * @author qiuzhenhao
 */
@Getter
public abstract class AbstractKiteElement implements KiteElement {

    /* 配置 */
    protected KiteConfiguration configuration;
    /* 所在命名空间 */
    protected String namespace;
    /* 所在模板 */
    protected String templateId;

    public AbstractKiteElement(KiteConfiguration configuration, String namespace, String templateId) {
        this.configuration = configuration;
        this.namespace = namespace;
        this.templateId = templateId;
    }
}
