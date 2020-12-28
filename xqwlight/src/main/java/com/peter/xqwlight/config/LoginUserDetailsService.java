package com.peter.xqwlight.config;

import com.peter.xqwlight.model.LoginUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 自定义用户信息生成器<br/><br/>
 *
 * 登陆时，前端会发送 /login 请求到后端，请求 Form Data 中会带有 username、password。
 * UserDetailsService 负责根据 username 生成一个 UserDetails，其中包含 username、password、Authorities 等信息。
 * 在 UserDetailsService 中，若发现 username 不存在，不应该返回 null，而应直接抛 UsernameNotFoundException，
 * 若 username 存在，则从数据库中读取数据，构造一个 UserDetails 并返回<br/><br/>
 *
 * 如果是 Session认证（默认），会交由 UsernamePasswordAuthenticationFilter 进行处理，进行密码校验等核实工作，若密码不匹配，会抛 BadCredentialsException<br/><br/>
 *
 * 如果是 JWT认证（自定义），可交由 JwtAuthenticationTokenFilter 进行处理，进行密码校验等核实工作，抛出异常等<br/><br/>
 *
 * 无论抛了任何异常，都表示认证不通过，则交由 AuthenticationFailureHandler 进行自定义 RESPONSE 返回，
 * 若认证通过了，则交由 AuthenticationSuccessHandler 进行自定义 RESPONSE 返回
 */
@Component
public class LoginUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginUserDetailsService.class);

    /**
     * 判断 username 是否在数据库中，
     * 若存在，则从数据库中读取数据，构造一个 UserDetails 并返回，
     * 若不存在，则 throw UsernameNotFoundException
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LoginUserDetails loginUserDetails = null;

        if ("admin".equals(username) || "peter".equals(username)) { // TODO：改用 mybatis + mysql
            loginUserDetails = new LoginUserDetails();
            loginUserDetails.setUsername(username);
            loginUserDetails.setPassword(new BCryptPasswordEncoder().encode(username));
        }

        LOGGER.debug("Username [" + username + "]" + (loginUserDetails == null ? " NOT" : "") + " found.");

        if (loginUserDetails == null) {
            throw new UsernameNotFoundException("");
        }

        return loginUserDetails;
    }
}
