package com.github.developframework.kite.core.parser;

import com.github.developframework.kite.core.element.AbstractKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.element.Template;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.source.ConfigurationSource;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.ElementTag;
import com.github.developframework.kite.core.structs.TemplateLocation;
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
 * @author qiushui on 2021-06-24.
 */
public final class ConfigurationXmlParser {

    private final Map<String, Class<? extends AbstractKiteElement>> kiteElementClasses = ElementTag.buildMap();

    /**
     * 读取一份xml文档解析模板包
     *
     * @param configurationSource 配置源
     */
    public List<TemplatePackage> read(ConfigurationSource configurationSource) throws IOException, DocumentException {
        final List<TemplatePackage> templatePackages = new LinkedList<>();
        final SAXReader reader = new SAXReader();
        final Document document = reader.read(configurationSource.getInputStream());
        final Element rootElement = document.getRootElement();
        final List<Element> templatePackageElements = rootElement.elements(ElementTag.TEMPLATE_PACKAGE.getTag());
        for (Element element : templatePackageElements) {
            final String namespace = element.attributeValue(ElementDefinition.Attribute.NAMESPACE);
            final TemplatePackage templatePackage = new TemplatePackage(namespace);
            for (Element templateElement : element.elements(ElementTag.TEMPLATE.getTag())) {
                final String id = templateElement.attributeValue(ElementDefinition.Attribute.ID);
                final TemplateLocation templateLocation = new TemplateLocation(namespace, id);
                final Template template = (Template) readKiteElement(templateElement, templateLocation);
                templatePackage.push(template);
            }
            templatePackages.add(templatePackage);
        }
        return templatePackages;
    }

    private KiteElement readKiteElement(Element element, TemplateLocation templateLocation) {
        final List<KiteElement> children = childrenElements(element, templateLocation);
        final Class<? extends AbstractKiteElement> clazz = kiteElementClasses.get(element.getName());
        final AbstractKiteElement kiteElement;
        try {
            kiteElement = clazz.getConstructor(TemplateLocation.class).newInstance(templateLocation);
        } catch (Exception e) {
            e.printStackTrace();
            throw new KiteException("xml read failed in \"%s\"", templateLocation);
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

    private List<KiteElement> childrenElements(Element element, TemplateLocation templateLocation) {
        return element.elements()
                .stream()
                .map(e -> readKiteElement(e, templateLocation))
                .collect(Collectors.toList());
    }
}
