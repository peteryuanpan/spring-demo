package com.peter.xqwlight.config;

import com.alibaba.fastjson.JSON;
import com.peter.xqwlight.model.HttpCode;
import com.peter.xqwlight.model.ResponseBody;
import com.peter.xqwlight.util.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录时，前端会发送/logout请求到后端，后端处理请求，返回200
 */
@Component
public class LoginLogoutSuccessHandler implements LogoutSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginLogoutSuccessHandler.class);

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 获取 username
        String username = ConfigUtils.getUsername(request, authentication);

        // 登出成功后，返回200
        response.setStatus(HttpCode.SUCCESS.getCode());
        // 设置 content-type
        response.setContentType("application/json;charset=utf-8");

        // 设置ResponseBody
        ResponseBody responseBody = new ResponseBody();
        responseBody.setStatus(HttpCode.SUCCESS.getCode());
        responseBody.setMsg(HttpCode.SUCCESS.getMessage());

        // 以JSON形式返回ResponseBody
        response.getWriter().write(JSON.toJSONString(responseBody));
        response.getWriter().flush();

        LOGGER.info("Username [" + username + "] logout successfully. Response status code 200");
    }
}
