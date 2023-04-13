package com.BikeHiringManagement.repository;

import com.BikeHiringManagement.entity.BikeImage;
import com.BikeHiringManagement.entity.MaintainBike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MaintainBikeRepository extends JpaRepository<MaintainBike, Long>, JpaSpecificationExecutor<MaintainBike> {
    Boolean existsByMaintainIdAndIsDeleted(Long maintainId, Boolean check);
    Boolean existsByMaintainIdAndBikeId(Long maintainId, Long bikeId);

    List<MaintainBike> findAllByMaintainIdAndIsDeleted(Long id, boolean check);
    MaintainBike findAllByMaintainIdAndBikeId(Long maintainId, Long bikeId);

    @Modifying
    @Transactional
    @Query("UPDATE MaintainBike mb SET mb.isDeleted = true WHERE mb.maintainId = :maintainId")
    void updateIsDelete(@Param("maintainId") Long id);
}
