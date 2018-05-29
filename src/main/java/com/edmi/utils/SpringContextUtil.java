package com.edmi.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    //通过名字获取上下文中的bean
    public static <T> T getBean(String name){
        return (T) applicationContext.getBean(name);
    }

    //通过类型获取上下文中的bean
    public static  <T> T getBean(Class<?> requiredType){
        return (T)applicationContext.getBean(requiredType);
    }
}
