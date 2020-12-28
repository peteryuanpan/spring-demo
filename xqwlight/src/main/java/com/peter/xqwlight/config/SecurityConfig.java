package com.peter.xqwlight.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService loginUserDetailsService;

    @Autowired
    AuthenticationSuccessHandler loginAuthenticationSuccessHandler;

    @Autowired
    AuthenticationFailureHandler loginAuthenticationFailureHandler;

    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    AuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    AuthorizationAccessDeniedHandler authorizationAccessDeniedHandler;

    @Autowired
    LogoutSuccessHandler loginLogoutSuccessHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            // 自定义用户信息生成器
            .userDetailsService(loginUserDetailsService)
            // 密码加密器
            .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 禁止 csrf，允许 POST 请求
        http.csrf().disable();

        // 表单方式登陆
        http.formLogin()
            // 登陆界面是 /login.html，允许访问，可以不认证
            .loginPage("/login.html").permitAll()
            // 登陆验证路径是 /login，允许访问，可以不认证
            .loginProcessingUrl("/login").permitAll()
            // 登陆成功后自定义返回 RESPONSE
            .successHandler(loginAuthenticationSuccessHandler)
            // 登陆失败后自定义返回 RESPONSE
            .failureHandler(loginAuthenticationFailureHandler);

        // 使用 JWT 认证，因此关闭 session 认证
        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // JWT 认证过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // JWT 认证失败后自定义返回 RESPONSE
        http.httpBasic()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint);

        // 业务请求鉴权
        http.authorizeRequests().anyRequest()
            .access("@authorizationAccessHandler.hasPermission(request,authentication)");

        // 业务请求鉴权失败后自定义返回RESPONSE
        http.exceptionHandling().accessDeniedHandler(authorizationAccessDeniedHandler);

        // 登出路径是 /logout
        http.logout().permitAll()
            // 登出成功后自定义返回RESPONSE
            .logoutSuccessHandler(loginLogoutSuccessHandler);
    }
}
