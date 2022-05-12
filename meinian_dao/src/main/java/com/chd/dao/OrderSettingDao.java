package com.chd.dao;

import com.chd.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    public void add(OrderSetting listData);

    public void editNumberByOrderDate(OrderSetting orderSetting);

    public int findCountByOrderDate(Date orderDate);

    List<OrderSetting> getOrderSettingByMonth(Map map);

    //更新已预约人数
    void editReservationsByOrderDate(OrderSetting orderSetting);
    //根据预约日期查询预约设置信息
    OrderSetting findByOrderDate(Date date);
}
