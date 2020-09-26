/*
 Navicat Premium Data Transfer

 Source Server         : database
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : localhost:3306
 Source Schema         : picture

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 24/08/2020 00:32:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for album
-- ----------------------------
DROP TABLE IF EXISTS `album`;
CREATE TABLE `album`  (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '相册id',
  `name` varchar(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '相册名',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `type` varchar(16) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '相册类型',
  `user_id` bigint(19) NULL DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `album_fk`(`user_id`) USING BTREE,
  CONSTRAINT `album_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of album
-- ----------------------------
INSERT INTO `album` VALUES (1, '默认相册', '2020-08-13 15:20:48', 'default', 1);
INSERT INTO `album` VALUES (2, '普通相册', '2020-08-14 20:35:40', 'default', 1);

-- ----------------------------
-- Table structure for picture
-- ----------------------------
DROP TABLE IF EXISTS `picture`;
CREATE TABLE `picture`  (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '图片id',
  `path` varchar(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '图片地址',
  `upload_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '上传时间',
  `describe` varchar(32) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '图片描述',
  `path_mini` varchar(255) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '缩略图地址',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '图片状态',
  `album_id` bigint(19) NULL DEFAULT NULL COMMENT '相册id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `picture_fk`(`album_id`) USING BTREE,
  CONSTRAINT `picture_fk` FOREIGN KEY (`album_id`) REFERENCES `album` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of picture
-- ----------------------------
INSERT INTO `picture` VALUES (2, 'http://p.return76.top/1/20200816210956_1.jpg', '2020-08-16 21:36:52', '新描述', 'http://p.return76.top/1/20200816210956_1.jpg?x-oss-process=style/mini', 0, 1);
INSERT INTO `picture` VALUES (3, 'http://p.return76.top/1/20200816210957_2.jpg', '2020-08-16 21:36:52', NULL, 'http://p.return76.top/1/20200816210957_2.jpg?x-oss-process=style/mini', 0, 1);
INSERT INTO `picture` VALUES (4, 'http://p.return76.top/1/20200816213606_battlefieild.png', '2020-08-16 21:36:09', NULL, 'http://p.return76.top/1/20200816213606_battlefieild.png?x-oss-process=style/mini', 0, 1);
INSERT INTO `picture` VALUES (5, 'http://p.return76.top/1/20200816213609_刺客信条.jpg', '2020-08-16 21:36:10', NULL, 'http://p.return76.top/1/20200816213609_刺客信条.jpg?x-oss-process=style/mini', 0, 1);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `email` varchar(255) CHARACTER SET utf8mb4 NOT NULL COMMENT '用户邮箱',
  `password` varchar(64) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '用户密码',
  `nickname` varchar(64) CHARACTER SET utf8mb4  NULL DEFAULT NULL COMMENT '用户昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4  NULL DEFAULT NULL COMMENT '用户头像地址',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `e_index`(`email`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '543851436@qq.com', 'ph123', '匿名用户', 'http://123.jpg');

SET FOREIGN_KEY_CHECKS = 1;
