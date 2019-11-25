package com.ray.dormitory.controller;

import com.ray.dormitory.bean.po.Permission;
import com.ray.dormitory.service.PermissionService;
import com.ray.dormitory.util.JwtUtil;
import com.ray.dormitory.util.bean.ErrorEnum;
import com.ray.dormitory.util.bean.ResponseBean;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Ray
 * @date : 2019.11.21 11:38
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/menu")
    private ResponseBean initMenu(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        int userId = 0;

        //校验登陆的token是否与缓存中的token保持一致
        if (authorization != null && JwtUtil.verifyToken(authorization)) {
            userId = JwtUtil.getId(authorization);
            return new ResponseBean(permissionService.getMenuOfUser(userId));
        } else {
            return new ResponseBean(ErrorEnum.ERROR_302);
        }

    }

    /**
     * 获取前端菜单树形结构
     *
     * @return
     */
    @GetMapping("/all")
    ResponseBean tree() {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setData(permissionService.getAllPermission());
        return responseBean;
    }


    @ApiOperation(value = "获取角色对应的权限")
    @GetMapping("/ofRole")
    ResponseBean getIdsByRole(int roleId) {
        List<Integer> ids = permissionService.getMenuIdsOfRole(roleId);
        Permission allPermission = permissionService.getAllPermission();
        Map<String, Object> map = new HashMap<>();
        map.put("ids", ids);
        map.put("allPermission", allPermission);
        ResponseBean responseBean = new ResponseBean();
        responseBean.setData(map);
        return responseBean;
    }


    @PostMapping("/save")
    ResponseBean addOrUpdate(Permission permission) {
        try {
            permissionService.saveOrUpdate(permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseBean(ErrorEnum.ERROR_204.getErrorCode(), "保存" + ErrorEnum.ERROR_204.getErrorMsg() + ":" + e.getMessage());
        }

        return new ResponseBean();
    }

    /**
     * 更新角色对象的菜单权限
     *
     * @param ids    菜单id列表
     * @param roleId 角色id
     * @return
     */
    @PostMapping("/set")
    ResponseBean setPermission(int[] ids, int roleId) {
        try {
            permissionService.setPermissionForRole(roleId, ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseBean(ErrorEnum.ERROR_204.getErrorCode(), "保存" + ErrorEnum.ERROR_204.getErrorMsg() + ":" + e.getMessage());
        }

        return new ResponseBean();
    }

}
