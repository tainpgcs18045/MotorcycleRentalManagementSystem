package com.BikeHiringManagement.service.system;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResponseUtils {
    public ResponseEntity<?> getResponseEntity(Object data, int code, String mess, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        response.put("code", code);
        response.put("message", mess);
        return new ResponseEntity<>(response, status);
    }

    public Sort getSort(String sortBy, String type) {
        Sort sort = null;
        if(type.equals("ASC")){
            sort = Sort.by(sortBy).ascending();
        }
        if(type.equals("DESC")){
            sort = Sort.by(sortBy).descending();
        }
        return sort;
    }

    public Integer getPageCount(Long totalItems, Integer limit) {
        if (totalItems > 0 && totalItems % limit == 0) {
            return Math.toIntExact(totalItems / limit);
        } else if (totalItems > 0 && totalItems % limit> 0) {
            return Math.toIntExact((totalItems / limit) + 1);
        } else if (totalItems < limit) {
            return 1;
        }
        return 0;
    }
}
