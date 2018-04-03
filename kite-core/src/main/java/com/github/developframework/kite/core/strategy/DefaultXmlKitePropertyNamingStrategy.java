package com.github.developframework.kite.core.strategy;

import com.github.developframework.kite.core.KiteConfiguration;

/**
 * 默认的xml节点命名规则
 * AbcDef  abc-def
 * @author qiuzhenhao
 */
public class DefaultXmlKitePropertyNamingStrategy implements KitePropertyNamingStrategy{

    @Override
    public String propertyShowName(KiteConfiguration configuration, String expressionString) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < expressionString.length(); i++) {
            char ch = expressionString.charAt(i);
            if(ch >= 'A' && ch <= 'Z') {
                if(i > 0) {
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
