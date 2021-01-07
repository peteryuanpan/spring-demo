package com.peter;

import com.peter.model.Model;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringBootConditionApplication {

    private static void getBean(ConfigurableApplicationContext context, String name) {
        try {
            Object bean = context.getBean(name);
            System.out.println(bean.getClass().getName());
            if (bean instanceof Model)
                System.out.println("Model.a: " + ((Model)bean).a);
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootConditionApplication.class);
        getBean(context, "model1");
        getBean(context, "model2");
        getBean(context, "model3");
    }
}

/*
org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named 'model3' available
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.getBeanDefinition(DefaultListableBeanFactory.java:814)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getMergedLocalBeanDefinition(AbstractBeanFactory.java:1282)
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:297)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:202)
	at org.springframework.context.support.AbstractApplicationContext.getBean(AbstractApplicationContext.java:1108)
	at com.peter.SpringBootConditionApplication.getBean(SpringBootConditionApplication.java:14)
	at com.peter.SpringBootConditionApplication.main(SpringBootConditionApplication.java:27)
com.peter.model.Model
Model.a: 1
com.peter.model.Model
Model.a: 2
 */
