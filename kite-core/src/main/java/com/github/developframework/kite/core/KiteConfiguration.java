package com.github.developframework.kite.core;

import com.github.developframework.kite.core.structs.TemplatePackageRegistry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 配置
 *
 * @author qiushui on 2021-06-23.
 */
@Getter
@RequiredArgsConstructor
public final class KiteConfiguration {

    private final KiteOptions options;

    private final TemplatePackageRegistry templatePackageRegistry;

    @Setter
    private Framework<?> jsonFramework;
    @Setter
    private Framework<?> xmlFramework;

}
