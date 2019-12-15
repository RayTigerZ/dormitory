package com.ray.dormitory.bean.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ray
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("permission")
public class Permission {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String path;
    private String component;
    private String name;
    private String iconClass;
    private int type;
    private Integer parentId;
    private boolean keepAlive;
    private boolean requireAuth;
    @TableField("`order`")
    private int order;

    @TableField(exist = false)
    private List<Role> roles;
    @TableField(exist = false)
    private List<Permission> children;

    public void addChild(Permission child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }

}
