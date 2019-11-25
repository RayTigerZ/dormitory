package com.ray.dormitory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ray.dormitory.bean.po.User;
import org.springframework.stereotype.Repository;

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
