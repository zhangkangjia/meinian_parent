package com.chd.dao;

import com.chd.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    //添加订单
    void add(Order order);
    //查询订单根据订单信息
    List<Order> findByCondition(Order order);

    Map findById4Detail(Integer id);

    int getTodayOrderNumber(String today);

    int getVisitsNumber(String today);

    int getThisWeekOrderNumber(Map<String, Object> paramWeek);

    int getThisWeekVisitsNumber(Map<String, Object> paramWeekVisit);

    int getThisWeekAndMonthOrderNumber(Map<String, Object> paramMonth);

    int getThisWeekAndMonthVisitsNumber(Map<String, Object> paramMonthVisit);

    List<Map<String, Object>> findHotSetmeal();
}
