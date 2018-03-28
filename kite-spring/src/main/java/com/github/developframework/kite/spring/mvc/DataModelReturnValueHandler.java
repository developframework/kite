package com.github.developframework.kite.spring.mvc;

import com.github.developframework.kite.core.KiteFactory;
import com.github.developframework.kite.core.data.DataModel;
import org.springframework.core.MethodParameter;

/**
 * 处理DataModel的ReturnValueHandler
 *
 * @author qiuzhenhao
 */
public final class DataModelReturnValueHandler extends AnnotationKiteReturnValueHandler<DataModel> {

    public DataModelReturnValueHandler(KiteFactory kiteFactory) {
        super(kiteFactory);
    }

    @Override
    protected Class<DataModel> returnType() {
        return DataModel.class;
    }

    @Override
    protected DataModel dataModel(DataModel returnValue, MethodParameter methodParameter) {
        return returnValue;
    }
}
