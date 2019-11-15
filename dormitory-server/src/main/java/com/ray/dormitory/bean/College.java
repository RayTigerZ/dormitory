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
 * @Data 2019.10.26 18:35
 */

@Getter
@Setter
@ToString
@NoArgsConstructor

@TableName("college")
public class College {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
}
