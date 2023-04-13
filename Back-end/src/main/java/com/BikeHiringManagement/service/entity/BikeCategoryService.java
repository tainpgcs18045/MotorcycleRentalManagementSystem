package com.BikeHiringManagement.service.entity;

import com.BikeHiringManagement.constant.Constant;
import com.BikeHiringManagement.dto.PageDto;
import com.BikeHiringManagement.entity.BikeCategory;
import com.BikeHiringManagement.model.temp.ComparedObject;
import com.BikeHiringManagement.model.temp.HistoryObject;
import com.BikeHiringManagement.model.temp.Result;
import com.BikeHiringManagement.model.request.BikeCategoryCreateRequest;
import com.BikeHiringManagement.model.request.PaginationRequest;
import com.BikeHiringManagement.repository.BikeCategoryRepository;
import com.BikeHiringManagement.service.system.CheckEntityExistService;
import com.BikeHiringManagement.service.system.ResponseUtils;
import com.BikeHiringManagement.specification.BikeCategorySpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BikeCategoryService {

    @Autowired
    BikeCategoryRepository bikeCategoryRepository;

    @Autowired
    BikeCategorySpecification bikeCategorySpecification;

    @Autowired
    ResponseUtils responseUtils;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CheckEntityExistService checkEntityExistService;

    @Autowired
    HistoryService historyService;

    public PageDto getBikeCategory(PaginationRequest filterObjectRequest) {
        try {
            Sort sort = responseUtils.getSort(filterObjectRequest.getSortBy(), filterObjectRequest.getSortType());
            Integer pageNum = filterObjectRequest.getPage() - 1;
            Page<BikeCategory> pageResult = bikeCategoryRepository.findAll(bikeCategorySpecification.filterBikeCategory(filterObjectRequest.getSearchKey()), PageRequest.of(pageNum, filterObjectRequest.getLimit(), sort));
            return PageDto.builder()
                    .content(pageResult.getContent())
                    .numberOfElements(pageResult.getNumberOfElements())
                    .page(filterObjectRequest.getPage())
                    .size(pageResult.getSize())
                    .totalPages(pageResult.getTotalPages())
                    .totalElements(pageResult.getTotalElements())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Result getBikeCategoryById(Long id) {
        try{
            Result result = new Result();
            if(!checkEntityExistService.isEntityExisted(Constant.BIKE_CATEGORY, "id", id)){
                return new Result(Constant.LOGIC_ERROR_CODE, "The bike category has not been existed!!!");
            }
            BikeCategory bikeCategory = bikeCategoryRepository.findBikeCategoriesById(id);
            result.setMessage("Get successful");
            result.setCode(Constant.SUCCESS_CODE);
            result.setObject(bikeCategory);
            return  result;
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "System error", null);
        }
    }

    public Result createBikeCategory(BikeCategoryCreateRequest bikeCategoryRequest){
        try{
            if(checkEntityExistService.isEntityExisted(Constant.BIKE_CATEGORY, "name", bikeCategoryRequest.getName())){
                return new Result(Constant.LOGIC_ERROR_CODE, "The bike category has not been existed!!!");
            }
            BikeCategory newBikeCategory = modelMapper.map(bikeCategoryRequest, BikeCategory.class);
            newBikeCategory.setCreatedDate(new Date());
            newBikeCategory.setCreatedUser(bikeCategoryRequest.getUsername());
            BikeCategory savedBikeCategory =  bikeCategoryRepository.save(newBikeCategory);

            HistoryObject historyObject = new HistoryObject();
            historyObject.setUsername(bikeCategoryRequest.getUsername());
            historyObject.setEntityId(savedBikeCategory.getId());
            historyService.saveHistory(Constant.HISTORY_CREATE, savedBikeCategory, historyObject);

            return new Result(Constant.SUCCESS_CODE, "Create new bike category successfully");

        }catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "Fail");
        }
    }

    public Result updateBikeCategory(BikeCategoryCreateRequest bikeCategoryRequest){
        try{
            if(!checkEntityExistService.isEntityExisted(Constant.BIKE_CATEGORY, "id", bikeCategoryRequest.getId())){
                return new Result(Constant.LOGIC_ERROR_CODE, "The bike category has not been existed!!!");
            }

            Boolean isNameExisted = bikeCategoryRepository.existsByNameAndIsDeleted(bikeCategoryRequest.getName(), false);
            if(isNameExisted){
                BikeCategory bikeCategoryCheckName = bikeCategoryRepository.findBikeCategoryByName(bikeCategoryRequest.getName());
                if(bikeCategoryCheckName.getId() != bikeCategoryRequest.getId()){
                    return new Result(Constant.LOGIC_ERROR_CODE, "The bike category has not been existed!!!");
                }
            }

            BikeCategory bikeCategory = bikeCategoryRepository.findBikeCategoriesById(bikeCategoryRequest.getId());
            HistoryObject historyObject = new HistoryObject();
            historyObject.setUsername(bikeCategoryRequest.getUsername());
            historyObject.setEntityId(bikeCategory.getId());
            historyObject.getComparingMap().put("name", new ComparedObject(bikeCategory.getName(), bikeCategoryRequest.getName()));
            historyObject.getComparingMap().put("price", new ComparedObject(bikeCategory.getPrice(), bikeCategoryRequest.getPrice()));
            historyService.saveHistory(Constant.HISTORY_UPDATE, bikeCategory, historyObject);

            bikeCategory.setModifiedDate(new Date());
            bikeCategory.setModifiedUser(bikeCategoryRequest.getUsername());
            bikeCategory.setPrice(bikeCategoryRequest.getPrice());
            bikeCategory.setName(bikeCategoryRequest.getName());
            bikeCategoryRepository.save(bikeCategory);
            return new Result(Constant.SUCCESS_CODE, "Update new bike category successfully");

        }catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "Fail");
        }
    }

    public Result deleteBikeCategory(Long id, String username){
        try{
            if(!checkEntityExistService.isEntityExisted(Constant.BIKE_CATEGORY, "id", id)){
                return new Result(Constant.LOGIC_ERROR_CODE, "The bike category has not been existed!!!");
            }

            BikeCategory bikeCategory = bikeCategoryRepository.findBikeCategoriesById(id);
            if(bikeCategory.getIsDeleted() == true){
                return new Result(Constant.LOGIC_ERROR_CODE, "The bike category has not been existed!!!");
            }
            bikeCategory.setModifiedDate(new Date());
            bikeCategory.setModifiedUser(username);
            bikeCategory.setIsDeleted(true);
            bikeCategoryRepository.save(bikeCategory);

            HistoryObject historyObject = new HistoryObject();
            historyObject.setUsername(username);
            historyObject.setEntityId(bikeCategory.getId());
            historyService.saveHistory(Constant.HISTORY_DELETE, bikeCategory, historyObject);
            return new Result(Constant.SUCCESS_CODE, "Delete bike category successfully");
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "Fail");
        }
    }

}
