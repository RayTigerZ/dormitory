package com.ray.dormitory.controller;

import com.alibaba.excel.EasyExcel;
import com.ray.dormitory.bean.po.Room;
import com.ray.dormitory.service.RoomService;
import com.ray.dormitory.util.UploadDataListener;
import com.ray.dormitory.util.bean.ErrorEnum;
import com.ray.dormitory.util.bean.ResponseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author : Ray
 * @date : 2019.11.22 16:53
 */
@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping("/ofFloor")
    public ResponseBean getFloor(int buildingId, String floor) {
        return new ResponseBean(roomService.getRoomsOfFloor(buildingId, floor));
    }

    @PostMapping("/uploadBatch")
    public ResponseBean uploadBatch(MultipartFile file) throws IOException {
        try {
            EasyExcel.read(file.getInputStream(), Room.class, new UploadDataListener(roomService)).sheet().doRead();
            return new ResponseBean();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseBean(ErrorEnum.ERROR_204);
        }

    }
}
