package com.github.developframework.kite.core.processor.json;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataModel;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 处理过程上下文
 * @author qiuzhenhao
 */
@Getter
@Setter
public class JsonProcessContext {

    /* 数据模型 */
    private DataModel dataModel;

    /* 配置 */
    private KiteConfiguration configuration;

    /* 扩展口Map */
    private Map<String, ExtendPortJsonProcessor.ExtendCallback> extendPortMap = new HashMap<>();

    public void pushExtendCallback(String portName, ExtendPortJsonProcessor.ExtendCallback callback) {
        extendPortMap.put(portName, callback);
    }

    protected Optional<ExtendPortJsonProcessor.ExtendCallback> getExtendCallback(String port) {
        return Optional.ofNullable(extendPortMap.get(port));
    }
}
