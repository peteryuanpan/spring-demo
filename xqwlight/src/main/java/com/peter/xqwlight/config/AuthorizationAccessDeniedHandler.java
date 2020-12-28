package com.peter.xqwlight.config;

import com.alibaba.fastjson.JSON;
import com.peter.xqwlight.model.HttpCode;
import com.peter.xqwlight.model.ResponseBody;
import com.peter.xqwlight.util.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthorizationAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException, ServletException {
        // 获取 username
        String username = ConfigUtils.getUsername(request, null);

        // 登陆失败后，返回401
        response.setStatus(HttpCode.ACCESS_DENIED.getCode());
        // 设置 content-type
        response.setContentType("application/json;charset=utf-8");

        // 设置ResponseBody
        ResponseBody responseBody = new ResponseBody();
        responseBody.setStatus(HttpCode.ACCESS_DENIED.getCode());
        responseBody.setMsg(HttpCode.ACCESS_DENIED.getMessage());

        // 以JSON形式返回ResponseBody
        response.getWriter().write(JSON.toJSONString(responseBody));
        response.getWriter().flush();

        LOGGER.info("Username [" + username + "] access denied with path [" + request.getRequestURI() + "]. Response status code 403." +
            " Exception message [" + exception.getMessage() + "]" + " Exception type [" + exception.getClass().getName() + "]");
    }
}
