package com.peter.controller;

import com.peter.service.QiniuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qiniu")
public class QiniuController {

    private static final Logger LOG = LoggerFactory.getLogger(QiniuController.class);

    @Autowired
    private QiniuService qiniuService;

    @GetMapping("/privateurl")
    public String privateurl(@RequestParam String path) {
        return qiniuService.getPrivateUrl(path);
    }
}
