# 创建community数据库
# create database if not exists `community`;
# 使用数据库
use `community`;
# 创建用户表
create table `user` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `account_id` varchar(100),
    `name` varchar(50),
    `token` char(36),
    `gmt_create` bigint,
    `gmt_modified` bigint,
    primary key (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- 初始化用户表
insert into `user` (account_id,`name`,`token`,`gmt_create`,`gmt_modified`)
values
(1,1,1,1,1)