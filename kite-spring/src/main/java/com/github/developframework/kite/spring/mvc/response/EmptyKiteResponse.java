package com.github.developframework.kite.spring.mvc.response;

import com.github.developframework.kite.core.data.DataModel;

/**
 * 空数据KiteResponse
 * @author qiuzhenhao
 */
public class EmptyKiteResponse extends KiteResponse {

    public EmptyKiteResponse() {
        this(null, null);
    }

    public EmptyKiteResponse(String namespace, String templateId) {
        super(namespace, templateId, new DataModel());
    }
}
