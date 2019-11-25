package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.po.Role;
import com.ray.dormitory.bean.po.User;
import com.ray.dormitory.bean.po.UserRole;
import com.ray.dormitory.mapper.RoleMapper;
import com.ray.dormitory.mapper.UserMapper;
import com.ray.dormitory.mapper.UserRoleMapper;
import com.ray.dormitory.service.UserService;
import com.ray.dormitory.util.PinyinUtil;
import com.ray.dormitory.util.bean.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Ray Z
 * @date 2019/10/27 21:54
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private static String defaultPsw = "123456";

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;


    @Override
    public User getUserByAccount(String account) {

        return baseMapper.getUserByAccount(account);
    }


    @Override
    public void updatePassword(String account, String oldPsw, String newPsw) {
        User user = baseMapper.getUserByAccount(account);
        if (user != null) {

            if (user.getPassword().equals(MD5Util.getMD5(oldPsw + user.getSalt()))) {
                String salt = MD5Util.getSalt();
                String password = MD5Util.getMD5(newPsw + salt);

                baseMapper.updatePsw(user.getId(), password, salt);

            } else {
                throw new NullPointerException("原密码有误，请重新输入");
            }
        } else {
            throw new NullPointerException("账户信息有误，请重新登录");
        }
    }

    @Override
    public boolean save(User user) {
        String roleStr = user.getRole();
        QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>().eq("name_zh", roleStr);
        Role role = roleMapper.selectOne(queryWrapper);

        if (role != null) {
            int count = baseMapper.selectCount(new QueryWrapper<User>().eq("account", user.getAccount()));
            if (count == 0) {
                String password = MD5Util.getMD5(PinyinUtil.getUpperCase(user.getName(), false) + defaultPsw);
                String salt = MD5Util.getSalt();
                password = MD5Util.getMD5(password + salt);
                user.setSalt(salt);
                user.setPassword(password);

                baseMapper.insert(user);
                userRoleMapper.insert(new UserRole(null, user.getId(), role.getId()));

            } else {
                throw new NullPointerException(user.getAccount() + "--" + user.getName() + " 帐号已存在");
            }
        } else {
            throw new NullPointerException(user.getAccount() + "--" + user.getName() + " 角色不存在");
        }

        return false;

    }
}
