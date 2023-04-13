package com.BikeHiringManagement.repository;

import com.BikeHiringManagement.entity.FormulaCoefficient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FormulaCoefficientRepository extends JpaRepository<FormulaCoefficient, Long>, JpaSpecificationExecutor<FormulaCoefficient> {
    Boolean existsByFormulaIdAndUpperLimitGreaterThanAndLowerLimitLessThanEqualAndIsDeleted(Long formulaId, Double upperLimit, Double lowerLimit, Boolean check);

    FormulaCoefficient findFormulaCoefficientByFormulaIdAndUpperLimitGreaterThanAndLowerLimitLessThanEqualAndIsDeleted(Long formulaId, Double upperLimit, Double lowerLimit, Boolean check);

    List<FormulaCoefficient> findFormulaCoefficientByFormulaIdAndIsDeleted(Long formulaID, Boolean check);
}
