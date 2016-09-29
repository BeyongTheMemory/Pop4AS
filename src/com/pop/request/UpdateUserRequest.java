package com.pop.request;

/**
 * Created by xugang on 16/9/22.
 */
public class UpdateUserRequest {
    private String name;
    private String headUrl;
    private String introduction;
    private long phone;
    private String email;
    private boolean basic;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getBasic() {
        return basic;
    }

    public void setBasic(boolean basic) {
        this.basic = basic;
    }
}
