package com.peter.xqwlight.config;

import com.peter.xqwlight.util.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * 业务请求鉴权，除了认证请求（/login.html、/login等）外，其余请求都需要经过该类进行权限校验<br/><br/>
 *
 * 鉴权过程，先通过 userDetails 判断是否登录，而 userDetails 会通过认证过滤器提前传入 authentication，
 * 然后判断 请求URI路径 是否可通过鉴权
 */
@Component
public class AuthorizationAccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationAccessHandler.class);

    @Autowired
    UrlPermissionConfig urlPermissionConfig;

    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {

        boolean hasPermission = false;
        String username = ConfigUtils.getUsername(request, authentication);
        Object principal = authentication.getPrincipal();
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        // permitAllUrls 不需要认证则有权限
        for (String url : urlPermissionConfig.getPermitAllUrls()) {
            if (antPathMatcher.match(url, request.getRequestURI())) {
                hasPermission = true;
                break;
            }
        }

        // authenticatedUrls 需要认证才有权限
        if (!hasPermission && (principal instanceof UserDetails)) {
            for (String url : urlPermissionConfig.getAuthenticatedUrls()) {
                if (antPathMatcher.match(url, request.getRequestURI())) {
                    hasPermission = true;
                    break;
                }
            }
        }

        LOGGER.debug("Username [" + username + "] has" + (hasPermission ? "" : " NOT") + " permission to requestURI [" + request.getRequestURI() + "]");

        return hasPermission;
    }
}
