package com.chd.dao;

import com.chd.pojo.Setmeal;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface SetmealDao {

    void add(Setmeal setmeal);

    void addTravelGroupAndSetmeal(Map<String, Integer> map);

    Page<Setmeal> findPage(String queryString);

    List<Setmeal> findAll();

    Setmeal findById(int id);

    Setmeal getSetmealById(int id);

    List<Map<String, Object>> findSetmealCount();
}
