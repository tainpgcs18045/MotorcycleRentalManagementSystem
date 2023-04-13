package com.BikeHiringManagement.service.entity;

import com.BikeHiringManagement.entity.History;
import com.BikeHiringManagement.model.temp.ComparedObject;
import com.BikeHiringManagement.model.temp.HistoryObject;
import com.BikeHiringManagement.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.*;

@Service
public class HistoryService {

    @Autowired
    HistoryRepository historyRepository;

    public void saveHistory(String actionType, Object object, HistoryObject historyObject) {
        History history = new History();
        history.setUserName(historyObject.getUsername());
        history.setDate(new Date());
        history.setActionType(actionType);
        switch (actionType) {
            case "LOGIN":
                historyRepository.save(history);
                break;
            case "CREATE":
            case "DELETE":
                history.setEntityName(object.getClass().getSimpleName());
                history.setEntityId(historyObject.getEntityId());
                historyRepository.save(history);
                break;
            case "UPDATE":
                HashMap<String, Object> result = getDiffObjectNewVersion(historyObject);
                List<History> saveList = new ArrayList<>();
                for (Entry<String, Object> entry : result.entrySet()) {
                    History historyUpdate = new History();
                    historyUpdate.setUserName(historyObject.getUsername());
                    historyUpdate.setDate(new Date());
                    historyUpdate.setActionType(actionType);
                    historyUpdate.setEntityName(object.getClass().getSimpleName());
                    historyUpdate.setEntityId(historyObject.getEntityId());
                    historyUpdate.setFieldName(entry.getKey());
                    historyUpdate.setPreviousValue(entry.getValue().toString());
                    saveList.add(historyUpdate);
                }
                historyRepository.saveAll(saveList);
                break;
        }
    }

    public HashMap<String, Object> getDiffObjectNewVersion (HistoryObject historyObject){
        HashMap<String, Object> result = new HashMap<>();
        for(Entry<String, ComparedObject> entry: historyObject.getComparingMap().entrySet()){
           if(!entry.getValue().getChangedValue().toString().equalsIgnoreCase(entry.getValue().getOriginalValue().toString()))
               result.put(entry.getKey(), entry.getValue().getOriginalValue());
        }
        return result;
    }
}
