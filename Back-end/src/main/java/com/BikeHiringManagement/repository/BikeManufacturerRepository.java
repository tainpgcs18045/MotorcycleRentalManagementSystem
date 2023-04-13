package com.BikeHiringManagement.repository;

import com.BikeHiringManagement.entity.BikeManufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BikeManufacturerRepository extends JpaRepository<BikeManufacturer, Long>, JpaSpecificationExecutor<BikeManufacturer> {
    boolean existsByIdAndIsDeleted(Long id, Boolean check);
    boolean existsByNameAndIsDeleted(String name, Boolean check);

    BikeManufacturer findBikeManufacturerById(Long id);
    BikeManufacturer findBikeManufacturerByName(String name);
}
