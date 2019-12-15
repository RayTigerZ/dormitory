package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.po.Organization;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.mapper.OrganizationMapper;
import com.ray.dormitory.service.OrganizationService;
import com.ray.dormitory.util.bean.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.28 19:43
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements OrganizationService {
    @Override
    public List<Organization> getAll() {
        return baseMapper.getAll();
    }


    @Override
    public boolean removeById(Serializable id) {

        QueryWrapper<Organization> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);

        if (baseMapper.selectCount(queryWrapper) == 0) {
            return baseMapper.deleteById(id) > 0;
        } else {
            throw new CustomException(ErrorEnum.CHILDREN_ORGANIZATION_EXIST);
        }


    }

    @Override
    public boolean save(Organization organization) {
        log.info("{}", organization);
        String parentName = organization.getParent();
        Organization parent = null;

        // parent不为空时，通过其名称找到parentId
        //否则抛出异常
        if (parentName != null) {
            parent = baseMapper.selectOne(new QueryWrapper<Organization>().eq("name", parentName));
            if (parent == null) {
                throw new NullPointerException("不存在名字为'" + parentName + "'的组织");
            }
        }

        //增加学院信息时，code和name在表中必须都是不存在的
        //更新学院信息时，code和name除表中自身数据外必须是不存在的
        QueryWrapper<Organization> queryWrapper = new QueryWrapper<>();
        Integer id = organization.getId();
        queryWrapper.ne(id != null, "id", id);
        queryWrapper.and(i -> i.eq("code", organization.getCode()).or().eq("name", organization.getName()));
        int count = baseMapper.selectCount(queryWrapper);
        if (count == 0) {
            if (parent != null) {
                organization.setParentId(parent.getId());
            }


            if (id == null) {
                baseMapper.insert(organization);
            } else {
                baseMapper.updateById(organization);
            }

            return true;
        } else {
            throw new NullPointerException("组织编码、组织名称存在重复");
        }


    }
}
