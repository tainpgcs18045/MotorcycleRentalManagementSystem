package com.BikeHiringManagement.model.temp;

import com.BikeHiringManagement.model.temp.ComparedObject;
import lombok.Data;

import java.util.HashMap;

@Data
public class HistoryObject {
    private String username;
    private Long entityId;
    private HashMap<String, ComparedObject> comparingMap;

    public HistoryObject() {
        comparingMap= new HashMap<>();
    }


}
