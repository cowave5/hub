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
    user_role        int8,
    environment      json,
    create_user        int8,
    create_dept        int8,
    create_time        timestamp,
    update_user        int8,
    update_dept        int8,
    update_time        timestamp
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
comment on column ldap_config.user_role is '用户默认角色';
comment on column ldap_config.environment is 'LDAP环境属性';
comment on column ldap_config.create_user is '创建人';
comment on column ldap_config.create_dept is '创建部门';
comment on column ldap_config.create_time is '创建时间';
comment on column ldap_config.update_user is '更新人';
comment on column ldap_config.update_dept is '更新部门';
comment on column ldap_config.update_time is '更新时间';

insert into ldap_config (ldap_name, ldap_status, ldap_url, ldap_user, ldap_passwd, base_dn, user_class, account_property,name_property,email_property,phone_property,post_property,dept_property,leader_property,info_property, user_role, create_user, create_dept, create_time, update_user, update_dept, update_time)
values ('cowave', 1, 'ldap://10.64.3.1:389', 'zhangyuliang@cowave.com', 'Cowave@123', 'OU=Cowavers,DC=cowave,DC=com', 'person', 'sAMAccountName','displayName','userPrincipalName','telephoneNumber','title','department','manager','distinguishedName', 3, 1, NULL, '2024-03-18 13:57:36.631', 1, NULL, '2024-03-18 13:59:53.624');

-- ldap 用户
drop table if exists ldap_user;
create table ldap_user
(
    id           bigserial primary key,
    user_id      int4 not null,
    ldap_id      int4 not null,
    user_info    character varying(128),
    user_account character varying(64),
    user_passwd  character varying(64),
    user_name    character varying(64),
    user_phone   character varying(64),
    user_email   character varying(128),
    user_post    character varying(128),
    user_dept    character varying(128),
    user_leader  character varying(64),
    is_sync      int2 default 1,
    create_time  timestamp,
    update_time  timestamp
);
create unique index ldap_user_unique on ldap_user(user_account);
comment on table ldap_user is 'ldap用户';
comment on column ldap_user.id is 'id';
comment on column ldap_user.user_id is '内部用户id';
comment on column ldap_user.ldap_id is 'Ldap配置id';
comment on column ldap_user.user_info is '用户信息';
comment on column ldap_user.user_account is '用户账号';
comment on column ldap_user.user_passwd is '用户密码';
comment on column ldap_user.user_name is '用户名称';
comment on column ldap_user.user_phone is '用户电话';
comment on column ldap_user.user_email is '用户邮箱';
comment on column ldap_user.user_post is '用户岗位';
comment on column ldap_user.user_dept is '用户部门';
comment on column ldap_user.user_leader is '上级用户';
comment on column ldap_user.is_sync is '用户信息是否同步';
comment on column ldap_user.create_time is '创建时间';
comment on column ldap_user.update_time is '更新时间';
