package com.BikeHiringManagement.repository;

import com.BikeHiringManagement.entity.Bike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BikeRepository extends JpaRepository<Bike, Long>, JpaSpecificationExecutor<Bike> {
    boolean existsByIdAndIsDeleted(Long id, Boolean check);
    boolean existsByNameAndIsDeleted(String name, Boolean check);
    boolean existsByBikeNoAndName(String bikeNo, String name);
    boolean existsByBikeManualIdAndIsDeleted(String manualId, Boolean check);

    Bike findBikeById(Long id);
    Bike findBikeByBikeManualId(String manualId);

    List<Bike> findAllByIdInAndIsDeleted(List<Long> listBikeId, Boolean check);
    List<Bike> findAllByBikeColorIdAndIsDeleted(Long colorId, Boolean check);
    List<Bike> findAllByBikeManufacturerIdAndIsDeleted(Long manufacturerId, Boolean check);

//    boolean existsByIdAndBikeManualIdAndIsDeleted(Long bikeId, String bikeManualId, Boolean check);
//    boolean existsByIdAndStatusAndIsDeleted(Long id, String status, Boolean check);
//    Integer countBikesByBikeCategoryIdAndIsDeleted(Long categoryId, Boolean check);
//    Integer countBikesByIsDeleted(Boolean check);
//    @Query("SELECT b, ct.name FROM Bike b inner join BikeCategory ct on b.bikeCategoryId = ct.id")
//    List<Bike> findBikeByIdCustom();
}
