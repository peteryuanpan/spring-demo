package com.peter.xqwlight.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class ConfigUtils {

    /**
     * 通过 request 求得 token<br/><br/>
     *
     * 先查看是否带有 Cookie loginToken，若有解析出其值，
     * 若无，查看是否带有请求头Authorization，解析出其值<br/><br/>
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
        String token = null;
        if (request != null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("loginToken".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
            if (token == null) {
                token = request.getHeader("Authorization");
            }
        }
        return token;
    }

    /**
     * 通过 token 获得 username
     * @param token
     * @return
     */
    public static String getUsername(String token) {
        return JwtTokenUtil.pasreToken(token);
    }


    /**
     * 通过 request 或者 authentication 获得 username
     * @param request
     * @param authentication
     * @return
     */
    public static String getUsername(HttpServletRequest request, Authentication authentication) {
        String username = null;
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            }
        }
        if (username == null && request != null) {
            username = request.getParameter("username");
            if (username == null) {
                String token = getToken(request);
                if (token != null) {
                    username = getUsername(token);
                }
            }
        }
        return username;
    }
}
