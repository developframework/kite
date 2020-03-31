package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.element.EnumPropertyKiteElement;
import com.github.developframework.kite.core.element.KiteElement;
import com.github.developframework.kite.core.exception.KiteException;
import org.dom4j.Element;

import java.util.Optional;

/**
 * @author qiushui on 2020-03-31.
 */
public class EnumPropertyXmlProcessor extends ContainerXmlProcessor<EnumPropertyKiteElement, Element> {

    public EnumPropertyXmlProcessor(XmlProcessContext xmlProcessContext, EnumPropertyKiteElement element) {
        super(xmlProcessContext, element);
    }

    @Override
    protected boolean prepare(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        Optional<Object> valueOptional = getDataValue(parentProcessor);
        if (valueOptional.isPresent()) {
            value = valueOptional.get();
            this.node = parentProcessor.node.addElement(element.showNameXML());
            return true;
        }
        if (!element.isNullHidden()) {
            this.node = parentProcessor.node.addElement(element.showNameXML());
        }
        return false;
    }

    @Override
    protected void handleCoreLogic(ContentXmlProcessor<? extends KiteElement, ? extends Element> parentProcessor) {
        String enumValue = value.toString();
        String enumText = element.getEnumText(enumValue);
        if (enumText == null) {
            throw new KiteException("No enum value for \"%s\" in template \"%s\".", enumValue, element.getTemplateLocation());
        }
        if (element.isXmlCdata()) {
            node.addCDATA(enumText);
        } else {
            node.addText(enumText);
        }
    }
}
