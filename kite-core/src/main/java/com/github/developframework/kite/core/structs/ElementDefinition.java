package com.github.developframework.kite.core.structs;

import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.element.KiteElement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 元素定义
 *
 * @author qiushui on 2021-06-24.
 */

@RequiredArgsConstructor
public final class ElementDefinition {

    public interface Attribute {
        String DATA = "data";
        String NAMESPACE = "namespace";
        String ID = "id";
        String ALIAS = "alias";
        String CONVERTER = "converter";
        String INNER_CONVERTER = "inner-converter";
        String COMPARATOR = "comparator";
        String LIMIT = "limit";
        String MAP = "map";
        String REL = "rel";
        String TYPE = "type";
        String NULL_HIDDEN = "null-hidden";
        String NULL_EMPTY = "null-empty";
        String ENUM_VALUE = "value";
        String ENUM_TEXT = "text";
        String MERGE_PARENT = "merge-parent";
        String UNIQUE = "unique";
        String CONDITION = "condition";
        String CHECK_DATA = "check-data";
        String CASE_TEST = "test";
        String EXTEND = "extend";
        String CHILDREN_NAMING_STRATEGY = "children-naming-strategy";
        String NAMING_STRATEGY = "naming-strategy";
        String XML_ROOT = "xml-root";
        String XML_ITEM = "xml-item";
        String XML_CDATA = "xml-cdata";
    }

    private final Map<String, String> attributes;

    @Getter
    private final List<KiteElement> children;

    public boolean getBoolean(String attributeName, boolean defaultValue) {
        final String value = getString(attributeName);
        return value == null ? defaultValue : Boolean.parseBoolean(value);
    }

    public DataDefinition getDataDefinition(String attributeName) {
        final String data = getString(attributeName);
        return data == null ? DataDefinition.EMPTY : new DataDefinition(data);
    }

    public String getString(String attributeName) {
        final String value = attributes.get(attributeName);
        return value == null ? null : value.trim();
    }

    public String getString(String attributeName, String defaultValue) {
        final String value = getString(attributeName);
        return StringUtils.isEmpty(value) ? defaultValue : value.trim();
    }

    public Integer getInteger(String attributeName) {
        final String value = getString(attributeName);
        return value == null ? null : Integer.parseInt(value);
    }

    public int getInteger(String attributeName, int defaultValue) {
        final Integer value = getInteger(attributeName);
        return value == null ? defaultValue : value;
    }

    public <T extends Enum<T>> T getEnum(String attributeName, Class<T> enumClass) {
        final String value = getString(attributeName);
        return value == null ? null : Enum.valueOf(enumClass, value.toUpperCase(Locale.ROOT).trim());
    }

    public <T extends Enum<T>> T getEnum(String attributeName, Class<T> enumClass, T defaultValue) {
        final T value = getEnum(attributeName, enumClass);
        return value == null ? defaultValue : value;
    }
}
