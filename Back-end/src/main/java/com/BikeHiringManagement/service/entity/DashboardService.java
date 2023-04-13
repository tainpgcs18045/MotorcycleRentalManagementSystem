package com.BikeHiringManagement.service.entity;

import com.BikeHiringManagement.constant.Constant;
import com.BikeHiringManagement.entity.*;
import com.BikeHiringManagement.model.request.DashboardRequest;
import com.BikeHiringManagement.model.response.DashboardResponse;
import com.BikeHiringManagement.model.temp.*;
import com.BikeHiringManagement.model.temp.dashboard.*;
import com.BikeHiringManagement.repository.*;
import com.BikeHiringManagement.service.system.CheckEntityExistService;
import com.BikeHiringManagement.service.system.ResponseUtils;
import com.BikeHiringManagement.specification.BikeSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    BikeRepository bikeRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    MaintainRepository maintainRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BikeSpecification bikeSpecification;

    @Autowired
    ResponseUtils responseUtils;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CheckEntityExistService checkEntityExistService;

    public Result getDataByFromTo(DashboardRequest dashboardRequest) {
        try {
            Date dateFrom = dashboardRequest.getDateFrom();
            Date dateTo = dashboardRequest.getDateTo();
            DashboardResponse result = new DashboardResponse();

            /* --------------------- DATA QUERY --------------------- */
            // ORDER
            List<Order> listCloseOrder = orderRepository.findAllByActualStartDateAfterAndActualEndDateBeforeAndStatusAndIsDeleted(dateFrom, dateTo, Constant.STATUS_ORDER_CLOSED, Boolean.FALSE);
            List<Order> listCancelOrder = orderRepository.findAllByExpectedStartDateAfterAndExpectedEndDateBeforeAndStatusAndIsDeleted(dateFrom, dateTo, Constant.STATUS_ORDER_CANCEL, Boolean.FALSE);
            List<Order> listPendingOrder = orderRepository.findAllByExpectedStartDateAfterAndExpectedEndDateBeforeAndStatusAndIsDeleted(dateFrom, dateTo, Constant.STATUS_ORDER_PENDING, Boolean.FALSE);

            // MAINTAIN
            List<Maintain> listMaintain = maintainRepository.findAllByDateAfterAndDateBeforeAndIsDeleted(dateFrom, dateTo, Boolean.FALSE);

            // CUSTOMER
            List<Customer> listNewCustomer = customerRepository.findAllByCreatedDateAfterAndCreatedDateBeforeAndIsDeleted(dateFrom, dateTo, Boolean.FALSE);


            /* --------------------- First Chart --------------------- */
            FirstChart firstChart = new FirstChart();

            // TOTAL INCOME
            Double totalRevenue = 0.0;
            Double totalExpense = 0.0;
            Double totalIncome = 0.0;
            totalRevenue = listCloseOrder.stream().mapToDouble(order -> order.getTotalAmount().doubleValue()).sum();
            totalExpense = listMaintain.stream().mapToDouble(maintain -> maintain.getCost().doubleValue()).sum();
            totalIncome = totalRevenue - totalExpense;
            firstChart.setTotalIncome(totalIncome);

            // TOTAL ORDER
            firstChart.setTotalOrder(listCloseOrder.size() + listCancelOrder.size() + listPendingOrder.size());

            // TOTAL NEW CUSTOMER
            firstChart.setTotalNewCustomer(listNewCustomer.size());


            /* --------------------- Second Chart --------------------- */
            SecondChart secondChart = new SecondChart();

            // TOTAL REVENUE
            secondChart.setTotalRevenue(totalRevenue);

            // TOTAL EXPENSE
            secondChart.setTotalExpense(totalExpense);


            /* --------------------- Third Chart --------------------- */
            ThirdChart thirdChart = new ThirdChart();

            // TOTAL ORDER BY STATUS
            thirdChart.setTotalOrderClose(listCloseOrder.size());
            thirdChart.setTotalOrderCancel(listCancelOrder.size());
            thirdChart.setTotalOrderPending(listPendingOrder.size());

            /* --------------------- Fourth Chart --------------------- */
            FourthChart fourthChart = new FourthChart();
            List<Long> listAutoBikeId = new ArrayList<>();
            List<Long> listManualBikeId = new ArrayList<>();
            for (Order order : listCloseOrder) {
                List<OrderDetail> listOrderDetail = orderDetailRepository.findAllOrderDetailByOrderIdAndIsDeleted(order.getId(), Boolean.FALSE);
                List<Long> listBikeID = listOrderDetail.stream().map(OrderDetail::getBikeId).collect(Collectors.toList());
                List<Bike> listBike = bikeRepository.findAllByIdInAndIsDeleted(listBikeID, Boolean.FALSE);
                for (Bike bike : listBike) {
                    if (bike.getBikeCategoryId().equals(Constant.BIKE_AUTO)) {
                        listAutoBikeId.add(bike.getId());
                    } else if (bike.getBikeCategoryId().equals(Constant.BIKE_MANUAL)) {
                        listManualBikeId.add(bike.getId());
                    }
                }
            }
            fourthChart.setTotalBikeAutoHired(listAutoBikeId.size());
            fourthChart.setTotalBikeManualHired(listManualBikeId.size());

            /* --------------------- Fifth Chart --------------------- */
            FifthChart fifthChart = new FifthChart();
            long countMaintainBike = listMaintain.stream().filter(x -> x.getType().equals(Constant.STATUS_MAINTAIN_BIKE)).count();
            long countMaintainGeneral = listMaintain.stream().filter(x -> x.getType().equals(Constant.STATUS_MAINTAIN_GENERAL)).count();

            fifthChart.setTotalMaintainBike((int) countMaintainBike);
            fifthChart.setTotalMaintainGeneral((int) countMaintainGeneral);

            /* --------------------- Sixth Chart --------------------- */
            SixthChart sixthChart = new SixthChart();
            HashMap<Long, Integer> mapCustomerAppear = new HashMap<>();
            HashMap<Long, Double> mapCustomerCost = new HashMap<>();
            for (Order order : listCloseOrder) {
                Long customerId = order.getCustomerId();

                // Hired Number Map
                if(mapCustomerAppear.size() < 6){
                    if (mapCustomerAppear.containsKey(customerId)) {
                        mapCustomerAppear.put(customerId, mapCustomerAppear.get(customerId) + 1);
                    } else {
                        mapCustomerAppear.put(customerId, 1);
                    }
                }

                // Cost Map
                if(mapCustomerCost.size() < 6){
                    if (mapCustomerCost.containsKey(customerId)) {
                        mapCustomerCost.put(customerId, mapCustomerCost.get(customerId) + order.getTotalAmount());
                    } else {
                        mapCustomerCost.put(customerId, order.getTotalAmount());
                    }
                }
            }

            // Sort 2 map
            mapCustomerAppear = sortByIntegerValueDesc(mapCustomerAppear);
            mapCustomerCost = sortByDoubleValueDesc(mapCustomerCost);

            // Set to 2 Lists
            List<CustomerRank> listCustomerAppear = new ArrayList<>();
            List<CustomerRank> listCustomerCost = new ArrayList<>();

            int rankAppear = 0;
            int rankCost = 0;

            for (Map.Entry<Long, Integer> entry : mapCustomerAppear.entrySet()) {
                rankAppear++;
                Customer customer = customerRepository.findCustomerByIdAndIsDeleted(entry.getKey(), Boolean.FALSE);
                CustomerRank customerRank = new CustomerRank();
                customerRank.setRank(rankAppear);
                customerRank.setCustomerId(entry.getKey());
                customerRank.setHiredNumber(entry.getValue());
                customerRank.setName(customer.getName());
                customerRank.setPhoneNumber(customer.getPhoneNumber());
                listCustomerAppear.add(customerRank);
            }

            for (Map.Entry<Long, Double> entry : mapCustomerCost.entrySet()) {
                rankCost++;
                Customer customer = customerRepository.findCustomerByIdAndIsDeleted(entry.getKey(), Boolean.FALSE);
                CustomerRank customerRank = new CustomerRank();
                customerRank.setRank(rankCost);
                customerRank.setCustomerId(entry.getKey());
                customerRank.setHiredCost(entry.getValue());
                customerRank.setName(customer.getName());
                customerRank.setPhoneNumber(customer.getPhoneNumber());
                listCustomerCost.add(customerRank);
            }
            sixthChart.setListTopCustomerHiringNumber(listCustomerAppear);
            sixthChart.setListTopCustomerHiringCost(listCustomerCost);


            /* --------------------- RETURN RESULT --------------------- */
            result.setFirstChart(firstChart);
            result.setSecondChart(secondChart);
            result.setThirdChart(thirdChart);
            result.setFourthChart(fourthChart);
            result.setFifthChart(fifthChart);
            result.setSixthChart(sixthChart);

            return new Result(Constant.SUCCESS_CODE, "Get dashboard successfully!", result);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "Fail");
        }
    }

    public Result getDataByYear(DashboardRequest dashboardRequest) {
        try {
            Integer year = Integer.parseInt(dashboardRequest.getYear());
            DashboardResponse result = new DashboardResponse();

            Integer[] arrMonth = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

            /* --------------------- Seventh Chart --------------------- */
            MonthChart monthChart = new MonthChart();

            List<MonthData> listMonthData = new ArrayList<>();

            for (int i = 0; i < arrMonth.length; i++) {
                Date fistDateOfMonth = generateDate(year, arrMonth[i], 1, 0, 0,0);
                Date lastDateOfMonth = getLastDateOfMonth(fistDateOfMonth);

                List<Order> listCloseOrder = orderRepository.findAllByActualStartDateAfterAndActualEndDateBeforeAndStatusAndIsDeleted(fistDateOfMonth, lastDateOfMonth, Constant.STATUS_ORDER_CLOSED, Boolean.FALSE);
                List<Maintain> listMaintain = maintainRepository.findAllByDateAfterAndDateBeforeAndIsDeleted(fistDateOfMonth, lastDateOfMonth, Boolean.FALSE);
                List<Customer> listNewCustomer = customerRepository.findAllByCreatedDateAfterAndCreatedDateBeforeAndIsDeleted(fistDateOfMonth, lastDateOfMonth, Boolean.FALSE);

                Double revenue = listCloseOrder.stream().mapToDouble(x -> x.getTotalAmount().doubleValue()).sum();
                Double expense = listMaintain.stream().mapToDouble(x -> x.getCost().doubleValue()).sum();
                Double income = revenue - expense;

                MonthData monthData = new MonthData();
                monthData.setMonth(arrMonth[i]);
                monthData.setTotalIncome(income);
                monthData.setTotalExpense(expense);
                monthData.setTotalRevenue(revenue);
                monthData.setTotalOrder(listCloseOrder.size());
                monthData.setTotalNewCustomer(listNewCustomer.size());
                listMonthData.add(monthData);
            }
            monthChart.setListMonthData(listMonthData);

            result.setMonthChart(monthChart);

            return new Result(Constant.SUCCESS_CODE, "Get dashboard successfully!", result);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(Constant.SYSTEM_ERROR_CODE, "Fail");
        }
    }


    public HashMap<Long, Integer> sortByIntegerValueDesc(HashMap<Long, Integer> map) {
        List<HashMap.Entry<Long, Integer>> list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator<HashMap.Entry<Long, Integer>>() {
            @Override
            public int compare(HashMap.Entry<Long, Integer> o1, HashMap.Entry<Long, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        HashMap<Long, Integer> result = new LinkedHashMap<>();
        for (HashMap.Entry<Long, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public HashMap<Long, Double> sortByDoubleValueDesc(HashMap<Long, Double> map) {
        List<HashMap.Entry<Long, Double>> list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator<HashMap.Entry<Long, Double>>() {
            @Override
            public int compare(HashMap.Entry<Long, Double> o1, HashMap.Entry<Long, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        HashMap<Long, Double> result = new LinkedHashMap<>();
        for (HashMap.Entry<Long, Double> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public Date generateDate(int year, int month, int date, int hour, int min, int second) throws ParseException {
        String stringDate = "" + date + "-" + month + "-" + year + " " + hour + ":" + min + ":" + second;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date generatedDate = dateFormat.parse(stringDate);
        return generatedDate;
    }

    public Date getLastDateOfMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date lastDayOfMonth = calendar.getTime();
        return lastDayOfMonth;
    }
}
