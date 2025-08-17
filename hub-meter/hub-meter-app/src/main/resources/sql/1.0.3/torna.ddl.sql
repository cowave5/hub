-- 接口定义
DROP TABLE IF EXISTS api_definition;
CREATE TABLE api_definition
(
    id                    bigserial primary key,
    parent_id             int8         DEFAULT 0,
    is_group              int2         DEFAULT 0,
    folder_id             int8         DEFAULT 0,
    data_id               varchar(64),
    doc_key               varchar(64),
    md5                   varchar(32),
    name                  varchar(128),
    description           text,
    author                varchar(64),
    type                  int2         DEFAULT 0, -- '0:http,1:dubbo',
    version               varchar(32),
    url                   varchar(256),
    http_method           varchar(12),
    content_type          varchar(128),
    deprecated            varchar(128) DEFAULT '$false$',
    is_use_global_headers int2         DEFAULT 1,
    is_use_global_params  int2         DEFAULT 1,
    is_use_global_returns int2         DEFAULT 1,
    is_request_array      int2         DEFAULT 0,
    is_response_array     int2         DEFAULT 0,
    request_array_type    varchar(16)  DEFAULT 'object',
    response_array_type   varchar(16)  DEFAULT 'object',
    order_index           int          DEFAULT 0,
    status                int2         DEFAULT 5,
    is_show               int2         DEFAULT 1,
    is_locked             int2         DEFAULT 0,
    is_deleted            int2         DEFAULT 0,
    remark                text,
    create_mode           int2         DEFAULT 0, -- '新增操作方式，0：人工操作，1：平台推送',
    create_user           varchar(64),
    create_user_name      varchar(64),
    create_time           timestamp,
    update_mode           int2         DEFAULT 0, -- '修改操作方式，0：人工操作，1：平台推送'
    update_user           varchar(64),
    update_user_name      varchar(64),
    update_time           timestamp
);
create unique index api_definition_data_id on api_definition(data_id);
create index api_definition_folder_id on api_definition(folder_id);
create index api_definition_parent_id on api_definition(parent_id);
create index api_definition_doc_key on api_definition(doc_key);

-- 接口参数
DROP TABLE IF EXISTS api_param;
CREATE TABLE api_param
(
    id            bigserial primary key,
    parent_id     int8 DEFAULT 0,
    api_id        int8 DEFAULT 0,
    enum_id       int8 DEFAULT 0,
    data_id       varchar(64),    -- md5(doc_id:parent_id:style:name)'
    name          varchar(64),
    type          varchar(64),
    required      int2 DEFAULT 0,
    max_length    varchar(64),
    example       varchar(1024),
    description   text,
    style         int2 DEFAULT 0, -- '0：path, 1：header， 2：body参数，3：返回参数，4：错误码, 5：query参数',
    order_index   int4 DEFAULT 0,
    is_deleted    int2 DEFAULT 0,
    create_mode           int2         DEFAULT 0, -- '新增操作方式，0：人工操作，1：平台推送',
    create_user           varchar(64),
    create_user_name      varchar(64),
    create_time           timestamp,
    update_mode           int2         DEFAULT 0, -- '修改操作方式，0：人工操作，1：平台推送'
    update_user           varchar(64),
    update_user_name      varchar(64),
    update_time           timestamp
);
create unique index api_param_data_id on api_param(data_id);
create index api_param_api_id on api_param(api_id);

-- 接口枚举
DROP TABLE IF EXISTS api_enum;
CREATE TABLE api_enum
(
    id          bigserial primary key,
    folder_id   int8 DEFAULT 0,
    data_id     varchar(64), -- md5(module_id:name)
    name        varchar(128),
    description varchar(128),
    is_deleted  int2 DEFAULT 0,
    create_time timestamp,
    update_time timestamp
);
create index api_enum_module_id on api_enum (folder_id);
create index api_enum_data_id on api_enum (data_id);

-- 接口枚举项
DROP TABLE IF EXISTS api_enum_item;
CREATE TABLE api_enum_item
(
    id          bigserial primary key,
    enum_id     int8 DEFAULT 0,
    name        varchar(128),
    type        varchar(64),
    value       varchar(64),
    description varchar(128),
    order_index int4 DEFAULT 0,
    is_deleted  int2 DEFAULT 0,
    create_time timestamp,
    update_time timestamp
);
create index api_enum_item_enum_id on api_enum_item(enum_id);

-- 接口快照
DROP TABLE IF EXISTS api_snapshot;
CREATE TABLE api_snapshot
(
    id          bigserial primary key,
    api_id      int8 DEFAULT 0,
    doc_key     varchar(64),
    md5         varchar(64),
    content     text,
    create_time timestamp,
    update_time timestamp
);
create index api_snapshot_api_id on api_snapshot(api_id);
create index api_snapshot_md5 on api_snapshot(md5);

-- 接口目录
DROP TABLE IF EXISTS api_folder;
CREATE TABLE api_folder
(
    id                  bigserial primary key,
    name                varchar(64),
    project_id          int8 DEFAULT 0,
    type                int2 DEFAULT 0, -- '模块类型，0：自定义添加，1：swagger导入，2：postman导入'
    import_url          varchar(128),
    basic_username varchar(128),
    basic_password varchar(128),
    token               varchar(128),
    order_index         int4 DEFAULT 0
);
create index api_folder_token on api_folder(token);
create index api_folder_project_id on api_folder(project_id);

-- 接口错误码
DROP TABLE IF EXISTS api_code;
CREATE TABLE api_code
(
    id          bigserial primary key,
    folder_id   int8 DEFAULT 0,
    content     text,
    create_time timestamp,
    update_time timestamp
);












-- 聚合文档附加页
DROP TABLE IF EXISTS compose_additional_page;
CREATE TABLE compose_additional_page
(
    id           bigserial primary key,
    project_id   int8,
    title        varchar(64),
    content      text,
    order_index  int  DEFAULT 0,
    status       int2 DEFAULT 1,
    is_deleted   int2 DEFAULT 0,
    gmt_create   timestamp,
    gmt_modified timestamp
);

-- 聚合文档公共参数
DROP TABLE IF EXISTS compose_common_param;
CREATE TABLE compose_common_param
(
    id                 bigserial primary key,
    parent_id          int8          DEFAULT 0,
    data_id            varchar(64),
    name               varchar(64),
    type               varchar(64)   DEFAULT 'String',
    required           int2 NOT NULL DEFAULT 0,
    max_length         varchar(64)   DEFAULT '-',
    example            varchar(1024),
    description        text,
    enum_id            int8          DEFAULT 0,
    compose_project_id int8          DEFAULT 0,
    style              int2 NOT NULL DEFAULT 0, -- '0：path, 1：header， 2：请求参数，3：返回参数，4：错误码'
    order_index        int4          DEFAULT 0,
    is_deleted         int2          DEFAULT 0,
    gmt_create         timestamp,
    gmt_modified       timestamp
);
create index compose_common_param_compose_project_id on compose_common_param(compose_project_id);
create unique index compose_common_param_data_id on compose_common_param(data_id);

-- 文档引用
DROP TABLE IF EXISTS compose_doc;
CREATE TABLE compose_doc
(
    id           bigserial primary key,
    parent_id    int8 DEFAULT 0,
    project_id   int8 DEFAULT 0,
    doc_id       int8 DEFAULT 0,
    is_folder    int2 DEFAULT 0,
    folder_name  varchar(64),
    origin       varchar(128),
    is_deleted   int2 DEFAULT 0,
    creator      varchar(64),
    order_index  int4 DEFAULT 0,
    gmt_create   timestamp,
    gmt_modified timestamp
);
create index compose_doc_project_id on compose_doc(project_id);

-- 组合项目表
DROP TABLE IF EXISTS compose_project;
CREATE TABLE compose_project
(
    id            bigserial primary key,
    name          varchar(64),
    description   varchar(128),
    space_id      int8 NOT NULL DEFAULT 0,
    type          int2          DEFAULT 1,
    password      varchar(64),
    creator_id    int8          DEFAULT 0,
    creator_name  varchar(64),
    modifier_id   int8          DEFAULT 0,
    modifier_name varchar(64),
    order_index   int4          DEFAULT 0,
    show_debug    int2          DEFAULT 1,
    gateway_url   varchar(128),
    status        int2          DEFAULT 1,
    is_deleted    int2          DEFAULT 0,
    gmt_create    timestamp,
    gmt_modified  timestamp
);
create index compose_project_space_id on compose_project(space_id);

-- 调试脚本表
DROP TABLE IF EXISTS debug_script;
CREATE TABLE debug_script
(
    id           bigserial primary key,
    NAME         varchar(64),
    description  varchar(128),
    content      text,
    type         int2 DEFAULT 0, -- 0：pre，1：after'
    scope        int2 DEFAULT 0, -- '作用域，0：当前文档，1：当前应用，2：当前项目'
    ref_id       int8 DEFAULT 0,
    creator_name varchar(64),
    enabled      int2 DEFAULT 1,
    is_deleted   int2 DEFAULT 0,
    gmt_create   timestamp,
    gmt_modified timestamp
);
create index debug_script_ref_id on debug_script(ref_id);

-- 文档比较记录
DROP TABLE IF EXISTS doc_diff_detail;
CREATE TABLE doc_diff_detail
(
    id            bigserial primary key,
    record_id     int8,
    position_type int2 DEFAULT 0, -- COMMENT '变更位置，0：文档名称，1：文档描述，2：contentType，3：httpMethod，10：参数名称，11：参数类型，12：参数必填，13：参数最大长度，14：参数描述，15：参数示例值',
    target_name   varchar(64),
    content       text,
    modify_type   int2 DEFAULT 0, -- '变更类型，0：修改，1：新增，2：删除',
    gmt_create    timestamp,
    gmt_modified  timestamp
);
create index doc_diff_detail_record_id on doc_diff_detail(record_id);

-- 文档比较记录
DROP TABLE IF EXISTS doc_diff_record;
CREATE TABLE doc_diff_record
(
    id              bigserial primary key,
    doc_id          int8 DEFAULT 0,
    doc_key         varchar(64),
    md5_old         varchar(64),
    md5_new         varchar(64),
    modify_source   int2 DEFAULT 0, -- '修改方式，0：推送，1：表单编辑',
    modify_user_id  int8 DEFAULT 0,
    modify_nickname varchar(64),
    modify_type     int2 DEFAULT 0, -- '变更类型，0：修改，1：新增，2：删除',
    modify_time     timestamp,
    gmt_create      timestamp,
    gmt_modified    timestamp
);
create index doc_diff_record_doc_id on doc_diff_record(doc_id);

-- 代码生成模板
DROP TABLE IF EXISTS gen_template;
CREATE TABLE gen_template
(
    id           bigserial primary key,
    name         varchar(64),
    content      text,
    is_deleted   int2 DEFAULT 0,
    group_name   varchar(64),
    gmt_create   timestamp,
    gmt_modified timestamp
);

-- 代码生成模板
DROP TABLE IF EXISTS gen_template2;
CREATE TABLE gen_template2
(
    id           bigserial primary key,
    name         varchar(64),
    content      text,
    is_deleted   int2 DEFAULT 0,
    group_name   varchar(64),
    gmt_create   timestamp,
    gmt_modified timestamp
);

-- mock配置
DROP TABLE IF EXISTS mock_config;
CREATE TABLE mock_config
(
    id                bigserial primary key,
    name              varchar(128),
    data_id           varchar(64),    -- md5(path+query+body)
    version           int  DEFAULT 0,
    path              varchar(128),
    ip                varchar(64),
    request_data      text,
    request_data_type int2 DEFAULT 0, -- '参数类型，0：KV形式，1：json形式'
    http_status       int4 DEFAULT 200,
    delay_mills       int4 DEFAULT 0,
    result_type       int2 DEFAULT 0, -- '返回类型，0：自定义内容，1：脚本内容'
    response_headers  text,
    response_body     text,
    mock_script       text,
    mock_result       text,
    doc_id            int8 DEFAULT 0,
    remark            varchar(128),
    creator_id        int8,
    creator_name      varchar(64),
    modifier_id       int8,
    modifier_name     varchar(64),
    is_deleted        int2 DEFAULT 0,
    gmt_create        timestamp,
    gmt_modified      timestamp
);
create index mock_config_doc_id on mock_config(doc_id);
create index mock_config_data_id on mock_config(data_id);

-- 模块配置
DROP TABLE IF EXISTS module_config;
CREATE TABLE module_config
(
    id           bigserial primary key,
    module_id    int8 DEFAULT 0,
    type         int2 DEFAULT 0,
    config_key   varchar(128),
    config_value varchar(256),
    extend_id    int8 DEFAULT 0,
    description  varchar(256),
    is_deleted   int2 DEFAULT 0,
    gmt_create   timestamp,
    gmt_modified timestamp
);
create index module_config_module_id_type on module_config(module_id, type);

-- 模块调试环境
DROP TABLE IF EXISTS module_environment;
CREATE TABLE module_environment
(
    id           bigserial primary key,
    module_id    int8,
    name         varchar(64),
    url          varchar(255),
    is_public    int2 DEFAULT 0,
    is_deleted   int2 DEFAULT 0,
    gmt_create   timestamp,
    gmt_modified timestamp
);
create index module_environment_module_id_name on module_environment(module_id, name);

-- 模块公共参数
DROP TABLE IF EXISTS module_environment_param;
CREATE TABLE module_environment_param
(
    id             bigserial primary key,
    data_id        varchar(64),    -- md5(doc_id:parent_id:style:name)'
    name           varchar(64),
    type           varchar(64),
    required       int2 DEFAULT 0,
    max_length     varchar(64),
    example        varchar(1024),
    description    text,
    enum_id        int8 DEFAULT 0,
    environment_id int8 DEFAULT 0,
    parent_id      int8 DEFAULT 0,
    style          int2 DEFAULT 0, -- '0：path, 1：header， 2：请求参数，3：返回参数，4：错误码'
    create_mode    int2 DEFAULT 0, -- '新增操作方式，0：人工操作，1：平台推送'
    modify_mode    int2 DEFAULT 0, -- '修改操作方式，0：人工操作，1：平台推送'
    creator_id     int8,
    creator_name   varchar(64),
    modifier_id    int8,
    modifier_name  varchar(64),
    order_index    int4 DEFAULT 0,
    is_deleted     int2 DEFAULT 0,
    gmt_create     timestamp,
    gmt_modified   timestamp
);
create unique index module_environment_param_data_id on module_environment_param(data_id);
create index module_environment_param_environment_id on module_environment_param(environment_id);

-- swagger配置表
DROP TABLE IF EXISTS module_swagger_config;
CREATE TABLE module_swagger_config
(
    id            bigserial primary key,
    module_id     int8,
    url           varchar(256),
    content       text,
    auth_username varchar(128),
    auth_password varchar(128),
    gmt_create    timestamp,
    gmt_modified  timestamp
);
create index module_swagger_config_module_id on module_swagger_config(module_id);

-- MeterSphere模块配置
DROP TABLE IF EXISTS ms_module_config;
CREATE TABLE ms_module_config
(
    id              bigserial primary key,
    module_id       int8,
    release_id      int8,
    ms_project_id   varchar(64),
    ms_module_id    varchar(64),
    ms_cover_module int2,
    name            varchar(100),
    gmt_create      timestamp,
    gmt_modified    timestamp
);

-- MeterSphere空间配置
DROP TABLE IF EXISTS ms_space_config;
CREATE TABLE ms_space_config
(
    id            bigserial primary key,
    space_id      int8,
    ms_space_id   varchar(64),
    ms_space_name varchar(64),
    ms_address    varchar(100),
    ms_access_key varchar(100),
    ms_secret_key varchar(100),
    gmt_create    timestamp,
    gmt_modified  timestamp,
    version       int NOT NULL DEFAULT 1
);
create unique index ms_space_config_space_id on ms_space_config(space_id);

-- 开放用户
DROP TABLE IF EXISTS open_user;
CREATE TABLE open_user
(
    id           bigserial primary key,
    app_key      varchar(100),
    secret       varchar(200),
    status       int2 DEFAULT 1,
    applicant    varchar(64),
    space_id     int8 DEFAULT 0,
    is_deleted   int2 DEFAULT 0,
    gmt_create   timestamp,
    gmt_modified timestamp
);
create unique index open_user_app_key on open_user(app_key);

-- 项目表
DROP TABLE IF EXISTS project;
CREATE TABLE project
(
    id            bigserial primary key,
    name          varchar(64),
    description   varchar(128),
    space_id      int8 DEFAULT 0,
    is_private    int2 DEFAULT 0,
    creator_id    int8,
    creator_name  varchar(64),
    modifier_id   int8,
    modifier_name varchar(64),
    order_index   int4 DEFAULT 0,
    is_deleted    int2 DEFAULT 0,
    gmt_create    timestamp,
    gmt_modified  timestamp
);

-- 项目版本表
DROP TABLE IF EXISTS project_release;
CREATE TABLE project_release
(
    id               bigserial primary key,
    project_id       int8 DEFAULT 0,
    release_no       varchar(20),
    release_desc     varchar(200),
    status           int2 DEFAULT 0,
    dingding_webhook varchar(200),
    we_com_webhook   varchar(200),
    is_deleted       int2 DEFAULT 0,
    gmt_create       timestamp,
    gmt_modified     timestamp
);

-- 项目版本关联文档表
DROP TABLE IF EXISTS project_release_doc;
CREATE TABLE project_release_doc
(
    id           bigserial primary key,
    project_id   int8,
    release_id   int8,
    module_id    int8,
    source_id    int8 DEFAULT 0,
    is_deleted   int2 DEFAULT 0,
    gmt_create   timestamp,
    gmt_modified timestamp
);

-- 项目用户关系表
DROP TABLE IF EXISTS project_user;
CREATE TABLE project_user
(
    id           bigserial primary key,
    project_id   int8,
    user_id      int8,
    role_code    varchar(64),
    is_deleted   int2 DEFAULT 0,
    gmt_create   timestamp,
    gmt_modified timestamp
);

-- 属性表
DROP TABLE IF EXISTS prop;
CREATE TABLE prop
(
    id           bigserial primary key,
    ref_id       int8,
    type         int2,
    name         varchar(128),
    val          text NOT NULL,
    gmt_create   timestamp,
    gmt_modified timestamp
);

-- 推送忽略字段
DROP TABLE IF EXISTS push_ignore_field;
CREATE TABLE push_ignore_field
(
    id                bigserial primary key,
    module_id         int8,
    data_id           varchar(64),
    field_name        varchar(64),
    field_description varchar(64),
    gmt_create        timestamp
);

-- 分享配置表
DROP TABLE IF EXISTS share_config;
CREATE TABLE share_config
(
    id                    bigserial primary key,
    type                  int2,
    password              varchar(128),
    expiration_time       timestamp,
    status                int2 DEFAULT 1,
    module_id             int8 DEFAULT 0,
    is_all                int2 DEFAULT 0,
    is_deleted            int2 DEFAULT 0,
    remark                varchar(128),
    creator_name          varchar(64),
    is_show_debug         int2,
    is_all_selected_debug int2,
    gmt_create            timestamp,
    gmt_modified          timestamp
);

-- 分享详情
DROP TABLE IF EXISTS share_content;
CREATE TABLE share_content
(
    id              bigserial primary key,
    share_config_id int8,
    doc_id          int8,
    parent_id       int8,
    is_share_folder int2,
    is_deleted      int2 DEFAULT 0,
    gmt_create      timestamp,
    gmt_modified    timestamp
);

-- 分享环境关联表
DROP TABLE IF EXISTS share_environment;
CREATE TABLE share_environment
(
    id                    bigserial primary key,
    share_config_id       int8,
    module_environment_id int8
);

-- 分组表
DROP TABLE IF EXISTS space;
CREATE TABLE space
(
    id            bigserial primary key,
    name          varchar(64),
    creator_id    int8,
    creator_name  varchar(64),
    modifier_id   int8,
    modifier_name varchar(64),
    is_compose    int2,
    is_deleted    int2 DEFAULT 0,
    gmt_create    timestamp,
    gmt_modified  timestamp
);

-- 分组用户关系表
DROP TABLE IF EXISTS space_user;
CREATE TABLE space_user
(
    id           bigserial primary key,
    user_id      int8,
    space_id     int8,
    role_code    varchar(64),
    is_deleted   int2 DEFAULT 0,
    gmt_create   timestamp,
    gmt_modified timestamp
);

-- 系统配置表
DROP TABLE IF EXISTS system_config;
CREATE TABLE system_config
(
    id           bigserial primary key,
    config_key   varchar(64),
    config_value varchar(256),
    remark       varchar(128),
    is_deleted   int2 DEFAULT 0,
    gmt_create   timestamp,
    gmt_modified timestamp
);

-- 国际化配置
DROP TABLE IF EXISTS system_i18n_config;
CREATE TABLE system_i18n_config
(
    id           bigserial primary key,
    lang         varchar(8),
    description  varchar(16),
    content      text,
    is_deleted   int2 DEFAULT 0,
    gmt_create   timestamp,
    gmt_modified timestamp
);

-- 登录token
DROP TABLE IF EXISTS system_login_token;
CREATE TABLE system_login_token
(
    id           bigserial primary key,
    login_key    varchar(64),
    token        varchar(256),
    expire_time  timestamp,
    gmt_create   timestamp,
    gmt_modified timestamp
);

-- 钉钉开放平台用户
DROP TABLE IF EXISTS user_dingtalk_info;
CREATE TABLE user_dingtalk_info
(
    id           bigserial primary key,
    nick         varchar(64),
    name         varchar(64),
    email        varchar(128),
    userid       varchar(128),
    unionid      varchar(128),
    openid       varchar(128),
    user_info_id int8,
    gmt_create   timestamp,
    gmt_modified timestamp
);

-- 用户表
DROP TABLE IF EXISTS user_info;
CREATE TABLE user_info
(
    id             bigserial primary key,
    username       varchar(128),
    password       varchar(128),
    nickname       varchar(64),
    is_super_admin int2 DEFAULT 0,
    source         varchar(64),
    email          varchar(128),
    status         int2,
    is_deleted     int2 DEFAULT 0,
    gmt_create     timestamp,
    gmt_modified   timestamp
);

-- 站内消息
DROP TABLE IF EXISTS user_message;
CREATE TABLE user_message
(
    id           bigserial primary key,
    user_id      int8,
    message      varchar(256),
    is_read      int2 DEFAULT 0,
    type         int2 DEFAULT 0,
    source_id    int8,
    gmt_create   timestamp,
    gmt_modified timestamp
);

-- 用户订阅表
DROP TABLE IF EXISTS user_subscribe;
CREATE TABLE user_subscribe
(
    id           bigserial primary key,
    user_id      int8,
    type         int2,
    source_id    int8,
    is_deleted   int2 DEFAULT 0,
    gmt_create   timestamp,
    gmt_modified timestamp
);

DROP TABLE IF EXISTS user_wecom_info;
CREATE TABLE user_wecom_info
(
    id           bigserial primary key,
    mobile       varchar(20),
    user_info_id int8,
    gmt_create   timestamp,
    gmt_modified timestamp
);
