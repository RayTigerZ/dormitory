package com.ray.dormitory.bean;

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
public class User {
    private Integer id;
    private String name;
    private String account;
    private String password;
    private String salt;
    private String phone;
    private String email;
    private String sex;
    private Boolean isUsable;
    private Set<Role> roles;

}
