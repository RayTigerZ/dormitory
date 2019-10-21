package com.ray.dormitory.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Role {
    private Integer id;
    private String name;
    private String nameZh;
    private String url;
}
