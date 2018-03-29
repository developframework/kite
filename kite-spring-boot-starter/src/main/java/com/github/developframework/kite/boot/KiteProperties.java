package com.github.developframework.kite.boot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kite")
@Getter
@Setter
public class KiteProperties {

    private String locations = "classpath*:kite/*.xml";

    private KiteJsonProperties json;

    private KiteXmlProperties xml;
}
