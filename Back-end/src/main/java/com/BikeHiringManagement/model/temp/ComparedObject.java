package com.BikeHiringManagement.model.temp;

import lombok.Data;

@Data
public class ComparedObject {
    private Object originalValue;
    private Object changedValue;

    public ComparedObject(){

    }

    public ComparedObject(Object originalValue, Object changedValue) {
        this.originalValue = originalValue;
        this.changedValue = changedValue;
    }
}
