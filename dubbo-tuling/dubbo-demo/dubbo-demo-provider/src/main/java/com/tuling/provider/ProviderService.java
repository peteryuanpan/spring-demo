package com.tuling.provider;

import com.tuling.api.ProviderServiceInterface;
import com.tuling.api.User;
import org.apache.dubbo.config.annotation.Service;

@Service
public class ProviderService implements ProviderServiceInterface {

    public User getUser() {
        return new User("周瑜");
    }
}
