package com.ray.dormitory.bean.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

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
    private String name;
    private String nameZh;
    private String description;
}
