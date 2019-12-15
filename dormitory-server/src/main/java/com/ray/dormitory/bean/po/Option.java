package com.ray.dormitory.bean.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * @author : Ray
 * @date : 2019.12.07 16:03
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("option")
public class Option {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String option;
    private Integer value;
    private Integer questionId;
}
