package com.github.developframework.kite.core;

import com.github.developframework.kite.core.data.DataModel;

/**
 * 生产者接口
 * @author qiuzhenhao
 */
public interface Producer {

    /**
     * 生产
     * @param dataModel 数据模型
     * @param namespace 命名空间
     * @param templateId 模板ID
     * @return 字符串
     */
    String produce(DataModel dataModel, String namespace, String templateId);

    /**
     * 生产
     * @param dataModel 数据模型
     * @param namespace 命名空间
     * @param templateId 模板ID
     * @param isPretty 是否美化
     * @return 字符串
     */
    String produce(DataModel dataModel, String namespace, String templateId, boolean isPretty);
}
