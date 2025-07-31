-- 凭据信息
drop table if exists env_credential;
create table env_credential
(
    credential_id    serial primary key,
    credential_name  character varying(128),
    credential_type  int4 default 0,
    credential_scope int4 default 0,
    username         character varying(128),
    secret           character varying(128),
    remark           character varying(200),
    create_by        character varying(64),
    create_time      timestamp,
    update_by        character varying(64),
    update_time      timestamp
);
