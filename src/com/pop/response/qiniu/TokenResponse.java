package com.pop.response.qiniu;

import com.pop.response.Response;

/**
 * Created by xugang on 16/9/7.
 */
public class TokenResponse extends Response{
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
