package com.ray.dormitory.mapper;


import com.ray.dormitory.bean.SysLogger;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper {

    /**
     * 保存日志
     *
     * @param logger
     * @return
     */
    void insert(SysLogger logger);

}