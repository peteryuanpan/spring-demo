package com.tuling.comsumer;

import com.tuling.framework.ProxyFactory;
import com.tuling.provider.api.HelloService;

public class Consumer {

    public static void main(String[] args) {

        HelloService helloService = ProxyFactory.getProxy(HelloService.class);
        String xxx = helloService.sayHello("xxx");
        System.out.println(xxx);


    }
}
