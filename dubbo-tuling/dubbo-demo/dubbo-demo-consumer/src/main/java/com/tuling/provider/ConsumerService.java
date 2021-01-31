package com.tuling.provider;

import com.alibaba.fastjson.JSONObject;
import com.tuling.api.ProviderServiceInterface;
import com.tuling.api.User;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    @Reference
    private ProviderServiceInterface providerService;

    public String test() {
        String result = HttpClient.get("http://localhost:8081/provider/service");
        User user = JSONObject.parseObject(result, User.class);
        return user.getUsername();
    }
}
