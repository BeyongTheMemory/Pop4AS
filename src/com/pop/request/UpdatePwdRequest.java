package com.pop.request;

/**
 * Created by xugang on 16/9/23.
 */
public class UpdatePwdRequest {
    public String oldPwd;
    public String newPwd;

    public UpdatePwdRequest(String oldPwd, String newPwd) {
        this.oldPwd = oldPwd;
        this.newPwd = newPwd;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}
