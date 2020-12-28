package com.peter.xqwlight.config;

import com.peter.xqwlight.util.ConfigUtils;
import com.peter.xqwlight.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 关闭了 Session 认证的话，则 JWT 认证是一种可选的方式，
 * Session 认证由 UsernamePasswordAuthenticationFilter 负责，而 JWT认证由 JwtAuthenticationTokenFilter 负责<br/><br/>
 *
 * 所有请求都需要带有 Cookie loginToken 或者 请求头Authorization，且 值需要能被 JWT 解析，解析出来是 username，
 * 通过 username 构造出 userDetails（这一步会检查 username 是否合法），都通过后会传入 authentication
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Autowired
    UserDetailsService loginUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 获取 token
        String token = ConfigUtils.getToken(request);

        LOGGER.debug("RequestURI [" + request.getRequestURI() + "] takes token [" + token + "]");

        if (token != null) {
            // 通过 token 获取 username
            String username = ConfigUtils.getUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 通过 username 构造 userDetails
                UserDetails userDetails = loginUserDetailsService.loadUserByUsername(username);

                if (userDetails != null) {
                    // 生成 UsernamePasswordAuthenticationToken
                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    // 设置 details
                    WebAuthenticationDetails webAuthenticationDetails = new WebAuthenticationDetailsSource().buildDetails(request);
                    authentication.setDetails(webAuthenticationDetails);

                    // 给 SecurityContextHolder 设置 authentication
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        chain.doFilter(request, response);
    }
}
