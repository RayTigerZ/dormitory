package com.ray.dormitory.bean.po;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import lombok.*;

import java.util.List;

/**
 * @author Ray
 * @date 2019/11/23 16:21
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

@TableName("user")
@JsonIgnoreProperties(value = {"handler"})
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("帐号")
    private String account;
    @JsonIgnore
    @Expose(serialize = false)
    private String password;
    @JsonIgnore
    @Expose(serialize = false)
    private String salt;
    @ExcelProperty("手机号码")
    private String phone;
    @ExcelProperty("邮箱")
    private String email;
    @ExcelProperty("性别")
    private String sex;
    private Boolean isUsable;
    private Integer classId;

    @TableField(exist = false)
    @ExcelProperty("班级")
    private String cla;
    @TableField(exist = false)
    @ExcelProperty("学院")
    private String college;

    @TableField(exist = false)
    private List<Role> roles;


    /**
     * 在excel批量导入信息中使用的角色字段，其余情况不使用该字段
     * note:多个角色时用英文逗号隔开(,)
     */
    @JsonIgnore
    @TableField(exist = false)
    @ExcelProperty("角色")
    private String role;


}
