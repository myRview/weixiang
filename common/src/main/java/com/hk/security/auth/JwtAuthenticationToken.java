package com.hk.security.auth;

import com.hk.vo.user.UserCacheVo;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * JWT认证token
 * @author huangkun
 * @date 2025/7/23 9:35
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final UserCacheVo principal;

    public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities, UserCacheVo principal) {
        super(authorities);
        super.setAuthenticated(true);
        this.principal = principal;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public UserCacheVo getPrincipal() {
        return principal;
    }
}
