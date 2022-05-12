package com.chd.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chd.constant.MessageConstant;
import com.chd.constant.RedisMessageConstant;
import com.chd.entity.Result;
import com.chd.pojo.Order;
import com.chd.service.OrderService;
import com.chd.util.SMSUtils;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    JedisPool jedisPool;

    @Reference
    OrderService orderService;

    @RequestMapping("/findById")
    public Result findById(Integer id){
        Map map =null;
        try {
            map=  orderService.findById4Detail(id);
            //查询预约信息成功
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            //查询预约信息失败
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);

        }

    }

    /**
     * 由于页面的表单数据来自多张表的数据操作，使用pojo对象接收，接收不完整，就用map接收
     * @param map
     * @return
     */
    @RequestMapping("/saveOrder")
    public Result saveOrder(@RequestBody Map map){
        //在页面获取手机号
        String telephone=(String) map.get("telephone");
        //在页面获取验证码
        String validateCode =(String)map.get("validateCode");
        String redisCode = jedisPool.getResource().get(telephone+ RedisMessageConstant.SENDTYPE_ORDER);
        //验证手机验证码
        if (redisCode==null || !redisCode.equals(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        Result result =null;
        //调用旅游预约服务
        try {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            // 断点调试，查看map里面封装了哪些数据
            result= orderService.saveOrder(map);
        }catch (Exception e){
            e.printStackTrace();
            //预约失败
            return result;
        }
        if (result.isFlag()){
            //预约成功，发送短信通知，短信通知内容可以是“预约时间”，“预约人”，“预约地点”，“预约事项”等信息。
            String orderDate=(String) map.get("orderDate");
            try {
                SMSUtils.sendShortMessage(telephone,orderDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
