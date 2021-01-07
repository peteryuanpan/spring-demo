package com.peter.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OperateSystemCondition.class)
public @interface ConditionalOnOperateSystem {

    String value();
}
