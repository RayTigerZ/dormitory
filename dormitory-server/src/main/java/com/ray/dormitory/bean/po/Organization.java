package com.ray.dormitory.bean.po;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.28 18:54
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("organization")

@JsonIgnoreProperties(value = {"handler"})
public class Organization {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @ExcelProperty("组织名称")
    private String name;
    @ExcelProperty("组织代码")
    private String code;

    private Integer parentId;

    @TableField(exist = false)
    private List<Organization> children;

    @ExcelProperty("父组织名称")
    @TableField(exist = false)
    @JsonIgnore
    private String parent;
}
