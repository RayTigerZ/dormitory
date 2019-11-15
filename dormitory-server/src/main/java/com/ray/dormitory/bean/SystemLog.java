package com.ray.dormitory.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 系统日志表
 *
 * @author Ray Z
 */
@Getter
@Setter
@ToString
@NoArgsConstructor

@TableName("system_log")
public class SystemLog {


    /**
     * 编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 操作功能
     */
    private String optype;
    /**
     * 操作IP地址
     */

    private String remoteAddr;
    /**
     * 请求URI
     */
    private String requestUri;
    /**
     * 操作方式
     */
    private String httpMethod;
    /**
     * 操作提交的数据
     */
    private String params;
    /**
     * 返回的数据
     */
    private String outBody;
    /**
     * 方法执行时间
     */
    private Long useTime;
    /**
     * 浏览器信息
     */
    private String browser;
    /**
     * 操作者帐号
     */
    private String account;

    /**
     * 创建时间
     */
    private Date createTime;
    private String remark;

}