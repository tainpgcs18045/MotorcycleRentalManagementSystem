package com.BikeHiringManagement.model.temp;

import lombok.Data;

@Data
public class Result {
    private Integer code;
    private String message;
    private Object object;

    public Result() {
    }

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(Integer code, String message, Object object) {
        this.code = code;
        this.message = message;
        this.object = object;
    }
}
