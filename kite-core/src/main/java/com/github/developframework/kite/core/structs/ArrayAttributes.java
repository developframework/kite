package com.github.developframework.kite.core.structs;

import com.github.developframework.expression.ExpressionUtils;
import com.github.developframework.kite.core.dynamic.KiteConverter;
import com.github.developframework.kite.core.exception.InvalidAttributeException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;

/**
 * @author qiushui on 2021-06-24.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ArrayAttributes {

    public KiteComponent<KiteConverter<Object, Object>> mapComponent;

    public KiteComponent<Comparator<Object>> comparatorComponent;

    public String relValue;

    public Integer limit;

    public boolean nullEmpty;

    public String xmlItem;

    public ArrayAttributes basic() {
        ArrayAttributes arrayAttributes = new ArrayAttributes();
        arrayAttributes.xmlItem = xmlItem;
        return arrayAttributes;
    }

    public static ArrayAttributes of(ElementDefinition elementDefinition) {
        ArrayAttributes arrayAttributes = new ArrayAttributes();
        arrayAttributes.limit = elementDefinition.getInteger(ElementDefinition.Attribute.LIMIT);
        arrayAttributes.relValue = elementDefinition.getString(ElementDefinition.Attribute.REL);
        arrayAttributes.nullEmpty = elementDefinition.getBoolean(ElementDefinition.Attribute.NULL_EMPTY, false);
        arrayAttributes.xmlItem = elementDefinition.getString(ElementDefinition.Attribute.XML_ITEM, "item");
        arrayAttributes.mapComponent = ContentAttributes.parseConverter(
                ElementDefinition.Attribute.MAP,
                elementDefinition.getString(ElementDefinition.Attribute.MAP)
        );
        arrayAttributes.comparatorComponent = parseComparator(
                ElementDefinition.Attribute.COMPARATOR,
                elementDefinition.getString(ElementDefinition.Attribute.COMPARATOR)
        );
        return arrayAttributes;
    }

    @SuppressWarnings("unchecked")
    private static KiteComponent<Comparator<Object>> parseComparator(String attributeName, String comparatorValue) {
        if (comparatorValue != null) {
            return new KiteComponent<>(
                    attributeName,
                    comparatorValue,
                    Comparator.class,
                    value -> {
                        /*
                            支持简单格式:
                                this.name ASC
                                this.name DESC
                                this DESC
                         */
                        if (value != null && value.startsWith("this")) {
                            final String[] parts = value.split("\\s+");
                            Comparator<Object> comparator;
                            if (parts[0].equals("this")) {
                                comparator = Comparator.comparing(v -> (Comparable<Object>) v);
                            } else {
                                final String expression = parts[0].substring(5);
                                if (!expression.equals("this.")) {
                                    throw new InvalidAttributeException(attributeName, comparatorValue, "格式不正确");
                                }
                                comparator = Comparator.comparing(v -> (Comparable<Object>) ExpressionUtils.getValue(v, expression));
                            }
                            if (parts.length == 2) {
                                if (parts[1].equalsIgnoreCase("DESC")) {
                                    comparator = comparator.reversed();
                                } else if (!parts[1].equalsIgnoreCase("ASC")) {
                                    throw new InvalidAttributeException(attributeName, comparatorValue, "格式不正确");
                                }
                            }
                            return comparator;
                        }
                        return null;
                    }
            );
        }
        return null;
    }
}

