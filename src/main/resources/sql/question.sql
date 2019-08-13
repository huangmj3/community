# 创建community数据库
# create database if not exists `community`;
# 使用数据库
use `community`;
# 创建用户表
create table `question` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT '问题id',
    `title` varchar(50),
    `description` text,
    `gmt_create` bigint(20),
    `gmt_modified` bigint(20),
    `creator` int(11),
    `comment_count` int(11),
    `view_count` int(11),
    `like_count` int(11),
    `tag` varchar(256),
    primary key (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='问题表';

-- 初始化问题表
# insert into `question` (`id`,`title`,`title`,`gmt_create`,`gmt_modified`)
# values
#     (1,1,1,1,1)