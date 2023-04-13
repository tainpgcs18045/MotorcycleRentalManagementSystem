package com.BikeHiringManagement.repository;

import com.BikeHiringManagement.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    boolean existsByCreatedUserAndStatusAndIsDeleted(String username, String status, Boolean check);
    boolean existsByIdAndStatus(Long id, String status);
    boolean existsByIdAndIsDeleted(Long id, Boolean check);

    Order findByCreatedUserAndStatusAndIsDeleted(String username, String status, Boolean check);
    Order findOrderByIdAndIsDeleted(Long id, Boolean check);

    List<Order> findAllByActualStartDateAfterAndActualEndDateBeforeAndStatusAndIsDeleted(Date dateFrom, Date dateTo, String status, Boolean check);
    List<Order> findAllByExpectedStartDateAfterAndExpectedEndDateBeforeAndStatusAndIsDeleted(Date dateFrom, Date dateTo, String status, Boolean check);
}
