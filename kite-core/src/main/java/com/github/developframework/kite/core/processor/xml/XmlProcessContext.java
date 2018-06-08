package com.github.developframework.kite.core.processor.xml;

import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.data.DataModel;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author qiuzhenhao
 */
@Getter
@Setter
public class XmlProcessContext {

    /* 数据模型 */
    private DataModel dataModel;

    /* 配置 */
    private KiteConfiguration configuration;

    /* 扩展口Map */
    private Map<String, ExtendPortXmlProcessor.ExtendCallback> extendPortMap = new HashMap<>();

    public void pushExtendCallback(String portName, ExtendPortXmlProcessor.ExtendCallback callback) {
        extendPortMap.put(portName, callback);
    }

    protected Optional<ExtendPortXmlProcessor.ExtendCallback> getExtendCallback(String port) {
        return Optional.ofNullable(extendPortMap.get(port));
    }
}
