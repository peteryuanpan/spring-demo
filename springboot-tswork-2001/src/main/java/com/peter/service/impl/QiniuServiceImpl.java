package com.peter.service.impl;

import com.peter.service.QiniuService;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class QiniuServiceImpl implements QiniuService {

    @Value("${qiniu.tswork.ak}")
    private String ak;

    @Value("${qiniu.tswork.sk}")
    private String sk;

    @Value("${qiniu.tswork.endpoint}")
    private String endpoint;

    @Value("${qiniu.tswork.expires}")
    private int expires;

    public String getPrivateUrl(String path) {
        Auth auth = Auth.create(ak, sk);
        String url = endpoint + path;
        return auth.privateDownloadUrl(url, expires);
    }
}
