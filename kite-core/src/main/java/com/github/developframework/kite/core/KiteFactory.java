package com.github.developframework.kite.core;

import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.exception.KiteException;
import com.github.developframework.kite.core.parser.KtlParser;
import com.github.developframework.kite.core.source.StringConfigurationSource;
import com.github.developframework.kite.core.structs.TemplatePackage;
import lombok.Getter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Kite 工厂
 *
 * @author qiuzhenhao
 */
public class KiteFactory {

    @Getter
    private final KiteConfiguration configuration;
    @Getter
    private final KtlParser ktlParser;

    protected KiteFactory(KiteConfiguration configuration) {
        this.configuration = configuration;
        this.ktlParser = new KtlParser(configuration.getOptions().getKtlIndent());
    }

    public void useJsonFramework(Framework<?> framework) {
        configuration.setJsonFramework(framework);
    }

    public void useXmlFramework(Framework<?> framework) {
        configuration.setXmlFramework(framework);
    }

    /**
     * 获得Json生成器
     *
     * @return Json生成器
     */
    public Producer getJsonProducer(DataModel dataModel, String namespace, String templateId) {
        final Framework<?> framework = configuration.getJsonFramework();
        if (framework == null) {
            throw new KiteException("没有初始化序列化Json的Framework");
        }
        return framework.buildProducer(configuration, dataModel, namespace, templateId);
    }

    /**
     * 获得Json生成器
     *
     * @return Json生成器
     */
    public Producer getJsonProducer(DataModel dataModel, String... ktl) {
        final Framework<?> framework = configuration.getJsonFramework();
        if (framework == null) {
            throw new KiteException("没有初始化序列化Json的Framework");
        }
        try {
            final StringConfigurationSource configurationSource = new StringConfigurationSource(String.join("\n", ktl), StandardCharsets.UTF_8);
            final List<TemplatePackage> templatePackages = ktlParser.read(configurationSource);
            return framework.buildProducer(configuration, dataModel, templatePackages);
        } catch (IOException e) {
            e.printStackTrace();
            throw new KiteException("ktl解析失败");
        }
    }

    /**
     * 获得Xml生成器
     *
     * @return Xml生成器
     */
    public Producer getXmlProducer(DataModel dataModel, String namespace, String templateId) {
        final Framework<?> framework = configuration.getXmlFramework();
        if (framework == null) {
            throw new KiteException("没有初始化序列化Xml的Framework");
        }
        return framework.buildProducer(configuration, dataModel, namespace, templateId);
    }

    /**
     * 获得Xml生成器
     *
     * @return Xml生成器
     */
    public Producer getXmlProducer(DataModel dataModel, String... ktl) {
        final Framework<?> framework = configuration.getXmlFramework();
        if (framework == null) {
            throw new KiteException("没有初始化序列化Xml的Framework");
        }
        try {
            final StringConfigurationSource configurationSource = new StringConfigurationSource(String.join("\n", ktl), StandardCharsets.UTF_8);
            final List<TemplatePackage> templatePackages = ktlParser.read(configurationSource);
            return framework.buildProducer(configuration, dataModel, templatePackages);
        } catch (IOException e) {
            e.printStackTrace();
            throw new KiteException("ktl解析失败");
        }
    }
}
