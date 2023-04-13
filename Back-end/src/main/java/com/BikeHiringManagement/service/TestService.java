package com.BikeHiringManagement.service;


import com.BikeHiringManagement.entity.Formula;
import com.BikeHiringManagement.entity.FormulaCoefficient;
import com.BikeHiringManagement.entity.FormulaVariable;
import com.BikeHiringManagement.repository.FormulaCoefficientRepository;
import com.BikeHiringManagement.repository.FormulaRepository;
import com.BikeHiringManagement.repository.FormulaVariableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
public class TestService {

    @Autowired
    FormulaRepository formulaRepository;

    @Autowired
    FormulaVariableRepository formulaVariableRepository;

    @Autowired
    FormulaCoefficientRepository formulaCoefficientRepository;

    public void test() throws ParseException {
//        createFormula();
    }

    public void createFormula() {

        // SAVE Formula
        Formula formula = new Formula();
        formula.setName("Motorbike Rental Formula");
        formula.setFormula("A * (((B - (B % 24)) / 24) + C)");
        formula.setCreatedUser("admin001");
        formula.setCreatedDate(new Date());
        Formula savedFormula = formulaRepository.save(formula);

        // SAVE Variable
        FormulaVariable formulaVariable = new FormulaVariable();
        formulaVariable.setFormulaId(savedFormula.getId());
        formulaVariable.setName("A");
        formulaVariable.setDescription("Total Bike Cost");
        formulaVariable.setCreatedUser("admin001");
        formulaVariable.setCreatedDate(new Date());
        formulaVariableRepository.save(formulaVariable);

        formulaVariable = new FormulaVariable();
        formulaVariable.setFormulaId(savedFormula.getId());
        formulaVariable.setName("B");
        formulaVariable.setDescription("Actual Total Hiring Hour");
        formulaVariable.setCreatedUser("admin001");
        formulaVariable.setCreatedDate(new Date());
        formulaVariableRepository.save(formulaVariable);

        formulaVariable = new FormulaVariable();
        formulaVariable.setFormulaId(savedFormula.getId());
        formulaVariable.setName("C");
        formulaVariable.setDescription("Coefficient");
        formulaVariable.setCreatedUser("admin001");
        formulaVariable.setCreatedDate(new Date());
        formulaVariableRepository.save(formulaVariable);

        // SAVE Coefficient
        FormulaCoefficient formulaCoefficient = new FormulaCoefficient();
        formulaCoefficient.setFormulaId(savedFormula.getId());
        formulaCoefficient.setLowerLimit(0.0);
        formulaCoefficient.setUpperLimit(1.0);
        formulaCoefficient.setCoefficient(0.0);
        formulaCoefficient.setCreatedUser("admin001");
        formulaCoefficient.setCreatedDate(new Date());
        formulaCoefficientRepository.save(formulaCoefficient);

        formulaCoefficient = new FormulaCoefficient();
        formulaCoefficient.setFormulaId(savedFormula.getId());
        formulaCoefficient.setLowerLimit(1.0);
        formulaCoefficient.setUpperLimit(7.0);
        formulaCoefficient.setCoefficient(0.5);
        formulaCoefficient.setCreatedUser("admin001");
        formulaCoefficient.setCreatedDate(new Date());
        formulaCoefficientRepository.save(formulaCoefficient);

        formulaCoefficient = new FormulaCoefficient();
        formulaCoefficient.setFormulaId(savedFormula.getId());
        formulaCoefficient.setLowerLimit(7.0);
        formulaCoefficient.setUpperLimit(24.0);
        formulaCoefficient.setCoefficient(1.0);
        formulaCoefficient.setCreatedUser("admin001");
        formulaCoefficient.setCreatedDate(new Date());
        formulaCoefficientRepository.save(formulaCoefficient);
    }
}
