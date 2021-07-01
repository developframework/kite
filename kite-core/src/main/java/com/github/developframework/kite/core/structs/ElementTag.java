package com.github.developframework.kite.core.structs;

import com.github.developframework.kite.core.element.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qiushui on 2021-06-29.
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ElementTag {

    IF("if", IfKiteElement.class),
    RAW("raw", RawKiteElement.class),
    LINK("link", LinkKiteElement.class),
    ELSE("else", ElseKiteElement.class),
    CASE("case", CaseKiteElement.class),
    SLOT("slot", SlotKiteElement.class),
    THIS("this", ThisKiteElement.class),
    TEMPLATE("template", Template.class),
    ARRAY("array", ArrayKiteElement.class),
    OBJECT("object", ObjectKiteElement.class),
    SWITCH("switch", SwitchKiteElement.class),
    INCLUDE("include", IncludeKiteElement.class),
    DEFAULT("default", DefaultKiteElement.class),
    ENUM_VALUE("enum", EnumValueKiteElement.class),
    RELEVANCE("relevance", RelevanceKiteElement.class),
    PROTOTYPE("prototype", PrototypeKiteElement.class),
    TEMPLATE_PACKAGE("template-package", null),
    PROPERTY("property", NormalPropertyKiteElement.class),
    PROPERTY_DATE("property-date", DatePropertyKiteElement.class),
    PROPERTY_ENUM("property-enum", EnumPropertyKiteElement.class),
    XML_ATTRIBUTE("xml-attribute", XmlAttributeKiteElement.class),
    OBJECT_VIRTUAL("object-virtual", VirtualObjectKiteElement.class),
    PROPERTY_BOOLEAN("property-boolean", BooleanPropertyKiteElement.class),
    PROPERTY_UNIXTIMESTAMP("property-unixtimestamp", UnixTimestampPropertyKiteElement.class);

    private final String tag;

    private final Class<? extends AbstractKiteElement> elementClass;

    public static Map<String, Class<? extends AbstractKiteElement>> buildMap() {
        Map<String, Class<? extends AbstractKiteElement>> map = new HashMap<>();
        for (ElementTag tag : ElementTag.values()) {
            if (tag.getElementClass() != null) {
                map.put(tag.getTag(), tag.getElementClass());
            }
        }
        return map;
    }
}
