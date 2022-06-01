package com.github.developframework.kite.core.parser;

import com.github.developframework.kite.core.source.ConfigurationSource;
import com.github.developframework.kite.core.structs.TemplatePackage;

import java.io.IOException;
import java.util.List;

/**
 * 配置解析器
 *
 * @author qiushui on 2021-07-05.
 */
public interface Parser {

    /**
     * 读取资源文件并解析成模板包
     *
     * @param configurationSource 配置源
     * @return 多模板包
     * @throws IOException IO异常
     */
    List<TemplatePackage> read(ConfigurationSource configurationSource) throws IOException;
}
