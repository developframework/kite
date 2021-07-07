package com.github.developframework.kite.core.parser;

import com.github.developframework.kite.core.element.AbstractKiteElement;
import com.github.developframework.kite.core.element.Fragment;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.Template;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.exception.KiteParseException;
import com.github.developframework.kite.core.source.ConfigurationSource;
import com.github.developframework.kite.core.source.StringConfigurationSource;
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
import java.nio.charset.StandardCharsets;
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
     * 只读取片段
     *
     * @param ktl
     * @return
     * @throws IOException
     */
    public List<Fragment> readFragment(String ktl) throws IOException {
        final List<LineNode> lineNodes = readLineDatas(new StringConfigurationSource(ktl, StandardCharsets.UTF_8));
        TemplatePackage templatePackage = new TemplatePackage();
        processFragment(templatePackage, lineNodes);
        return new ArrayList<>(templatePackage.values());
    }

    /**
     * 处理片段
     *
     * @param templatePackage
     * @param nodes
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
        final List<KiteElement> children = childrenElements(node, fragmentLocation);
        final Class<? extends AbstractKiteElement> clazz = kiteElementClasses.get(node.getElement());
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
        return node.getChildren()
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

        protected LineNode(String line) {
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
                    final String literal = KiteUtils.getLiteral(kv[1]);
                    attributes.put(kv[0], literal == null ? kv[1] : literal);
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
