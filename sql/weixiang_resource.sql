/*
 Navicat Premium Data Transfer

 Source Server Type    : MySQL
 Source Server Version : 80043
 Source Schema         : Weixiang_Resource

 Target Server Type    : MySQL
 Target Server Version : 80043
 File Encoding         : 65001

 Date: 23/08/2025 14:59:41
*/
create database if not exists Weixiang_Resource;
use Weixiang_Resource;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                            `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
                            `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
                            `user_id` bigint NOT NULL COMMENT '用户id',
                            `publish_status` tinyint NOT NULL DEFAULT 0 COMMENT '状态，0-草稿，1-已发布',
                            `audit_status` tinyint NOT NULL DEFAULT 0 COMMENT '审核状态，0-待审核，1-审核通过，2-审核不通过',
                            `audit_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核原因',
                            `view_count` int NOT NULL DEFAULT 0 COMMENT '浏览量',
                            `like_count` int NOT NULL DEFAULT 0 COMMENT '点赞量',
                            `category_id` bigint NOT NULL COMMENT '分类id',
                            `delete_flag` tinyint NULL DEFAULT 0 COMMENT '删除标识,0-正常，1-已删除',
                            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`id`) USING BTREE,
                            INDEX `idx_user_id`(`user_id`) USING BTREE,
                            INDEX `idx_article_update_time`(`update_time`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article_comment
-- ----------------------------
DROP TABLE IF EXISTS `article_comment`;
CREATE TABLE `article_comment`  (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                    `article_id` bigint NOT NULL COMMENT '文章id',
                                    `user_id` bigint NOT NULL COMMENT '用户id',
                                    `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
                                    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    INDEX `idx_article_id`(`article_id`) USING BTREE,
                                    INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for article_tag
-- ----------------------------
DROP TABLE IF EXISTS `article_tag`;
CREATE TABLE `article_tag`  (
                                `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                `article_id` bigint NOT NULL COMMENT '文章id',
                                `tag_id` bigint NOT NULL COMMENT '标签id',
                                `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                PRIMARY KEY (`id`) USING BTREE,
                                INDEX `idx_tag_id`(`tag_id`) USING BTREE,
                                INDEX `idx_article_id`(`article_id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
                             `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                             `parent_id` bigint NULL DEFAULT 0 COMMENT '父分类Id',
                             `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
                             `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类描述',
                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for member_plan
-- ----------------------------
DROP TABLE IF EXISTS `member_plan`;
CREATE TABLE `member_plan`  (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `NAME` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会员名称',
                                `price` decimal(10, 2) NOT NULL COMMENT '价格',
                                `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '描述',
                                `validity_days` int NOT NULL COMMENT '有效期(天)',
                                `status` tinyint NOT NULL DEFAULT 0 COMMENT '0-禁用，1-启用',
                                `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员套餐表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`  (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `order_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单编号',
                               `user_id` bigint NOT NULL COMMENT '用户id',
                               `plan_id` bigint NOT NULL COMMENT '套餐id',
                               `order_date` date NOT NULL COMMENT '下单日期',
                               `amount` decimal(10, 2) NOT NULL COMMENT '订单金额',
                               `status` tinyint NOT NULL DEFAULT 0 COMMENT '订单状态,0未支付，1-已支付',
                               `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
                               `plan_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '套餐名',
                               PRIMARY KEY (`id`) USING BTREE,
                               UNIQUE INDEX `order_number`(`order_number`) USING BTREE,
                               INDEX `idx_user_id`(`user_id`) USING BTREE,
                               INDEX `idx_plan_id`(`plan_id`) USING BTREE,
                               INDEX `idx_order_date`(`order_date`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`  (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
                                  `user_id` bigint NOT NULL COMMENT '用户id',
                                  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'IP地址',
                                  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录地点',
                                  `device` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录设备',
                                  `login_time` datetime NOT NULL COMMENT '登录时间',
                                  `status` tinyint NOT NULL DEFAULT 0 COMMENT '1-登录成功，0-登录失败',
                                  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `idx_user_id`(`user_id`) USING BTREE,
                                  INDEX `idx_ip_address`(`ip_address`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '登录日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`  (
                                      `id` bigint NOT NULL AUTO_INCREMENT,
                                      `user_id` bigint NOT NULL COMMENT '用户id',
                                      `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
                                      `operation_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作内容',
                                      `operation_module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作模块',
                                      `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'IP地址',
                                      `operation_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作地址',
                                      `operation_time` datetime NOT NULL COMMENT '操作时间',
                                      `status` tinyint NOT NULL DEFAULT 0 COMMENT '1-成功，0-失败',
                                      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      PRIMARY KEY (`id`) USING BTREE,
                                      INDEX `idx_ip_user`(`ip_address`, `username`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
                                   `id` bigint NOT NULL AUTO_INCREMENT,
                                   `permission_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限名称',
                                   `permission_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限编码',
                                   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
                             `role_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色编码',
                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
                                        `id` bigint NOT NULL AUTO_INCREMENT,
                                        `role_id` bigint NOT NULL COMMENT '角色id',
                                        `permission_id` bigint NOT NULL COMMENT '权限id',
                                        `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                        PRIMARY KEY (`id`) USING BTREE,
                                        INDEX `idx_role_id`(`role_id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '账号',
                             `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
                             `salt` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码盐',
                             `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
                             `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
                             `status` tinyint NOT NULL DEFAULT 0 COMMENT '0-禁用，1-启用',
                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
                             `gender` tinyint NOT NULL DEFAULT 0 COMMENT '性别,0-男,1-女',
                             `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '头像',
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE INDEX `account`(`account`) USING BTREE,
                             UNIQUE INDEX `uk_email`(`email`) USING BTREE COMMENT '邮箱唯一索引',
                             UNIQUE INDEX `uk_phone`(`phone`) USING BTREE COMMENT '手机号唯一索引'
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_info`;
CREATE TABLE `sys_user_info`  (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `user_id` bigint NOT NULL COMMENT '用户id',
                                  `birthday` date NULL DEFAULT NULL COMMENT '生日',
                                  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省',
                                  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市',
                                  `district` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区/县',
                                  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细地址',
                                  `bio` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '个人简介',
                                  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_plan
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_plan`;
CREATE TABLE `sys_user_plan`  (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `user_id` bigint NOT NULL COMMENT '用户id',
                                  `plan_id` bigint NOT NULL COMMENT '套餐id',
                                  `order_id` bigint NOT NULL COMMENT '订单id',
                                  `start_date` date NOT NULL COMMENT '起效日期',
                                  `end_date` date NOT NULL COMMENT '失效日期',
                                  `status` tinyint NOT NULL DEFAULT 0 COMMENT '0-失效，1-有效',
                                  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `idx_user_plan`(`user_id`, `plan_id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户套餐表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `user_id` bigint NOT NULL COMMENT '用户id',
                                  `role_id` bigint NOT NULL COMMENT '角色id',
                                  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`  (
                        `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                        `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名称',
                        `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_message
-- ----------------------------
DROP TABLE IF EXISTS `user_message`;
CREATE TABLE `user_message`  (
                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                 `user_id` bigint NOT NULL COMMENT '用户id',
                                 `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息内容',
                                 `read_status` tinyint NOT NULL DEFAULT 0 COMMENT '阅读状态，0-未读，1-已读',
                                 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 `delete_flag` tinyint NULL DEFAULT 0 COMMENT '删除标识,0-正常，1-已删除',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户消息表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
