package com.chd.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chd.constant.RedisConstant;
import com.chd.dao.SetmealDao;
import com.chd.entity.PageResult;
import com.chd.pojo.Setmeal;
import com.chd.service.SetmealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService{
    @Autowired
    SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;


    @Override
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }

    @Override
    public void add(Setmeal setmeal, Integer[] travelgroupIds) {
        setmealDao.add(setmeal);
        addTravelGroupAndSetmeal(setmeal.getId(),travelgroupIds);
        //将图片名称保存到Redis
       String pic =setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pic);
    }



    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> page =setmealDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public Setmeal getSetmealById(int id) {
        return setmealDao.getSetmealById(id);
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

    private void addTravelGroupAndSetmeal(Integer SetmealId, Integer[] travelgroupIds) {
        if (travelgroupIds!=null&&travelgroupIds.length>0){
            for (Integer travelgroupId : travelgroupIds) {
                Map<String,Integer> map=new HashMap<>();
                map.put("setmeal",SetmealId);
                map.put("travelgroup",travelgroupId);
               setmealDao.addTravelGroupAndSetmeal(map);
            }
        }
    }
}
