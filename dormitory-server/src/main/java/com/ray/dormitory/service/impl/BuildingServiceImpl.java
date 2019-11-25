package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.bo.Floor;
import com.ray.dormitory.bean.po.Building;
import com.ray.dormitory.mapper.BuildingMapper;
import com.ray.dormitory.service.BuildingService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.22 14:52
 */
@Service
public class BuildingServiceImpl extends ServiceImpl<BuildingMapper, Building> implements BuildingService {

    @Override
    public List<Floor> getFloors(int buildingId) {
        return baseMapper.getFloors(buildingId);
    }

}
