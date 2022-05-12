package com.chd.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chd.constant.MessageConstant;
import com.chd.constant.RedisConstant;
import com.chd.entity.PageResult;
import com.chd.entity.QueryPageBean;
import com.chd.entity.Result;
import com.chd.pojo.Setmeal;
import com.chd.service.SetmealService;
import com.chd.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;


    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
      PageResult pageResult = setmealService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
        return pageResult;
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,Integer [] travelgroupIds){
        try {
            setmealService.add(setmeal,travelgroupIds);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);

        }
    }

    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile")MultipartFile imgFile){

        try {
            //获取原始文件名称
            String originalFilename = imgFile.getOriginalFilename();
            //生成新的文件名称（防止同名文件被覆盖）
            int index =originalFilename.lastIndexOf(".");
            String suffix = originalFilename.substring(index);
            String fileName = UUID.randomUUID().toString()+suffix;
            //调用工具类，上传图片到七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            /*****
             * 解决七牛云上垃圾图片的问题
             */
            //将上传图片名称存入Redis，基于Redis的Set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            //返回结果
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);

        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }


    }
}
