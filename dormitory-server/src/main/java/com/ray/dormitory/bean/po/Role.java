package com.ray.dormitory.bean.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Ray Z
 * @date 2019/10/26 18:12:39
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("role")
public class Role {
    @TableId(type = IdType.AUTO)

    private Integer id;
    @NotNull(message = "角色英文名不能为空")
    @Size(min = 1, max = 16, message = "角色英文名长度不符合要求")
    private String name;
    @NotNull(message = "角色中文名不能为空")
    @Size(min = 1, max = 16, message = "角色中文名长度不符合要求")
    private String nameZh;
    @NotNull(message = "角色描述不能为空")
    @Size(min = 1, max = 64, message = "角色描述长度不符合要求")
    private String description;
}
