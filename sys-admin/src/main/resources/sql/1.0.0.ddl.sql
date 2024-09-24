-- 1.部门信息
drop table if exists sys_dept;
create table sys_dept(
    dept_id       bigserial primary key,
    dept_code     character varying(64),
    dept_name     character varying(128),
    dept_short    character varying(64),
    dept_addr     character varying(512),
    dept_phone    character varying(64),
    remark        character varying(200),
    read_only     int2 default 0,
    create_user   int8,
    create_dept   int8,
    create_time   timestamp,
    update_user   int8,
    update_dept   int8,
    update_time  timestamp

);
create unique index sys_dept_dept_code on sys_dept(dept_code);
comment on table sys_dept is '部门信息';
comment on column sys_dept.dept_id is '部门id';
comment on column sys_dept.dept_code is '部门编码';
comment on column sys_dept.dept_name is '部门名称';
comment on column sys_dept.dept_short is '部门简称';
comment on column sys_dept.dept_addr is '部门地址';
comment on column sys_dept.dept_phone is '部门电话';
comment on column sys_dept.remark is '备注';
comment on column sys_dept.read_only is '是否只读 1是 0否';
comment on column sys_dept.create_user is '创建人';
comment on column sys_dept.create_dept is '创建部门';
comment on column sys_dept.create_time is '创建时间';
comment on column sys_dept.update_user is '更新人';
comment on column sys_dept.update_dept is '更新部门';
comment on column sys_dept.update_time is '更新时间';

-- 2.部门关系
drop table if exists sys_dept_tree;
create table sys_dept_tree(
    dept_id   int8 not null,
    parent_id int8 not null,
    tree_type int2 default 1,
    constraint sys_dept_tree_pkey primary key (dept_id, parent_id, tree_type)
);
comment on table sys_dept_tree is '部门关系';
comment on column sys_dept_tree.dept_id is '部门id';
comment on column sys_dept_tree.parent_id is '上级部门id';
comment on column sys_dept_tree.tree_type is '关系类型';

-- 3.岗位信息
drop table if exists sys_post;
create table sys_post (
    post_id bigserial primary key,
    post_code varchar(64),
    post_name varchar(64) not null,
    post_level  int2      default 1,
	post_type   varchar(64),
    post_status int2      default 1,
    remark      character varying(200),
    read_only   int2      default 0,
    create_user  int8,
    create_dept  int8,
    create_time  timestamp,
    update_user  int8,
    update_dept  int8,
    update_time  timestamp
);
comment on table sys_post is '岗位信息';
comment on column sys_post.post_id is '岗位id';
comment on column sys_post.post_code is '岗位编码';
comment on column sys_post.post_name is '岗位名称';
comment on column sys_post.post_level is '岗位级别';
comment on column sys_post.post_type is '岗位类型';
comment on column sys_post.post_status is '岗位状态';
comment on column sys_post.remark is '备注';
comment on column sys_post.read_only is '是否只读 1是 0否';
comment on column sys_post.create_user is '创建人';
comment on column sys_post.create_dept is '创建部门';
comment on column sys_post.create_time is '创建时间';
comment on column sys_post.update_user is '更新人';
comment on column sys_post.update_dept is '更新部门';
comment on column sys_post.update_time is '更新时间';

-- 4.岗位关系
drop table if exists sys_post_tree;
create table sys_post_tree(
    post_id   int8 not null,
    parent_id int8 not null,
    tree_type int2 default 1,
    constraint sys_post_tree_pkey primary key (post_id, parent_id, tree_type)
);
comment on table sys_post_tree is '岗位关系';
comment on column sys_post_tree.post_id is '岗位id';
comment on column sys_post_tree.parent_id is '上级岗位id';
comment on column sys_post_tree.tree_type is '关系类型';

-- 5.部门岗位
drop table if exists sys_dept_post;
create table sys_dept_post(
    dept_id   int8 not null,
    post_id   int8 not null,
    is_default int2 default 0,
    constraint sys_dept_post_pkey primary key (dept_id, post_id)
);
comment on table sys_dept_post is '部门岗位';
comment on column sys_dept_post.dept_id is '部门id';
comment on column sys_dept_post.post_id is '岗位id';
comment on column sys_dept_post.is_default is '是否部门默认岗位';

-- 6.用户信息
drop table if exists sys_user;
create table sys_user(
    user_id      bigserial primary key,
    user_type    int2      default 0,
    user_code    character varying(64),
    user_name    character varying(64) not null,
    user_account character varying(64) not null,
    user_passwd  character varying(256) not null,
    user_sex     int2      default 0,
    user_phone   character varying(11),
    user_email   character varying(128),
    user_status  int2      default 1,
    rank         character varying(64),
    remark       character varying(200),
    read_only    int2      default 0,
    create_user  int8,
    create_dept  int8,
    create_time  timestamp,
    update_user  int8,
    update_dept  int8,
    update_time  timestamp
);
create unique index sys_user_user_code on sys_user(user_code);
create unique index sys_user_user_account on sys_user(user_account);
comment on table sys_user is '用户信息';
comment on column sys_user.user_id is '用户id';
comment on column sys_user.user_type is '用户类型 0:默认用户';
comment on column sys_user.user_code is '用户编码';
comment on column sys_user.user_name is '用户名称';
comment on column sys_user.user_account is '用户账号';
comment on column sys_user.user_passwd is '用户密码';
comment on column sys_user.user_sex is '用户性别';
comment on column sys_user.user_phone is '用户电话';
comment on column sys_user.user_email is '用户邮箱';
comment on column sys_user.user_status is '用户状态';
comment on column sys_user.rank is '职级';
comment on column sys_user.remark is '备注';
comment on column sys_user.read_only is '是否只读 1是 0否';
comment on column sys_user.create_user is '创建人';
comment on column sys_user.create_dept is '创建部门';
comment on column sys_user.create_time is '创建时间';
comment on column sys_user.update_user is '更新人';
comment on column sys_user.update_dept is '更新部门';
comment on column sys_user.update_time is '更新时间';

-- 7.用户关系
drop table if exists sys_user_tree;
create table sys_user_tree(
    user_id   int8 not null,
    parent_id int8 not null,
    tree_type int2 default 1,
    constraint sys_user_tree_pkey primary key (user_id, parent_id, tree_type)
);
comment on table sys_user_tree is '用户关系';
comment on column sys_user_tree.user_id is '用户id';
comment on column sys_user_tree.parent_id is '上级用户id';
comment on column sys_user_tree.tree_type is '关系类型';

-- 8.用户部门
drop table if exists sys_user_dept;
create table sys_user_dept(
    id         bigserial primary key,
    user_id    int8 not null,
    dept_id    int8 not null,
    post_id    int8 default -1,
    is_default int2 default 0,
    is_leader  int2 default 0
);
create unique index sys_user_dept_uk on sys_user_dept(user_id, dept_id, post_id);
comment on table sys_user_dept is '用户部门';
comment on column sys_user_dept.id is '主键';
comment on column sys_user_dept.user_id is '用户id';
comment on column sys_user_dept.dept_id is '部门id';
comment on column sys_user_dept.post_id is '岗位id';
comment on column sys_user_dept.is_default is '是否用户默认部门';
comment on column sys_user_dept.is_leader is '是否部门负责人';

-- 9.角色信息
drop table if exists sys_role;
create table sys_role(
    role_id bigserial primary key,
    role_code   character varying(100) not null,
    role_name   character varying(64) not null,
    role_type   character varying(64),
    remark      character varying(200),
    read_only   int2      default 0,
    create_user  int8,
    create_dept  int8,
    create_time  timestamp,
    update_user  int8,
    update_dept  int8,
    update_time  timestamp
);
create unique index sys_role_role_code on sys_role(role_code);
comment on table sys_role is '角色信息';
comment on column sys_role.role_id is '角色id';
comment on column sys_role.role_code is '角色编码';
comment on column sys_role.role_name is '角色名称';
comment on column sys_role.role_type is '角色类型';
comment on column sys_role.remark is '备注';
comment on column sys_role.read_only is '是否只读 1是 0否';
comment on column sys_role.create_user is '创建人';
comment on column sys_role.create_dept is '创建部门';
comment on column sys_role.create_time is '创建时间';
comment on column sys_role.update_user is '更新人';
comment on column sys_role.update_dept is '更新部门';
comment on column sys_role.update_time is '更新时间';

-- 10.用户角色
drop table if exists sys_user_role;
create table sys_user_role(
    user_id bigint not null,
    role_id bigint not null,
    constraint sys_user_role_pkey primary key (user_id, role_id)
);
comment on table sys_user_role is '用户角色';
comment on column sys_user_role.user_id is '用户id';
comment on column sys_user_role.role_id is '角色id';

-- 11.菜单信息
drop table if exists sys_menu;
create table sys_menu(
    menu_id bigserial primary key,
	parent_id bigint default 0,
    menu_name character varying(64) not null,
    menu_order integer default 0,
    menu_permit character varying(255),
    menu_path character varying(255) default '#',
	menu_param character varying(255),
	menu_type char(1) not null,
    menu_icon character varying(100) default '#',
	component character varying(255),
    menu_status   int2 default 1,
	is_frame      int2 DEFAULT 1,
    is_cache      int2 DEFAULT 1,
    is_visible    int2 DEFAULT 1,
    is_protected  int2 DEFAULT 1,
    remark        character varying(200),
    read_only     int2 default 0,
    create_user   int8,
    create_dept   int8,
    create_time   timestamp,
    update_user   int8,
    update_dept   int8,
    update_time   timestamp
);
comment on table sys_menu is '菜单信息';
comment on column sys_menu.menu_id is '菜单id';
comment on column sys_menu.parent_id is '父菜单id';
comment on column sys_menu.menu_name is '菜单名称';
comment on column sys_menu.menu_order is '菜单顺序';
comment on column sys_menu.menu_permit is '权限标识';
comment on column sys_menu.menu_path is '菜单路径';
comment on column sys_menu.menu_param is '路径参数';
comment on column sys_menu.menu_type is '菜单类型：M:目录 C:菜单 B:按钮';
comment on column sys_menu.menu_icon is '菜单图标';
comment on column sys_menu.component is '组件路径';
comment on column sys_menu.menu_status is '菜单状态 1启用 2停用';
comment on column sys_menu.is_frame is '是否内部链接 1是 0否';
comment on column sys_menu.is_cache is '是否缓存 1是 0否';
comment on column sys_menu.is_visible is '是否显示 1是 0否';
comment on column sys_menu.is_protected is '是否受保护的菜单 1是 0否';
comment on column sys_menu.read_only is '是否只读 1是 0否';
comment on column sys_menu.remark is '备注';
comment on column sys_menu.create_user is '创建人';
comment on column sys_menu.create_dept is '创建部门';
comment on column sys_menu.create_time is '创建时间';
comment on column sys_menu.update_user is '更新人';
comment on column sys_menu.update_dept is '更新部门';
comment on column sys_menu.update_time is '更新时间';

-- 12.角色菜单
drop table if exists sys_role_menu;
create table sys_role_menu(
    role_id bigint not null,
    menu_id bigint not null,
    constraint sys_role_menu_pkey primary key (role_id, menu_id)
);
comment on table sys_role_menu is '角色菜单';
comment on column sys_role_menu.role_id is '角色id';
comment on column sys_role_menu.menu_id is '菜单id';

-- 13. 字典数据
drop table if exists sys_dict;
create table sys_dict(
    id bigserial primary key,
    parent_code character varying(100),
	dict_code character varying(100),
	dict_label character varying(100),
	dict_en character varying(100),
    dict_value character varying(100),
    value_parser varchar(100),
    value_param varchar(100),
    dict_order int2 default 0,
	is_default int2 default 0,
	css character varying(100),
    status int2 default 1,
    remark      character varying(200),
    read_only int2 default 0,
    create_user  int8,
    create_dept  int8,
    create_time  timestamp,
    update_user  int8,
    update_dept  int8,
    update_time  timestamp
);
create unique index sys_dict_uk on sys_dict(dict_code);
comment on table sys_dict is '字典数据';
comment on column sys_dict.id is '主键';
comment on column sys_dict.parent_code is '父字典编码';
comment on column sys_dict.dict_code is '字典编码';
comment on column sys_dict.dict_label is '字典标签';
comment on column sys_dict.dict_en is '英文标签';
comment on column sys_dict.dict_value is '字典值';
comment on column sys_dict.value_parser is '值转换器';
comment on column sys_dict.value_param is '转换参数';
comment on column sys_dict.dict_order is '字典排序';
comment on column sys_dict.status is '字典状态';
comment on column sys_dict.is_default is '是否默认';
comment on column sys_dict.read_only is '是否只读 1是 0否';
comment on column sys_dict.css is '字典展示样式';
comment on column sys_dict.remark is '备注';
comment on column sys_dict.create_user is '创建人';
comment on column sys_dict.create_dept is '创建部门';
comment on column sys_dict.create_time is '创建时间';
comment on column sys_dict.update_user is '更新人';
comment on column sys_dict.update_dept is '更新部门';
comment on column sys_dict.update_time is '更新时间';

-- 14.操作日志
drop table if exists sys_log;
create table sys_log(
    id bigserial primary key,
    user_id int8,
    dept_id int8,
    log_group character varying(64),
    log_type character varying(64),
    log_action character varying(64),
    ip  character varying(128),
    url character varying(255),
    log_desc character varying(255) default '' ,
	log_content json,
    log_status int2 default 1,
    log_time timestamp

);
comment on table sys_log is '操作日志';
comment on column sys_log.id is '日志id';
comment on column sys_log.user_id is '用户id';
comment on column sys_log.dept_id is '部门id';
comment on column sys_log.log_group is '日志模块';
comment on column sys_log.log_type is '日志类型';
comment on column sys_log.log_action is '日志动作';
comment on column sys_log.ip is '访问ip';
comment on column sys_log.url is '访问url';
comment on column sys_log.log_desc is '日志描述';
comment on column sys_log.log_content is '日志详情';
comment on column sys_log.log_status is '日志状态';
comment on column sys_log.log_time is '日志时间';

-- 15.系统公告
drop table if exists sys_notice;
create table sys_notice(
    notice_id bigserial primary key,
    notice_title   character varying(255),
    notice_status  int2 default 0,
    notice_type    int2 default 0,
    notice_level   int2 default 0,
    content        text,
    is_system      int2 default 0,
    stat_total     int4 default 0,
    stat_read      int4 default 0,
    goals_all      int2 default 0,
    goals_dept     int8[],
    goals_role     int8[],
    goals_user     int8[],
    publish_time   timestamp,
    create_user  int8,
    create_dept  int8,
    create_time  timestamp,
    update_user  int8,
    update_dept  int8,
    update_time  timestamp
);
comment on table sys_notice is '系统公告';
comment on column sys_notice.notice_id is '公告id';
comment on column sys_notice.notice_title is '标题';
comment on column sys_notice.notice_status is '公告状态 0草稿 1已发布 2已撤回 3已删除';
comment on column sys_notice.notice_type is '公告类型 0公告 1通知';
comment on column sys_notice.notice_level is '公告等级 0普通 1紧急';
comment on column sys_notice.content is '公告内容';
comment on column sys_notice.is_system is '是否系统公告 0否 1是';
comment on column sys_notice.stat_total is '目标数';
comment on column sys_notice.stat_read is '已读数';
comment on column sys_notice.goals_all is '是否全员 0否 1是';
comment on column sys_notice.goals_dept is '目标单位';
comment on column sys_notice.goals_role is '目标角色';
comment on column sys_notice.goals_user is '目标用户';

-- 16.公告已读
drop table if exists sys_notice_read;
create table sys_notice_read(
    id  bigserial primary key,
    notice_id     int8,
    user_id       int8,
    read_status   int2 default 0,
    read_back   character varying(512),
    read_time   timestamp
);
create unique index sys_notice_read_uk on sys_notice_read(user_id, notice_id);
comment on table sys_notice_read is '公告已读';
comment on column sys_notice_read.id is '主键';
comment on column sys_notice_read.notice_id is '公告id';
comment on column sys_notice_read.user_id is '用户id';
comment on column sys_notice_read.read_status is '已读状态';
comment on column sys_notice_read.read_back is '读反馈';
comment on column sys_notice_read.read_time is '读时间';

-- 17.附件信息
drop table if exists sys_attach;
create table sys_attach(
    attach_id     bigserial primary key,
    master_id     int8,
    attach_group  character varying(64),
    attach_type   character varying(64),
    attach_name   character varying(1024),
    attach_size   int8,
    attach_path   character varying(1024),
    attach_status int2 default 0,
    is_public     int2 default 0,
    create_time   timestamp,
    expire_set    int2 default 0,
    expire_time   timestamp
);
create index sys_attach_master on sys_attach(master_id, attach_type, attach_group);
comment on table sys_attach is '附件信息';
comment on column sys_attach.attach_id is '附件id';
comment on column sys_attach.master_id is '宿主id';
comment on column sys_attach.attach_group is '附件分组';
comment on column sys_attach.attach_type is '附件类型';
comment on column sys_attach.attach_name is '附件名称';
comment on column sys_attach.attach_size is '附件大小';
comment on column sys_attach.attach_path is '附件路径';
comment on column sys_attach.attach_status is '附件状态 0未生效 1已生效';
comment on column sys_attach.is_public is '是否公开的 0否 1是';
comment on column sys_attach.create_time is '创建时间';
comment on column sys_attach.expire_set is '是否设置过期时间 0未设置 1设置';
comment on column sys_attach.expire_time is '过期时间';

-- 18.系统配置
drop table if exists sys_config;
CREATE TABLE sys_config(
    config_id    serial primary key,
    config_name  varchar(100) DEFAULT '',
    config_key   varchar(100) DEFAULT '',
    config_value varchar(500) DEFAULT '',
    value_parser varchar(100),
    value_param  varchar(100),
    is_default   int2         DEFAULT 0,
    remark       varchar(500) DEFAULT NULL,
    create_user  int8,
    create_dept  int8,
    create_time  timestamp,
    update_user  int8,
    update_dept  int8,
    update_time  timestamp
);
create unique index sys_config_config_key on sys_config(config_key);
comment on table sys_config is '系统配置';
comment on column sys_config.config_id is '参数id';
comment on column sys_config.config_name is '参数名称';
comment on column sys_config.config_key is '参数键';
comment on column sys_config.config_value is '参数值';
comment on column sys_config.value_parser is '值转换器';
comment on column sys_config.value_param is '转换参数';
comment on column sys_config.is_default is '是否默认 1是 0否';
comment on column sys_config.remark is '备注';
comment on column sys_config.create_user is '创建人';
comment on column sys_config.create_dept is '创建部门';
comment on column sys_config.create_time is '创建时间';
comment on column sys_config.update_user is '更新人';
comment on column sys_config.update_dept is '更新部门';
comment on column sys_config.update_time is '更新时间';

-- 19.数据权限
drop table if exists sys_scope;
CREATE TABLE sys_scope(
    scope_id     bigserial primary key,
    scope_name   character varying(255),
    scope_type   character varying(64),
    scope_status int2 DEFAULT 1,
    content      json default '{}',
    remark       varchar(200)
);
comment on table sys_scope is '数据权限';
comment on column sys_scope.scope_id is '权限id';
comment on column sys_scope.scope_name is '权限名称';
comment on column sys_scope.scope_type is '权限类型（字典值设置表单）';
comment on column sys_scope.scope_status is '权限状态';
comment on column sys_scope.content is '权限规则';
comment on column sys_scope.remark is '备注';

-- 20.角色菜单数据权限
drop table if exists sys_role_scope;
CREATE TABLE sys_role_scope(
    scope_id    bigint not null,
    role_id     bigint not null,
    menu_permit character varying(255),
    priority    int2 DEFAULT 1,
    constraint sys_role_scope_pkey primary key (role_id, scope_id, menu_permit)
);
comment on table sys_role_scope is '角色数据权限';
comment on column sys_role_scope.scope_id is '数据权限id';
comment on column sys_role_scope.role_id is '角色id';
comment on column sys_role_scope.menu_permit is '菜单权限符';
comment on column sys_role_scope.priority is '优先级';

-- 21.系统告警
drop table if exists sys_alarm;
create table sys_alarm(
    id            bigserial primary key,
    alarm_code    character varying(128) not null,
    alarm_type    int8,
    alarm_level   int2 default 1,
    source_id     int8,
    source_name   character varying(64),
    source_type   character varying(64),
    alarm_status  int2 default 0,
    alarm_times   int4 default 1,
    first_time    timestamp,
    last_time     timestamp,
    alarm_desc    character varying(255),
    alarm_content json default '{}',
    resolve_user  int8,
    resolve_msg   character varying(255),
    resolve_time  timestamp,
    resolve_type  int2 default 1

);
create index sys_alarm_alarm_code on sys_alarm(alarm_code);
comment on table sys_alarm is '系统告警';
comment on column sys_alarm.id is '告警id';
comment on column sys_alarm.alarm_code is '唯一编码';
comment on column sys_alarm.alarm_type is '告警类型';
comment on column sys_alarm.source_id is '告警来源id';
comment on column sys_alarm.source_name is '告警来源名称';
comment on column sys_alarm.source_type is '告警来源类型';
comment on column sys_alarm.alarm_level is '告警级别：1提示 2普通 3重要 4严重 5灾难';
comment on column sys_alarm.alarm_status is '告警状态 0未处理 1已确认 2已解决';
comment on column sys_alarm.alarm_times is '告警次数';
comment on column sys_alarm.first_time is '首次告警时间';
comment on column sys_alarm.last_time is '最后告警时间';
comment on column sys_alarm.alarm_desc is '告警描述';
comment on column sys_alarm.alarm_content is '告警内容';
comment on column sys_alarm.resolve_user is '处理人';
comment on column sys_alarm.resolve_time is '处理时间';
comment on column sys_alarm.resolve_msg is '处理意见';
comment on column sys_alarm.resolve_type is '处理方式：1:手动 2:自动';

-- 22.告警类型
drop table if exists sys_alarm_type;
create table sys_alarm_type(
    id          bigserial primary key,
    type_name   character varying(128),
    type_view   character varying(128),
    description text
);
comment on table sys_alarm_type is '告警类型';
comment on column sys_alarm_type.id is '主键';
comment on column sys_alarm_type.type_name is '类型名称';
comment on column sys_alarm_type.type_view is '类型表单';
comment on column sys_alarm_type.description is '类型描述';

-- 23.告警级别
drop table if exists sys_alarm_level;
create table sys_alarm_level(
    id          bigserial primary key,
    alarm_type  int8,
    alarm_level int2,
    level_rule  json default '{}',
    description text
);
comment on table sys_alarm_level is '告警类型';
comment on column sys_alarm_level.id is '主键';
comment on column sys_alarm_level.alarm_type is '告警类型';
comment on column sys_alarm_level.alarm_level is '告警级别';
comment on column sys_alarm_level.level_rule is '级别判断规则';
comment on column sys_alarm_level.description is '级别描述';
