package com.ray.dormitory.controller;

import com.ray.dormitory.bean.po.Questionnaire;
import com.ray.dormitory.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : Ray
 * @date : 2019.12.08 14:59
 */
@RestController
public class QuestionnaireController {
    @Autowired
    private QuestionnaireService questionnaireService;

    @GetMapping("/questionnaires")
    public List<Questionnaire> getQuestionnaires() {
        return questionnaireService.list();
    }
}
