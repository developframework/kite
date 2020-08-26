package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.TemplateLocation;
import com.github.developframework.kite.core.strategy.KitePropertyNamingStrategy;
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
    /* 所在位置 */
    protected TemplateLocation templateLocation;

    @Getter
    protected KitePropertyNamingStrategy childrenNamingStrategy;

    public AbstractKiteElement(KiteConfiguration configuration, TemplateLocation templateLocation) {
        this.configuration = configuration;
        this.templateLocation = templateLocation;
    }

    public void setChildrenNamingStrategy(KitePropertyNamingStrategy childrenNamingStrategy) {
        if (this.childrenNamingStrategy == null) {
            this.childrenNamingStrategy = childrenNamingStrategy;
        }
    }
}
