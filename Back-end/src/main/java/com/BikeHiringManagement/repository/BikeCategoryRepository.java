package com.BikeHiringManagement.repository;

import com.BikeHiringManagement.entity.BikeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BikeCategoryRepository extends JpaRepository<BikeCategory, Long>, JpaSpecificationExecutor<BikeCategory> {
    boolean existsByIdAndIsDeleted(Long id, Boolean check);
    boolean existsByNameAndIsDeleted(String name, Boolean check);

    BikeCategory findBikeCategoriesById(Long id);
    BikeCategory findBikeCategoryByName(String name);

}
