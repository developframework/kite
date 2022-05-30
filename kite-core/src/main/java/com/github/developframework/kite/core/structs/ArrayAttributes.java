package com.github.developframework.kite.core.structs;

import com.github.developframework.kite.core.dynamic.KiteConverter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * @author qiushui on 2021-06-24.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ArrayAttributes {

    public KiteComponent<KiteConverter<Object, Object>> mapComponent;

    public String comparatorValue;

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
        arrayAttributes.comparatorValue = elementDefinition.getString(ElementDefinition.Attribute.COMPARATOR);
        arrayAttributes.limit = elementDefinition.getInteger(ElementDefinition.Attribute.LIMIT);
        arrayAttributes.relValue = elementDefinition.getString(ElementDefinition.Attribute.REL);
        arrayAttributes.nullEmpty = elementDefinition.getBoolean(ElementDefinition.Attribute.NULL_EMPTY, false);
        arrayAttributes.xmlItem = elementDefinition.getString(ElementDefinition.Attribute.XML_ITEM, "item");
        arrayAttributes.mapComponent = ContentAttributes.parseConverter(
                ElementDefinition.Attribute.MAP,
                elementDefinition.getString(ElementDefinition.Attribute.MAP)
        );
        return arrayAttributes;
    }
}

