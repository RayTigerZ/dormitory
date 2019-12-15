package com.ray.dormitory.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.bo.Floor;
import com.ray.dormitory.bean.po.Building;
import com.ray.dormitory.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public IPage<Building> getList(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {

        IPage<Building> page = new Page<>(pageNum, pageSize);
        return buildingService.page(page);

    }

    @PostMapping("/save")
    public boolean save(Building building) {

        return buildingService.save(building);

    }

    @GetMapping("/floors")
    public List<Floor> floors(int buildingId) {
        return buildingService.getFloors(buildingId);

    }

    @PostMapping("/test")
    public boolean test() {
        buildingService.testTx();
        return true;
    }


}
