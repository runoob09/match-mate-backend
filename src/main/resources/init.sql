# 创建数据库
create database if not exists match_mate default character set utf8mb4 collate utf8mb4_general_ci;
use match_mate;
drop table if exists `user`;
create table if not exists `user`
(
    id            bigint primary key comment "非空主键",
    username      varchar(50)  not null comment "用户昵称",
    user_account  varchar(50)  not null comment "用户账号",
    avatar_url    varchar(100) comment "用户头像",
    gender        tinyint comment "用户性别",
    user_password varchar(100) not null comment "用户密码",
    phone_number  varchar(20) comment "用户手机号",
    email         varchar(100) comment "用户邮箱",
    user_status   int comment "用户状态 0-正常 1-封禁",
    is_delete     tinyint comment "是否删除 0-未删除 1-已删除",
    user_role     int comment "用户角色 0-普通用户 1-管理员",
    user_tags     varchar(1024) comment "用户标签（json字符串）",
    user_profile  varchar(1024) comment "用户简介",
    create_time   timestamp default CURRENT_TIMESTAMP comment "创建时间",
    update_time   timestamp default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment "更新时间"
) comment "用户表";
drop table if exists `tags`;
create table if not exists `tags`
(
    id          bigint primary key comment "非空主键",
    tag_name    varchar(50) not null comment "标签名",
    user_id     bigint      not null comment "用户id",
    parent_id   int comment "父标签id",
    is_parent   tinyint comment "是否父标签",
    create_time timestamp default CURRENT_TIMESTAMP comment "创建时间",
    update_time timestamp default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment "更新时间",
    is_delete   tinyint comment "是否删除 0-未删除 1-已删除"
) comment "标签表";
create unique index idx_tag_name on tags (tag_name); # 对标签名称添加唯一索引