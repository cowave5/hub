-- ldap 配置
drop table if exists ldap_config;
create table ldap_config
(
    id               serial primary key,
    ldap_name        character varying(64) not null,
    ldap_status      int2 default 0,
    ldap_url         character varying(128),
    ldap_user        character varying(128),
    ldap_passwd      character varying(128),
    base_dn          character varying(128),
    readonly         int2 default 0,
    user_dn          character varying(64),
    user_class       character varying(64),
    account_property character varying(64),
    name_property    character varying(64),
    email_property   character varying(64),
    phone_property   character varying(64),
    post_property    character varying(64),
    dept_property    character varying(64),
    leader_property  character varying(64),
    info_property    character varying(64),
    environment      json,
    role_code        character varying(64),
    create_by        character varying(64),
    create_time      timestamp,
    update_by        character varying(64),
    update_time      timestamp
);
comment on table ldap_config is 'ldap配置';
comment on column ldap_config.id is 'id';
comment on column ldap_config.ldap_name is 'Ldap名称';
comment on column ldap_config.ldap_status is 'Ldap状态';
comment on column ldap_config.ldap_url is 'Ldap地址';
comment on column ldap_config.ldap_user is 'Ldap用户';
comment on column ldap_config.ldap_passwd is 'Ldap密码';
comment on column ldap_config.base_dn is '基本搜索DN';
comment on column ldap_config.readonly is '是否匿名进行只读连接';
comment on column ldap_config.user_dn is '用户搜索DN';
comment on column ldap_config.user_class is '用户对象类';
comment on column ldap_config.account_property is '用户名属性';
comment on column ldap_config.name_property is '姓名属性';
comment on column ldap_config.email_property is '邮箱属性';
comment on column ldap_config.phone_property is '电话属性';
comment on column ldap_config.post_property is '岗位属性';
comment on column ldap_config.dept_property is '部门属性';
comment on column ldap_config.leader_property is '上级用户属性';
comment on column ldap_config.info_property is '用户信息属性';
comment on column ldap_config.environment is 'LDAP环境属性';
comment on column ldap_config.role_code is '用户默认角色';
comment on column ldap_config.create_by is '创建人';
comment on column ldap_config.create_time is '创建时间';
comment on column ldap_config.update_by is '更新人';
comment on column ldap_config.update_time is '更新时间';

-- ldap 用户
drop table if exists ldap_user;
create table ldap_user
(
    id           bigserial primary key,
    user_code    character varying(64),
    role_code    character varying(64),
    ldap_name    character varying(64),
    user_account character varying(64),
    user_passwd  character varying(64),
    user_name    character varying(64),
    user_phone   character varying(64),
    user_email   character varying(128),
    user_post    character varying(128),
    user_dept    character varying(128),
    user_leader  character varying(64),
    user_info    character varying(128),
    create_time  timestamp,
    update_time  timestamp
);
create unique index ldap_user_unique on ldap_user(user_account);
comment on table ldap_user is 'ldap用户';
comment on column ldap_user.id is 'id';
comment on column ldap_user.role_code is '用户角色';
comment on column ldap_user.ldap_name is 'Ldap名称';
comment on column ldap_user.user_info is '用户信息';
comment on column ldap_user.user_account is '用户账号';
comment on column ldap_user.user_passwd is '用户密码';
comment on column ldap_user.user_name is '用户名称';
comment on column ldap_user.user_phone is '用户电话';
comment on column ldap_user.user_email is '用户邮箱';
comment on column ldap_user.user_post is '用户岗位';
comment on column ldap_user.user_dept is '用户部门';
comment on column ldap_user.user_leader is '上级用户';
comment on column ldap_user.create_time is '创建时间';
comment on column ldap_user.update_time is '更新时间';