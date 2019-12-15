package com.ray.dormitory.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ray.dormitory.bean.po.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Ray
 * @date : 2019.11.21 13:21
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
    /**
     * 通过用户名获取用户基本信息
     *
     * @param account
     * @return
     */
    User getUserByAccount(String account);

    List<User> getPage(long start, long size, @Param(Constants.WRAPPER) Wrapper<User> queryWrapper);


    /**
     * 更新用户密码
     *
     * @param id   用户ID
     * @param psw  新密码（加盐加密过后）
     * @param salt 盐值
     * @return
     */
    int updatePsw(int id, String psw, String salt);


}
