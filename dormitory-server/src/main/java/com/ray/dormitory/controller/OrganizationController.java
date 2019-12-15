package com.ray.dormitory.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ray.dormitory.bean.po.Organization;
import com.ray.dormitory.service.OrganizationService;
import com.ray.dormitory.util.UploadDataListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.28 19:46
 */
@Slf4j
@RestController
@RequestMapping("/organization")
public class OrganizationController {
    private static int[] values = {3, 5, 10};
    @Autowired
    private OrganizationService organizationService;

    @GetMapping("/all")
    public List<Organization> getAll(Integer level) {

        List<Organization> list;
        if (level == null) {
            list = organizationService.getAll();
            //将组织树结构的第三层 children属性设置为null，方便前端渲染
            for (Organization a : list) {
                for (Organization b : a.getChildren()) {
                    for (Organization c : b.getChildren()) {
                        c.setChildren(null);
                    }
                }
            }
        } else {
            //获取指定层级的组织
            QueryWrapper<Organization> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("length(code)", values[level - 1]).orderByAsc("code");

            list = organizationService.list(queryWrapper);
        }

        return list;

    }


    @PostMapping("/save")
    public boolean save(Organization organization) {

        return organizationService.save(organization);

    }

    @PostMapping("/delete")
    public boolean deleteById(Integer id) {

        return organizationService.removeById(id);

    }

    @PostMapping("/batchSave")
    public boolean save(String time, MultipartFile file) throws IOException {

        UploadDataListener<Organization> listener = new UploadDataListener<>(organizationService, time);
        EasyExcel.read(file.getInputStream(), Organization.class, listener).sheet().doRead();
        return true;

    }

}
