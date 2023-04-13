package com.BikeHiringManagement.model.request;
import com.BikeHiringManagement.model.response.FormulaVariableResponse;
import lombok.Data;

import java.util.List;

@Data
public class FormulaRequest {
    public Long id;
    public String username;

    public List<FormulaVariableResponse> listFormulaVariable;
}
