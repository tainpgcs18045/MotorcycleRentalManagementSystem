package com.BikeHiringManagement.service.entity;
import com.BikeHiringManagement.constant.Constant;
import com.BikeHiringManagement.dto.PageDto;
import com.BikeHiringManagement.entity.Bike;
import com.BikeHiringManagement.entity.BikeManufacturer;
import com.BikeHiringManagement.model.temp.ComparedObject;
import com.BikeHiringManagement.model.temp.HistoryObject;
import com.BikeHiringManagement.model.temp.Result;
import com.BikeHiringManagement.model.request.BikeManafacturerRequest;
import com.BikeHiringManagement.model.request.PaginationRequest;
import com.BikeHiringManagement.model.request.ObjectNameRequest;
import com.BikeHiringManagement.repository.BikeManufacturerRepository;
import com.BikeHiringManagement.repository.BikeRepository;
import com.BikeHiringManagement.service.system.CheckEntityExistService;
import com.BikeHiringManagement.service.system.ResponseUtils;
import com.BikeHiringManagement.specification.BikeManufacturerSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BikeManufacturerService {
    @Autowired
    BikeManufacturerRepository bikeManufacturerRepository;

    @Autowired
    BikeManufacturerSpecification bikeManufacturerSpecification;

    @Autowired
    BikeRepository bikeRepository;

    @Autowired
    ResponseUtils responseUtils;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CheckEntityExistService checkEntityExistService;

    @Autowired
    HistoryService historyService;

    public PageDto getBikeManufacturer(PaginationRequest filterObjectRequest) {
        try {
            Sort sort = responseUtils.getSort(filterObjectRequest.getSortBy(), filterObjectRequest.getSortType());
            Integer pageNum = filterObjectRequest.getPage() - 1;
            Page<BikeManufacturer> pageResult = bikeManufacturerRepository.findAll(bikeManufacturerSpecification.filterBikeManufacturer(filterObjectRequest.getSearchKey()), PageRequest.of(pageNum, filterObjectRequest.getLimit(), sort));
            return PageDto.builder()
                    .content(pageResult.getContent())
                    .numberOfElements(pageResult.getNumberOfElements())
                    .page(filterObjectRequest.getPage())
                    .size(pageResult.getSize())
                    .totalPages(pageResult.getTotalPages())
                    .totalElements(pageResult.getTotalElements())
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

    public Result getBikeManufacturerById(Long id){
        try{
            Result result = new Result();
            BikeManufacturer bikeManufacturer = bikeManufacturerRepository.findBikeManufacturerById(id);
            if(!bikeManufacturerRepository.existsById(id)){
                return new Result(Constant.LOGIC_ERROR_CODE, "Bike manufacturer id is invalid !!!");
            }

            result.setMessage("Get successful");
            result.setCode(Constant.SUCCESS_CODE);
            result.setObject(bikeManufacturer);
            return  result;

        }catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "System error", null);
        }
    }

    public Result createBikeManufacturer(ObjectNameRequest bikeManufacturerRequest){
        try{
            if(checkEntityExistService.isEntityExisted(Constant.BIKE_MANUFACTURER, "name", bikeManufacturerRequest.getName())){
                return new Result(Constant.LOGIC_ERROR_CODE, "The bike manufacturer has been existed!!!");
            }else{
                BikeManufacturer newBikeManufacturer = modelMapper.map(bikeManufacturerRequest, BikeManufacturer.class);
                newBikeManufacturer.setCreatedDate(new Date());
                newBikeManufacturer.setCreatedUser(bikeManufacturerRequest.getUsername());
                BikeManufacturer savedBikeManufacturer =  bikeManufacturerRepository.save(newBikeManufacturer);

                HistoryObject historyObject = new HistoryObject();
                historyObject.setUsername(bikeManufacturerRequest.getUsername());
                historyObject.setEntityId(savedBikeManufacturer.getId());
                historyService.saveHistory(Constant.HISTORY_CREATE, savedBikeManufacturer, historyObject);

                return new Result(Constant.SUCCESS_CODE, "Create new bike manufacturer successfully");
            }

        }catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "Fail");
        }
    }

    public Result updateBikeManufacturer(BikeManafacturerRequest bikeManufacturerRequest){
        try{
            if(!checkEntityExistService.isEntityExisted(Constant.BIKE_MANUFACTURER, "id", bikeManufacturerRequest.getId())){
                return new Result(Constant.LOGIC_ERROR_CODE, "The bike manufacturer has not been existed!!!");
            }

            Boolean isNameExisted = bikeManufacturerRepository.existsByNameAndIsDeleted(bikeManufacturerRequest.getName(), false);
            if(isNameExisted){
                BikeManufacturer bikeManufacturerCheckName = bikeManufacturerRepository.findBikeManufacturerByName(bikeManufacturerRequest.getName());
                if(bikeManufacturerCheckName.getId() != bikeManufacturerRequest.getId()){
                    return new Result(Constant.LOGIC_ERROR_CODE, "The bike manufacturer has not been existed!!!");
                }
            }

            BikeManufacturer bikeManufacturer = bikeManufacturerRepository.findBikeManufacturerById(bikeManufacturerRequest.getId());
            HistoryObject historyObject = new HistoryObject();
            historyObject.setUsername(bikeManufacturerRequest.getUsername());
            historyObject.setEntityId(bikeManufacturer.getId());
            historyObject.getComparingMap().put("name", new ComparedObject(bikeManufacturer.getName(), bikeManufacturerRequest.getName()));
            historyService.saveHistory(Constant.HISTORY_UPDATE, bikeManufacturer, historyObject);

            bikeManufacturer.setModifiedDate(new Date());
            bikeManufacturer.setModifiedUser(bikeManufacturerRequest.getUsername());
            bikeManufacturer.setName(bikeManufacturerRequest.getName());
            bikeManufacturerRepository.save(bikeManufacturer);
            return new Result(Constant.SUCCESS_CODE, "Update new bike manufacturer successfully");

        }catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "Fail");
        }
    }

    public Result deleteBikeManufacturer(Long id, String username){
        try{
            if(!checkEntityExistService.isEntityExisted(Constant.BIKE_MANUFACTURER, "id", id)){
                return new Result(Constant.LOGIC_ERROR_CODE, "The bike manufacturer has not been existed!!!");
            }

            BikeManufacturer bikeManufacturer = bikeManufacturerRepository.findBikeManufacturerById(id);
            if(bikeManufacturer.getIsDeleted() == true){
                return new Result(Constant.LOGIC_ERROR_CODE, "The bike manufacturer has not been existed!!!");
            }

            List<Bike> listBikeByManufacturerId = bikeRepository.findAllByBikeManufacturerIdAndIsDeleted(id, Boolean.FALSE);
            if(listBikeByManufacturerId.size() > 0){
                String error_message = bikeManufacturer.getName() + " is being used for motorcycles ";
                for(Bike bike : listBikeByManufacturerId){
                    error_message += bike.getBikeManualId() + ", ";
                }
                error_message = error_message.substring(0, error_message.length() - 2);
                error_message += " -> Please update the manufacturer for these bikes before continuing to delete this manufacturer.";
                return new Result(Constant.LOGIC_ERROR_CODE, error_message);
            }

            bikeManufacturer.setModifiedDate(new Date());
            bikeManufacturer.setModifiedUser(username);
            bikeManufacturer.setIsDeleted(true);
            bikeManufacturerRepository.save(bikeManufacturer);

            HistoryObject historyObject = new HistoryObject();
            historyObject.setUsername(username);
            historyObject.setEntityId(bikeManufacturer.getId());
            historyService.saveHistory(Constant.HISTORY_DELETE, bikeManufacturer, historyObject);
            return new Result(Constant.SUCCESS_CODE, "Delete bike manufacturer successfully");
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "Fail");
        }
    }
}
