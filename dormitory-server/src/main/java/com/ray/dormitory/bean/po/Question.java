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
 * @date : 2019.12.07 16:07
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("question")

@JsonIgnoreProperties(value = {"handler"})
public class Question {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private Integer questionnaireId;

    @TableField(exist = false)
    private List<Option> options;
}
