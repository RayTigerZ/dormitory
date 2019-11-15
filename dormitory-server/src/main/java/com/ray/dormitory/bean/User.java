package com.ray.dormitory.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/**
 * @author Ray
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

@TableName(value = "user", resultMap = "userResultMap")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String account;
    private String password;
    private String salt;
    private String phone;
    private String email;
    private String sex;
    private Boolean isUsable;
    private Set<String> roles;
    private String className;
    private String college;
    private String roomNum;


}
