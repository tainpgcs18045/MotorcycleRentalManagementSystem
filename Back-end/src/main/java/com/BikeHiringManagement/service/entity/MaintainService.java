package com.BikeHiringManagement.service.entity;
import com.BikeHiringManagement.constant.Constant;
import com.BikeHiringManagement.dto.PageDto;
import com.BikeHiringManagement.entity.*;
import com.BikeHiringManagement.model.request.*;
import com.BikeHiringManagement.model.response.AttachmentResponse;
import com.BikeHiringManagement.model.response.BikeResponse;
import com.BikeHiringManagement.model.response.MaintainResponse;
import com.BikeHiringManagement.model.temp.ComparedObject;
import com.BikeHiringManagement.model.temp.HistoryObject;
import com.BikeHiringManagement.model.temp.Result;
import com.BikeHiringManagement.repository.BikeRepository;
import com.BikeHiringManagement.repository.MaintainBikeRepository;
import com.BikeHiringManagement.repository.MaintainRepository;
import com.BikeHiringManagement.service.system.CheckEntityExistService;
import com.BikeHiringManagement.service.system.ResponseUtils;
import com.BikeHiringManagement.specification.BikeSpecification;
import com.BikeHiringManagement.specification.MaintainSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MaintainService {
    @Autowired
    MaintainRepository maintainRepository;
    @Autowired
    MaintainBikeRepository maintainBikeRepository;
    @Autowired
    BikeRepository bikeRepository;
    @Autowired
    BikeSpecification bikeSpecification;
    @Autowired
    MaintainSpecification maintainSpecification;
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CheckEntityExistService checkEntityExistService;
    @Autowired
    HistoryService historyService;

    public PageDto getMaintainPagination(PaginationMaintainRequest paginationMaintainRequest) {
        try {
            String searchKey = paginationMaintainRequest.getSearchKey();
            Integer page = paginationMaintainRequest.getPage();
            Integer limit = paginationMaintainRequest.getLimit();
            String sortBy = paginationMaintainRequest.getSortBy();
            String sortType = paginationMaintainRequest.getSortType();
            Date dateTo = paginationMaintainRequest.getDateTo();
            Date dateFrom = paginationMaintainRequest.getDateFrom();

            Map<String, Object> mapResult = maintainSpecification.getMaintainPagination(searchKey, page, limit, sortBy, sortType, dateFrom, dateTo);
            List<MaintainResponse> listRes = (List<MaintainResponse>) mapResult.get("data");
            Long totalItems = (Long) mapResult.get("count");
            Integer totalPage = responseUtils.getPageCount(totalItems, limit);

            return PageDto.builder()
                    .content(listRes)
                    .numberOfElements(Math.toIntExact(totalItems))
                    .page(page)
                    .size(limit)
                    .totalPages(totalPage)
                    .totalElements(totalItems)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Result getMaintainById(Long id){
        try{
            Result result = new Result();
            if(!checkEntityExistService.isEntityExisted(Constant.MAINTAIN, "id", id)){
                return new Result(Constant.LOGIC_ERROR_CODE, "Maintain id " + id + " is invalid !!!");
            }

            Maintain maintain = maintainRepository.findMaintainByIdAndIsDeleted(id, Boolean.FALSE);
            MaintainResponse maintainResponse = modelMapper.map(maintain, MaintainResponse.class);

            // IF TYPE = GENERAL -> GET LIST BIKE
            if(maintain.getType().equalsIgnoreCase(Constant.STATUS_MAINTAIN_BIKE)){
                List<MaintainBike> listMaintainBike = maintainBikeRepository.findAllByMaintainIdAndIsDeleted(id,Boolean.FALSE);
                List<Long> listBikeID = listMaintainBike.stream().map(x -> x.getBikeId()).collect(Collectors.toList());
                Map<String, Object> mapBike = bikeSpecification.getBikeListById(listBikeID);
                List<BikeResponse> listRes = (List<BikeResponse>) mapBike.get("data");
                maintainResponse.setListBike(listRes);

                String stringListManualId = "";
                for(BikeResponse bike : listRes){
                    stringListManualId += bike.getBikeManualId() + ", ";
                }
                stringListManualId = stringListManualId.substring(0, stringListManualId.length() - 2);
                maintainResponse.setStringListManualId(stringListManualId);
            }

            result.setMessage("Get successfully!!!");
            result.setCode(Constant.SUCCESS_CODE);
            result.setObject(maintainResponse);
            return  result;
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "System error", null);
        }
    }

    public Result createMaintain(MaintainRequest maintainRequest, String username){
        try{
            String type = maintainRequest.getType();
            String stringListManualId = maintainRequest.getStringListManualId();

            List<String> listInvalidManualId = new ArrayList<>();
            List<Bike> listBike = new ArrayList<>();
            String error_message = null;

            // IF STATUS = BIKE -> check valid bike manual ID
            if(type.equalsIgnoreCase(Constant.STATUS_MAINTAIN_BIKE))
            {
                if(stringListManualId == null || stringListManualId.isEmpty()){
                    error_message = "Please input bike manual ID list";
                    return new Result(Constant.LOGIC_ERROR_CODE, error_message);
                }

                String[] arrManualId = stringListManualId.split(",");
                for (int i = 0; i < arrManualId.length; i++) {
                    // IF valid manual ID -> get Bike list
                    if(bikeRepository.existsByBikeManualIdAndIsDeleted(arrManualId[i].trim(), Boolean.FALSE)){
                        Bike bike = bikeRepository.findBikeByBikeManualId(arrManualId[i].trim());
                        listBike.add(bike);
                    }
                    // IF invalid manual ID -> add to error list
                    else{
                        listInvalidManualId.add(arrManualId[i]);
                    }
                }
            }

            // IF error list have value -> Throw error
            if(type.equalsIgnoreCase(Constant.STATUS_MAINTAIN_BIKE) && listInvalidManualId.size() > 0){
                error_message = "Invalid manual IDs: ";
                for(String id : listInvalidManualId){
                    error_message += id.trim() + ", ";
                }
                error_message = error_message.substring(0, error_message.length() - 2);
                return new Result(Constant.LOGIC_ERROR_CODE, error_message);
            }

            // Save maintain
            Maintain newMaintain = modelMapper.map(maintainRequest, Maintain.class);
            newMaintain.setCreatedDate(new Date());
            newMaintain.setCreatedUser(username);
            Maintain savedMaintain =  maintainRepository.save(newMaintain);

            // Save maintain detail
            List<MaintainBike> savedListMaintainBike = new ArrayList<>();
            if(type.equalsIgnoreCase(Constant.STATUS_MAINTAIN_BIKE) && listBike.size() > 0){
                List<MaintainBike> maintainBikeList = new ArrayList<>();
                for(Bike bike : listBike){
                    MaintainBike newMaintainBike = new MaintainBike();
                    newMaintainBike.setMaintainId(savedMaintain.getId());
                    newMaintainBike.setBikeId(bike.getId());
                    newMaintainBike.setCreatedDate(new Date());
                    newMaintainBike.setCreatedUser(username);
                    maintainBikeList.add(newMaintainBike);
                }
                savedListMaintainBike = maintainBikeRepository.saveAll(maintainBikeList);
            }

            // HISTORY FOR MAINTAIN
            HistoryObject historyObject = new HistoryObject();
            historyObject.setUsername(username);
            historyObject.setEntityId(savedMaintain.getId());
            historyService.saveHistory(Constant.HISTORY_CREATE, savedMaintain, historyObject);

            // HISTORY FOR MAINTAIN_BIKE
            for(MaintainBike maintainBike : savedListMaintainBike){
                HistoryObject childHistoryObject = new HistoryObject();
                childHistoryObject.setUsername(username);
                childHistoryObject.setEntityId(maintainBike.getId());
                historyService.saveHistory(Constant.HISTORY_CREATE, maintainBike, childHistoryObject);
            }

            return new Result(Constant.SUCCESS_CODE, "Create new maintain successfully");
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "Fail");
        }
    }

    public Result updateMaintain(MaintainRequest maintainRequest, String username){
        try{
            String error_message = null;
            List<String> listInvalidManualId = new ArrayList<>();
            List<Bike> listBike = new ArrayList<>();

            Long maintainId = maintainRequest.getId();
            String stringListManualId = maintainRequest.getStringListManualId();

            if(!checkEntityExistService.isEntityExisted(Constant.MAINTAIN, "id", maintainId)){
                return new Result(Constant.LOGIC_ERROR_CODE, "The maintain Id " + maintainId + " has not been existed!!!");
            }
            Maintain maintain = maintainRepository.findMaintainByIdAndIsDeleted(maintainId, Boolean.FALSE);

            List<MaintainBike> oldMaintainBikeList = maintainBikeRepository.findAllByMaintainIdAndIsDeleted(maintainId, Boolean.FALSE);
            List<MaintainBike> newMaintainBikeList = new ArrayList<>();

            // IF STATUS = BIKE -> check valid bike manual ID
            if(maintain.getType().equalsIgnoreCase(Constant.STATUS_MAINTAIN_BIKE))
            {
                if(stringListManualId == null || stringListManualId.isEmpty()){
                    error_message = "Please input bike manual ID list";
                    return new Result(Constant.LOGIC_ERROR_CODE, error_message);
                }

                String[] arrManualId = stringListManualId.split(",");
                for (int i = 0; i < arrManualId.length; i++) {
                    // IF valid manual ID -> get Bike list
                    if(bikeRepository.existsByBikeManualIdAndIsDeleted(arrManualId[i].trim(), Boolean.FALSE)){
                        Bike bike = bikeRepository.findBikeByBikeManualId(arrManualId[i].trim());
                        listBike.add(bike);
                    }
                    // IF invalid manual ID -> add to error list
                    else{
                        listInvalidManualId.add(arrManualId[i]);
                    }
                }

                // IF error list have value -> Throw error
                if(maintain.getType().equalsIgnoreCase(Constant.STATUS_MAINTAIN_BIKE) && listInvalidManualId.size() > 0){
                    error_message = "Invalid manual IDs: ";
                    for(String id : listInvalidManualId){
                        error_message += id.trim() + ", ";
                    }
                    error_message = error_message.substring(0, error_message.length() - 2);
                    return new Result(Constant.LOGIC_ERROR_CODE, error_message);
                }
            }

            // SAVE MAINTAIN
            maintain.setTitle(maintainRequest.getTitle());
            maintain.setDate(maintainRequest.getDate());
            maintain.setDescription(maintainRequest.getDescription());
            maintain.setCost(maintainRequest.getCost());
            maintain.setModifiedDate(new Date());
            maintain.setModifiedUser(username);
            maintainRepository.save(maintain);

            // SAVE MAINTAIN BIKE
            HashMap<Long, MaintainBike> hashMap = new HashMap<>();
            if(maintain.getType().equalsIgnoreCase(Constant.STATUS_MAINTAIN_BIKE) && listBike.size() > 0){

                maintainBikeRepository.updateIsDelete(maintainId);
                List<MaintainBike> savedMaintainBikeList = new ArrayList<>();

                for(Bike bike : listBike){
                    if(maintainBikeRepository.existsByMaintainIdAndBikeId(maintainId, bike.getId())){
                        MaintainBike maintainBike = maintainBikeRepository.findAllByMaintainIdAndBikeId(maintainId, bike.getId());

                        hashMap.put(maintainBike.getId(), maintainBike);

                        maintainBike.setCreatedDate(new Date());
                        maintainBike.setCreatedUser(username);
                        maintainBike.setIsDeleted(Boolean.FALSE);
                        savedMaintainBikeList.add(maintainBike);
                    }else{
                        MaintainBike newMaintainBike = new MaintainBike();
                        newMaintainBike.setMaintainId(maintainId);
                        newMaintainBike.setBikeId(bike.getId());
                        newMaintainBike.setCreatedDate(new Date());
                        newMaintainBike.setCreatedUser(username);
                        savedMaintainBikeList.add(newMaintainBike);
                    }
                }
                newMaintainBikeList = maintainBikeRepository.saveAll(savedMaintainBikeList);
            }

            // HISTORY FOR MAINTAIN
            HistoryObject historyMaintainObject = new HistoryObject();
            historyMaintainObject.setUsername(username);
            historyMaintainObject.setEntityId(maintainId);
            historyMaintainObject.getComparingMap().put("cost", new ComparedObject(maintain.getCost(), maintainRequest.getCost()));
            historyMaintainObject.getComparingMap().put("date", new ComparedObject(maintain.getDate(), maintainRequest.getDate()));
            historyMaintainObject.getComparingMap().put("description", new ComparedObject(maintain.getDescription(), maintainRequest.getDescription()));
            historyMaintainObject.getComparingMap().put("title", new ComparedObject(maintain.getTitle(), maintainRequest.getTitle()));
            historyService.saveHistory(Constant.HISTORY_UPDATE, maintain, historyMaintainObject);

            // HISTORY FOR MAINTAIN_BIKE
            if(maintain.getType().equalsIgnoreCase(Constant.STATUS_MAINTAIN_BIKE)){
                for(MaintainBike oldItem : oldMaintainBikeList){
                    if(!hashMap.containsKey(oldItem.getId())){
                        HistoryObject historyObjectDelete = new HistoryObject();
                        historyObjectDelete.setUsername(username);
                        historyObjectDelete.setEntityId(oldItem.getId());
                        historyService.saveHistory(Constant.HISTORY_DELETE, oldItem, historyObjectDelete);
                    }
                }
                for(MaintainBike newItem : newMaintainBikeList){
                    if(!hashMap.containsKey(newItem.getId())){
                        HistoryObject historyObjectCreate = new HistoryObject();
                        historyObjectCreate.setUsername(username);
                        historyObjectCreate.setEntityId(newItem.getId());
                        historyService.saveHistory(Constant.HISTORY_CREATE, newItem, historyObjectCreate);
                    }
                }
            }
            return new Result(Constant.SUCCESS_CODE, "Update new maintain successfully");
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "Fail");
        }
    }

    public Result deleteMaintainById(Long id, String username){
        try{
            if(!checkEntityExistService.isEntityExisted(Constant.MAINTAIN, "id", id)){
                return new Result(Constant.LOGIC_ERROR_CODE, "The maintain id " + id + " has not been existed!!!");
            }
            Maintain maintain = maintainRepository.findMaintainById(id);
            if(maintain.getIsDeleted() == Boolean.TRUE){
                return new Result(Constant.LOGIC_ERROR_CODE, "The maintain id " + id + " has not been existed!!!");
            }

            maintain.setModifiedUser(username);
            maintain.setModifiedDate(new Date());
            maintain.setIsDeleted(Boolean.TRUE);
            maintainRepository.save(maintain);

            HistoryObject historyObject = new HistoryObject();
            historyObject.setUsername(username);
            historyObject.setEntityId(maintain.getId());
            historyService.saveHistory(Constant.HISTORY_DELETE, maintain, historyObject);
            return new Result(Constant.SUCCESS_CODE, "Delete maintain successfully");
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "Fail");
        }
    }

}
