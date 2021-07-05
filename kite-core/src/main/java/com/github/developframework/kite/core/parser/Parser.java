package com.github.developframework.kite.core.parser;

import com.github.developframework.kite.core.element.AbstractKiteElement;
import com.github.developframework.kite.core.source.ConfigurationSource;
import com.github.developframework.kite.core.structs.ElementTag;
import com.github.developframework.kite.core.structs.TemplatePackage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 配置解析器
 *
 * @author qiushui on 2021-07-05.
 */
public abstract class Parser {

    protected final Map<String, Class<? extends AbstractKiteElement>> kiteElementClasses = ElementTag.buildMap();

    /**
     * 读取资源文件并解析成模板包
     *
     * @param configurationSource 配置源
     * @return 多模板包
     * @throws IOException IO异常
     */
    public abstract List<TemplatePackage> read(ConfigurationSource configurationSource) throws IOException;
}
