package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.AssembleContext;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.FragmentLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author qiushui on 2021-06-23.
 */
public abstract class ContainerKiteElement extends ContentKiteElement implements Iterable<KiteElement> {

    /* 子节点列表 */
    protected List<KiteElement> elements;

    public ContainerKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    public void configure(ElementDefinition elementDefinition) {
        super.configure(elementDefinition);
        elements = childrenElementLoadHandle(elementDefinition);
    }

    @Override
    public Iterator<KiteElement> iterator() {
        return elements.iterator();
    }

    /**
     * 遍历组装子节点
     */
    public final void forEachAssemble(AssembleContext context) {
        for (KiteElement element : elements) {
            if (element instanceof SlotKiteElement) {
                // 特殊处理插槽节点 调用对应的插槽片段
                if (!context.slotStack.isEmpty()) {
                    // 先出栈处理子节点后再入栈
                    final Fragment fragment = context.slotStack.pop();
                    fragment.forEachAssemble(context);
                    context.slotStack.push(fragment);
                }
            } else {
                element.assemble(context);
            }
        }
    }

    /**
     * 处理子节点
     */
    protected List<KiteElement> childrenElementLoadHandle(ElementDefinition elementDefinition) {
        KiteElement previous = null;
        final List<KiteElement> children = elementDefinition.getChildren();
        final List<KiteElement> list = new ArrayList<>(children.size());
        for (KiteElement child : children) {
            // 发现else节点时需要拼接到上层的if节点
            if (child instanceof ElseKiteElement) {
                if (previous instanceof IfKiteElement) {
                    ((IfKiteElement) previous).setElseKiteElement((ElseKiteElement) child);
                }
            } else {
                previous = child;
                list.add(child);
            }
        }
        return Collections.unmodifiableList(list);
    }
}
