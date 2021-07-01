package com.github.developframework.kite.core.strategy;

import com.github.developframework.kite.core.Framework;

/**
 * 下划线命名策略
 *
 * @author qiushui on 2020-03-31.
 */
public class UnderlineKitePropertyNamingStrategy implements KitePropertyNamingStrategy {

    @Override
    public String propertyDisplayName(Framework<?> framework, String expressionString) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expressionString.length(); ++i) {
            char c = expressionString.charAt(i);
            if (i != 0 && c >= 'A' && c <= 'Z') {
                sb.append('_').append((char) (c + 32));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
