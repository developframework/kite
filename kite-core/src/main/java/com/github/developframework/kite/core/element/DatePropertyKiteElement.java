package com.github.developframework.kite.core.element;

import com.github.developframework.kite.core.node.ObjectNodeProxy;
import com.github.developframework.kite.core.structs.ElementAttributes;
import com.github.developframework.kite.core.structs.ElementDefinition;
import com.github.developframework.kite.core.structs.FragmentLocation;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.time.*;
import java.util.HashSet;
import java.util.Set;

/**
 * property-date 日期时间元素
 *
 * @author qiushui on 2021-06-24.
 */
@ElementAttributes(ElementDefinition.Attribute.PATTERN)
public class DatePropertyKiteElement extends PropertyKiteElement {

    // 支持的类型集合
    private static final Set<Class<?>> ACCEPT_CLASS_SET = new HashSet<>();

    private String pattern;

    static {
        ACCEPT_CLASS_SET.add(java.util.Date.class);
        ACCEPT_CLASS_SET.add(java.util.Calendar.class);
        ACCEPT_CLASS_SET.add(java.sql.Date.class);
        ACCEPT_CLASS_SET.add(java.sql.Time.class);
        ACCEPT_CLASS_SET.add(java.sql.Timestamp.class);
        ACCEPT_CLASS_SET.add(LocalDate.class);
        ACCEPT_CLASS_SET.add(LocalDateTime.class);
        ACCEPT_CLASS_SET.add(LocalTime.class);
        ACCEPT_CLASS_SET.add(Instant.class);
    }

    public DatePropertyKiteElement(FragmentLocation fragmentLocation) {
        super(fragmentLocation);
    }

    @Override
    public void configure(ElementDefinition elementDefinition) {
        super.configure(elementDefinition);
        this.pattern = elementDefinition.getString(ElementDefinition.Attribute.PATTERN, "yyyy-MM-dd HH:mm:ss");
    }

    @Override
    protected boolean support(Class<?> sourceClass) {
        return ACCEPT_CLASS_SET.contains(sourceClass);
    }

    @Override
    protected void handle(ObjectNodeProxy parentNode, Object value, String displayName) {
        java.util.Date date = transformDate(value);
        if (date == null) {
            parentNode.putNull(displayName);
            return;
        }
        parentNode.putValue(displayName, DateFormatUtils.format(date, pattern), contentAttributes.xmlCDATA);
    }

    protected java.util.Date transformDate(Object value) {
        final Class<?> clazz = value.getClass();
        java.util.Date date = null;
        if (clazz == java.util.Date.class) {
            // java.util.Date
            date = (java.util.Date) value;
        } else if (clazz == java.sql.Date.class) {
            // java.sql.Date
            date = new java.util.Date(((java.sql.Date) value).getTime());
        } else if (clazz == java.sql.Time.class) {
            // java.sql.Time
            date = new java.util.Date(((java.sql.Time) value).getTime());
        } else if (clazz == java.sql.Timestamp.class) {
            // java.sql.Timestamp
            date = new java.util.Date(((java.sql.Timestamp) value).getTime());
        } else if (clazz == LocalDateTime.class) {
            // LocalDateTime
            date = java.util.Date.from(((LocalDateTime) value).atZone(ZoneId.systemDefault()).toInstant());
        } else if (clazz == LocalDate.class) {
            // LocalDate
            LocalTime localTime = LocalTime.of(0, 0, 0, 0);
            LocalDateTime localDateTime = LocalDateTime.of((LocalDate) value, localTime);
            date = java.util.Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        } else if (clazz == LocalTime.class) {
            // LocalTime
            LocalDate localDate = LocalDate.now();
            LocalDateTime localDateTime = LocalDateTime.of(localDate, (LocalTime) value);
            date = java.util.Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        } else if (clazz == Instant.class) {
            // Instant
            date = java.util.Date.from((Instant) value);
        }
        return date;
    }
}
