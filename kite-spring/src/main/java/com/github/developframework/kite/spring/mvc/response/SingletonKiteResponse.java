package com.github.developframework.kite.spring.mvc.response;

/**
 * 单例数据KiteResponse
 *
 * @param <T> 数据类型
 * @author qiuzhenhao
 */
public final class SingletonKiteResponse<T> extends EmptyKiteResponse {

    public SingletonKiteResponse(String dataName, T data) {
        super();
        this.dataModel.putData(dataName, data);
    }

    public SingletonKiteResponse(String namespace, String templateId, String dataName, T data) {
        super(namespace, templateId);
        this.dataModel.putData(dataName, data);
    }
}
