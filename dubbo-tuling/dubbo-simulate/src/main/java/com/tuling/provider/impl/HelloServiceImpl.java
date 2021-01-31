package com.tuling.provider.impl;

import com.tuling.provider.api.HelloService;

public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String userName) {
        return "Hello: " + userName;
    }
}
