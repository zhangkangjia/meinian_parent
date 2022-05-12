package com.chd.service;

import com.chd.entity.PageResult;
import com.chd.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
     Setmeal findById(int id);




    void add(Setmeal setmeal, Integer[] travelgroupIds);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    List<Setmeal> findAll();

    Setmeal getSetmealById(int id);

    List<Map<String, Object>> findSetmealCount();
}
