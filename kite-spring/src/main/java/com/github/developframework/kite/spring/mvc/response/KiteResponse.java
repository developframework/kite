package com.github.developframework.kite.spring.mvc.response;

import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.spring.mvc.annotation.TemplateType;
import lombok.Getter;

/**
 * Kite的响应对象
 *
 * @author qiuzhenhao
 */
@Getter
public abstract class KiteResponse {

    /* 命名空间 */
    protected String namespace;
    /* 模板ID */
    protected String templateId;
    /* 模板类型 */
    protected TemplateType templateType;
    /* 数据模型 */
    protected DataModel dataModel;

    public KiteResponse(String namespace, String templateId, DataModel dataModel, TemplateType templateType) {
        this.namespace = namespace;
        this.templateId = templateId;
        this.dataModel = dataModel;
        this.templateType = templateType;
    }

    public KiteResponse(String namespace, String templateId, DataModel dataModel) {
        this(namespace, templateId, dataModel, TemplateType.JSON);
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

    /**
     * 设置type
     *
     * @param templateType
     * @return
     */
    public KiteResponse type(TemplateType templateType) {
        this.templateType = templateType;
        return this;
    }
}
