package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.TemplateLocation;
import com.github.developframework.kite.core.data.DataDefinition;
import lombok.Getter;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 属性节点
 *
 * @author qiuzhenhao
 */
public abstract class PropertyKiteElement extends ContainerKiteElement {

    @Getter
    protected boolean isXmlCdata;

    public PropertyKiteElement(KiteConfiguration configuration, TemplateLocation templateLocation, DataDefinition dataDefinition, String alias) {
        super(configuration, templateLocation, dataDefinition, alias);
    }

    public void setXmlCdata(String xmlCdataStr) {
        this.isXmlCdata = isNotBlank(xmlCdataStr) && Boolean.parseBoolean(xmlCdataStr);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = hash * 31 + templateLocation.hashCode();
        hash = hash * 31 + dataDefinition.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PropertyKiteElement)) {
            return false;
        }
        PropertyKiteElement otherPropertyElement = (PropertyKiteElement) obj;
        return dataDefinition.equals(otherPropertyElement.dataDefinition);
    }
}
