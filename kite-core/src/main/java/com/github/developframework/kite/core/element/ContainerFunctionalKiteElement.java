package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.TemplateLocation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 容器功能节点
 * @author qiuzhenhao
 */
public abstract class ContainerFunctionalKiteElement extends FunctionalKiteElement implements ContainChildElementable{

    /* 子节点列表 */
    protected List<KiteElement> childElements = new LinkedList<>();

    public ContainerFunctionalKiteElement(KiteConfiguration configuration, TemplateLocation templateLocation) {
        super(configuration, templateLocation);
    }

    @Override
    public void addChildElement(KiteElement element) {
        childElements.add(element);
    }

    @Override
    public final Iterator<KiteElement> childElementIterator() {
        return childElements.iterator();
    }

    @Override
    public final List<KiteElement> getChildKiteElements() {
        return childElements;
    }

    @Override
    public final boolean isChildElementEmpty() {
        return childElements.isEmpty();
    }
}
