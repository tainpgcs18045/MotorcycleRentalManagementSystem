package com.BikeHiringManagement.specification;


import com.BikeHiringManagement.entity.*;
import com.BikeHiringManagement.model.response.CartResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class OrderSpecification {

    @PersistenceContext
    EntityManager entityManager;

    public Specification<Order> filterOrder(String searchKey){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(searchKey)) {
                try {
                    Long parseLong = Long.parseLong(searchKey);
                    predicates.add(cb.or(
                            cb.equal(root.get("id"), parseLong),
                            cb.like(root.get("status"), "%" + searchKey + "%")
                    ));
                } catch (Exception e) {
                    predicates.add(cb.or(
                            cb.like(root.get("status"), "%" + searchKey + "%")
                    ));
                }
            }
            predicates.add(cb.notLike(root.get("status"), "IN CART"));
            predicates.add(cb.isFalse(root.get("isDeleted")));
            return cb.and(predicates.stream().toArray(Predicate[]::new));
        };
    }

    public Map<String, Object> getOrderPagination(String searchKey, Integer page, Integer limit, String sortBy, String sortType,
                                                  String status, Date startDate, Date endDate){
        try{
            Map<String, Object> mapFinal = new HashMap<>();

            //----------------------CREATE QUERY -----------------------------//
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();

            // ROOT
            CriteriaQuery<CartResponse> query = cb.createQuery(CartResponse.class);
            Root<Order> root = query.from(Order.class);
            Root<Customer> rootCustomer = query.from(Customer.class);

            // ROOT COUNT
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<Order> rootCount = countQuery.from(Order.class);
            Root<Customer> rootCustomerCount = countQuery.from(Customer.class);

            //---------------------- CONDITION -----------------------------//

            // CONDITION
            // ROOT
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("customerId"), rootCustomer.get("id")));

            predicates.add(cb.isFalse(root.get("isDeleted")));
            predicates.add(cb.isFalse(rootCustomer.get("isDeleted")));
            predicates.add(cb.like(cb.upper(root.get("status")), status.toUpperCase() ));

            // DATE
            if(status.toUpperCase().equalsIgnoreCase("CLOSED")){
                if(startDate != null){
                    predicates.add(cb.greaterThanOrEqualTo(root.get("actualStartDate"), startDate));
                }
                if(endDate != null){
                    predicates.add(cb.lessThanOrEqualTo(root.get("actualEndDate"), endDate));
                }
            }else{
                if(startDate != null){
                    predicates.add(cb.greaterThanOrEqualTo(root.get("expectedStartDate"), startDate));
                }
                if(endDate != null){
                    predicates.add(cb.lessThanOrEqualTo(root.get("expectedEndDate"), endDate));
                }
            }



            // CONDITION
            // ROOT COUNT
            List<Predicate> predicatesCount = new ArrayList<>();
            predicatesCount.add(cb.equal(rootCount.get("customerId"), rootCustomerCount.get("id")));

            predicatesCount.add(cb.isFalse(rootCount.get("isDeleted")));
            predicatesCount.add(cb.isFalse(rootCustomerCount.get("isDeleted")));
            predicatesCount.add(cb.like(cb.upper(root.get("status")), status.toUpperCase() ));

            // DATE
            if(status.toUpperCase().equalsIgnoreCase("CLOSED")){
                if(startDate != null){
                    predicatesCount.add(cb.greaterThanOrEqualTo(root.get("actualStartDate"), startDate));
                }
                if(endDate != null){
                    predicatesCount.add(cb.lessThanOrEqualTo(root.get("actualEndDate"), endDate));
                }
            }else{
                if(startDate != null){
                    predicatesCount.add(cb.greaterThanOrEqualTo(root.get("expectedStartDate"), startDate));
                }
                if(endDate != null){
                    predicatesCount.add(cb.lessThanOrEqualTo(root.get("expectedEndDate"), endDate));
                }
            }


            //------------------------SEARCH LOGIC-----------------------------//
            if (!StringUtils.isEmpty(searchKey)) {
                try {
                    Long parseLong = Long.parseLong(searchKey);
                    Double parseDouble = Double.parseDouble(searchKey);
                    predicates.add(cb.or(
                            cb.equal(root.get("id"), parseLong),
                            cb.equal(root.get("totalAmount"), parseDouble)
                    ));

                    predicatesCount.add(cb.or(
                            cb.equal(rootCount.get("id"), parseLong),
                            cb.equal(rootCount.get("totalAmount"), parseDouble)
                    ));
                }catch (Exception e){
                    List<CartResponse> emptyList = new ArrayList<>();
                    mapFinal.put("data", emptyList );
                    mapFinal.put("count", (long) 0);
                    return mapFinal;
                }
            }


            //------------------------CREATE SORT-----------------------------//
            if (sortType.equalsIgnoreCase("asc")) {
                switch (sortBy) {
                    case "id":
                        query.orderBy(cb.asc(root.get("id")));
                        break;
                    case "status":
                        query.orderBy(cb.asc(root.get("status")));
                        break;
                    case "expectedStartDate":
                        query.orderBy(cb.asc(root.get("expectedStartDate")));
                        break;
                    case "expectedEndDate":
                        query.orderBy(cb.asc(root.get("expectedEndDate")));
                        break;
                    case "actualStartDate":
                        query.orderBy(cb.asc(root.get("actualStartDate")));
                        break;
                    case "actualEndDate":
                        query.orderBy(cb.asc(root.get("actualEndDate")));
                        break;
                    case "totalAmount":
                        query.orderBy(cb.asc(root.get("totalAmount")));
                        break;
                }
            } else {
                switch (sortBy) {
                    case "id":
                        query.orderBy(cb.desc(root.get("id")));
                        break;
                    case "status":
                        query.orderBy(cb.desc(root.get("status")));
                        break;
                    case "expectedStartDate":
                        query.orderBy(cb.desc(root.get("expectedStartDate")));
                        break;
                    case "expectedEndDate":
                        query.orderBy(cb.desc(root.get("expectedEndDate")));
                        break;
                    case "actualStartDate":
                        query.orderBy(cb.desc(root.get("actualStartDate")));
                        break;
                    case "actualEndDate":
                        query.orderBy(cb.desc(root.get("actualEndDate")));
                        break;
                    case "totalAmount":
                        query.orderBy(cb.desc(root.get("totalAmount")));
                        break;
                }
            }

            //----------------------END SORT-----------------------------//
            query.multiselect(
                    root.get("id"),
                    root.get("customerId"),
                    rootCustomer.get("name"),
                    rootCustomer.get("phoneNumber"),
                    root.get("expectedStartDate"),
                    root.get("expectedEndDate"),
                    root.get("actualStartDate"),
                    root.get("actualEndDate"),
                    root.get("status"),
                    root.get("totalAmount")
            ).where(cb.and(predicates.stream().toArray(Predicate[]::new)));
            List<CartResponse> listResult = entityManager.createQuery(query) != null ? entityManager.createQuery(query).
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

    public Map<String, Object> getOrderById(Long orderId){
        try{
            Map<String, Object> mapFinal = new HashMap<>();

            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<CartResponse> query = cb.createQuery(CartResponse.class);
            Root<Order> root = query.from(Order.class);
            Root<Customer> rootCustomer = query.from(Customer.class);

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("customerId"), rootCustomer.get("id")));

            predicates.add(cb.isFalse(root.get("isDeleted")));
            predicates.add(cb.isFalse(rootCustomer.get("isDeleted")));

            if(orderId != null){
                predicates.add(cb.equal(root.get("id"), orderId));
            }

            query.multiselect(
                    root.get("id"),
                    root.get("customerId"),
                    rootCustomer.get("name"),
                    rootCustomer.get("phoneNumber"),
                    root.get("expectedStartDate"),
                    root.get("expectedEndDate"),
                    root.get("actualStartDate"),
                    root.get("actualEndDate"),
                    root.get("calculatedCost"),
                    root.get("isUsedService"),
                    root.get("serviceDescription"),
                    root.get("serviceCost"),
                    root.get("depositType"),
                    root.get("depositAmount"),
                    root.get("depositIdentifyCard"),
                    root.get("depositHotel"),
                    root.get("note"),
                    root.get("totalAmount"),
                    root.get("status")
            ).where(cb.and(predicates.stream().toArray(Predicate[]::new)));

            List<CartResponse> result = entityManager.createQuery(query) != null ? entityManager.createQuery(query).getResultList() : new ArrayList<>();
            if(result.size() == 0) {
                return new HashMap<>();
            }else{
                mapFinal.put("data", result.get(0));
                return mapFinal;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}
