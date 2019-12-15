package com.ray.dormitory.bean.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

/**
 * @author : Ray
 * @date : 2019.12.07 16:01
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("questionnaire")

@JsonIgnoreProperties(value = {"handler"})
public class Questionnaire {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;

    @TableField(exist = false)
    private List<Question> questions;

}
