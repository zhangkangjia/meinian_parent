package com.chd.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chd.dao.OrderSettingDao;
import com.chd.pojo.OrderSetting;
import com.chd.service.OrderSettingService;
import com.chd.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    OrderSettingDao orderSettingDao;
    //获取每月的预约
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        /**  传递的参数
         *   date（格式：2019-8）
         *  构建的数据List<Map>
         *    map.put("date",1);
         map.put("number",120);
         map.put("reservations",10);

         *  查询方案：SELECT * FROM t_ordersetting WHERE orderDate LIKE '2019-08-%'
         *  查询方案：SELECT * FROM t_ordersetting WHERE orderDate BETWEEN '2019-9-1' AND '2019-9-31'
         */
        // 1.组织查询Map，dateBegin表示月份开始时间，dateEnd月份结束时间
        String dateBegin = date + "-1";//2019-3-1
        String dateEnd = date + "-31";//2019-3-31
        Map<String,Object>  map = new HashMap();
        map.put("dateBegin",dateBegin);
        map.put("dateEnd",dateEnd);

        // 2.查询当前月份的预约设置
        List<OrderSetting> list =orderSettingDao.getOrderSettingByMonth(map);
        List<Map> data=new ArrayList<>();
        // 3.将List<OrderSetting>，组织成List<Map>
        for (OrderSetting orderSetting : list) {
            Map hashMap = new HashMap();
            hashMap.put("date",orderSetting.getOrderDate().getDate());//获得日期（几号）
            hashMap.put("number",orderSetting.getNumber());//可预约人数
            hashMap.put("reservations",orderSetting.getReservations());//已预约人数
            data.add(hashMap);
        }
        return data;
    }
    //根据指定日期修改可预约人数
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {

        System.out.println(orderSetting.getOrderDate());
        int count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
            if (count>0){
                //当前日期已经进行预约设置，需要进行修改操作

                orderSettingDao.editNumberByOrderDate(orderSetting);
            }else {
                //当前日期没有进行预约设置，进行添加操作
                orderSettingDao.add(orderSetting);
            }
    }
    //上传文档，更改可预约数量
    @Override
    public void add(List<OrderSetting> orderSettings) {
        // 1：遍历List<OrderSetting>
        for (OrderSetting orderSetting : orderSettings) {
            // 判断当前的日期之前是否已经被设置过预约日期，使用当前时间作为条件查询数量
                int count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                // 如果设置过预约日期，更新number数量
                if (count > 0) {
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                } else {
                    // 如果没有设置过预约日期，执行保存
                    orderSettingDao.add(orderSetting);
                }


        }

    }


}
