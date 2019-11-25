package com.ray.dormitory.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.po.Building;
import com.ray.dormitory.service.BuildingService;
import com.ray.dormitory.util.bean.ErrorEnum;
import com.ray.dormitory.util.bean.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Ray
 * @date : 2019.11.22 14:54
 */
@RestController
@RequestMapping("/building")
public class BuildingController {
    @Autowired
    private BuildingService buildingService;

    @GetMapping("/list")
    public ResponseBean getList(Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageSize == null) {
            return new ResponseBean(buildingService.list());
        } else {
            IPage<Building> page = new Page<>(pageNum, pageSize);

            return new ResponseBean(buildingService.page(page));
        }
    }

    @PostMapping("/save")
    public ResponseBean save(Building building) {
        String name = building.getName();
        int count = buildingService.count(new QueryWrapper<Building>().eq("name", name));
        if (count == 0) {
            buildingService.save(building);
            return new ResponseBean();
        } else {
            return new ResponseBean(ErrorEnum.ERROR_204.getErrorCode(), "楼宇名称重复");
        }

    }

    @GetMapping("/floors")
    public ResponseBean floors(int buildingId) {
        return new ResponseBean(buildingService.getFloors(buildingId));


    }


}
