package com.ray.dormitory.util.bean;


import lombok.*;

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

    public ResponseBean(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseBean(ErrorEnum errorEnum) {
        this.code = errorEnum.getErrorCode();
        this.msg = errorEnum.getErrorMsg();
    }

    public ResponseBean(Object data) {

        this.data = data;
    }


}