package com.pop.response.user;

import com.pop.model.UserDto;
import com.pop.response.Response;

/**
 * Created by xugang on 16/8/31.
 */
public class LoginResponse extends Response {
    private UserDto userDto;
    private String sessionId;

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
