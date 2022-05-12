package com.chd.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.chd.constant.MessageConstant;
import com.chd.entity.PageResult;
import com.chd.entity.QueryPageBean;
import com.chd.entity.Result;
import com.chd.pojo.TravelGroup;
import com.chd.service.TravelGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/travelGroup")
public class TravelGroupController {
    @Reference
    TravelGroupService travelGroupService;
    @RequestMapping("/findAll")
    public Result findAll(){
      List<TravelGroup> list = travelGroupService.findAll();
      return new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS,list);
    }

    @RequestMapping("/edit")
    public Result edit(Integer[] travelItemIds,@RequestBody TravelGroup travelGroup){
        try {
            travelGroupService.edit(travelItemIds,travelGroup);
            return new Result(true,MessageConstant.EDIT_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.EDIT_TRAVELGROUP_FAIL);
        }
    }

    @RequestMapping("/findTravelItemIdByTravelGroupId")
    public List<Integer> findTravelItemIdByTravelGroupId(Integer id){
       List <Integer> list = travelGroupService.findTravelItemIdByTravelGroupId(id);
        return list;
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
       TravelGroup travelGroup= travelGroupService.findById(id);
       return new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS,travelGroup);
    }

    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            travelGroupService.delete(id);
            return new Result(true,MessageConstant.DELETE_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_TRAVELGROUP_FAIL);
        }
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
      PageResult pageResult = travelGroupService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
      return pageResult;
    }

    @RequestMapping("/add")
    public Result add(Integer[] travelItemIds,@RequestBody TravelGroup travelGroup){
        try {
            travelGroupService.add(travelGroup,travelItemIds);
            return new Result(true, MessageConstant.ADD_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_TRAVELGROUP_FAIL);

        }
    }
}
