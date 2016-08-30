package com.pop.response.user;

import com.pop.model.UserDto;
import com.pop.response.Response;

/**
 * Created by xugang on 16/8/31.
 */
public class UserDtoResponse extends Response {
    private UserDto userDto;

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }
}
