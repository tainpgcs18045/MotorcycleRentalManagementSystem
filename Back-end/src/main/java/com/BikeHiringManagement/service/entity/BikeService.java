package com.BikeHiringManagement.service.entity;

import com.BikeHiringManagement.constant.Constant;
import com.BikeHiringManagement.dto.PageDto;
import com.BikeHiringManagement.entity.Bike;
import com.BikeHiringManagement.entity.BikeImage;
import com.BikeHiringManagement.entity.Order;
import com.BikeHiringManagement.entity.OrderDetail;
import com.BikeHiringManagement.model.temp.ComparedObject;
import com.BikeHiringManagement.model.temp.HistoryObject;
import com.BikeHiringManagement.model.temp.Result;
import com.BikeHiringManagement.model.request.AttachmentRequest;
import com.BikeHiringManagement.model.request.BikeRequest;
import com.BikeHiringManagement.model.request.PaginationBikeRequest;
import com.BikeHiringManagement.model.response.AttachmentResponse;
import com.BikeHiringManagement.model.response.BikeResponse;
import com.BikeHiringManagement.repository.*;
import com.BikeHiringManagement.service.system.CheckEntityExistService;
import com.BikeHiringManagement.service.system.ResponseUtils;
import com.BikeHiringManagement.specification.BikeSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BikeService {

    @Autowired
    BikeRepository bikeRepository;

    @Autowired
    BikeCategoryRepository bikeCategoryRepository;

    @Autowired
    BikeImageRepository bikeImageRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    BikeSpecification bikeSpecification;

    @Autowired
    ResponseUtils responseUtils;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    HistoryService historyService;

    @Autowired
    CheckEntityExistService checkEntityExistService;

    public PageDto getBikePagination(PaginationBikeRequest paginationBikeRequest) {
        try {
            String searchKey = paginationBikeRequest.getSearchKey();
            Integer page = paginationBikeRequest.getPage();
            Integer limit = paginationBikeRequest.getLimit();
            String sortBy = paginationBikeRequest.getSortBy();
            String sortType = paginationBikeRequest.getSortType();
            Long categoryId = paginationBikeRequest.getCategoryId();
            Boolean isInCart = paginationBikeRequest.getIsInCart();
            String username = paginationBikeRequest.getUsername();

            Map<String, Object> mapBike = bikeSpecification.getBikePagination(searchKey, page, limit, sortBy, sortType, categoryId, isInCart);
            List<BikeResponse> listRes = (List<BikeResponse>) mapBike.get("data");
            Long totalItems = (Long) mapBike.get("count");
            Integer totalPage = responseUtils.getPageCount(totalItems, limit);

            // Image handling
            List<BikeResponse> listResult = new ArrayList<>();
            for(BikeResponse bikeResponse : listRes){
                List<BikeImage> listImage = bikeImageRepository.findAllByBikeIdAndIsDeletedOrderByNameAsc(bikeResponse.getId(), false);

                if(!listImage.isEmpty()){

                    List<AttachmentResponse> listImageResponse = new ArrayList<>();
                    for(BikeImage bikeImage : listImage){
                        AttachmentResponse attachmentResponse = new AttachmentResponse();
                        attachmentResponse.setId(bikeImage.getId());
                        attachmentResponse.setFilePath(bikeImage.getPath());
                        attachmentResponse.setFileName(bikeImage.getName());
                        listImageResponse.add(attachmentResponse);
                    }
                    bikeResponse.setImageList(listImageResponse);
                }
                listResult.add(bikeResponse);
            }

            // Get orderId IF in CART
            if(isInCart != null)
            {
                if(orderRepository.existsByCreatedUserAndStatusAndIsDeleted(username, "IN CART", false))
                {
                    Order order = orderRepository.findByCreatedUserAndStatusAndIsDeleted(username, "IN CART", false);
                    List<OrderDetail> listOrderDetail = orderDetailRepository.findAllOrderDetailByOrderIdAndIsDeleted(order.getId(), false);
                    for(OrderDetail item : listOrderDetail)
                    {
                        for(BikeResponse bikeResponse : listResult)
                        {
                            if(bikeResponse.getId() == item.getBikeId())
                            {
                                bikeResponse.setOrderId(order.getId());
                            }
                        }
                    }
                }
            }

            return PageDto.builder()
                    .content(listResult)
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

    public Result getBikeById(Long bikeId){
        try{
            Result result = new Result();

            /*-------------- GET BIKE --------------------*/
            Map<String, Object> mapBike = bikeSpecification.getBikeById(bikeId);
            if(mapBike.size() == 0) {
                result.setMessage("No Bike found");
                result.setCode(Constant.LOGIC_ERROR_CODE);
                return  result;
            }

            BikeResponse bikeResponse = (BikeResponse) mapBike.get("data");
            List<BikeImage> listImage = bikeImageRepository.findAllByBikeIdAndIsDeletedOrderByNameAsc(bikeResponse.getId(),false);
            List<AttachmentResponse> listImageResponse = new ArrayList<>();
            if(!listImage.isEmpty()){
                for(BikeImage bikeImage : listImage){
                    AttachmentResponse attachmentResponse = new AttachmentResponse();
                    attachmentResponse.setId(bikeImage.getId());
                    attachmentResponse.setFilePath(bikeImage.getPath());
                    attachmentResponse.setFileName(bikeImage.getName());
                    listImageResponse.add(attachmentResponse);
                }
            }
            bikeResponse.setImageList(listImageResponse);

            /*-------------- GET RELATION BIKE LIST --------------------*/
            PaginationBikeRequest paginationBikeRequest = new PaginationBikeRequest();
            paginationBikeRequest.setSearchKey(null);
            paginationBikeRequest.setLimit(7);
            paginationBikeRequest.setPage(1);
            paginationBikeRequest.setSortBy("hiredNumber");
            paginationBikeRequest.setSortType("DESC");
            paginationBikeRequest.setCategoryId(bikeResponse.getBikeCategoryId());
            PageDto pageDto = getBikePagination(paginationBikeRequest);
            List<BikeResponse> listBike = pageDto.getContent();
            listBike = listBike.stream().filter(x -> x.getId() != bikeResponse.getId()).collect(Collectors.toList());
            bikeResponse.setListBike(listBike);


            result.setMessage("Get successful");
            result.setCode(Constant.SUCCESS_CODE);
            result.setObject(bikeResponse);
            return  result;

        }catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "System error", null);
        }
    }

    public Result createBike(BikeRequest bikeRequest, String username){
        try{
            if(bikeRepository.existsByBikeNoAndName(bikeRequest.getBikeNo(), bikeRequest.getName())){
                return new Result(Constant.LOGIC_ERROR_CODE, "The bike number has been existed!!!");
            }

            Bike newBike = modelMapper.map(bikeRequest, Bike.class);
            newBike.setCreatedDate(new Date());
            newBike.setCreatedUser(username);
            newBike.setStatus("AVAILABLE");
            newBike.setHiredNumber(0);
            Bike savedBike = bikeRepository.save(newBike);

            List<BikeImage> saveList = new ArrayList<>();
            for(AttachmentRequest item : bikeRequest.getFiles()){
                BikeImage bikeImage = new BikeImage();
                bikeImage.setBikeId(savedBike.getId());
                bikeImage.setName(item.getFileName());
                bikeImage.setPath(item.getFilePath());
                bikeImage.setCreatedDate(new Date());
                bikeImage.setCreatedUser(username);
                saveList.add(bikeImage);
            }

            bikeImageRepository.saveAll(saveList);

            HistoryObject historyObject = new HistoryObject();
            historyObject.setUsername(username);
            historyObject.setEntityId(savedBike.getId());
            historyService.saveHistory(Constant.HISTORY_CREATE, savedBike, historyObject);

            return new Result(Constant.SUCCESS_CODE, "Create new bike successfully");
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "Fail");
        }
    }

    public Result deleteBike(Long id, String username){
        try{
            // Check bike exist
            if(!checkEntityExistService.isEntityExisted(Constant.BIKE, "id", id)){
                return new Result(Constant.LOGIC_ERROR_CODE, "The bike id " + id + " has not been existed!!!");
            }
            Bike bike = bikeRepository.findBikeById(id);
            if(bike.getIsDeleted() == true){
                return new Result(Constant.LOGIC_ERROR_CODE, "The bike has not been existed!!!");
            }

            if(!bike.getStatus().equalsIgnoreCase("AVAILABLE")){
                return new Result(Constant.LOGIC_ERROR_CODE, "The bike is being hired! Please check status");
            }

            // REMOVE BIKE
            bike.setModifiedDate(new Date());
            bike.setModifiedUser(username);
            bike.setIsDeleted(true);
            bikeRepository.save(bike);

            // HISTORY FOR BIKE
            HistoryObject historyObject = new HistoryObject();
            historyObject.setUsername(username);
            historyObject.setEntityId(bike.getId());
            historyService.saveHistory(Constant.HISTORY_DELETE, bike, historyObject);

            // HISTORY FOR IMAGES
            List<BikeImage> removedBikeImage = bikeImageRepository.findAllByBikeIdAndIsDeletedOrderByNameAsc(bike.getId(), false);
            for(BikeImage image : removedBikeImage){
                HistoryObject historyBikeObjectImage = new HistoryObject();
                historyBikeObjectImage.setUsername(username);
                historyBikeObjectImage.setEntityId(image.getId());
                historyService.saveHistory(Constant.HISTORY_DELETE, image, historyBikeObjectImage);
            }

            // REMOVE BIKE IMAGES
            bikeImageRepository.updateIsDelete(bike.getId());

            return new Result(Constant.SUCCESS_CODE, "Delete bike successfully");
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "Fail");
        }
    }

    public Result updateBike(BikeRequest bikeRequest){
        try{
            if(!checkEntityExistService.isEntityExisted(Constant.BIKE, "id", bikeRequest.getId())){
                return new Result(Constant.LOGIC_ERROR_CODE, "The bike has not been existed!!!");
            }
            Bike bike = bikeRepository.findBikeById(bikeRequest.getId());

            //History Bike
            HistoryObject historyBikeObject = new HistoryObject();
            historyBikeObject.setUsername(bikeRequest.getUsername());
            historyBikeObject.setEntityId(bike.getId());
            historyBikeObject.getComparingMap().put("name", new ComparedObject(bike.getName(), bikeRequest.getName()));
            historyBikeObject.getComparingMap().put("bikeManualId", new ComparedObject(bike.getBikeManualId(), bikeRequest.getBikeManualId()));
            historyBikeObject.getComparingMap().put("bikeNo", new ComparedObject(bike.getBikeNo(), bikeRequest.getBikeNo()));
            historyBikeObject.getComparingMap().put("bikeCategoryId", new ComparedObject(bike.getBikeCategoryId(), bikeRequest.getBikeCategoryId()));
            historyBikeObject.getComparingMap().put("bikeColorId", new ComparedObject(bike.getBikeColorId(), bikeRequest.getBikeColorId()));
            historyBikeObject.getComparingMap().put("bikeManufacturerId", new ComparedObject(bike.getBikeManufacturerId(), bikeRequest.getBikeManufacturerId()));
            historyBikeObject.getComparingMap().put("status", new ComparedObject(bike.getStatus(), bikeRequest.getStatus()));
            historyBikeObject.getComparingMap().put("hiredNumber", new ComparedObject(bike.getHiredNumber(), bikeRequest.getHiredNumber()));
            historyService.saveHistory(Constant.HISTORY_UPDATE, bike, historyBikeObject);

            // Save bike
            bike.setModifiedDate(new Date());
            bike.setModifiedUser(bikeRequest.getUsername());
            bike.setName(bikeRequest.getName());
            bike.setBikeManualId(bikeRequest.getBikeManualId());
            bike.setBikeNo(bikeRequest.getBikeNo());
            bike.setBikeCategoryId(bikeRequest.getBikeCategoryId());
            bike.setBikeColorId(bikeRequest.getBikeColorId());
            bike.setBikeManufacturerId(bikeRequest.getBikeManufacturerId());
            bike.setStatus(bikeRequest.getStatus());
            bike.setHiredNumber(bikeRequest.getHiredNumber());
            bikeRepository.save(bike);

            // Save new image
            List<BikeImage> saveList = new ArrayList<>();
            for(AttachmentRequest item : bikeRequest.getFiles()){
                BikeImage bikeImage = new BikeImage();
                bikeImage.setBikeId(bike.getId());
                bikeImage.setName(item.getFileName());
                bikeImage.setPath(item.getFilePath());
                bikeImage.setCreatedDate(new Date());
                bikeImage.setCreatedUser(bikeRequest.getUsername());
                saveList.add(bikeImage);
            }
            List<BikeImage> savedList =  bikeImageRepository.saveAll(saveList);

            //History Image
            for(BikeImage image : savedList){
                HistoryObject historyBikeObjectImage = new HistoryObject();
                historyBikeObjectImage.setUsername(bikeRequest.getUsername());
                historyBikeObjectImage.setEntityId(image.getId());
                historyService.saveHistory(Constant.HISTORY_CREATE, image, historyBikeObjectImage);
            }

            return new Result(Constant.SUCCESS_CODE, "Update new bike successfully");
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "Fail");
        }
    }

    public Result deleteBikeImageById(Long imageId, String username){
        try{
            if(!checkEntityExistService.isEntityExisted(Constant.BIKE_IMAGE, "id", imageId)){
                return new Result(Constant.LOGIC_ERROR_CODE, "The bike image " + imageId +" has not been existed!!!");
            }

            BikeImage bikeImage = bikeImageRepository.findBikeImageById(imageId);
            if(bikeImage.getIsDeleted() == true){
                return new Result(Constant.LOGIC_ERROR_CODE, "The bike image " + imageId +" has not been existed!!!");
            }
            bikeImage.setModifiedDate(new Date());
            bikeImage.setModifiedUser(username);
            bikeImage.setIsDeleted(true);
            bikeImageRepository.save(bikeImage);

            HistoryObject historyObject = new HistoryObject();
            historyObject.setUsername(username);
            historyObject.setEntityId(imageId);
            historyService.saveHistory(Constant.HISTORY_DELETE, bikeImage, historyObject);
            return new Result(Constant.SUCCESS_CODE, "Delete bike image successfully");
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "Fail");
        }
    }

}
