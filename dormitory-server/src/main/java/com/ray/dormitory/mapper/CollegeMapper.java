package com.ray.dormitory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ray.dormitory.bean.College;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Ray Z
 * @Data 2019.10.26 18:42
 */

@Mapper
public interface CollegeMapper extends BaseMapper<College> {
}
