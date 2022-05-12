package com.chd.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chd.constant.MessageConstant;
import com.chd.dao.MemberDao;
import com.chd.dao.OrderDao;
import com.chd.dao.OrderSettingDao;
import com.chd.entity.Result;
import com.chd.pojo.Member;
import com.chd.pojo.Order;
import com.chd.pojo.OrderSetting;
import com.chd.service.OrderService;
import com.chd.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;



@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService{
    @Autowired
    OrderDao orderDao;

    @Autowired
    MemberDao memberDao;

    @Autowired
    OrderSettingDao orderSettingDao;

    /**
     * 1.判断当前的日期是否可以预约(根据orderDate查询t_ordersetting, 能查询出来可以预约;查询不出来,不能预约)
     * 2. 判断当前日期是否预约已满(判断reservations（已经预约人数）是否等于number（最多预约人数）)
     * 3. 判断是否是会员(根据手机号码查询t_member)
     *     - 如果是会员(能够查询出来), 防止重复预约(根据member_id,orderDate,setmeal_id查询t_order)
     *     - 如果不是会员(不能够查询出来) ,自动注册为会员(直接向t_member插入一条记录)
     * 4.进行预约
     *       - 向t_order表插入一条记录
     *       - t_ordersetting表里面预约的人数reservations+1
     * @param map
     * @return
     */
    @Override
    public Result saveOrder(Map map) throws Exception {
        //检查当前日期是否进行了预约设置
        String orderDate = (String) map.get("orderDate");
        // 因为数据库预约设置表里面的时间是date类型，http协议传递的是字符串类型，所以需要转换
        Date date= DateUtils.parseString2Date(orderDate);
        //（1）使用预约时间，查询预约设置表，判断是否有该记录
        OrderSetting orderSetting= orderSettingDao.findByOrderDate(date);
        // 如果预约设置表等于null，说明不能进行预约，压根就没有开团
        if (orderSetting==null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }else {
            //如果有，说明预约可以进行预约
            int number= orderSetting.getNumber();//可预约人数
            int reservations=orderSetting.getReservations();//已预约人数
            if (number<=reservations){
                return new Result(false,MessageConstant.ORDER_FULL);
            }
        }

        //获取手机号
        String telephone =(String) map.get("telephone");
        //根据手机号查询会员表，判断当前预约人是否是会员
        Member member = memberDao.findByTelephone(telephone);
        //如果是会员，防止重复预约（一个会员、一个时间、一个套餐不能重复，否则是重复预约）
        if (member!=null){
            Integer memberId = member.getId();
            int setmealId = Integer.parseInt((String) map.get("setmealId"));
            Order order =new Order(memberId,date,null,null,setmealId);
            // 根据预约信息查询是否已经预约
           List<Order> list= orderDao.findByCondition(order);
           if (list!=null&&list.size()>0){
               //已经完成了预约，不能重复预约
               return new Result(false,MessageConstant.HAS_ORDERED);
           }
        }else {
            //如果不是会员，注册会员，向会员表中添加数据
            member=new Member();
            member.setName((String)map.get("name"));
            member.setSex((String) map.get("sex"));
            member.setPhoneNumber((String) map.get("telephone"));
            member.setIdCard((String) map.get("idCard"));
            member.setRegTime(new Date());
            memberDao.add(member);//主键回填
        }
        //（3）可以进行预约，更新预约设置表中预约人数的值，使其的值+1
        //可以预约，设置预约人数加一
            orderSetting.setReservations(orderSetting.getReservations()+1);
            orderSettingDao.editReservationsByOrderDate(orderSetting);

        //（4）可以进行预约，向预约表中添加1条数据
        //保存预约信息到预约表
        Order order =new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(date);
        order.setOrderStatus(Order.ORDERSTATUS_NO);//预约状态
        order.setOrderType((String) map.get("orderType"));
        order.setSetmealId(Integer.parseInt((String)map.get("setmealId")));
        orderDao.add(order);//主键回填
        return new Result(true,MessageConstant.ORDER_SUCCESS,order);
    }

    @Override
    public Map findById4Detail(Integer id) throws Exception {
        Map map = orderDao.findById4Detail(id);
        if (map!=null){
            //处理日期格式
            Date orderDate =(Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
            return map;

        }
        return map;
    }
}
