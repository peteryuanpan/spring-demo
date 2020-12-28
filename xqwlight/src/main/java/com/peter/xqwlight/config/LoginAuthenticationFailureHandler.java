package com.peter.xqwlight.config;

import com.alibaba.fastjson.JSON;
import com.peter.xqwlight.model.HttpCode;
import com.peter.xqwlight.model.ResponseBody;
import com.peter.xqwlight.util.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登陆时，前端会发送/login请求到后端，请求Form Data中会带有username、password<br/><br/>
 *
 * 然后，后端会进行账号密码校验，若未通过，则会来到 LoginAuthenticationFailureHandler 处理，自定义返回 RESPONSE<br/><br/>
 *
 * 错误类型有很多种，比如 LockedException、CredentialsExpiredException、AccountExpiredException、DisabledException、BadCredentialsException、UsernameNotFoundException
 * 都是 AuthenticationException 的派生类
 */
@Component
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 获取 username
        String username = ConfigUtils.getUsername(request, null);

        // 登陆失败后，返回401
        response.setStatus(HttpCode.LOGIN_AUTH_FAIL.getCode());
        // 设置 content-type
        response.setContentType("application/json;charset=utf-8");

        // 设置ResponseBody
        ResponseBody responseBody = new ResponseBody();
        responseBody.setStatus(HttpCode.LOGIN_AUTH_FAIL.getCode());
        responseBody.setMsg(HttpCode.LOGIN_AUTH_FAIL.getMessage());

        // 以JSON形式返回ResponseBody
        response.getWriter().write(JSON.toJSONString(responseBody));
        response.getWriter().flush();

        LOGGER.info("Username [" + username + "] login authenticate failed. Response status code 401." +
            " Exception message [" + exception.getMessage() + "]" + " Exception type [" + exception.getClass().getName() + "]");
    }
}
