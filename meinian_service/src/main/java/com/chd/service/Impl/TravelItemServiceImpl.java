package com.chd.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chd.dao.TravelItemDao;
import com.chd.entity.PageResult;
import com.chd.pojo.TravelItem;
import com.chd.service.TravelItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = TravelItemService.class)//发布服务注册到zk中心
@Transactional//声明式事务，所有方法都增加事务
public class TravelItemServiceImpl implements TravelItemService {

    @Autowired
    TravelItemDao travelItemDao;
    @Override
    public void add(TravelItem travelItem) {
        travelItemDao.add(travelItem);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
       Page<TravelItem> page= travelItemDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void delete(Integer id) {
        //查询自由行关联表中是否存在关联数据，如果存在，就抛出异常，不进行删除
        long count =travelItemDao.findCountByTravelitemId(id);
        if (count>0){
            throw new RuntimeException("删除自由行失败，因为存在关联数据，先解除关系再删除");
        }
        travelItemDao.delete(id);
    }

    @Override
    public TravelItem findById(Integer id) {
       TravelItem travelItem = travelItemDao.findById(id);
        return travelItem;
    }

    @Override
    public void edit(TravelItem travelItem) {
        travelItemDao.edit(travelItem);
    }

    @Override
    public List<TravelItem> findAll() {

        return travelItemDao.findAll();
    }

}
