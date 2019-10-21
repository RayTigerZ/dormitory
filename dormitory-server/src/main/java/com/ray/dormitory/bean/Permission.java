package com.ray.dormitory.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Permission {
    private Integer id;

    private String url;
    private Boolean isUsable;
    private String name;

    private List<Role> roles;


}
