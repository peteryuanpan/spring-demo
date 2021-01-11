package com.peter.xqwlight.config;

import com.alibaba.fastjson.JSON;
import com.peter.xqwlight.model.HttpCode;
import com.peter.xqwlight.model.ResponseBody;
import com.peter.xqwlight.util.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 业务请求（除了登录认证请求外）都需要JWT 认证（代替 Session 认证），
 * 若认证失败，交由 JwtAuthenticationEntryPoint 处理，自定义返回 RESPONSE
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 获取 username
        String username = ConfigUtils.getUsername(request, null);

        // 登陆失败后，返回401
        response.setStatus(HttpCode.JWT_AUTH_FAIL.getCode());
        // 设置 content-type
        response.setContentType("application/json;charset=utf-8");

        // 设置ResponseBody
        ResponseBody responseBody = new ResponseBody();
        responseBody.setStatus(HttpCode.JWT_AUTH_FAIL.getCode());
        responseBody.setMsg(HttpCode.JWT_AUTH_FAIL.getMessage());

        // 以JSON形式返回ResponseBody
        response.getWriter().write(JSON.toJSONString(responseBody));
        response.getWriter().flush();

        LOGGER.info("Username [" + username + "] JWT authenticate failed with path [" + request.getRequestURI() + "]. Response status code 401." +
            " Exception message [" + exception.getMessage() + "]" + " Exception type [" + exception.getClass().getName() + "]");
    }
}
