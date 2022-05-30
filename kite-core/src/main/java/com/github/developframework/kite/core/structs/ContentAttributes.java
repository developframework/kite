package com.github.developframework.kite.core.structs;

import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.dynamic.FieldKiteConverter;
import com.github.developframework.kite.core.dynamic.KiteConverter;
import com.github.developframework.kite.core.strategy.NamingStrategy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 内容节点属性
 *
 * @author qiushui on 2021-06-24.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ContentAttributes {

    public DataDefinition dataDefinition;

    public String alias;

    public KiteComponent<KiteConverter<Object, Object>> converterComponent;

    public boolean nullHidden;

    public NamingStrategy namingStrategy;

    public boolean xmlCDATA;

    public static ContentAttributes of(ElementDefinition elementDefinition) {
        ContentAttributes contentAttributes = new ContentAttributes();
        contentAttributes.dataDefinition = elementDefinition.getDataDefinition(ElementDefinition.Attribute.DATA);
        contentAttributes.alias = elementDefinition.getString(ElementDefinition.Attribute.ALIAS);
        contentAttributes.nullHidden = elementDefinition.getBoolean(ElementDefinition.Attribute.NULL_HIDDEN, false);
        contentAttributes.namingStrategy = elementDefinition.getEnum(ElementDefinition.Attribute.NAMING_STRATEGY, NamingStrategy.class);
        contentAttributes.xmlCDATA = elementDefinition.getBoolean(ElementDefinition.Attribute.XML_CDATA, false);
        contentAttributes.converterComponent = parseConverter(
                ElementDefinition.Attribute.CONVERTER,
                elementDefinition.getString(ElementDefinition.Attribute.CONVERTER)
        );
        return contentAttributes;
    }

    public static KiteComponent<KiteConverter<Object, Object>> parseConverter(String attributeName, String converterValue) {
        if (converterValue != null) {
            return new KiteComponent<>(
                    attributeName,
                    converterValue,
                    KiteConverter.class,
                    value -> value.startsWith("this.") ? new FieldKiteConverter(value.substring(5)) : null
            );
        }
        return null;
    }
}
