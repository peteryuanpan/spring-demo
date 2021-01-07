package com.peter.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

import java.util.List;

public class OperateSystemCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        MultiValueMap<String, Object> attrs = metadata.getAllAnnotationAttributes(ConditionalOnOperateSystem.class.getName());
        if (attrs != null) {
            List<Object> values = attrs.get("value");
            if (values != null) {
                for (Object value : values) {
                    if ("windows".equals(value))
                        return true;
                }
            }
        }
        return false;
    }
}
