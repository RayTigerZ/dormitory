package com.ray.dormitory.controller;

import com.ray.dormitory.util.SysConfig;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
    @Autowired
    private SysConfig sysConfig;

    @PostMapping("/batchExcel")
    public void batchExcel(String code, HttpServletResponse response) {
        String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8";
        try {
            String filename = code + excelExtension;
            String filePath = sysConfig.getTemplatePath() + filename;

            // 如果文件名不为空，则进行下载
            if (filename != null) {
                File file = new File(filePath);
                // 如果文件存在，则进行下载
                if (file.exists()) {
                    // 配置文件下载
                    response.setContentType(contentType);
                    response.setHeader("fileName", URLEncoder.encode(filename, "UTF-8"));
                    response.addHeader("Access-Control-Expose-Headers", "fileName");
                    // 下载文件能正常显示中文
                    response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
                    // 实现文件下载
                    byte[] buffer = new byte[1024];

                    @Cleanup
                    FileInputStream fileInputStream = new FileInputStream(file);
                    @Cleanup
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                    OutputStream outputStream = response.getOutputStream();
                    int len;
                    while ((len = bufferedInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, len);

                    }
                    outputStream.flush();
                    outputStream.close();
                    log.info("download {} successfully!", filePath);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();

        }


    }

}
