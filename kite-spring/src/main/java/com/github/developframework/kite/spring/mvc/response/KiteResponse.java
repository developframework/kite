package com.github.developframework.kite.spring.mvc.response;

import com.github.developframework.kite.core.data.DataModel;
import lombok.Getter;

/**
 * Kite的响应对象
 *
 * @author qiuzhenhao
 */
@Getter
public class KiteResponse {

    /* 命名空间 */
    protected String namespace;
    /* 模板ID */
    protected String templateId;
    /* 数据模型 */
    protected DataModel dataModel;

    public KiteResponse(String namespace, String templateId, DataModel dataModel) {
        this.namespace = namespace;
        this.templateId = templateId;
        this.dataModel = dataModel;
    }

    /**
     * 放入数据
     *
     * @param dataName 数据名称
     * @param data     数据值
     * @return 对象自己
     */
    public KiteResponse data(String dataName, Object data) {
        dataModel.putData(dataName, data);
        return this;
    }
}
