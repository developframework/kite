package com.github.developframework.kite.core.structs;

import com.github.developframework.kite.core.element.*;
import lombok.Getter;

import java.util.*;

/**
 * @author qiushui on 2021-06-29.
 */
@Getter
public enum ElementTag {

    IF("if", IfKiteElement.class),
    RAW("raw", RawKiteElement.class),
    LINK("link", LinkKiteElement.class),
    ELSE("else", ElseKiteElement.class),
    CASE("case", CaseKiteElement.class),
    SLOT("slot", SlotKiteElement.class),
    THIS("this", ThisKiteElement.class),
    FLAT("flat", FlatKiteElement.class),
    TEMPLATE("template", Template.class),
    FRAGMENT("fragment", Fragment.class),
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

    private final Set<String> validAttributes;

    ElementTag(String tag, Class<? extends AbstractKiteElement> elementClass) {
        this.tag = tag;
        this.elementClass = elementClass;
        validAttributes = getValidAttributes(elementClass);
    }

    public static Map<String, Class<? extends AbstractKiteElement>> buildMap() {
        Map<String, Class<? extends AbstractKiteElement>> map = new HashMap<>();
        for (ElementTag tag : ElementTag.values()) {
            if (tag.getElementClass() != null) {
                map.put(tag.getTag(), tag.getElementClass());
            }
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private static Set<String> getValidAttributes(Class<? extends AbstractKiteElement> clazz) {
        if (clazz == null) {
            return null;
        }
        Set<String> set = new HashSet<>();
        while (clazz != AbstractKiteElement.class) {
            final ElementAttributes annotation = clazz.getAnnotation(ElementAttributes.class);
            if (annotation != null) {
                set.addAll(Arrays.asList(annotation.value()));
                set.addAll(getValidAttributes(annotation.baseClass()));
            }
            clazz = (Class<? extends AbstractKiteElement>) clazz.getSuperclass();
        }
        return set;
    }

    public static ElementTag of(String tag) {
        for (ElementTag elementTag : ElementTag.values()) {
            if (elementTag.getTag().equals(tag)) {
                return elementTag;
            }
        }
        throw new AssertionError();
    }
}
