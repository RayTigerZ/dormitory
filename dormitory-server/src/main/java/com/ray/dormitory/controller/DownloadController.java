package com.ray.dormitory.controller;

import com.ray.dormitory.util.SysConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @author : Ray
 * @date : 2019/11/19 20:05
 */

@Slf4j
@RestController
@RequestMapping("/download")
public class DownloadController {

    private static String excelExtension = ".xlsx";
    private static String excelContentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8";
    @Autowired
    private SysConfig sysConfig;

    @GetMapping("/batchExcel")
    public void batchExcel(String code, HttpServletResponse response) {

        try {
            String filename = code + excelExtension;
            String filePath = sysConfig.getTemplatePath() + filename;

            // 如果文件名不为空，则进行下载
            if (filename != null) {
                File file = new File(filePath);
                // 如果文件存在，则进行下载
                if (file.exists()) {
                    // 配置文件下载"application/octet-stream"
//                    response.setContentType(excelContentType);
                    response.setHeader("fileName", URLEncoder.encode(filename, "UTF-8"));
                    response.addHeader("Access-Control-Expose-Headers", "fileName");

                    // 实现文件下载
//                    byte[] bytes = FileUtil.fileToByte(filePath);
                    InputStream fileInputStream = new FileInputStream(file);
                    //response.setHeader("Content-length", String.valueOf(bytes.length));
                    response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
                    OutputStream out = response.getOutputStream();
                    byte[] buff = new byte[1024 * 8];
//                    out.write(bytes, 0, bytes.length);
                    int len = 0;
                    while ((len = fileInputStream.read(buff)) != -1) {
                        out.write(buff, 0, len);
                    }
                    fileInputStream.close();
                    out.flush();
                    out.close();


                    log.info("download {} successfully!", filePath);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();

        }


    }

}
