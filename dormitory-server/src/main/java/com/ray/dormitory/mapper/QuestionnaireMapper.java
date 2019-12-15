package com.ray.dormitory.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ray.dormitory.bean.po.Questionnaire;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : Ray
 * @date : 2019.12.07 16:11
 */
@Repository
public interface QuestionnaireMapper extends BaseMapper<Questionnaire> {
    @Override
    List<Questionnaire> selectList(@Param(Constants.WRAPPER) Wrapper<Questionnaire> queryWrapper);
}
