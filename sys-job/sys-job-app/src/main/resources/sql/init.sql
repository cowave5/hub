drop table if exists job_client;
create table job_client
(
    id             serial primary key,
    client_name    varchar(255) NOT NULL,
    client_address varchar(255) NOT NULL,
    update_time    timestamp DEFAULT NULL
);
create unique index job_client_unique on job_client(client_name, client_address);

drop table if exists job_client_handler;
create table job_client_handler
(
    id             serial primary key,
    client_id      int4         NOT NULL,
    client_handler varchar(255) NOT NULL
);
create unique index job_client_handler_unique on job_client_handler(client_id, client_handler);

drop table if exists job_trigger;
create table job_trigger
(
    id                    serial primary key,
    status                int2         NOT NULL DEFAULT '0',
    task_name             varchar(64)  NOT NULL,
    task_type             varchar(50)  NOT NULL,
    task_over_time        int4         NOT NULL DEFAULT '0',
    task_begin_time       timestamp,
    task_end_time         timestamp,
    glue_source           text,
    glue_update_time      int8                  DEFAULT NULL,
    trigger_type          varchar(50)  NOT NULL DEFAULT 'NONE',
    trigger_param         varchar(128)          DEFAULT NULL,
    trigger_times         int8         NOT NULL DEFAULT '0',
    trigger_success_times int8         NOT NULL DEFAULT '0',
    trigger_last_time     int8         NOT NULL DEFAULT '0',
    trigger_next_time     int8         NOT NULL DEFAULT '0',
    handler_name          varchar(255)          DEFAULT NULL,
    handler_param         varchar(512)          DEFAULT NULL,
    misfire_strategy      varchar(50)  NOT NULL DEFAULT 'DO_NOTHING',
    route_strategy        varchar(50)           DEFAULT NULL,
    block_strategy        varchar(50)           DEFAULT NULL,
    create_by             varchar(64),
    create_time           timestamp,
    update_time           timestamp
);

drop table if exists job_trigger_log;
create table job_trigger_log
(
    id             bigserial primary key,
    task_name      varchar(64) NOT NULL,
    trigger_id     int4        NOT NULL,
    trigger_status int4         DEFAULT '0',
    trigger_type   varchar(64)  DEFAULT NULL,
    trigger_time   timestamp    DEFAULT NULL,
    client_address varchar(255) DEFAULT NULL,
    sharding_param varchar(20)  DEFAULT NULL,
    handler_name   varchar(255) DEFAULT NULL,
    handler_param  varchar(512) DEFAULT NULL,
    handle_time    timestamp    DEFAULT NULL,
    handle_cost    int8,
    fail_msg       text
);

drop table if exists job_trigger_glue;
create table job_trigger_glue
(
    id          serial primary key,
    trigger_id  int4         NOT NULL,
    glue_type   varchar(50) DEFAULT NULL,
    glue_source text,
    remark      varchar(128) NOT NULL,
    create_time timestamp   DEFAULT NULL,
    update_time timestamp   DEFAULT NULL
);
