package com.github.developframework.kite.core.parser;

import com.github.developframework.kite.core.element.AbstractKiteElement;
import com.github.developframework.kite.core.element.Fragment;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.Template;
import com.github.developframework.kite.core.exception.InvalidAttributeException;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.exception.KiteParseException;
import com.github.developframework.kite.core.source.ConfigurationSource;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.ElementTag;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.core.structs.TemplatePackage;
import com.github.developframework.kite.core.utils.KiteUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ktl文件解析器
 *
 * @author qiushui on 2021-07-05.
 */
@RequiredArgsConstructor
public final class KtlParser extends Parser {

    private final int indent;

    @Override
    public List<TemplatePackage> read(ConfigurationSource configurationSource) throws IOException {
        final List<TemplatePackage> templatePackages = new LinkedList<>();
        final List<LineNode> lineNodes = readLineDatas(configurationSource);
        if (lineNodes.get(0).element.equals(ElementTag.TEMPLATE_PACKAGE.getTag())) {
            // 根节点是template-package
            for (LineNode lineNode : lineNodes) {
                final String namespace = lineNode.getAttributes().get(ElementDefinition.Attribute.NAMESPACE);
                TemplatePackage templatePackage = new TemplatePackage(namespace);
                templatePackages.add(templatePackage);
                processFragment(templatePackage, lineNode.getChildren());
            }
        } else {
            // 根节点是fragment
            TemplatePackage templatePackage = new TemplatePackage();
            templatePackages.add(templatePackage);
            processFragment(templatePackage, lineNodes);
        }
        return templatePackages;
    }

    /**
     * 处理片段
     *
     * @param templatePackage 模板包
     * @param nodes           节点
     */
    private void processFragment(TemplatePackage templatePackage, List<LineNode> nodes) {
        for (LineNode node : nodes) {
            if (node.element.equals(ElementTag.TEMPLATE.getTag())) {
                final String id = node.getAttributes().get(ElementDefinition.Attribute.ID);
                templatePackage.push((Template) readKiteElement(node, new FragmentLocation(templatePackage.getNamespace(), id)));
            } else if (node.element.equals(ElementTag.FRAGMENT.getTag())) {
                final String id = node.getAttributes().get(ElementDefinition.Attribute.ID);
                templatePackage.push((Fragment) readKiteElement(node, new FragmentLocation(templatePackage.getNamespace(), id)));
            }
        }
    }

    private KiteElement readKiteElement(LineNode node, FragmentLocation fragmentLocation) {
        checkAttributes(node);
        final Class<? extends AbstractKiteElement> clazz = kiteElementClasses.get(node.getElement());
        final List<KiteElement> children = childrenElements(node, fragmentLocation);
        final AbstractKiteElement kiteElement;
        try {
            kiteElement = clazz.getConstructor(FragmentLocation.class).newInstance(fragmentLocation);
        } catch (Exception e) {
            e.printStackTrace();
            throw new KiteException("KTL解析失败，错误位置在\"%s\"", fragmentLocation);
        }
        kiteElement.configure(new ElementDefinition(node.getAttributes(), children));
        return kiteElement;
    }

    private List<KiteElement> childrenElements(LineNode node, FragmentLocation fragmentLocation) {
        return node
                .getChildren()
                .stream()
                .map(n -> readKiteElement(n, fragmentLocation))
                .collect(Collectors.toList());
    }

    /**
     * 读取配置所有行数据
     */
    private List<LineNode> readLineDatas(ConfigurationSource configurationSource) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(configurationSource.getInputStream()));
        List<LineNode> roots = new LinkedList<>();
        final Map<Integer, LineNode> lastMap = new HashMap<>();
        String line;
        Integer startLevel = null;
        while ((line = reader.readLine()) != null) {
            // 跳过空行或注释行
            if (StringUtils.isBlank(line) || line.matches("^\\s*#")) continue;
            final LineNode node = new LineNode(line);
            final int level = node.getLevel();
            if (startLevel == null) {
                startLevel = level;
            }
            if (level == startLevel) {
                roots.add(node);
            }
            lastMap.computeIfPresent(level - 1, (k, v) -> {
                v.getChildren().add(node);
                return v;
            });
            lastMap.put(level, node);
        }
        return roots;
    }

    private void checkAttributes(LineNode node) {
        // 参数检测
        final Set<String> validAttributes = ElementTag.of(node.getElement()).getValidAttributes();
        final Set<String> attributes = node.attributes.keySet();
        for (String attribute : attributes) {
            if (!validAttributes.contains(attribute)) {
                throw new InvalidAttributeException(attribute, node.attributes.get(attribute), "不合法的属性");
            }
        }
    }

    /**
     * 行数据节点
     */
    @Getter
    @Setter
    private final class LineNode {

        // 层级
        private int level;
        // 节点名称
        private String element;
        // 属性
        private Map<String, String> attributes = new HashMap<>();
        // 子节点
        private List<LineNode> children = new LinkedList<>();

        private LineNode(String line) {
            level = computeLevel(line);
            final String[] parts = line.trim().split("\\s*:\\s*");
            element = parts[0];
            if (parts.length > 1) {
                final String[] parameterStrs = parts[1].split("\\s*,\\s*");
                for (String str : parameterStrs) {
                    final String[] kv = str.split("\\s*=\\s*");
                    if (kv.length != 2) {
                        throw new KiteException("ktl格式错误“%s”", line);
                    }
                    attributes.put(kv[0], KiteUtils.getLiteral(kv[1]).orElse(kv[1]));
                }
            }
        }

        /**
         * 计算层级
         */
        private int computeLevel(String line) {
            int i = 0, ind = 0;
            for (int length = line.length(); i < length; i++) {
                final char c = line.charAt(i);
                if (c == ' ') {
                    ind++;
                } else {
                    break;
                }
            }
            if (ind % indent == 0) {
                return ind / indent;
            }
            throw new KiteParseException("ktl解析失败，行“%s”缩进错误：%d", line, ind);
        }
    }
}
