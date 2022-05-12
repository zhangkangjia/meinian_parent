package com.chd.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chd.dao.MemberDao;
import com.chd.dao.OrderDao;
import com.chd.pojo.Order;
import com.chd.service.ReportService;
import com.chd.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    OrderDao orderDao;
    @Autowired
    MemberDao memberDao;
    @Override
    public Map<String, Object> getBusinessReportData() {
        Map<String, Object> map =null;

        try {//1：获取当前时间
           String today= DateUtils.parseDate2String(DateUtils.getToday());
           //2：本周周一
           String weekMonday= DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
           //3：本周周日
            String weekSunday = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
            //4:本月第一天
          String monthFirst = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
            // 5：本月（31号）
            String monthLast = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());
            
            //1 今日新增会员数
            int todayNewMember =memberDao.getTodayNewMember(today);
            //2 总会员数
            int totalMember =memberDao.getTotalMember();
            //3 本周新增会员数
            int thisWeekNewMember =memberDao.getThisWeekNewMember(weekMonday);
            //4 本月新增会员数
            int thisMonthNewMember =memberDao.GetThisMonthNewMember(monthFirst);

            //5 今日预约数
            int todayOrderNumber =orderDao.getTodayOrderNumber(today);
            //6 今日出游数
            int todayVisitsNumber =orderDao.getVisitsNumber(today);
            //7 本周预约数
            Map<String,Object> paramWeek =new HashMap<>();
            paramWeek.put("begin",weekMonday);
            paramWeek.put("end",weekSunday);
            int thisWeekOrderNumber =orderDao.getThisWeekOrderNumber(paramWeek);
            //8 本周出游数
            Map<String,Object> paramWeekVisit =new HashMap<>();
            paramWeek.put("begin",weekMonday);
            paramWeek.put("end",weekSunday);
            int thisWeekVisitsNumber =orderDao.getThisWeekVisitsNumber(paramWeekVisit);
            //9 本月预约数
            Map<String,Object> paramMonth =new HashMap<>();
            paramMonth.put("begin",monthFirst);
            paramMonth.put("end",monthLast);
            int thisMonthOrderNumber = orderDao.getThisWeekAndMonthOrderNumber(paramMonth);
            // （10）本月出游数
            Map<String,Object> paramMonthVisit = new HashMap<String,Object>();
            paramMonthVisit.put("begin",monthFirst);
            paramMonthVisit.put("end",monthLast);
            int thisMonthVisitsNumber = orderDao.getThisWeekAndMonthVisitsNumber(paramMonthVisit);
            // （11）热门套餐
            List<Map<String,Object>> hotSetmeal = orderDao.findHotSetmeal();

            map = new HashMap<String,Object>();
            map.put("reportDate",today);
            map.put("todayNewMember",todayNewMember);
            map.put("totalMember",totalMember);
            map.put("thisWeekNewMember",thisWeekNewMember);
            map.put("thisMonthNewMember",thisMonthNewMember);
            map.put("todayOrderNumber",todayOrderNumber);
            map.put("todayVisitsNumber",todayVisitsNumber);
            map.put("thisWeekOrderNumber",thisWeekOrderNumber);
            map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
            map.put("thisMonthOrderNumber",thisMonthOrderNumber);
            map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
            map.put("hotSetmeal",hotSetmeal);
        } catch (Exception e) {
            e.printStackTrace();
        }
       

        return map;

    }
}
