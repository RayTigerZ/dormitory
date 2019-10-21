package com.ray.dormitory.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PermissionRole {
    private String url;
    private Set<Role> roles;

    private Integer type;
    private String name;


}
