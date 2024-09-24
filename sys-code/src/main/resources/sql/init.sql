-- 1.应用信息
drop table if exists app_info;
create table app_info(
    id           bigserial primary key,
    app_code     character varying(64),
    app_name     character varying(255),
    app_version  character varying(64),
    app_desc     character varying(512),
    http_port    int4,
    http_context character varying(64) default '/',
    project_code character varying(64),
    db_id        int8,
    is_security  int2,
    update_time  timestamp
);

-- 2.应用模型
drop table if exists app_model;
create table app_model(
    app_id int8,
    model_id int8
);

-- 3.模型信息
drop table if exists model_info;
create table model_info(
    id             bigserial primary key,
    table_id       int8,
    class_name     character varying(255),
    class_comment  character varying(255),
    api_context    character varying(64) default '/',
    auth_prefix    character varying(64),
    is_excel       int2 default 0,
    is_access      int2 default 0,
    is_log         int2 default 0,
    log_type       character varying(64),
    remark         character varying(512),
    update_time    timestamp
);

-- 4.模型字段
drop table if exists model_field;
create table model_field(
    id              bigserial primary key,
    model_id        int8,
    column_id       int8,
    field_name      character varying(64),
    field_type      int8,
    field_comment   character varying(255),
    sort            int4,
    is_notnull      int2 default 0,
    is_collect      int2 default 0,
    is_list         int2 default 1,
    is_info         int2 default 1,
    is_insert       int2 default 1,
    is_edit         int2 default 1,
    is_where        int2 default 0,
    where_type      character varying(64),
    html_type       character varying(64),
    is_excel        int2 default 0,
    excel_name      character varying(64),
    excel_width     int4 default 20,
    excel_converter character varying(255),
    update_time     timestamp
);
create unique index model_field_unique on model_field(model_id, field_name);

-- 5.数据库管理
drop table if exists db_info;
create table db_info(
    id              bigserial primary key,
    db_code         character varying(64),
    db_name         character varying(128),
    db_type         character varying(64),
    db_url          character varying(255),
    db_user         character varying(64),
    db_passwd       character varying(64),
    remark          text,
    update_time     timestamp
);

-- 6.数据库表
drop table if exists db_table;
create table db_table(
    id             bigserial primary key,
    db_id          int8,
    table_name     character varying(255),
    table_comment  character varying(255),
    update_time    timestamp
);

-- 7.表字段
drop table if exists db_table_column;
create table db_table_column(
    id               bigserial primary key,
    table_id         int8,
    column_name      character varying(255),
    column_comment   character varying(255),
    column_typename  character varying(64),
    column_type      character varying(64),
    column_precision int4,
    column_scale     int4,
    column_default   character varying(64),
    sort             int4,
    is_primary       int2 default 0,
    is_notnull       int2 default 0,
    is_increment     int2 default 0,
    update_time      timestamp
);
create unique index db_table_column_unique on db_table_column(table_id, column_name);

-- 8.索引信息
drop table if exists db_table_index;
create table db_table_index
(
    id              bigserial primary key,
    table_id        int8,
    index_type      character varying(64),
    index_name      character varying(255),
    is_unique       int2,
    column_name     character varying(255),
    column_position int4
);

-- 9.序列信息
drop table if exists db_sequence;
create table db_sequence
(
    id            bigserial primary key,
    db_id         int8,
    sequence_name character varying(64),
    min_value     int8,
    max_value     int8,
    increment_by  int8,
    last_number   int8
);
