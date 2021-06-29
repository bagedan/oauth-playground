package com.example.restservice;

import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OktaPrincipalExtractor
        implements PrincipalExtractor {

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        return null;
    }
}
