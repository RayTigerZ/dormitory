package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.mapper.ClassMapper;
import com.ray.dormitory.service.ClassService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : Ray
 * @date : 2019.11.28 15:21
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {
}
