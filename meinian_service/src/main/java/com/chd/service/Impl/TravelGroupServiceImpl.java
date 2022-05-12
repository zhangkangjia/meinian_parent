package com.chd.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chd.dao.TravelGroupDao;
import com.chd.entity.PageResult;
import com.chd.pojo.TravelGroup;
import com.chd.service.TravelGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass= TravelGroupService.class)
@Transactional
public class TravelGroupServiceImpl implements TravelGroupService {
    @Autowired
    TravelGroupDao travelGroupDao;

    @Override
    public void add(TravelGroup travelGroup, Integer[] travelItemIds) {
        TravelGroupDao.add(travelGroup);
        addTravelGroupAndTravelItem(travelGroup.getId(),travelItemIds);

    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page <TravelGroup> page= travelGroupDao.findPage(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void delete(Integer id) {
        // 在删除自由行之前，先判断自由行的id，在中间表中是否存在数据
        long count =  travelGroupDao.findCountByTravelGroupId(id);
        // 中间表如果有数据，不要往后面执行，直接抛出异常
        if (count > 0){
            throw new RuntimeException("不允许删除");
        }

        travelGroupDao.delete(id);
    }

    @Override
    public List<Integer> findTravelItemIdByTravelGroupId(Integer id) {

        return  travelGroupDao.findTravelItemIdByTravelGroupId(id);
    }

    @Override
    public TravelGroup findById(Integer id) {
        return travelGroupDao.findById(id);
    }

    @Override
    public void edit(Integer[] travelItemIds, TravelGroup travelGroup) {
        travelGroupDao.edit(travelGroup);
        Integer travelGroupId = travelGroup.getId();
        addTravelGroupAndTravelItem(travelGroupId,travelItemIds);
    }

    @Override
    public List<TravelGroup> findAll() {

        return travelGroupDao.findAll();
    }

    private void addTravelGroupAndTravelItem(Integer travelGroupId, Integer[] travelItemIds) {
        //新增几条数据，由travelItemIds长度决定
        if (travelItemIds!=null && travelItemIds.length>0){
            for (Integer travelItemId : travelItemIds) {
                Map<String,Integer> map =new HashMap<>();
                map.put("travelGroup",travelGroupId);
                map.put("travelItem",travelItemId);
                travelGroupDao.addTravelGroupAndTravelItem(map);
            }
        }

    }
}
