package com.peter.config;

import com.peter.condition.ConditionalOnOperateSystem;
import com.peter.model.Model;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConditionConfig {

    @Bean
    public Model model1() {
        return new Model(1);
    }

    @Bean
    @ConditionalOnOperateSystem(value="windows")
    public Model model2() {
        return new Model(2);
    }

    @Bean
    @ConditionalOnOperateSystem(value="linux")
    public Model model3() {
        return new Model(3);
    }
}
