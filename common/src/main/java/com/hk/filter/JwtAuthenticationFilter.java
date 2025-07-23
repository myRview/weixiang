package com.hk.filter;

import com.hk.manager.TokenManager;
import com.hk.security.auth.JwtAuthenticationToken;
import com.hk.vo.user.UserCacheVo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * JWT认证过滤器
 *
 * @author huangkun
 * @date 2025/7/22 21:16
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenManager tokenManager;

    public JwtAuthenticationFilter(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        UserCacheVo userCacheVo = tokenManager.getLoginUser(request);
        if (userCacheVo != null) {
            tokenManager.verifyToken(userCacheVo);
            Set<String> permissions = userCacheVo.getPermissions();
            List<SimpleGrantedAuthority> authorityList = permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            // 创建Spring Security认证对象
            JwtAuthenticationToken authentication = new JwtAuthenticationToken(authorityList, userCacheVo);
            // 注入Security上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
