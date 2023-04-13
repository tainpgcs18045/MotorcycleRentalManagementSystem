package com.BikeHiringManagement.repository;

import com.BikeHiringManagement.entity.Bike;
import com.BikeHiringManagement.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface HistoryRepository extends JpaRepository<History, Long>, JpaSpecificationExecutor<History> {

}
