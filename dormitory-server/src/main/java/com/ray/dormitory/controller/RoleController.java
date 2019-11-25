package com.ray.dormitory.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.po.Role;
import com.ray.dormitory.service.RoleService;
import com.ray.dormitory.util.bean.ErrorEnum;
import com.ray.dormitory.util.bean.ResponseBean;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Ray
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 分页角色列表
     *
     * @param pageSize
     * @param pageNum
     * @return
     */

    @GetMapping("/list")
    ResponseBean getRoles(long pageSize, long pageNum) {
        IPage<Role> rolePage = new Page<>();
        rolePage.setCurrent(pageNum);
        rolePage.setSize(pageSize);
        List<Role> roles = roleService.page(rolePage).getRecords();

        ResponseBean responseBean = new ResponseBean();
        responseBean.setData(roles);
        return responseBean;
    }


    @ApiOperation(value = "增加/更新角色", notes = "增加角色时参数id为空,更新角色必须传id")
    @PostMapping("/save")
    ResponseBean addOrUpdate(Role role) {
        try {
            roleService.saveOrUpdate(role);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseBean(ErrorEnum.ERROR_204.getErrorCode(), "保存" + ErrorEnum.ERROR_204.getErrorMsg() + ":" + e.getMessage());
        }
        return new ResponseBean();

    }

    @ApiOperation(value = "删除角色", notes = "通过id删除角色")
    @PostMapping("/delete")
    ResponseBean delete(Integer roleId) {

        try {
            roleService.removeById(roleId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseBean(ErrorEnum.ERROR_204.getErrorCode(), "删除" + ErrorEnum.ERROR_204.getErrorMsg() + ":" + e.getMessage());
        }
        return new ResponseBean();

    }
}
