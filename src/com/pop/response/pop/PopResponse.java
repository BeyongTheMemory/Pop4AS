package com.pop.response.pop;

import com.pop.model.PopDto;
import com.pop.response.Response;

import java.util.List;

/**
 * Created by xugang on 16/9/28.
 */
public class PopResponse extends Response {
    private List<PopDto> popDtoList;

    public List<PopDto> getPopDtoList() {
        return popDtoList;
    }

    public void setPopDtoList(List<PopDto> popDtoList) {
        this.popDtoList = popDtoList;
    }
}
