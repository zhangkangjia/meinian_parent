package com.chd.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chd.constant.MessageConstant;
import com.chd.constant.RedisMessageConstant;
import com.chd.entity.Result;
import com.chd.pojo.Member;
import com.chd.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Reference
    MemberService memberService;
    @Autowired
    JedisPool jedisPool;
    @RequestMapping("/check")
    public Result check(HttpServletResponse response, @RequestBody Map map){
        //获取手机号
        String telephone = (String) map.get("telephone");
        //前台输入的获取验证码
        String validateCode= (String) map.get("validateCode");
        //从redis中获取验证码
        String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        //验证码正确
        if (code!=null&&validateCode.equals(code)){
            //查询用户是否是会员，不是会员直接进行注册
           Member member= memberService.findByTelephone(telephone);
           if (member==null){
               //不是会员，直接进行注册，然后登陆
               member =new Member();
               member.setPhoneNumber(telephone);
               member.setRegTime(new Date());
               memberService.add(member);
           }
           //登陆成功
            //写入Cookie，跟踪用户
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");//路径
            cookie.setMaxAge(60*60*24*30);//有效期30天（单位秒）
            response.addCookie(cookie);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);

        }else {
            //验证码验证失败
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }
}
