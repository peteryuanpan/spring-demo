package com.peter.xqwlight.config;

import com.alibaba.fastjson.JSON;
import com.peter.xqwlight.model.HttpCode;
import com.peter.xqwlight.model.ResponseBody;
import com.peter.xqwlight.util.ConfigUtils;
import com.peter.xqwlight.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录时，前端会发送/login请求到后端，请求Form Data中会带有username、password<br/><br/>
 *
 * 然后，后端会进行账号密码校验，若通过，则会来到 LoginAuthenticationSuccessHandler 处理，自定义返回 RESPONSE<br/><br/>
 *
 * 这个类与 http.formLogin().defaultSuccessUrl("/index.html") 功能是冲突的
 */
@Component
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 获取 username
        String username = ConfigUtils.getUsername(request, authentication);

        // 登录成功后，返回200
        response.setStatus(HttpCode.SUCCESS.getCode());
        // 设置 content-type
        response.setContentType("application/json;charset=utf-8");

        // 生成token
        String token = JwtTokenUtil.generateToken(username);

        // 设置ResponseBody
        ResponseBody responseBody = new ResponseBody();
        responseBody.setStatus(HttpCode.SUCCESS.getCode());
        responseBody.setMsg(HttpCode.SUCCESS.getMessage());
        responseBody.setToken(token);

        // 以JSON形式返回ResponseBody
        response.getWriter().write(JSON.toJSONString(responseBody));
        response.getWriter().flush();

        LOGGER.info("Username [" + username + "] login authenticate successfully. Response status code 200 and BearerToken [" + token + "]");
    }
}
