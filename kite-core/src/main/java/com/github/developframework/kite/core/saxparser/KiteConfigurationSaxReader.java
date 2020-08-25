package com.github.developframework.kite.core.saxparser;

import com.github.developframework.kite.core.ConfigurationSource;
import com.github.developframework.kite.core.FileConfigurationSource;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.exception.KiteParseXmlException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * 配置解析器
 * @author qiuzhenhao
 */
@Slf4j
public class KiteConfigurationSaxReader {

    @Getter
    private final Set<ConfigurationSource> sources;

    public KiteConfigurationSaxReader(String config) {
        this.sources = new HashSet<>();
        sources.add(new FileConfigurationSource(config));
    }

    public KiteConfigurationSaxReader(ConfigurationSource source) {
        this.sources = new HashSet<>();
        sources.add(source);
    }

    public KiteConfigurationSaxReader(Set<ConfigurationSource> sources) {
        this.sources = sources;
    }

    /**
     * 读配置
     * @return 生成的配置项
     */
    public KiteConfiguration readConfiguration() {
        KiteConfiguration kiteConfiguration = new KiteConfiguration();
        ConfigurationSaxParseHandler handler = new ConfigurationSaxParseHandler(kiteConfiguration);
        for (ConfigurationSource source : sources) {
            handleSingleSource(handler, source);
            log.debug("Kite framework loaded the configuration source \"{}\".", source.getSourceName());
        }
        return kiteConfiguration;
    }

    /**
     * 处理单个源
     * @param handler handler
     * @param source 源
     */
    private void handleSingleSource(ConfigurationSaxParseHandler handler, ConfigurationSource source) {
        try {
            InputStream is = source.getInputStream();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(is, handler);
            is.close();
        } catch (Exception e) {
            throw new KiteParseXmlException("Kite Framework parse configuration source \"%s\" happened error: %s", source.getSourceName(), e.getMessage());
        }
    }
}
