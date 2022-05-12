package com.chd.controller;

import com.chd.constant.MessageConstant;
import com.chd.constant.RedisConstant;
import com.chd.constant.RedisMessageConstant;
import com.chd.entity.Result;
import com.chd.util.SMSUtils;
import com.chd.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    JedisPool jedisPool;
    //验证码登录验证
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        try {
            //发送验证码
            SMSUtils.sendShortMessage(telephone,code.toString());
            //将验证码存入到redis中
            jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_LOGIN,5*60,code.toString());
            System.out.println("telephone="+telephone+"code="+code);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
    //订单预约发送验证码并进行验证
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
       Integer code = ValidateCodeUtils.generateValidateCode(4);//生成4位数字的验证码
        try { //发送短信
            SMSUtils.sendShortMessage(telephone,code.toString());
            //将验证码存储到redis,进行后期验证
            jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,5*60,code.toString());
            System.out.println("telephone:"+telephone+"code="+code);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
}
