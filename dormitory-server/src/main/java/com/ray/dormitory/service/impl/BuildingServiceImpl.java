package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.bo.Floor;
import com.ray.dormitory.bean.po.Building;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.mapper.BuildingMapper;
import com.ray.dormitory.service.BuildingService;
import com.ray.dormitory.util.bean.ErrorEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.22 14:52
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BuildingServiceImpl extends ServiceImpl<BuildingMapper, Building> implements BuildingService {

    @Override
    public List<Floor> getFloors(int buildingId) {
        return baseMapper.getFloors(buildingId);
    }

    @Override
    public void testTx() {
        Building building = new Building(1, "1");
        int result = baseMapper.updateById(building);
        System.out.println(result);
        throw new NullPointerException("事务回滚");
    }

    @Override
    public boolean save(Building building) {
        String name = building.getName();
        int count = baseMapper.selectCount(new QueryWrapper<Building>().eq("name", name));
        if (count == 0) {
            return super.saveOrUpdate(building);
        } else {
            throw new CustomException(ErrorEnum.BUILDING_NAME_EXIST);
        }

    }
}
