package com.pop.response.pop;

import com.pop.model.PopInfoDto;
import com.pop.response.Response;

/**
 * Created by xugang on 16/10/11.
 */
public class PopInfoResponse extends Response {
    private PopInfoDto popInfoDto;

    public PopInfoDto getPopInfoDto() {
        return popInfoDto;
    }

    public void setPopInfoDto(PopInfoDto popInfoDto) {
        this.popInfoDto = popInfoDto;
    }
}
