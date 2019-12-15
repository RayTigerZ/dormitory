package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.po.Questionnaire;
import com.ray.dormitory.mapper.QuestionnaireMapper;
import com.ray.dormitory.service.QuestionnaireService;
import org.springframework.stereotype.Repository;

/**
 * @author : Ray
 * @date : 2019.12.08 14:58
 */
@Repository
public class QuestionnaireServiceImpl extends ServiceImpl<QuestionnaireMapper, Questionnaire> implements QuestionnaireService {
}
