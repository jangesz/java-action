drop table if exists t_ueditor;
create table t_ueditor(
  `id` bigint not null auto_increment comment '主键',
  `content` text character set utf8,
  `created_at` bigint(20),
  `updated_at` bigint(20),
  primary key (id)
) engine=innodb default charset=utf8;