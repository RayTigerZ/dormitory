package com.ray.dormitory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ray.dormitory.bean.po.Operation;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : Ray
 * @date : 2019.12.05 15:31
 */
@Repository
public interface OperationMapper extends BaseMapper<Operation> {
    List<Operation> getApiPermission();
}
