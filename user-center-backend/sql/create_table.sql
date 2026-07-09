CREATE DATABASE `yupi` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */

CREATE TABLE `user` (
                        `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id (主键)',
                        `userAccount` varchar(256) NOT NULL COMMENT '登录账号',
                        `userPassword` varchar(512) NOT NULL COMMENT '密码',
                        `username` varchar(256) DEFAULT NULL COMMENT '昵称',
                        `avatarUrl` varchar(1024) DEFAULT NULL COMMENT '头像',
                        `gender` tinyint DEFAULT NULL COMMENT '性别',
                        `phone` varchar(128) DEFAULT NULL COMMENT '电话',
                        `email` varchar(512) DEFAULT NULL COMMENT '邮箱',
                        `userRole` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '用户角色 0用户 1管理员',
                        `userStatus` int NOT NULL DEFAULT '0' COMMENT '用户状态 0 - 正常',
                        `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除 0 1 (逻辑删除)',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';
