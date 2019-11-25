package com.ray.dormitory.bean.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * @author : Ray
 * @date : 2019.11.22 14:46
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("building")
public class Building {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
}
