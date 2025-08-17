-- 构建目录
drop table if exists build_folder;
create table build_folder
(
    folder_id    serial primary key,
    parent_id    int4 default 0,
    folder_name  character varying(64),
    folder_order int4 default 0,
    visibility   int2 default 0,
    owner_name   character varying(64),
    owner_code   character varying(64),
    create_time  timestamp,
    update_by    character varying(64),
    update_time  timestamp
);

-- 构建目录成员
drop table if exists build_folder_member;
create table build_folder_member
(
    folder_id int4,
    user_code character varying(64),
    user_name character varying(64),
    user_role int2,
    create_by    character varying(64),
    create_time  timestamp,
    constraint build_folder_member_pkey primary key (folder_id, user_code)
);

-- 构建配置
drop table if exists build_config;
create table build_config
(
    build_id          serial primary key,
    folder_id         int4,
    build_name        character varying(64),
    repo_url          character varying(256),
    repo_type         character varying(64),
    repo_protocol     character varying(64),
    last_status       int2,
    last_cost         int8,
    last_time_success timestamp,
    last_time_failure timestamp,
    create_by         character varying(64),
    create_time       timestamp,
    update_by         character varying(64),
    update_time       timestamp
);

-- 构建历史
drop table if exists build_history;
create table build_history
(
    history_id    bigserial primary key,
    build_id      int4,
    build_index   int8,
    build_home    character varying(128),
    build_branch  character varying(64),
    build_tag     character varying(64),
    build_version character varying(64),
    build_time    timestamp,
    build_cost    int8,
    create_by     character varying(64),
    create_time   timestamp
);

-- 构建历史日志
drop table if exists build_history_log;
create table build_history_log
(
    history_id int8 primary key

);

