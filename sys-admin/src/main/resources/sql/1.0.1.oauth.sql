-- oauth 配置
drop table if exists oauth_config;
create table oauth_config
(
    id              serial primary key,
    "status"        int2 default 0,
    "user_role"     int8,
    "app_type"      character varying(64),
    "app_id"        character varying(64),
    "app_secret"    character varying(64),
    "auth_url"      character varying(256),
    "redirect_url"  character varying(256),
    "grant_type"    character varying(64),
    "response_type" character varying(64),
    "auth_scope"    character varying(128),
    create_user     int8,
    create_dept     int8,
    create_time     timestamp,
    update_user     int8,
    update_dept     int8,
    update_time     timestamp
);
create unique index oauth_config_unique on oauth_config(app_type);
comment on table oauth_config is 'oauth配置';
comment on column oauth_config.id is 'id';
comment on column oauth_config.status is '状态 0 关闭 1开启';
comment on column oauth_config.user_role is '用户角色';
comment on column oauth_config.app_type is '授权服务类';
comment on column oauth_config.app_id is '应用id';
comment on column oauth_config.app_secret is '应用密钥';
comment on column oauth_config.auth_url is '授权服务url';
comment on column oauth_config.redirect_url is '应用回调地址';
comment on column oauth_config.grant_type is '授权类型';
comment on column oauth_config.response_type is '响应类型';
comment on column oauth_config.auth_scope is '授权范围';
comment on column oauth_config.create_user is '创建人';
comment on column oauth_config.create_dept is '创建部门';
comment on column oauth_config.create_time is '创建时间';
comment on column oauth_config.update_user is '更新人';
comment on column oauth_config.update_dept is '更新部门';
comment on column oauth_config.update_time is '更新时间';

INSERT INTO oauth_config ("status", "user_role", "app_type", "app_id", "app_secret", "auth_url", "redirect_url", "grant_type", "response_type", "auth_scope", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES (1, 3, 'gitlab', '155cbf79f0e8dc8c6b912b2ac033e741e9efb9ecffe661571693d392c279bc5d', '5eb6f1172bb175145f29d772f8ebacde5e37be4743cebbf917da2e8c68d999ec', 'https://gitlab.cowave.com', 'http://10.64.4.74/oauth/gitlab', 'authorization_code', 'code', 'read_user', 1, NULL, '2024-03-18 13:57:36.631', 1, NULL, '2024-03-18 13:59:53.624');

-- oauth 授权用户
drop table if exists oauth_user;
create table oauth_user
(
    id             bigserial primary key,
    "app_type"     character varying(64),
    "user_role"    int8,
    "user_name"    character varying(64),
    "user_account" character varying(64),
    "user_avatar"  character varying(256),
    "user_email"   character varying(64),
    "user_dept"    character varying(256),
    create_time    timestamp,
    update_time    timestamp
);
create unique index oauth_user_unique on oauth_user(app_type, user_account);
comment on table oauth_user is '授权用户';
comment on column oauth_user.id is 'id';
comment on column oauth_user.app_type is '应用类型';
comment on column oauth_user.user_role is '用户角色';
comment on column oauth_user.user_name is '用户名称';
comment on column oauth_user.user_account is '用户账号';
comment on column oauth_user.user_avatar is '用户头像';
comment on column oauth_user.user_email is '用户邮箱';
comment on column oauth_user.user_dept is '用户部门';
comment on column oauth_user.create_time is '创建时间';
comment on column oauth_user.update_time is '更新时间';

-- 菜单
delete from sys_menu where menu_type = 'M' and menu_path = 'oauth';
delete from sys_menu where menu_type = 'C' and component = 'system/oauth/gitlab';
delete from sys_menu where menu_type = 'B' and menu_permit = 'oauth:gitlab:edit';
delete from sys_menu where menu_type = 'B' and menu_permit = 'oauth:gitlab:query';
delete from sys_menu where menu_type = 'B' and menu_permit = 'oauth:gitlab:user:query';
delete from sys_menu where menu_type = 'B' and menu_permit = 'oauth:gitlab:user:edit';
delete from sys_menu where menu_type = 'B' and menu_permit = 'oauth:gitlab:user:delete';

INSERT INTO "sys_menu" ("parent_id", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ((select menu_id from sys_menu where menu_type = 'M' and menu_path = 'system'), 'OAuth认证', 9, NULL, 'oauth', NULL, 'M', 'vscope', NULL, 1, 1, 1, 1, 1, NULL, 0, 1, NULL, '2024-03-02 23:08:01.779', 1, NULL, '2024-03-02 23:08:01.779');
INSERT INTO "sys_menu" ("parent_id", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ((select menu_id from sys_menu where menu_type = 'M' and menu_path = 'oauth'), 'Gitlab', 1, 'sys:oauth:config', 'gitlab', NULL, 'C', 'gitlab', 'system/oauth/gitlab', 1, 1, 1, 1, 1, NULL, 0, 1, NULL, '2024-03-02 23:10:39.323', 1, NULL, '2024-03-03 07:20:04.089');
INSERT INTO "sys_menu" ("parent_id", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ((select menu_id from sys_menu where menu_type = 'C' and component = 'system/oauth/gitlab'), '配置', 1, 'oauth:gitlab:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 0, 1, NULL, '2024-03-21 13:57:12.65', 1, NULL, '2024-03-21 13:57:12.65');
INSERT INTO "sys_menu" ("parent_id", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ((select menu_id from sys_menu where menu_type = 'C' and component = 'system/oauth/gitlab'), '查询', 2, 'oauth:gitlab:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 0, 1, NULL, '2024-03-21 13:57:12.65', 1, NULL, '2024-03-21 13:57:12.65');
INSERT INTO "sys_menu" ("parent_id", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ((select menu_id from sys_menu where menu_type = 'C' and component = 'system/oauth/gitlab'), '用户查询', 3, 'oauth:gitlab:user:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 0, 1, NULL, '2024-03-21 13:57:12.65', 1, NULL, '2024-03-21 13:57:12.65');
INSERT INTO "sys_menu" ("parent_id", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ((select menu_id from sys_menu where menu_type = 'C' and component = 'system/oauth/gitlab'), '用户修改', 4, 'oauth:gitlab:user:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 0, 1, NULL, '2024-03-21 13:57:12.65', 1, NULL, '2024-03-21 13:57:12.65');
INSERT INTO "sys_menu" ("parent_id", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ((select menu_id from sys_menu where menu_type = 'C' and component = 'system/oauth/gitlab'), '用户删除', 5, 'oauth:gitlab:user:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 0, 1, NULL, '2024-03-21 13:57:12.65', 1, NULL, '2024-03-21 13:57:12.65');
