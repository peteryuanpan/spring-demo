package com.peter.xqwlight.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class AjaxController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AjaxController.class);

    private void printRequest(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.forEach((key, value) -> {
            StringBuilder sb = new StringBuilder();
            sb.append("key: " + key);
            sb.append(", value: [");
            for (int i = 0; i < value.length; i ++) {
                sb.append((i == 0 ? "" : " ") + value[i]);
            }
            sb.append("]\n");
            LOGGER.debug(sb.toString());
        });
        LOGGER.debug("Authorization: " + request.getHeader("Authorization"));
    }

    @GetMapping("/test/1.txt")
    public String path1() {
        return "hello world";
    }

    @PostMapping("/test/2.txt")
    public String path2(HttpServletRequest request, HttpServletResponse response) {
        printRequest(request);
        return "hello world";
    }

}