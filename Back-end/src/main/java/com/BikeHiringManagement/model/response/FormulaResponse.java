package com.BikeHiringManagement.model.response;
import com.BikeHiringManagement.entity.FormulaCoefficient;
import com.BikeHiringManagement.entity.FormulaVariable;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class FormulaResponse {
    public Long id;

    public Date createdDate;
    public String createdUser;
    public Date modifiedDate;
    public String modifiedUser;
    public String formula;
    public String name;

    public List<FormulaCoefficientResponse> listFormulaCoefficient;

    public List<FormulaVariableResponse> listFormulaVariable;
}
