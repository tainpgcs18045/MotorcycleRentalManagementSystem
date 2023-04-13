package com.BikeHiringManagement.repository;

import com.BikeHiringManagement.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    Customer findCustomerByIdAndIsDeleted(Long customerId, Boolean check);
    Customer findCustomerByPhoneNumberAndIsDeleted(String phoneNumber, Boolean check);

    boolean existsByIdAndIsDeleted(Long id, Boolean check);
    boolean existsByPhoneNumberAndIsDeleted(String phoneNumber, Boolean check);

    List<Customer> findAllByCreatedDateAfterAndCreatedDateBeforeAndIsDeleted(Date dateFrom, Date dateTo, Boolean check);
}
