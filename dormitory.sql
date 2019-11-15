/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : localhost:3306
 Source Schema         : dormitory

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 15/11/2019 23:15:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for attachment
-- ----------------------------
DROP TABLE IF EXISTS `attachment`;
CREATE TABLE `attachment`  (
  `id` int(10) NOT NULL COMMENT 'ID',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件名称',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件文件路径',
  `news_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '公告ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `news_id`(`news_id`) USING BTREE,
  CONSTRAINT `attachment_ibfk_1` FOREIGN KEY (`news_id`) REFERENCES `news` (`news_type_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公告附件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for building
-- ----------------------------
DROP TABLE IF EXISTS `building`;
CREATE TABLE `building`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '宿舍楼名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '宿舍楼' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for charge
-- ----------------------------
DROP TABLE IF EXISTS `charge`;
CREATE TABLE `charge`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '费用项名称',
  `value` double(10, 3) UNSIGNED NOT NULL DEFAULT 0.000 COMMENT '每单位费用',
  `version` int(10) UNSIGNED NULL DEFAULT 1 COMMENT '版本',
  `units` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '费用项' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of charge
-- ----------------------------
INSERT INTO `charge` VALUES (1, '水费', 4.670, 1, '元/吨', NULL, NULL);
INSERT INTO `charge` VALUES (2, '电费', 0.584, 1, '元/度', NULL, NULL);
INSERT INTO `charge` VALUES (3, '热水费', 8.780, 1, '元/吨', NULL, NULL);

-- ----------------------------
-- Table structure for class
-- ----------------------------
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '班级名',
  `grade` int(4) NULL DEFAULT NULL COMMENT '年级',
  `college_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '学院ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `college_id`(`college_id`) USING BTREE,
  CONSTRAINT `class_ibfk_1` FOREIGN KEY (`college_id`) REFERENCES `college` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '班级' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for college
-- ----------------------------
DROP TABLE IF EXISTS `college`;
CREATE TABLE `college`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学院名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '学院' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of college
-- ----------------------------
INSERT INTO `college` VALUES (1, '计算机学院');
INSERT INTO `college` VALUES (2, '外国语学院');
INSERT INTO `college` VALUES (3, '机械工程学院');

-- ----------------------------
-- Table structure for cost
-- ----------------------------
DROP TABLE IF EXISTS `cost`;
CREATE TABLE `cost`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `room_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '宿舍ID',
  `charge_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '费用项ID',
  `date` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账单年月',
  `count` double(10, 2) UNSIGNED NULL DEFAULT NULL COMMENT '用量计数',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_payed` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否缴费',
  `pay_time` datetime(0) NULL DEFAULT NULL COMMENT '缴费时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `room_id`(`room_id`) USING BTREE,
  INDEX `charge_id`(`charge_id`) USING BTREE,
  CONSTRAINT `cost_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `cost_ibfk_2` FOREIGN KEY (`charge_id`) REFERENCES `charge` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '账单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路径',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件名称',
  `icon_class` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标样式',
  `keep_alive` tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT 'keepAlive参数值',
  `require_auth` tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '是否鉴权',
  `parent_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '父菜单节点',
  `is_usable` tinyint(1) UNSIGNED NULL DEFAULT NULL COMMENT '是否可用',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `parentId`(`parent_id`) USING BTREE,
  CONSTRAINT `parentId` FOREIGN KEY (`parent_id`) REFERENCES `menu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告内容',
  `author` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作者',
  `publish_time` datetime(0) NULL DEFAULT NULL COMMENT '发布时间',
  `news_type_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '公告类型ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `is_publish` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否发布',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `news_type_id`(`news_type_id`) USING BTREE,
  CONSTRAINT `news_ibfk_1` FOREIGN KEY (`news_type_id`) REFERENCES `news_type` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for news_type
-- ----------------------------
DROP TABLE IF EXISTS `news_type`;
CREATE TABLE `news_type`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告类型名',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `version` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '版本',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of news_type
-- ----------------------------
INSERT INTO `news_type` VALUES (1, '社区活动', NULL, NULL, NULL);
INSERT INTO `news_type` VALUES (2, '违规违纪', NULL, NULL, NULL);
INSERT INTO `news_type` VALUES (3, '社区通知', NULL, NULL, NULL);
INSERT INTO `news_type` VALUES (4, '寻物招领', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '接收人ID',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通知内容',
  `is_read` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否阅读（0：否，1：是）',
  `notice_time` datetime(0) NULL DEFAULT NULL COMMENT '通知时间',
  `read_time` datetime(0) NULL DEFAULT NULL COMMENT '阅读时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `notice_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口URL',
  `is_usable` tinyint(1) UNSIGNED NULL DEFAULT 1 COMMENT '是否可用',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `version` int(10) NULL DEFAULT NULL COMMENT '版本',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '后端权限' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for repair_record
-- ----------------------------
DROP TABLE IF EXISTS `repair_record`;
CREATE TABLE `repair_record`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `room_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '宿舍ID',
  `student_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '报修学生ID',
  `problem` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维修问题',
  `status` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态',
  `repairmen_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '维修人员ID',
  `appointment_time` datetime(0) NULL DEFAULT NULL COMMENT '预约上门时间',
  `result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维修结果',
  `feedback` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '维修反馈',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `room_id`(`room_id`) USING BTREE,
  INDEX `student_id`(`student_id`) USING BTREE,
  INDEX `repairmen_id`(`repairmen_id`) USING BTREE,
  CONSTRAINT `repair_record_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `repair_record_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `user` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `repair_record_ibfk_3` FOREIGN KEY (`repairmen_id`) REFERENCES `user` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称英文',
  `name_zh` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称中文',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, 'ROLE_sys_manager', '系统管理员');
INSERT INTO `role` VALUES (2, 'ROLE_hoste_manager', '宿舍管理员');
INSERT INTO `role` VALUES (3, 'ROLE_accendant', '维修人员');
INSERT INTO `role` VALUES (4, 'ROLE_student', '学生');
INSERT INTO `role` VALUES (13, 'ROLE_test2', '测试角色2');
INSERT INTO `role` VALUES (14, 'ROLE_test1', '测试角色1');

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  INDEX `menu_id`(`menu_id`) USING BTREE,
  CONSTRAINT `role_menu_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `role_menu_ibfk_2` FOREIGN KEY (`menu_id`) REFERENCES `menu` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色--菜单联系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '角色ID',
  `permission_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '后端权限ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  INDEX `permission_id`(`permission_id`) USING BTREE,
  CONSTRAINT `role_permission_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `role_permission_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色--后端权限关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `number` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '宿舍号',
  `size` int(2) NULL DEFAULT NULL COMMENT '容量',
  `building_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '宿舍楼号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `building_id`(`building_id`) USING BTREE,
  CONSTRAINT `room_ibfk_1` FOREIGN KEY (`building_id`) REFERENCES `building` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '宿舍' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for stay_apply
-- ----------------------------
DROP TABLE IF EXISTS `stay_apply`;
CREATE TABLE `stay_apply`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `student_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '学生ID',
  `reason` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '留校原因',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '申请时间',
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
  `is_consent` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否同意',
  `process_time` datetime(0) NULL DEFAULT NULL COMMENT '处理时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `student_id`(`student_id`) USING BTREE,
  CONSTRAINT `stay_apply_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `user` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '留校申请' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_log
-- ----------------------------
DROP TABLE IF EXISTS `system_log`;
CREATE TABLE `system_log`  (
  `id` int(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `optype` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '事件',
  `remote_addr` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `request_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问地址',
  `http_method` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方法',
  `params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `out_body` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `use_time` int(10) NULL DEFAULT NULL,
  `browser` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `account` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '帐号',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_log
-- ----------------------------
INSERT INTO `system_log` VALUES (1, '登录', '127.0.0.1', '/user/login', 'POST', '{\"password\":\"fgjdh\",\"account\":\"hjnfghjmfgjj\"}', NULL, 3, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 13:51:18', NULL);
INSERT INTO `system_log` VALUES (2, '登录', '127.0.0.1', '/user/login', 'POST', '{\"password\":\"fgjdh\",\"account\":\"hjnfghjmfgjj\"}', NULL, 1, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 13:51:26', NULL);
INSERT INTO `system_log` VALUES (3, '登录', '127.0.0.1', '/user/login', 'POST', '{\"password\":\"fgjdh\",\"account\":\"hjnfghjmfgjj\"}', NULL, 180, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 13:59:28', NULL);
INSERT INTO `system_log` VALUES (4, '登录', '127.0.0.1', '/user/login', 'POST', '{\"password\":\"fgjdh\",\"account\":\"hjnfghjmfgjj\"}', NULL, 145, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 14:38:23', NULL);
INSERT INTO `system_log` VALUES (5, '登录', '127.0.0.1', '/user/login', 'POST', '{\"password\":\"fgjdh\",\"account\":\"hjnfghjmfgjj\"}', NULL, 3, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 14:43:07', NULL);
INSERT INTO `system_log` VALUES (6, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{}', NULL, 3, 'PostmanRuntime/7.15.2', NULL, '2019-10-26 14:43:12', NULL);
INSERT INTO `system_log` VALUES (7, '登录', '127.0.0.1', '/user/login', 'POST', '{\"password\":\"fgjdh\",\"account\":\"hjnfghjmfgjj\"}', NULL, 13, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 19:15:40', NULL);
INSERT INTO `system_log` VALUES (8, '登录', '127.0.0.1', '/user/login', 'POST', '{\"password\":\"fgjdh\",\"account\":\"hjnfghjmfgjj\"}', NULL, 3, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 19:15:47', NULL);
INSERT INTO `system_log` VALUES (9, '登录', '127.0.0.1', '/user/login', 'POST', '{\"password\":\"fgjdh\",\"account\":\"hjnfghjmfgjj\"}', NULL, 2, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 19:16:00', NULL);
INSERT INTO `system_log` VALUES (10, '登录', '127.0.0.1', '/user/login', 'POST', '{\"password\":\"ghdthytj\",\"account\":\"hgjfjkj\"}', NULL, 2, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 19:16:10', NULL);
INSERT INTO `system_log` VALUES (11, '登录', '127.0.0.1', '/user/login', 'POST', '{\"password\":\"ghdthytj\",\"account\":\"hgjfjkj\"}', NULL, 21, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 19:18:51', NULL);
INSERT INTO `system_log` VALUES (12, '登录', '127.0.0.1', '/user/login', 'POST', '{\"password\":\"ghdthytj\",\"account\":\"hgjfjkj\"}', NULL, 20, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 19:20:32', NULL);
INSERT INTO `system_log` VALUES (13, '登录', '127.0.0.1', '/user/login', 'POST', '{\"password\":\"dfghcfgh\",\"account\":\"gulihio\"}', NULL, 4, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 19:22:30', NULL);
INSERT INTO `system_log` VALUES (14, '登录', '127.0.0.1', '/user/login', 'POST', '{\"password\":\"dfghcfgh\",\"account\":\"gulihio\"}', NULL, 3, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 19:22:51', NULL);
INSERT INTO `system_log` VALUES (15, '登录', '127.0.0.1', '/user/login', 'POST', '{\"password\":\"dfghcfgh\",\"account\":\"gulihio\"}', NULL, 2, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 19:24:35', NULL);
INSERT INTO `system_log` VALUES (16, '登录', '127.0.0.1', '/user/login', 'POST', '{\"password\":\"dfghcfgh\",\"account\":\"gulihio\"}', NULL, 29, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 19:26:34', NULL);
INSERT INTO `system_log` VALUES (17, '登录', '127.0.0.1', '/user/login', 'POST', '{\"password\":\"dfghcfgh\",\"account\":\"gulihio\"}', NULL, 6, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 19:26:42', NULL);
INSERT INTO `system_log` VALUES (18, '登录', '127.0.0.1', '/user/login', 'POST', '{\"password\":\"dfghcfgh\",\"account\":\"gulihio\"}', NULL, 11, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 19:32:23', NULL);
INSERT INTO `system_log` VALUES (19, '登录', '127.0.0.1', '/user/login', 'POST', '{\"password\":\"dfghcfgh\",\"account\":\"gulihio\"}', NULL, 2, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 19:32:31', NULL);
INSERT INTO `system_log` VALUES (20, '登录', '127.0.0.1', '/user/login', 'POST', '{\"password\":\"dfghcfgh\",\"account\":\"gulihio\"}', NULL, 3, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 19:32:42', NULL);
INSERT INTO `system_log` VALUES (21, '登录', '127.0.0.1', '/user/login', 'POST', '{\"password\":\"dfghcfgh\",\"account\":\"gulihio\"}', NULL, 12, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 19:38:21', NULL);
INSERT INTO `system_log` VALUES (22, '登录', '127.0.0.1', '/user/login', 'POST', '{\"password\":\"dfghcfgh\",\"account\":\"gulihio\"}', NULL, 10, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 19:39:38', NULL);
INSERT INTO `system_log` VALUES (23, '登录', '127.0.0.1', '/user/login', 'POST', '{\"password\":\"dfghcfgh\",\"account\":\"gulihio\"}', NULL, 2, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 19:41:08', NULL);
INSERT INTO `system_log` VALUES (24, '登录', '127.0.0.1', '/user/login', 'POST', '{\"password\":\"dfghcfgh\",\"account\":\"gulihio\"}', NULL, 10, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 19:44:41', NULL);
INSERT INTO `system_log` VALUES (25, NULL, '0:0:0:0:0:0:0:1', '/test', 'POST', '{}', NULL, 2, 'PostmanRuntime/7.15.2', NULL, '2019-10-26 19:49:45', NULL);
INSERT INTO `system_log` VALUES (26, NULL, '0:0:0:0:0:0:0:1', '/test', 'POST', '{}', NULL, 3, 'PostmanRuntime/7.15.2', NULL, '2019-10-26 19:49:49', NULL);
INSERT INTO `system_log` VALUES (27, NULL, '0:0:0:0:0:0:0:1', '/test', 'POST', '{}', NULL, 11, 'PostmanRuntime/7.15.2', NULL, '2019-10-26 19:51:12', NULL);
INSERT INTO `system_log` VALUES (28, '登录', '127.0.0.1', '/user/login', 'POST', '{\"password\":\"dfghcfgh\",\"account\":\"gulihio\"}', NULL, 10, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 20:23:19', NULL);
INSERT INTO `system_log` VALUES (29, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{\"password\":\"y6eiut6j\",\"account\":\"hbklml\"}', NULL, 14, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-26 20:57:27', NULL);
INSERT INTO `system_log` VALUES (30, NULL, '0:0:0:0:0:0:0:1', '/index.html', 'POST', '{}', NULL, 2, 'PostmanRuntime/7.15.2', NULL, '2019-10-26 21:31:30', NULL);
INSERT INTO `system_log` VALUES (31, NULL, '0:0:0:0:0:0:0:1', '/index.html', 'POST', '{}', NULL, 12, 'PostmanRuntime/7.15.2', NULL, '2019-10-26 21:32:51', NULL);
INSERT INTO `system_log` VALUES (32, NULL, '0:0:0:0:0:0:0:1', '/index.html', 'POST', '{}', NULL, 14, 'PostmanRuntime/7.15.2', NULL, '2019-10-26 21:34:08', NULL);
INSERT INTO `system_log` VALUES (33, NULL, '0:0:0:0:0:0:0:1', '/index.html', 'POST', '{}', NULL, 2, 'PostmanRuntime/7.15.2', NULL, '2019-10-26 21:34:14', NULL);
INSERT INTO `system_log` VALUES (34, NULL, '0:0:0:0:0:0:0:1', '/index.html', 'POST', '{}', NULL, 3, 'PostmanRuntime/7.15.2', NULL, '2019-10-26 21:35:17', NULL);
INSERT INTO `system_log` VALUES (35, NULL, '0:0:0:0:0:0:0:1', '/index.html', 'GET', '{}', NULL, 1, 'PostmanRuntime/7.15.2', NULL, '2019-10-26 21:36:25', NULL);
INSERT INTO `system_log` VALUES (36, NULL, '0:0:0:0:0:0:0:1', '/index.html', 'GET', '{}', NULL, 67, 'PostmanRuntime/7.19.0', NULL, '2019-10-26 22:48:40', NULL);
INSERT INTO `system_log` VALUES (37, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'GET', '{}', NULL, 36, 'PostmanRuntime/7.19.0', NULL, '2019-10-26 22:49:15', NULL);
INSERT INTO `system_log` VALUES (38, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{}', NULL, 67, 'PostmanRuntime/7.19.0', NULL, '2019-10-26 22:49:22', NULL);
INSERT INTO `system_log` VALUES (39, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{}', NULL, 23, 'PostmanRuntime/7.19.0', NULL, '2019-10-26 22:51:01', NULL);
INSERT INTO `system_log` VALUES (40, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{}', NULL, 16, 'PostmanRuntime/7.19.0', NULL, '2019-10-26 23:01:40', NULL);
INSERT INTO `system_log` VALUES (41, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{}', NULL, 234319, 'PostmanRuntime/7.19.0', NULL, '2019-10-26 23:12:01', NULL);
INSERT INTO `system_log` VALUES (42, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{}', NULL, 654, 'PostmanRuntime/7.19.0', NULL, '2019-10-26 23:21:26', NULL);
INSERT INTO `system_log` VALUES (43, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{}', NULL, 3694, 'PostmanRuntime/7.19.0', NULL, '2019-10-26 23:29:29', NULL);
INSERT INTO `system_log` VALUES (44, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{}', NULL, 13, 'PostmanRuntime/7.19.0', NULL, '2019-10-26 23:31:19', NULL);
INSERT INTO `system_log` VALUES (45, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{}', NULL, 2990, 'PostmanRuntime/7.19.0', NULL, '2019-10-26 23:32:49', NULL);
INSERT INTO `system_log` VALUES (46, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{}', NULL, 8016, 'PostmanRuntime/7.19.0', NULL, '2019-10-26 23:45:23', NULL);
INSERT INTO `system_log` VALUES (47, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{}', NULL, 8, 'PostmanRuntime/7.19.0', NULL, '2019-10-26 23:51:54', NULL);
INSERT INTO `system_log` VALUES (48, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{}', NULL, 2023, 'PostmanRuntime/7.19.0', NULL, '2019-10-26 23:53:30', NULL);
INSERT INTO `system_log` VALUES (49, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{}', NULL, 195, 'PostmanRuntime/7.19.0', NULL, '2019-10-26 23:57:55', NULL);
INSERT INTO `system_log` VALUES (50, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{}', NULL, 620, 'PostmanRuntime/7.19.0', NULL, '2019-10-27 00:00:31', NULL);
INSERT INTO `system_log` VALUES (51, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{}', NULL, 154, 'PostmanRuntime/7.19.0', NULL, '2019-10-27 00:08:47', NULL);
INSERT INTO `system_log` VALUES (52, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{}', NULL, 95, 'PostmanRuntime/7.19.0', NULL, '2019-10-27 00:10:00', NULL);
INSERT INTO `system_log` VALUES (53, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{}', NULL, 110, 'PostmanRuntime/7.19.0', NULL, '2019-10-27 00:13:50', NULL);
INSERT INTO `system_log` VALUES (54, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{\"password\":\"bfgjhndgjk\",\"account\":\"hgdghj\"}', NULL, 179, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-27 20:04:15', NULL);
INSERT INTO `system_log` VALUES (55, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{\"password\":\"bfgjhndgjk\",\"account\":\"hgdghj\"}', NULL, 107, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-27 20:06:56', NULL);
INSERT INTO `system_log` VALUES (56, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"account\":\"2016123\"}', NULL, 8, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-27 20:26:35', NULL);
INSERT INTO `system_log` VALUES (57, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"account\":\"2016123\"}', NULL, 7, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-27 20:26:42', NULL);
INSERT INTO `system_log` VALUES (58, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{\"password\":\"1195f503113b7b237f0a30d1e5ad8ad7\",\"account\":\"201623\"}', NULL, 14, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-27 21:30:37', NULL);
INSERT INTO `system_log` VALUES (59, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"account\":\"201623\"}', NULL, 132, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-27 21:32:46', NULL);
INSERT INTO `system_log` VALUES (60, '登录', '0:0:0:0:0:0:0:1', '/user/login', 'POST', '{\"password\":\"0192023a7bbd73250516f069df18b500\",\"account\":\"20100001\"}', NULL, 8, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36', NULL, '2019-10-27 21:42:21', NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户真实姓名',
  `account` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账户',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '加密盐值',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `email` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `sex` enum('男','女') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '性别',
  `is_usable` tinyint(1) NULL DEFAULT NULL COMMENT '是否启用',
  `class_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '班级ID',
  `room_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '宿舍ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `class_id`(`class_id`) USING BTREE,
  INDEX `room_id`(`room_id`) USING BTREE,
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`class_id`) REFERENCES `class` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `user_ibfk_2` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '20100001', 'c730e17e14f52d9173822def188e2b90', '123456', '13433629366', NULL, '男', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `userId`(`user_id`) USING BTREE,
  INDEX `roleId`(`role_id`) USING BTREE,
  CONSTRAINT `roleId` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `userId` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户--角色关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for vilolation_record
-- ----------------------------
DROP TABLE IF EXISTS `vilolation_record`;
CREATE TABLE `vilolation_record`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `student_id` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '学生ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '违规时间',
  `violation` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '违规事项',
  `punishment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处罚',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `student_id`(`student_id`) USING BTREE,
  CONSTRAINT `vilolation_record_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `user` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '违规记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for visited_record
-- ----------------------------
DROP TABLE IF EXISTS `visited_record`;
CREATE TABLE `visited_record`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `visit_time` datetime(0) NULL DEFAULT NULL COMMENT '访客来访时间',
  `true_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访客真实姓名',
  `identity` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访客身份证',
  `leave_time` datetime(0) NULL DEFAULT NULL COMMENT '访客离开时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注特别情况',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '来访记录表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
