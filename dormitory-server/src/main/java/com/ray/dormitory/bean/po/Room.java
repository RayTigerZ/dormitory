package com.ray.dormitory.bean.po;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ray.dormitory.bean.bo.Student;
import lombok.*;

import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.22 16:49
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("room")
public class Room {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @ExcelProperty("宿舍号")
    private String number;
    @ExcelProperty("最大可住")
    private int size;
    @ExcelProperty("宿舍楼")
    private Integer buildingId;

    @TableField(exist = false)
    private int lived;
    @TableField(exist = false)
    private List<Student> students;
}
