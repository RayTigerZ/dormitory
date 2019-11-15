package com.ray.dormitory.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Ray Z
 * @date 2019/10/26 18:12:39
 */
@Getter
@Setter
@ToString
@NoArgsConstructor

@TableName("role")
public class Role {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String nameZh;
}
