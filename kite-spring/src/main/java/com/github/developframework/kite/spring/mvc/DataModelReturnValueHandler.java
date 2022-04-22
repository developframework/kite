package com.github.developframework.kite.spring.mvc;

import com.github.developframework.kite.core.KiteFactory;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.structs.FragmentLocation;
import com.github.developframework.kite.spring.KiteResponseBodyProcessor;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 处理DataModel的ReturnValueHandler
 *
 * @author qiuzhenhao
 */
public final class DataModelReturnValueHandler extends AnnotationKiteReturnValueHandler<DataModel> {

    public DataModelReturnValueHandler(KiteFactory kiteFactory, KiteResponseBodyProcessor kiteResponseBodyProcessor, Map<Method, FragmentLocation> fragmentLocationMap) {
        super(kiteFactory, kiteResponseBodyProcessor, fragmentLocationMap);
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
