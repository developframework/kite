package com.github.developframework.kite.core.parser;

import com.github.developframework.kite.core.element.AbstractKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.exception.KiteParseException;
import com.github.developframework.kite.core.source.ConfigurationSource;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.ElementTag;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.core.structs.TemplatePackage;
import com.github.developframework.kite.core.utils.KiteUtils;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * ktl文件解析器
 *
 * @author qiushui on 2021-07-05.
 */
@RequiredArgsConstructor
public final class KtlParser extends Parser {

    private final Map<String, Class<? extends AbstractKiteElement>> kiteElementClasses = ElementTag.buildMap();

    private final int indent;

    @Override
    public List<TemplatePackage> read(ConfigurationSource configurationSource) throws IOException {
        final List<TemplatePackage> templatePackages = new LinkedList<>();
        final LineData[] lineDatas = readLineDatas(configurationSource);
        Stack<LineData> stack = new Stack<>();
        for (LineData lineData : lineDatas) {
            if (stack.isEmpty()) {
                stack.push(lineData);
            } else {
                final LineData prev = stack.peek();
                if (prev.level <= lineData.level) {
                    stack.push(lineData);
                } else {
                    final LineData ld = stack.peek();

                }
            }
        }
        return templatePackages;
    }

    private LineData[] readLineDatas(ConfigurationSource configurationSource) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(configurationSource.getInputStream()));
        List<LineData> list = new LinkedList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            // 跳过空行或注释行
            if (StringUtils.isBlank(line) || line.matches("^\\s*#")) continue;
            final LineData lineData = new LineData(line);
            list.add(lineData);
        }
        return list.toArray(LineData[]::new);
    }

    private int recursive(LineData[] lineDatas, LineData parent, int i) {
        if (i < lineDatas.length) {
            final LineData next = lineDatas[i];
            if (next.level > parent.level) {
                parent.children.add(next);
                i = recursive(lineDatas, next, i + 1);
            } else if (next.level == parent.level) {
                parent.children.add(next);
            } else {
                i = recursive(lineDatas, parent, i + 1);
            }
        }
        return i;
    }

    /**
     * 行数据
     */
    @Setter
    private final class LineData {

        // 层级
        private int level;
        // 节点类
        private Class<? extends AbstractKiteElement> elementClass;
        // 属性
        private Map<String, String> attributes = new HashMap<>();

        private List<LineData> children = new LinkedList<>();

        protected LineData(String line) {
            level = computeLevel(line);
            line = line.trim();
            final String[] parts = line.split("\\s*:\\s*");
            elementClass = kiteElementClasses.get(parts[0]);
            if (elementClass == null) {
                throw new KiteParseException("ktl解析失败，不存在“%s”节点", parts[0]);
            }
            if (parts.length > 1) {
                final String[] parameterStrs = parts[1].split("\\s*,\\s*");
                for (String str : parameterStrs) {
                    final String[] kv = str.split("\\s*=\\s*");
                    attributes.put(kv[0], KiteUtils.getLiteral(kv[1]));
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

        /**
         * 创建节点
         */
        public KiteElement createElement(FragmentLocation fragmentLocation, List<KiteElement> children) {
            try {
                AbstractKiteElement element = elementClass.getConstructor(FragmentLocation.class).newInstance(fragmentLocation);
                element.configure(new ElementDefinition(attributes, children));
                return element;
            } catch (Exception e) {
                throw new KiteException("ktl生成节点对象发生错误");
            }
        }
    }

}
