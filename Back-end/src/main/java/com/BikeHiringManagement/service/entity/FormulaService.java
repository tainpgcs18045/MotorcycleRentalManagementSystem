package com.BikeHiringManagement.service.entity;
import com.BikeHiringManagement.constant.Constant;
import com.BikeHiringManagement.dto.PageDto;
import com.BikeHiringManagement.entity.*;
import com.BikeHiringManagement.model.request.*;
import com.BikeHiringManagement.model.response.*;
import com.BikeHiringManagement.model.temp.ComparedObject;
import com.BikeHiringManagement.model.temp.HistoryObject;
import com.BikeHiringManagement.model.temp.Result;
import com.BikeHiringManagement.repository.*;
import com.BikeHiringManagement.service.system.CheckEntityExistService;
import com.BikeHiringManagement.service.system.ResponseUtils;
import com.BikeHiringManagement.specification.BikeSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FormulaService {
    @Autowired
    FormulaRepository formulaRepository;

    @Autowired
    FormulaCoefficientRepository formulaCoefficientRepository;

    @Autowired
    FormulaVariableRepository formulaVariableRepository;
    @Autowired
    CheckEntityExistService checkEntityExistService;

    public Result getAllFormula() {
        try {
            Result result = new Result();
            FormulaResponse formulaResponse = new FormulaResponse();
            if (formulaRepository.existsAllByIsDeleted(Boolean.FALSE)){
                Formula formula = formulaRepository.findAllByIsDeleted(Boolean.FALSE);
                formulaResponse.setId(formula.getId());
                formulaResponse.setCreatedDate(formula.getCreatedDate());
                formulaResponse.setCreatedUser(formula.getCreatedUser());
                formulaResponse.setModifiedDate(formulaResponse.getModifiedDate());
                formulaResponse.setModifiedUser(formula.getModifiedUser());
                formulaResponse.setFormula(formula.getFormula());
                formulaResponse.setName(formulaResponse.getName());
                result.setMessage("Get successful");
                result.setCode(Constant.SUCCESS_CODE);
                result.setObject(formulaResponse);
                return  result;
            }
            else{
                return new Result(Constant.LOGIC_ERROR_CODE, "Cant find any formula in database!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "System error", null);
        }
    }

    public Result getFormulaById(Long formulaID){
        try{
            if(!formulaRepository.existsByIdAndIsDeleted(formulaID, Boolean.FALSE)){
                return new Result(Constant.LOGIC_ERROR_CODE, "Formula id is not exists in database!!!");
            }
            Result result = new Result();
            FormulaResponse formulaResponse = new FormulaResponse();

            Formula formula = formulaRepository.findFormulaByIdAndIsDeleted(formulaID, Boolean.FALSE);
            //formula entity
            formulaResponse.setId(formula.getId());
            formulaResponse.setCreatedDate(formula.getCreatedDate());
            formulaResponse.setCreatedUser(formula.getCreatedUser());
            formulaResponse.setModifiedDate(formulaResponse.getModifiedDate());
            formulaResponse.setModifiedUser(formula.getModifiedUser());
            formulaResponse.setFormula(formula.getFormula());
            formulaResponse.setName(formula.getName());

            //formula coefficient
            List<FormulaCoefficient> listFormulaCoefficient = formulaCoefficientRepository.findFormulaCoefficientByFormulaIdAndIsDeleted(formulaID, Boolean.FALSE);
            List<FormulaCoefficientResponse> listFormulaCoefficientResponse = new ArrayList<>();
            if(!listFormulaCoefficient.isEmpty()){
                for(FormulaCoefficient formulaCoefficient : listFormulaCoefficient){
                    FormulaCoefficientResponse formulaCoefficientResponse = new FormulaCoefficientResponse();
                    formulaCoefficientResponse.setCoefficient(formulaCoefficient.getCoefficient());
                    formulaCoefficientResponse.setUpperLimit(formulaCoefficient.getUpperLimit());
                    formulaCoefficientResponse.setLowerLimit(formulaCoefficient.getLowerLimit());
                    listFormulaCoefficientResponse.add(formulaCoefficientResponse);
                }
            }
            formulaResponse.setListFormulaCoefficient(listFormulaCoefficientResponse);

            //formula variable
            List<FormulaVariable> listFormulaVariable = formulaVariableRepository.findFormulaVariableByFormulaIdAndIsDeleted(formulaID, Boolean.FALSE);
            List<FormulaVariableResponse> listFormulaVariableResponse = new ArrayList<>();
            if(!listFormulaVariable.isEmpty()){
                for(FormulaVariable formulaVariable : listFormulaVariable){
                    FormulaVariableResponse formulaVariableResponse = new FormulaVariableResponse();
                    formulaVariableResponse.setName(formulaVariable.getName());
                    formulaVariableResponse.setDescription(formulaVariable.getDescription());
                    listFormulaVariableResponse.add(formulaVariableResponse);
                }
            }
            formulaResponse.setListFormulaVariable(listFormulaVariableResponse);

            result.setMessage("Get successful");
            result.setCode(Constant.SUCCESS_CODE);
            result.setObject(formulaResponse);
            return  result;
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "System error", null);
        }
    }
/*
    public Result updateVariable(FormulaRequest formulaRequest){
        try{
            if(!checkEntityExistService.isEntityExisted(Constant.FORMULA, "id", formulaRequest.getId())){
                return new Result(Constant.LOGIC_ERROR_CODE, "Formula ID has not been existed!!!");
            }
            List<FormulaVariable> listFormulaVariable = formulaVariableRepository.findFormulaVariableByFormulaIdAndIsDeleted(formulaRequest.getId(), Boolean.FALSE);
            List<FormulaVariableResponse> listFormulaVariableRequest = formulaRequest.getListFormulaVariable();

            if(!listFormulaVariable.isEmpty()){
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


            return new Result(Constant.SUCCESS_CODE, "Update new bike successfully");
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "Fail");
        }
    }

 */

}
