package com.ray.dormitory.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ray.dormitory.bean.po.SystemLog;
import com.ray.dormitory.service.SystemLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Ray
 * @date : 2019.11.21 16:32
 */
@RestController
@Slf4j
@RequestMapping("/systemLog")
public class SystemLogController {
    @Autowired
    private SystemLogService systemLogService;

    @GetMapping("/list")
    public IPage<SystemLog> getLogList(int pageNum, int pageSize, String account, String begin, String end) {

        return systemLogService.getPage(pageNum, pageSize, account, begin, end);
    }
}
