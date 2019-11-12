package com.github.developframework.kite.core.processor.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.developframework.kite.core.element.PropertyKiteElement;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.time.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 日期时间类型属性节点处理器
 * @author qiuzhenhao
 */
public class DatePropertyJsonProcessor extends PropertyJsonProcessor {

    // 支持的类型集合
    private static final Set<Class<?>> ACCEPT_CLASS_SET = new HashSet<>();

    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

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

    public DatePropertyJsonProcessor(JsonProcessContext context, PropertyKiteElement element) {
        this(context, element, null);
    }

    public DatePropertyJsonProcessor(JsonProcessContext context, PropertyKiteElement element, String pattern) {
        super(context, element);
        this.pattern = StringUtils.isEmpty(pattern) ? DEFAULT_PATTERN : pattern;
    }

    @Override
    protected boolean support(Class<?> sourceClass) {
        return ACCEPT_CLASS_SET.contains(sourceClass);
    }

    @Override
    protected void handle(ObjectNode parentNode, Class<?> clazz, Object value, String showName) {
        java.util.Date date = transformDate(clazz, value);
        if (Objects.isNull(date)) {
            parentNode.putNull(showName);
            return;
        }
        parentNode.put(showName, DateFormatUtils.format(date, pattern));
    }

    protected java.util.Date transformDate(Class<?> clazz, Object value) {
        java.util.Date date = null;
        if (clazz == java.util.Date.class) {
            // java.util.Date
            date = (java.util.Date) value;
        } else if (clazz == java.util.Calendar.class) {
            // java.util.Calendar
            date = ((java.util.Calendar) value).getTime();
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
