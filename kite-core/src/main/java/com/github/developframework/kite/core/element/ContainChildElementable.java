package com.github.developframework.kite.core.element;

import java.util.Iterator;
import java.util.List;

/**
 * 可包含子节点的
 *
 * @author qiuzhenhao
 */
public interface ContainChildElementable {

    /**
     * 增加子节点
     * @param kiteElement 子节点
     */
    void addChildElement(KiteElement kiteElement);

    /**
     * 返回子节点列表的迭代器
     * @return 子节点列表的迭代器
     */
    Iterator<KiteElement> childElementIterator();

    /**
     * 判断是否是空子节点
     * @return 判断结果
     */
    boolean isChildElementEmpty();

    /**
     * 返回子节点列表
     * @return 子节点列表
     */
    List<KiteElement> getChildKiteElements();

    /**
     * 复制其它容器的子节点
     * @param otherContainer 其它容器
     */
    void copyChildElement(ContainChildElementable otherContainer);
}
