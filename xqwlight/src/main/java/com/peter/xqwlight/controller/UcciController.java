package com.peter.xqwlight.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/ucci")
public class UcciController {

    // TODO：实现切面Apsect类

    @PostMapping("/returnint21812")
    public int returnint21812(HttpServletRequest request, HttpServletResponse response) {
        return 21812;
    }
}
