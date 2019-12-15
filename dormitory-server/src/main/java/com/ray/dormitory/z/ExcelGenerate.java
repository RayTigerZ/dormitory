package com.ray.dormitory.z;

import com.alibaba.excel.EasyExcel;
import com.ray.dormitory.bean.po.Organization;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.29 19:39
 */
public class ExcelGenerate {

    public static void main(String[] args) {
        // 写法1
        String fileName = "D:\\graduation-project\\code\\dormitory-server\\src\\main\\resources\\excel\\organizationBatch.xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        List<Organization> list = new ArrayList<>();
        list.add(new Organization(null, "", "", null, null, ""));
        EasyExcel.write(fileName, Organization.class).sheet("模板").doWrite(list);
    }
}
