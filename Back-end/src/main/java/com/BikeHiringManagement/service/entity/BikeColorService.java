package com.BikeHiringManagement.service.entity;
import com.BikeHiringManagement.constant.Constant;
import com.BikeHiringManagement.dto.PageDto;
import com.BikeHiringManagement.entity.Bike;
import com.BikeHiringManagement.entity.BikeColor;
import com.BikeHiringManagement.model.temp.ComparedObject;
import com.BikeHiringManagement.model.temp.HistoryObject;
import com.BikeHiringManagement.model.temp.Result;
import com.BikeHiringManagement.model.request.BikeColorRequest;
import com.BikeHiringManagement.model.request.PaginationRequest;
import com.BikeHiringManagement.model.request.ObjectNameRequest;
import com.BikeHiringManagement.repository.BikeColorRepository;
import com.BikeHiringManagement.repository.BikeRepository;
import com.BikeHiringManagement.service.system.CheckEntityExistService;
import com.BikeHiringManagement.service.system.ResponseUtils;
import com.BikeHiringManagement.specification.BikeColorSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BikeColorService {
    @Autowired
    BikeColorRepository bikeColorRepository;

    @Autowired
    BikeColorSpecification bikeColorSpecification;

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

    public PageDto getBikeColor(PaginationRequest filterObjectRequest) {
        try {
            Sort sort = responseUtils.getSort(filterObjectRequest.getSortBy(), filterObjectRequest.getSortType());
            Integer pageNum = filterObjectRequest.getPage() - 1;
            Page<BikeColor> pageResult = bikeColorRepository.findAll(bikeColorSpecification.filterBikeColor(filterObjectRequest.getSearchKey()), PageRequest.of(pageNum, filterObjectRequest.getLimit(), sort));
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

    public Result getBikeColorById(Long id){
        try{
            Result result = new Result();
            BikeColor bikeColor = bikeColorRepository.findBikeColorById(id);
            if(!bikeColorRepository.existsById(id)){
                return new Result(Constant.LOGIC_ERROR_CODE, "Bike color id is invalid !!!");
            }

            result.setMessage("Get successful");
            result.setCode(Constant.SUCCESS_CODE);
            result.setObject(bikeColor);
            return  result;

        }catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "System error", null);
        }
    }

    public Result createBikeColor(ObjectNameRequest bikeColorRequest){
        try{
            if(checkEntityExistService.isEntityExisted(Constant.BIKE_COLOR, "name", bikeColorRequest.getName())){
                return new Result(Constant.LOGIC_ERROR_CODE, "The bike color has been existed!!!");
            }else{
                BikeColor newBikeColor = modelMapper.map(bikeColorRequest, BikeColor.class);
                newBikeColor.setCreatedDate(new Date());
                newBikeColor.setCreatedUser(bikeColorRequest.getUsername());
                BikeColor savedBikeColor =  bikeColorRepository.save(newBikeColor);

                HistoryObject historyObject = new HistoryObject();
                historyObject.setUsername(bikeColorRequest.getUsername());
                historyObject.setEntityId(savedBikeColor.getId());
                historyService.saveHistory(Constant.HISTORY_CREATE, savedBikeColor, historyObject);

                return new Result(Constant.SUCCESS_CODE, "Create new bike color successfully");
            }

        }catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "Fail");
        }
    }

    public Result updateBikeColor(BikeColorRequest bikeColorRequest){
        try{
            if(!checkEntityExistService.isEntityExisted(Constant.BIKE_COLOR, "id", bikeColorRequest.getId())){
                return new Result(Constant.LOGIC_ERROR_CODE, "The bike color has not been existed!!!");
            }

            Boolean isNameExisted = bikeColorRepository.existsByNameAndIsDeleted(bikeColorRequest.getName(), false);
            if(isNameExisted){
                BikeColor bikeColorCheckName = bikeColorRepository.findBikeColorByName(bikeColorRequest.getName());
                if(bikeColorCheckName.getId() != bikeColorRequest.getId()){
                    return new Result(Constant.LOGIC_ERROR_CODE, "The bike color has not been existed!!!");
                }
            }


            BikeColor bikeColor = bikeColorRepository.findBikeColorById(bikeColorRequest.getId());
            HistoryObject historyObject = new HistoryObject();
            historyObject.setUsername(bikeColorRequest.getUsername());
            historyObject.setEntityId(bikeColor.getId());
            historyObject.getComparingMap().put("name", new ComparedObject(bikeColor.getName(), bikeColorRequest.getName()));
            historyService.saveHistory(Constant.HISTORY_UPDATE, bikeColor, historyObject);

            bikeColor.setModifiedDate(new Date());
            bikeColor.setModifiedUser(bikeColorRequest.getUsername());
            bikeColor.setName(bikeColorRequest.getName());
            bikeColorRepository.save(bikeColor);
            return new Result(Constant.SUCCESS_CODE, "Update new bike color successfully");

        }catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "Fail");
        }
    }

    public Result deleteBikeColor(Long id, String username){
        try{
            if(!checkEntityExistService.isEntityExisted(Constant.BIKE_COLOR, "id", id)){
                return new Result(Constant.LOGIC_ERROR_CODE, "The bike color has not been existed!!!");
            }

            BikeColor bikeColor = bikeColorRepository.findBikeColorById(id);
            if(bikeColor.getIsDeleted() == true){
                return new Result(Constant.LOGIC_ERROR_CODE, "The bike color has not been existed!!!");
            }

            List<Bike> listBikeByColorId = bikeRepository.findAllByBikeColorIdAndIsDeleted(id, Boolean.FALSE);
            if(listBikeByColorId.size() > 0){
                String error_message = bikeColor.getName() + " is being used for motorcycles ";
                for(Bike bike : listBikeByColorId){
                    error_message += bike.getBikeManualId() + ", ";
                }
                error_message = error_message.substring(0, error_message.length() - 2);
                error_message += " -> Please update the color for these bikes before continuing to delete this color.";
                return new Result(Constant.LOGIC_ERROR_CODE, error_message);
            }

            bikeColor.setModifiedDate(new Date());
            bikeColor.setModifiedUser(username);
            bikeColor.setIsDeleted(true);
            bikeColorRepository.save(bikeColor);

            HistoryObject historyObject = new HistoryObject();
            historyObject.setUsername(username);
            historyObject.setEntityId(bikeColor.getId());
            historyService.saveHistory(Constant.HISTORY_DELETE, bikeColor, historyObject);
            return new Result(Constant.SUCCESS_CODE, "Delete bike color successfully");
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "Fail");
        }
    }
}
