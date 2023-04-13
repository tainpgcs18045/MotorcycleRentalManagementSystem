package com.BikeHiringManagement.specification;

import com.BikeHiringManagement.constant.Constant;
import com.BikeHiringManagement.entity.*;
import com.BikeHiringManagement.model.response.BikeResponse;
import com.BikeHiringManagement.model.response.CartResponse;
import com.BikeHiringManagement.model.response.MaintainResponse;
import com.BikeHiringManagement.repository.BikeCategoryRepository;
import com.BikeHiringManagement.repository.MaintainRepository;
import com.BikeHiringManagement.service.system.CheckEntityExistService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class MaintainSpecification {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    MaintainRepository maintainRepository;

    @Autowired
    CheckEntityExistService checkEntityExistService;

    public Map<String, Object> getMaintainPagination(String searchKey, Integer page, Integer limit, String sortBy, String sortType, Date dateFrom, Date dateTo){
        try{
            Map<String, Object> mapFinal = new HashMap<>();

            //----------------------CREATE QUERY -----------------------------//
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();

            // ROOT
            CriteriaQuery<MaintainResponse> query = cb.createQuery(MaintainResponse.class);
            Root<Maintain> root = query.from(Maintain.class);

            // ROOT COUNT
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<Maintain> rootCount = countQuery.from(Maintain.class);

            //---------------------- CONDITION -----------------------------//
            // CONDITION
            // ROOT
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("isDeleted")));

            if(dateFrom != null){
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), dateFrom));
            }
            if(dateTo != null){
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), dateTo));
            }

            // CONDITION
            // ROOT COUNT
            List<Predicate> predicatesCount = new ArrayList<>();
            predicatesCount.add(cb.isFalse(rootCount.get("isDeleted")));

            if(dateFrom != null){
                predicatesCount.add(cb.greaterThanOrEqualTo(rootCount.get("date"), dateFrom));
            }
            if(dateTo != null){
                predicatesCount.add(cb.lessThanOrEqualTo(rootCount.get("date"), dateTo));
            }

            //------------------------SEARCH LOGIC-----------------------------//
            if (!StringUtils.isEmpty(searchKey)) {
                try {
                    Long parseLong = Long.parseLong(searchKey);
                    Double parseDouble = Double.parseDouble(searchKey);
                    predicates.add(cb.or(
                            cb.equal(root.get("id"), parseLong),
                            cb.equal(root.get("cost"), parseDouble),
                            cb.like(cb.lower(root.get("title")) , "%" + searchKey.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("type")) , "%" + searchKey.toLowerCase() + "%")
                    ));
                    predicatesCount.add(cb.or(
                            cb.equal(rootCount.get("id"), parseLong),
                            cb.equal(rootCount.get("cost"), parseDouble),
                            cb.like(cb.lower(rootCount.get("title")) , "%" + searchKey.toLowerCase() + "%"),
                            cb.like(cb.lower(rootCount.get("type")) , "%" + searchKey.toLowerCase() + "%")
                    ));
                }catch (Exception e){
                    predicates.add(cb.or(
                            cb.like(cb.lower(root.get("title")) , "%" + searchKey.toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("type")) , "%" + searchKey.toLowerCase() + "%")
                    ));
                    predicatesCount.add(cb.or(
                            cb.like(cb.lower(rootCount.get("title")) , "%" + searchKey.toLowerCase() + "%"),
                            cb.like(cb.lower(rootCount.get("type")) , "%" + searchKey.toLowerCase() + "%")
                    ));
                }
            }

            //------------------------CREATE SORT-----------------------------//
            // Sort theo Name - Cate Name - Hired Number - Price
            if (sortType.equalsIgnoreCase("asc")) {
                switch (sortBy) {
                    case "id":
                        query.orderBy(cb.asc(root.get("id")));
                        break;
                    case "title":
                        query.orderBy(cb.asc(root.get("title")));
                        break;
                    case "type":
                        query.orderBy(cb.asc(root.get("type")));
                        break;
                    case "cost":
                        query.orderBy(cb.asc(root.get("cost")));
                        break;
                }
            } else {
                switch (sortBy) {
                    case "id":
                        query.orderBy(cb.desc(root.get("id")));
                        break;
                    case "title":
                        query.orderBy(cb.desc(root.get("title")));
                        break;
                    case "type":
                        query.orderBy(cb.desc(root.get("type")));
                        break;
                    case "cost":
                        query.orderBy(cb.desc(root.get("cost")));
                        break;
                }
            }

            //----------------------END SORT-----------------------------//
            query.multiselect(
                    root.get("id"),
                    root.get("date"),
                    root.get("type"),
                    root.get("title"),
                    root.get("cost")
            ).where(cb.and(predicates.stream().toArray(Predicate[]::new)));
            List<MaintainResponse> listResult = entityManager.createQuery(query) != null ? entityManager.createQuery(query).
                    setFirstResult((page - 1) * limit)
                    .setMaxResults(limit).getResultList() : new ArrayList<>();

            countQuery.select(cb.count(rootCount)).where(cb.and(predicatesCount.stream().toArray(Predicate[]::new)));
            Long count = entityManager.createQuery(countQuery).getSingleResult();


            mapFinal.put("data", listResult);
            mapFinal.put("count", count);
            return mapFinal;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }


}
