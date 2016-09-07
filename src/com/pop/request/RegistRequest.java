package com.pop.request;

/**
 * Created by xugang on 16/8/6.
 */
public class RegistRequest {
    private String account;
    private String password;

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

    public RegistRequest(String account, String password) {
        this.account = account;
        this.password = password;
    }
}
