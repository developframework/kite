package com.github.developframework.kite.gson;

import com.github.developframework.kite.core.Framework;
import com.github.developframework.kite.core.KiteConfiguration;
import com.github.developframework.kite.core.Producer;
import com.github.developframework.kite.core.data.DataModel;
import com.github.developframework.kite.core.strategy.KitePropertyNamingStrategy;
import com.github.developframework.kite.core.strategy.NamingStrategy;
import com.github.developframework.kite.core.structs.TemplatePackage;
import com.google.gson.*;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 由gson框架实现
 *
 * @author qiushui on 2021-07-15.
 */
@RequiredArgsConstructor
public final class GsonFramework implements Framework<Gson> {

    private final Gson gson;

    public GsonFramework() {
        this.gson = new GsonBuilder()
                .serializeNulls()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> new JsonPrimitive(src.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) -> new JsonPrimitive(src.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .create();
    }

    @Override
    public KitePropertyNamingStrategy namingStrategy() {
        return NamingStrategy.UNDERLINE.getNamingStrategy();
    }

    @Override
    public Gson getCore() {
        return gson;
    }

    @Override
    public Producer buildProducer(KiteConfiguration configuration, DataModel dataModel, String namespace, String templateId) {
        return new GsonProducer(configuration, dataModel, namespace, templateId);
    }

    @Override
    public Producer buildProducer(KiteConfiguration configuration, DataModel dataModel, List<TemplatePackage> templatePackages) {
        return new GsonProducer(configuration, dataModel, templatePackages);
    }
}
