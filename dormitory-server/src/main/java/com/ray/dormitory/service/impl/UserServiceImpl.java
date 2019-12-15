package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ray Z
 * @date 2019/10/27 21:54
 */
@Service
@Transactional(rollbackFor = Exception.class)
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
    public boolean updatePassword(String account, String oldPsw, String newPsw) {
        User user = baseMapper.getUserByAccount(account);
        if (user != null) {

            if (user.getPassword().equals(MD5Util.getMD5(oldPsw + user.getSalt()))) {
                String salt = MD5Util.getSalt();
                String password = MD5Util.getMD5(newPsw + salt);


                return baseMapper.updatePsw(user.getId(), password, salt) > 0;

            } else {
                throw new NullPointerException("原密码有误，请重新输入");
            }
        } else {
            throw new NullPointerException("账户信息有误，请重新登录");
        }
    }

    @Override
    public IPage<User> page(IPage<User> page, Wrapper<User> queryWrapper) {
        List<User> users = baseMapper.getPage(page.offset(), page.getSize(), queryWrapper);
        page.setRecords(users);
        page.setTotal(baseMapper.selectCount(queryWrapper));

        return page;

    }

    @Override
    public boolean save(User user) {
        try {
            List<Integer> roleIds = getRoleId(user);
            if (roleIds != null && roleIds.size() > 0) {
                int count = baseMapper.selectCount(new QueryWrapper<User>().eq("account", user.getAccount()));
                if (count == 0) {
                    String password = MD5Util.getMD5(PinyinUtil.getUpperCase(user.getName(), false) + defaultPsw);
                    String salt = MD5Util.getSalt();
                    password = MD5Util.getMD5(password + salt);
                    user.setSalt(salt);
                    user.setPassword(password);

                    baseMapper.insert(user);
                    for (int roleId : roleIds) {
                        userRoleMapper.insert(new UserRole(null, user.getId(), roleId));
                    }

                } else {
                    throw new NullPointerException(user.getAccount() + "--" + user.getName() + " 帐号已存在");
                }
            }
        } catch (Exception e) {
            throw e;
        }

        return true;
    }


    /**
     * 通过user的roles或者role属性获取roleId（role优先）
     *
     * @param user
     * @return
     */
    private List<Integer> getRoleId(User user) {
        List<Integer> ids = new ArrayList<>();
        List<Role> roles = user.getRoles();
        if (roles != null && roles.size() > 0) {
            for (Role role : roles) {
                int id = role.getId();
                int count = roleMapper.selectCount(new QueryWrapper<Role>().eq("id", id));
                if (count == 0) {
                    throw new NullPointerException("角色不存在");
                } else {
                    ids.add(id);
                }

            }
        } else {
            String roleStr = user.getRole();
            if (StringUtils.isBlank(roleStr)) {
                throw new NullPointerException("角色信息不能为空");
            }
            String[] roleStrs = roleStr.split(",");
            for (String s : roleStrs) {
                QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>().eq("name_zh", s);
                Role role = roleMapper.selectOne(queryWrapper);
                if (role == null) {
                    throw new NullPointerException("角色不存在");
                } else {
                    ids.add(role.getId());
                }
            }
        }
        return ids;
    }
}
