package com.peter.xqwlight.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseBody {

    private Integer status;
    private String msg;
    private Object result;
    private String token;
}
