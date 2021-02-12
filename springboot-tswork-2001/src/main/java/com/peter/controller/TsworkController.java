package com.peter.controller;

import com.peter.service.QiniuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tswork")
public class TsworkController {

    private static final Logger LOG = LoggerFactory.getLogger(TsworkController.class);

    @Autowired
    private QiniuService qiniuService;

    @PostMapping("/privateurl")
    public String privateurl(@RequestBody String path) {
        return qiniuService.getPrivateUrl(path);
    }
}
