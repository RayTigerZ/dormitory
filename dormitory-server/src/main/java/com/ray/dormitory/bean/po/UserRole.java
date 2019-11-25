package com.ray.dormitory.bean.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * @author : Ray
 * @date : 2019.11.21 9:19
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_role")
public class UserRole {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer roleId;

}
