package com.github.developframework.kite.core;

import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.exception.KiteException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Kite 工厂
 *
 * @author qiuzhenhao
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class KiteFactory {

    private final KiteConfiguration configuration;

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
            throw new KiteException("json framework uninitialized");
        }
        return framework.buildProducer(configuration, dataModel, namespace, templateId);
    }

    /**
     * 获得Xml生成器
     *
     * @return Xml生成器
     */
    public Producer getXmlProducer(DataModel dataModel, String namespace, String templateId) {
        final Framework<?> framework = configuration.getXmlFramework();
        if (framework == null) {
            throw new KiteException("xml framework uninitialized");
        }
        return framework.buildProducer(configuration, dataModel, namespace, templateId);
    }
}
