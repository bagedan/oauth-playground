package com.example.restservice.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)

//@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.oauth2Login().userInfoEndpoint().userAuthoritiesMapper(this.userAuthoritiesMapper());
        //.oidcUserService(this.oidcUserService());
        super.configure(http);
    }

    private GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return (authorities) -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            authorities.forEach(authority -> {
                if (authority instanceof OidcUserAuthority oidcUserAuthority) {
                    OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();
                    userInfo.getClaimAsStringList("groups")
                            .forEach(group ->
                                    mappedAuthorities.add(new SimpleGrantedAuthority(String.format("ROLE_%s", group.toUpperCase(Locale.ROOT)))));
                }
            });
            return mappedAuthorities;
        };
    }
}
