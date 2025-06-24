-- 系统管理员
drop table if exists sys_user_admin;
create table sys_user_admin
(
    user_id      serial primary key,
    user_type    int2 default -1,
    user_code    character varying(64),
    user_name    character varying(64)  not null,
    user_account character varying(64)  not null,
    user_passwd  character varying(256) not null
);

-- 1.部门信息
drop table if exists sys_dept;
create table sys_dept(
    dept_id     serial primary key,
    dept_code   character varying(64),
    dept_name   character varying(128),
    dept_short  character varying(64),
    dept_addr   character varying(512),
    dept_phone  character varying(64),
    remark      character varying(200),
    create_by   character varying(64),
    create_time timestamp,
    update_by   character varying(64),
    update_time timestamp

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
comment on column sys_dept.create_by is '创建人';
comment on column sys_dept.create_time is '创建时间';
comment on column sys_dept.update_by is '更新人';
comment on column sys_dept.update_time is '更新时间';

-- 2.部门关系
drop table if exists sys_dept_tree;
create table sys_dept_tree(
    dept_id   int4 not null,
    parent_id int4 not null,
    tree_type int2 default 1,
    constraint sys_dept_tree_pkey primary key (dept_id, parent_id, tree_type)
);
comment on table sys_dept_tree is '部门关系';
comment on column sys_dept_tree.dept_id is '部门id';
comment on column sys_dept_tree.parent_id is '上级部门id';
comment on column sys_dept_tree.tree_type is '关系类型';

-- 3.岗位信息
drop table if exists sys_post;
create table sys_post(
    post_id     serial primary key,
    post_code   varchar(64),
    post_name   varchar(64) not null,
    post_level  int2 default 1,
    post_type   varchar(64),
    post_status int2 default 1,
    remark      character varying(200),
    create_by   varchar(64),
    create_time timestamp,
    update_by   varchar(64),
    update_time timestamp
);
comment on table sys_post is '岗位信息';
comment on column sys_post.post_id is '岗位id';
comment on column sys_post.post_code is '岗位编码';
comment on column sys_post.post_name is '岗位名称';
comment on column sys_post.post_level is '岗位级别';
comment on column sys_post.post_type is '岗位类型';
comment on column sys_post.post_status is '岗位状态';
comment on column sys_post.remark is '备注';
comment on column sys_post.create_by is '创建人';
comment on column sys_post.create_time is '创建时间';
comment on column sys_post.update_by is '更新人';
comment on column sys_post.update_time is '更新时间';

-- 4.岗位关系
drop table if exists sys_post_tree;
create table sys_post_tree(
    post_id   int4 not null,
    parent_id int4 not null,
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
    dept_id   int4 not null,
    post_id   int4 not null,
    is_default int2 default 0,
    constraint sys_dept_post_pkey primary key (dept_id, post_id)
);
comment on table sys_dept_post is '部门岗位';
comment on column sys_dept_post.dept_id is '部门id';
comment on column sys_dept_post.post_id is '岗位id';
comment on column sys_dept_post.is_default is '是否部门默认岗位';

-- 6.用户信息
drop table if exists sys_user;
create table sys_user
(
    user_id      serial primary key,
    user_code    character varying(64),
    user_type    int2 default 0,
    user_name    character varying(64)  not null,
    user_account character varying(64)  not null,
    user_passwd  character varying(256) not null,
    user_sex     int2 default 0,
    user_phone   character varying(11),
    user_email   character varying(128),
    user_rank    character varying(64),
    user_status  int2 default 1,
    remark       character varying(200),
    create_by    character varying(64),
    create_time  timestamp,
    update_by    character varying(64),
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
comment on column sys_user.user_rank is '职级';
comment on column sys_user.remark is '备注';
comment on column sys_user.create_by is '创建人';
comment on column sys_user.create_time is '创建时间';
comment on column sys_user.update_by is '更新人';
comment on column sys_user.update_time is '更新时间';

-- 7.用户关系
drop table if exists sys_user_tree;
create table sys_user_tree(
    user_id   int4 not null,
    parent_id int4 not null,
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
    user_id    int4 not null,
    dept_id    int4 not null,
    post_id    int4 default -1,
    is_default int2 default 0,
    is_leader  int2 default 0,
    constraint sys_user_dept_pkey primary key (user_id, dept_id, post_id)
);
comment on table sys_user_dept is '用户部门';
comment on column sys_user_dept.user_id is '用户id';
comment on column sys_user_dept.dept_id is '部门id';
comment on column sys_user_dept.post_id is '岗位id';
comment on column sys_user_dept.is_default is '是否用户默认部门';
comment on column sys_user_dept.is_leader is '是否部门负责人';

-- 9.角色信息
drop table if exists sys_role;
create table sys_role(
    role_id     serial primary key,
    role_code   character varying(100) not null,
    role_name   character varying(64)  not null,
    role_type   character varying(64),
    remark      character varying(200),
    create_by   character varying(64),
    create_time timestamp,
    update_by   character varying(64),
    update_time timestamp
);
create unique index sys_role_role_code on sys_role(role_code);
comment on table sys_role is '角色信息';
comment on column sys_role.role_id is '角色id';
comment on column sys_role.role_code is '角色编码';
comment on column sys_role.role_name is '角色名称';
comment on column sys_role.role_type is '角色类型';
comment on column sys_role.remark is '备注';
comment on column sys_role.create_by is '创建人';
comment on column sys_role.create_time is '创建时间';
comment on column sys_role.update_by is '更新人';
comment on column sys_role.update_time is '更新时间';

-- 10.用户角色
drop table if exists sys_user_role;
create table sys_user_role(
    user_id int4 not null,
    role_id int4 not null,
    constraint sys_user_role_pkey primary key (user_id, role_id)
);
comment on table sys_user_role is '用户角色';
comment on column sys_user_role.user_id is '用户id';
comment on column sys_user_role.role_id is '角色id';

-- 11.菜单信息
drop table if exists sys_menu;
create table sys_menu(
    menu_id      serial primary key,
    parent_id    int4                   default 0,
    menu_name    character varying(64) not null,
    menu_order   integer                default 0,
    menu_permit  character varying(255),
    menu_path    character varying(255) default '#',
    menu_param   character varying(255),
    menu_type    char(1)               not null,
    menu_icon    character varying(100) default '#',
    component    character varying(255),
    menu_status  int2                   default 1,
    is_frame     int2                   DEFAULT 1,
    is_cache     int2                   DEFAULT 1,
    is_visible   int2                   DEFAULT 1,
    is_protected int2                   DEFAULT 1,
    remark       character varying(255),
    create_by    character varying(64),
    create_time  timestamp,
    update_by    character varying(64),
    update_time  timestamp
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
comment on column sys_menu.remark is '备注';
comment on column sys_menu.create_by is '创建人';
comment on column sys_menu.create_time is '创建时间';
comment on column sys_menu.update_by is '更新人';
comment on column sys_menu.update_time is '更新时间';

-- 12.角色菜单
drop table if exists sys_role_menu;
create table sys_role_menu(
    role_id int4 not null,
    menu_id int4 not null,
    constraint sys_role_menu_pkey primary key (role_id, menu_id)
);
comment on table sys_role_menu is '角色菜单';
comment on column sys_role_menu.role_id is '角色id';
comment on column sys_role_menu.menu_id is '菜单id';

-- 13.用户ApiToken
drop table if exists api_token;
create table api_token(
    token_id    serial primary key,
    token_name  character varying(128),
    token_value character varying(1024),
    expire      timestamp,
    ip_rule     character varying(128),
    user_code   character varying(64),
    create_time timestamp
);
comment on table api_token is '用户ApiToken';
comment on column api_token.token_id is '令牌id';
comment on column api_token.token_name is '令牌名称';
comment on column api_token.token_value is '令牌token';
comment on column api_token.expire is '到期时间';
comment on column api_token.ip_rule is 'ip限制';
comment on column api_token.user_code is '用户编码';
comment on column api_token.create_time is '创建时间';

-- 13.用户ApiToken权限
drop table if exists api_token_menu;
create table api_token_menu(
    token_id int4,
    menu_id int4,
    constraint api_token_menu_pkey primary key (token_id, menu_id)
);
comment on table api_token_menu is '用户ApiToken权限';
comment on column api_token_menu.token_id is '令牌id';
comment on column api_token_menu.menu_id is '菜单id';
