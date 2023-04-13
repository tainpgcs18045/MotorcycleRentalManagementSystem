package com.BikeHiringManagement.repository;

import com.BikeHiringManagement.entity.Formula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FormulaRepository extends JpaRepository<Formula, Long>, JpaSpecificationExecutor<Formula> {
    Boolean existsByIdAndIsDeleted(Long id, Boolean check);
    Formula findFormulaByIdAndIsDeleted(Long id, Boolean check);

    Formula findAllByIsDeleted(Boolean Check);
    Boolean existsAllByIsDeleted(Boolean Check);
}
