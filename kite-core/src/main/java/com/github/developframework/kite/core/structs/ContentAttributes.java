package com.github.developframework.kite.core.structs;

import com.github.developframework.kite.core.data.DataDefinition;
import com.github.developframework.kite.core.strategy.NamingStrategy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 内容节点属性
 *
 * @author qiushui on 2021-06-24.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ContentAttributes {

    public DataDefinition dataDefinition;

    public String alias;

    public String converterValue;

    public boolean nullHidden;

    public NamingStrategy namingStrategy;

    public boolean xmlCDATA;

    public static ContentAttributes of(ElementDefinition elementDefinition) {
        ContentAttributes contentAttributes = new ContentAttributes();
        contentAttributes.dataDefinition = elementDefinition.getDataDefinition(ElementDefinition.Attribute.DATA);
        contentAttributes.alias = elementDefinition.getString(ElementDefinition.Attribute.ALIAS);
        contentAttributes.converterValue = elementDefinition.getString(ElementDefinition.Attribute.CONVERTER);
        contentAttributes.nullHidden = elementDefinition.getBoolean(ElementDefinition.Attribute.NULL_HIDDEN, false);
        contentAttributes.namingStrategy = elementDefinition.getEnum(ElementDefinition.Attribute.NAMING_STRATEGY, NamingStrategy.class);
        contentAttributes.xmlCDATA = elementDefinition.getBoolean(ElementDefinition.Attribute.XML_CDATA, false);
        return contentAttributes;
    }
}
