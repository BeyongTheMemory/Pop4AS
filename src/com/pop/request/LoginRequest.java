package com.pop.request;

/**
 * Created by xugang on 16/8/30.
 */
public class LoginRequest {
    private String account;
    private String password;
    private String ip;


    public LoginRequest(String account, String password, String ip) {
        this.account = account;
        this.password = password;
        this.ip = ip;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
