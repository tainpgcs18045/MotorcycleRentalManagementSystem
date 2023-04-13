package com.BikeHiringManagement.repository;

import com.BikeHiringManagement.entity.BikeColor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BikeColorRepository extends JpaRepository<BikeColor, Long>, JpaSpecificationExecutor<BikeColor> {
    boolean existsByIdAndIsDeleted(Long id, Boolean check);
    boolean existsByNameAndIsDeleted(String name, Boolean check);

    BikeColor findBikeColorById(Long id);
    BikeColor findBikeColorByName(String name);
}
