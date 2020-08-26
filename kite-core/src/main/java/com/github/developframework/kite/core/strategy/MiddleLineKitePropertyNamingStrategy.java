package com.github.developframework.kite.core.strategy;

import com.github.developframework.kite.core.KiteConfiguration;

/**
 * 中划线命名规则
 * AbcDef  abc-def
 *
 * @author qiuzhenhao
 */
public class MiddleLineKitePropertyNamingStrategy implements KitePropertyNamingStrategy {

    @Override
    public String propertyShowName(KiteConfiguration configuration, String expressionString) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expressionString.length(); i++) {
            char ch = expressionString.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                if (i > 0) {
                    sb.append('-');
                }
                sb.append((char)(ch + 32));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
