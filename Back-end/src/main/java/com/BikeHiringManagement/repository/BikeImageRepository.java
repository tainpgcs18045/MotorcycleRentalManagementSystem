package com.BikeHiringManagement.repository;
import org.springframework.transaction.annotation.Transactional;
import com.BikeHiringManagement.entity.BikeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BikeImageRepository extends JpaRepository<BikeImage, Long>, JpaSpecificationExecutor<BikeImage> {
    boolean existsByNameAndIsDeleted(String name, Boolean check);
    boolean existsByIdAndIsDeleted(Long id, Boolean check);

    List<BikeImage> findAllByBikeIdAndIsDeletedOrderByNameAsc(Long id, Boolean check);
    BikeImage findBikeImageById(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE BikeImage bi SET bi.isDeleted = true WHERE bi.bikeId = :bikeId")
    void updateIsDelete(@Param("bikeId") Long id);
}
