package com.ray.dormitory.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserMenu {
    private String id;
    private String code;
    private String name;
    private String parentId;
    private String url;
    private Integer type;
    private String icon;
    private String permissionId;
    private boolean isEdit = false;
    private Integer level;
    private List<UserMenu> children;


}
