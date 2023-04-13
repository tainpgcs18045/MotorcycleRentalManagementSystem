package com.BikeHiringManagement.repository;

import com.BikeHiringManagement.entity.Maintain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface MaintainRepository extends JpaRepository<Maintain, Long>, JpaSpecificationExecutor<Maintain> {
    boolean existsByIdAndIsDeleted(Long id, Boolean check);

    Maintain findMaintainByIdAndIsDeleted(Long id, Boolean check);
    Maintain findMaintainById(Long id);

    List<Maintain> findAllByIsDeleted(Boolean check);
    List<Maintain> findAllByDateAfterAndDateBeforeAndIsDeleted(Date dateFrom, Date dateTo, Boolean check);
}
