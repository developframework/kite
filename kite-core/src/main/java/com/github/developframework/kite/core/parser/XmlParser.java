package com.github.developframework.kite.core.parser;

import com.github.developframework.kite.core.element.AbstractKiteElement;
import com.github.developframework.kite.core.element.Fragment;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.Template;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.source.ConfigurationSource;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.ElementTag;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.core.structs.TemplatePackage;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Xml解析器
 *
 * @author qiushui on 2021-06-24.
 */
public final class XmlParser extends Parser {

    private final Map<String, Class<? extends AbstractKiteElement>> kiteElementClasses = ElementTag.buildMap();

    /**
     * 读取一份xml文档解析模板包
     *
     * @param configurationSource 配置源
     */
    public List<TemplatePackage> read(ConfigurationSource configurationSource) throws IOException {
        final List<TemplatePackage> templatePackages = new LinkedList<>();
        final SAXReader reader = new SAXReader();
        final Document document;
        try {
            document = reader.read(configurationSource.getInputStream());
        } catch (DocumentException e) {
            throw new KiteException("xml parse fail: %s", e.getMessage());
        }
        final Element rootElement = document.getRootElement();
        final List<Element> templatePackageElements = rootElement.elements(ElementTag.TEMPLATE_PACKAGE.getTag());
        for (Element element : templatePackageElements) {
            final String namespace = element.attributeValue(ElementDefinition.Attribute.NAMESPACE);
            final TemplatePackage templatePackage = new TemplatePackage(namespace);
            for (Element templateElement : element.elements(ElementTag.TEMPLATE.getTag())) {
                final String id = templateElement.attributeValue(ElementDefinition.Attribute.ID);
                templatePackage.push((Template) readKiteElement(templateElement, new FragmentLocation(namespace, id)));
            }
            for (Element fragmentElement : element.elements(ElementTag.FRAGMENT.getTag())) {
                final String id = fragmentElement.attributeValue(ElementDefinition.Attribute.ID);
                templatePackage.push((Fragment) readKiteElement(fragmentElement, new FragmentLocation(namespace, id)));
            }
            templatePackages.add(templatePackage);
        }
        return templatePackages;
    }

    private KiteElement readKiteElement(Element element, FragmentLocation fragmentLocation) {
        final List<KiteElement> children = childrenElements(element, fragmentLocation);
        final Class<? extends AbstractKiteElement> clazz = kiteElementClasses.get(element.getName());
        final AbstractKiteElement kiteElement;
        try {
            kiteElement = clazz.getConstructor(FragmentLocation.class).newInstance(fragmentLocation);
        } catch (Exception e) {
            e.printStackTrace();
            throw new KiteException("xml read failed in \"%s\"", fragmentLocation);
        }
        final Map<String, String> attributesMap = attributesMap(element);
        kiteElement.configure(new ElementDefinition(attributesMap, children));
        return kiteElement;
    }

    private Map<String, String> attributesMap(Element element) {
        Map<String, String> map = new HashMap<>();
        final Iterator<Attribute> iterator = element.attributeIterator();
        while (iterator.hasNext()) {
            final Attribute attribute = iterator.next();
            map.put(attribute.getName(), attribute.getValue());
        }
        return map;
    }

    private List<KiteElement> childrenElements(Element element, FragmentLocation fragmentLocation) {
        return element.elements()
                .stream()
                .map(e -> readKiteElement(e, fragmentLocation))
                .collect(Collectors.toList());
    }
}
