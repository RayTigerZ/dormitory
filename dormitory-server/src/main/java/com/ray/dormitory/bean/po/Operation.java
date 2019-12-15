package com.ray.dormitory.bean.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

/**
 * @author : Ray
 * @date : 2019.12.05 15:27
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("operation")

@JsonIgnoreProperties(value = {"handler"})
public class Operation {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String opName;
    private String uri;
    private String method;
    private Integer permissionId;

    @TableField(exist = false)
    private List<Role> roleList;
}
