package com.chd.dao;

import com.chd.pojo.TravelGroup;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface TravelGroupDao {
    static void add(TravelGroup travelGroup) {
    }

    void addTravelGroupAndTravelItem(Map<String, Integer> map);

    Page<TravelGroup> findPage(String queryString);

    void delete(Integer id);

    long findCountByTravelGroupId(Integer id);

    List<Integer> findTravelItemIdByTravelGroupId(Integer id);

    TravelGroup findById(Integer id);

    void edit(TravelGroup travelGroup);

    List<TravelGroup> findAll();

    List<TravelGroup> findTravelGroupListById(Integer id);
}
