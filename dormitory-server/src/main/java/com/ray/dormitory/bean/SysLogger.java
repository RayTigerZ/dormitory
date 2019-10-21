package com.ray.dormitory.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 系统日志表
 *
 * @author zhengrt
 * @since 2019-02-28 ${time}
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class SysLogger {


    /**
     * 编号
     */
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
    private String addAccount;
    /**
     * 操作者用户名
     */
    private String addUsername;
    /**
     * 创建时间
     */
    private Date addTime;
    private String remarks;

}