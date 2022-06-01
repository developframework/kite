package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.strategy.NamingStrategy;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.FragmentLocation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 抽象元素
 *
 * @author qiushui on 2021-06-23.
 */
@Getter
@RequiredArgsConstructor
public abstract class AbstractKiteElement implements KiteElement {

    // 片段位置
    protected final FragmentLocation fragmentLocation;

    // 子元素命名策略
    protected NamingStrategy childrenNamingStrategy;

    /**
     * 对自己配置
     *
     * @param elementDefinition 元素定义
     */
    public void configure(ElementDefinition elementDefinition) {
        childrenNamingStrategy = elementDefinition.getEnum(ElementDefinition.Attribute.CHILDREN_NAMING_STRATEGY, NamingStrategy.class);
    }
}
