package com.ray.dormitory.bean;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBean {

    // http 状态码
    private int code = 200;

    // 返回信息
    private String msg = "操作成功";

    // 返回的数据
    private Object data;


}