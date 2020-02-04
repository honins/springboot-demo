package com.hy.demo.data;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * JSON Web Token
 *
 * @author MrBird
 */
public class JwtToken implements AuthenticationToken {

    private static final long serialVersionUID = 1282057025599826155L;

    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return getToken();
    }

    @Override
    public Object getCredentials() {
        return getToken();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
