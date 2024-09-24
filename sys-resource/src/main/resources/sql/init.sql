-- 1. 卫星 info_satellite
drop table if exists info_satellite;
create table info_satellite
(
    sat_id            serial primary key,
    sat_name          varchar(256) not null,
    sat_version       int8 default 0,
    sat_type          int4,
    sat_orbit         int4,
    sat_plat          int4,
    beacon            int8,
    freq_type         int4,
    manufacturer      varchar(128),
    maintainer        varchar(128),
    operator          varchar(128),
    longitude         numeric(10, 2),
    latitude          numeric(10, 2),
    altitude          numeric(10, 2),
    launch_date       timestamp,
    orbit_date        timestamp,
    service_years     int4,
    create_user       varchar(256),
    create_username   varchar(256),
    create_dept       varchar(256),
    create_deptname   varchar(256),
    create_time       timestamp,
    update_user       varchar(256),
    update_username   varchar(256),
    update_dept       varchar(256),
    update_deptname   varchar(256),
    update_time       timestamp
);
comment on table info_satellite is '卫星';
comment on column info_satellite.sat_id is '卫星id';
comment on column info_satellite.sat_name is '卫星名称';
comment on column info_satellite.sat_version is '卫星信息版本';
comment on column info_satellite.sat_type is '卫星类型';
comment on column info_satellite.sat_orbit is '卫星轨道';
comment on column info_satellite.sat_plat is '卫星平台';
comment on column info_satellite.beacon is '信标频率';
comment on column info_satellite.freq_type is '频段类型';
comment on column info_satellite.manufacturer is '生产厂商';
comment on column info_satellite.maintainer is '管理单位';
comment on column info_satellite.operator is '管理单位';
comment on column info_satellite.longitude is '经度';
comment on column info_satellite.latitude is '纬度';
comment on column info_satellite.altitude is '轨道高度';
comment on column info_satellite.launch_date is '发射时间';
comment on column info_satellite.orbit_date is '定轨时间';
comment on column info_satellite.service_years is '使用年限';
comment on column info_satellite.create_user is '创建人';
comment on column info_satellite.create_username is '创建人名称';
comment on column info_satellite.create_dept is '创建单位';
comment on column info_satellite.create_deptname is '创建单位名称';
comment on column info_satellite.create_time is '创建时间';
comment on column info_satellite.update_user is '更新人';
comment on column info_satellite.update_username is '更新人名称';
comment on column info_satellite.update_dept is '更新单位';
comment on column info_satellite.update_deptname is '更新单位名称';
comment on column info_satellite.update_time is '更新时间';

-- 2. 天线 info_antenna
drop table if exists info_antenna;
create table info_antenna
(
    sat_id          int4   not null,
    antenna_id      serial primary key,
    antenna_name    varchar(256) not null,
    freq_type       int4,
    ante_calib      int4,
    power           numeric(10, 2),
    gain            numeric(10, 2),
    create_user     varchar(256),
    create_username varchar(256),
    create_dept     varchar(256),
    create_deptname varchar(256),
    create_time     timestamp,
    update_user     varchar(256),
    update_username varchar(256),
    update_dept     varchar(256),
    update_deptname varchar(256),
    update_time     timestamp
);
comment on table info_antenna is '天线';
comment on column info_antenna.sat_id is '卫星id';
comment on column info_antenna.antenna_id is '天线id';
comment on column info_antenna.antenna_name is '天线名称';
comment on column info_antenna.create_user is '创建人';
comment on column info_antenna.create_username is '创建人名称';
comment on column info_antenna.create_dept is '创建单位';
comment on column info_antenna.create_deptname is '创建单位名称';
comment on column info_antenna.create_time is '创建时间';
comment on column info_antenna.update_user is '更新人';
comment on column info_antenna.update_username is '更新人名称';
comment on column info_antenna.update_dept is '更新单位';
comment on column info_antenna.update_deptname is '更新单位名称';
comment on column info_antenna.update_time is '更新时间';

-- 3. 波束 info_beam
drop table if exists info_beam;
create table info_beam
(
    sat_id            int4   not null,
    beam_id           serial primary key,
    beam_name         varchar(256) not null,
    beam_version      int8 default 0,
    beam_type         int4,
    beam_antenna      int4,
    freq_type         int4,
    longitude         numeric(10, 2),
    latitude          numeric(10, 2),
    radius            numeric(10, 2),
    eirp              numeric(10, 2),
    gt                numeric(10, 2),
    angle_edge        numeric(10, 2),
    angle             int4,
    coverage          text,
    coverage_area     varchar(1024),
    move_flag         int4,
    work_mode         int4,
    create_user       varchar(256),
    create_username   varchar(256),
    create_dept       varchar(256),
    create_deptname   varchar(256),
    create_time       timestamp,
    update_user       varchar(256),
    update_username   varchar(256),
    update_dept       varchar(256),
    update_deptname   varchar(256),
    update_time       timestamp
);
comment on table info_beam is '波束';
comment on column info_beam.sat_id is '卫星id';
comment on column info_beam.beam_id is '波束id';
comment on column info_beam.beam_name is '波束名称';
comment on column info_beam.beam_version is '波束信息版本';
comment on column info_beam.beam_type is '波束类型';
comment on column info_beam.freq_type is '频段类型';
comment on column info_beam.longitude is '经度';
comment on column info_beam.longitude is '经度';
comment on column info_beam.latitude is '纬度';
comment on column info_beam.radius is '半径';
comment on column info_beam.eirp is 'eirp';
comment on column info_beam.gt is 'gt';
comment on column info_beam.angle_edge is '夹角边缘衰减';
comment on column info_beam.angle is '波束夹角(3db)';
comment on column info_beam.coverage is '波束范围';
comment on column info_beam.coverage_area is '覆盖面积(描述性)';
comment on column info_beam.move_flag is '移动标识';
comment on column info_beam.work_mode is '工作模式';
comment on column info_beam.create_user is '创建人';
comment on column info_beam.create_username is '创建人名称';
comment on column info_beam.create_dept is '创建单位';
comment on column info_beam.create_deptname is '创建单位名称';
comment on column info_beam.create_time is '创建时间';
comment on column info_beam.update_user is '更新人';
comment on column info_beam.update_username is '更新人名称';
comment on column info_beam.update_dept is '更新单位';
comment on column info_beam.update_deptname is '更新单位名称';
comment on column info_beam.update_time is '更新时间';

-- 4. 波位 info_beam_position
drop table if exists info_beam_position;
create table info_beam_position
(
    position_id serial primary key,
    position_type    int4 not null,
    longitude        numeric(10, 2),
    latitude         numeric(10, 2),
    coverage         text
);
comment on table info_beam_position is '波位';
comment on column info_beam_position.position_id is '波位id';

-- 5. 通道 info_tunnel
drop table if exists info_tunnel;
create table info_tunnel
(
    sat_id              int4   not null,
    beam_id             int4   not null,
    tunnel_id           serial primary key,
    tunnel_name         varchar(256) not null,
    tunnel_version      int8 default 0,
    tunnel_type         int4,
    freq_type           int4,
    freq_begin          int8 not null,
    freq_end            int8 not null,
    bandwidth           int8,
    usage_unit          int8,
    create_user         varchar(256),
    create_username     varchar(256),
    create_dept         varchar(256),
    create_deptname     varchar(256),
    create_time         timestamp,
    update_user         varchar(256),
    update_username     varchar(256),
    update_dept         varchar(256),
    update_deptname     varchar(256),
    update_time         timestamp
);
comment on table info_tunnel is '波束';
comment on column info_tunnel.sat_id is '卫星id';
comment on column info_tunnel.beam_id is '波束id';
comment on column info_tunnel.tunnel_id is '通道id';
comment on column info_tunnel.tunnel_name is '通道名称';
comment on column info_tunnel.tunnel_version is '通道信息版本';
comment on column info_tunnel.tunnel_type is '通道类型';
comment on column info_tunnel.freq_type is '频段类型';
comment on column info_tunnel.freq_begin is '起始频点【单位:Hz】';
comment on column info_tunnel.freq_end is '终止频点【单位:Hz】';
comment on column info_tunnel.bandwidth is '带宽【单位:Hz】';
comment on column info_tunnel.usage_unit is '最小使用带宽【单位:Hz】';
comment on column info_tunnel.create_user is '创建人';
comment on column info_tunnel.create_username is '创建人名称';
comment on column info_tunnel.create_dept is '创建单位';
comment on column info_tunnel.create_deptname is '创建单位名称';
comment on column info_tunnel.create_time is '创建时间';
comment on column info_tunnel.update_user is '更新人';
comment on column info_tunnel.update_username is '更新人名称';
comment on column info_tunnel.update_dept is '更新单位';
comment on column info_tunnel.update_deptname is '更新单位名称';
comment on column info_tunnel.update_time is '更新时间';

-- 6. 转发器 info_transponder
drop table if exists info_transponder;
create table info_transponder
(
    tspd_id              serial primary key,
    tspd_name            varchar(256) not null,
    tspd_version         int8 default 0,
    tspd_type            int4,
    tspd_status          int2,
    sat_id               int4 not null,
    up_beam_id           int4 not null,
    up_tunnel_id         int4 not null,
    up_polarization      int4,
    down_beam_id         int4 not null,
    down_tunnel_id       int4 not null,
    down_polarization    int4,
    freq_diff            int8,
    sfd                  numeric(18, 2),
    sfp                  numeric(18, 2),
    erip                 numeric(18, 2),
    gt                   numeric(18, 2),
    cbi                  int4,
    cbo                  int4,
    gain                 numeric(18, 2),
    compensate_in        numeric(18, 2),
    compensate_out       numeric(18, 2),
    create_user          varchar(256),
    create_username      varchar(256),
    create_dept          varchar(256),
    create_deptname      varchar(256),
    create_time          timestamp,
    update_user          varchar(256),
    update_username      varchar(256),
    update_dept          varchar(256),
    update_deptname      varchar(256),
    update_time          timestamp
);
create unique index unique_tspd on info_transponder using btree(sat_id, up_beam_id, down_beam_id, up_tunnel_id, down_tunnel_id);
comment on table info_transponder is '转发器';
comment on column info_transponder.tspd_id is '转发器id';
comment on column info_transponder.tspd_name is '转发器名称';
comment on column info_transponder.tspd_version is '转发器信息版本';
comment on column info_transponder.tspd_type is '转发器类型';
comment on column info_transponder.tspd_status is '转发器状态';
comment on column info_transponder.sat_id is '卫星id';
comment on column info_transponder.up_beam_id is '上行波束id';
comment on column info_transponder.up_tunnel_id is '上行通道id';
comment on column info_transponder.up_polarization is '上行极化方式';
comment on column info_transponder.down_beam_id is '下行波束id';
comment on column info_transponder.down_tunnel_id is '下行通道id';
comment on column info_transponder.down_polarization is '下行极化方式';
comment on column info_transponder.freq_diff is '上下行频差';
comment on column info_transponder.sfd is '饱和通量密度：dBW/m2';
comment on column info_transponder.sfp is 'sfp';
comment on column info_transponder.erip is '有效辐射功率：dBW';
comment on column info_transponder.gt is 'G/T值：dB/K';
comment on column info_transponder.cbi is '载波输入回退：dB';
comment on column info_transponder.cbo is '载波输出回退：dB';
comment on column info_transponder.gain is 'gain';
comment on column info_transponder.compensate_in is '输入功率补偿';
comment on column info_transponder.compensate_out is '输出功率补偿';
comment on column info_transponder.create_user is '创建人';
comment on column info_transponder.create_username is '创建人名称';
comment on column info_transponder.create_dept is '创建单位';
comment on column info_transponder.create_deptname is '创建单位名称';
comment on column info_transponder.create_time is '创建时间';
comment on column info_transponder.update_user is '更新人';
comment on column info_transponder.update_username is '更新人名称';
comment on column info_transponder.update_dept is '更新单位';
comment on column info_transponder.update_deptname is '更新单位名称';
comment on column info_transponder.update_time is '更新时间';

drop sequence if exists sequence_src;
create sequence sequence_src increment by 1 minvalue 1 no maxvalue start with 1;

-- 7. 资源池 resource_pool
drop table if exists resource_pool;
create table resource_pool
(
    id                  bigserial primary key,
    src_id              int8,
    src_status          int2 default 0,
    src_type            int2 default 0,
    sat_id              int4,
    sat_name            varchar(256),
    down_beam_id        int4,
    down_beam_name      varchar(256),
    down_tunnel_id      int4,
    down_tunnel_name    varchar(256),
    tspd_id             int4,
    tspd_name           varchar(256),
    up_beam_id          int4,
    up_beam_name        varchar(256),
    up_tunnel_id        int4,
    up_tunnel_name      varchar(256),
    sat_version         int8 default 0,
    tspd_version        int8 default 0,
    up_beam_version     int8 default 0,
    up_tunnel_version   int8 default 0,
    down_beam_version   int8 default 0,
    down_tunnel_version int8 default 0,
    time_begin          timestamp,
    time_end            timestamp,
    timewidth           int8      default -1,
    down_freq_begin     int8      default -1,
    down_freq_end       int8      default -1,
    bandwidth           int8      default -1,
    freq_diff           int8      default -1,
    up_freq_begin       int8      default -1,
    up_freq_end         int8      default -1,
    usage_unit          int8,
    sfd                 numeric(18, 2),
    sfp                 numeric(18, 2),
    erip                numeric(18, 2),
    gt                  numeric(18, 2),
    cbi                 int4,
    cbo                 int4,
    gain                numeric(18, 2),
    compensate_in       numeric(18, 2),
    compensate_out      numeric(18, 2),
    tspd_type           int4,
    down_beam_type      int4,
    down_beam_coverage  text,
    down_tunnel_type    int4,
    down_freq_type      int4,
    down_polarization   int4,
    up_beam_type        int4,
    up_beam_coverage    text,
    up_tunnel_type      int4,
    up_freq_type        int4,
    up_polarization     int4,
    occupy_dept         varchar(64),
    occupy_deptname     varchar(256),
    owner_dept          varchar(64),
    owner_deptname      varchar(256),
    from_dept           varchar(64),
    form_deptname       varchar(256),
    form_time           timestamp,
    form_usage          int8,
    form_flow           varchar(64),
    form_task           varchar(64),
    form_duty           varchar(64),
    create_user         varchar(256),
    create_username     varchar(256),
    create_dept         varchar(256),
    create_deptname     varchar(256),
    create_time         timestamp,
    update_user         varchar(256),
    update_username     varchar(256),
    update_dept         varchar(256),
    update_deptname     varchar(256),
    update_time         timestamp
);
comment on table resource_pool is '资源池';
comment on column resource_pool.id is '主键';
comment on column resource_pool.src_id is '资源id(空闲回收场景,可能会对应多个id)';
comment on column resource_pool.src_status is '资源状态';
comment on column resource_pool.src_type is '资源类型';
comment on column resource_pool.sat_id is '卫星id';
comment on column resource_pool.sat_name is '卫星名称';
comment on column resource_pool.down_beam_id is '下行波束id';
comment on column resource_pool.down_beam_name is '下行波束名称';
comment on column resource_pool.down_tunnel_id is '下行通道id';
comment on column resource_pool.down_tunnel_name is '下行通道名称';
comment on column resource_pool.tspd_id is '转发器id';
comment on column resource_pool.tspd_name is '转发器名称';
comment on column resource_pool.up_beam_id is '上行波束id';
comment on column resource_pool.up_beam_name is '上行波束名称';
comment on column resource_pool.up_tunnel_id is '上行通道id';
comment on column resource_pool.up_tunnel_name is '上行通道名称';
comment on column resource_pool.sat_version is '卫星信息版本';
comment on column resource_pool.tspd_version is '转发器信息版本';
comment on column resource_pool.up_beam_version is '上行波束信息版本';
comment on column resource_pool.up_tunnel_version is '上行通道信息版本';
comment on column resource_pool.down_beam_version is '下行波束信息版本';
comment on column resource_pool.down_tunnel_version is '下行通道信息版本';
comment on column resource_pool.time_begin is '起始时间';
comment on column resource_pool.time_end is '终止时间';
comment on column resource_pool.timewidth is '时宽';
comment on column resource_pool.down_freq_begin is '下行起始频点';
comment on column resource_pool.down_freq_end is '下行终止频点';
comment on column resource_pool.bandwidth is '带宽';
comment on column resource_pool.freq_diff is '频差';
comment on column resource_pool.up_freq_begin is '上行起始频点';
comment on column resource_pool.up_freq_end is '上行终止频点';
comment on column resource_pool.usage_unit is '最小使用单位(子带)';
comment on column resource_pool.sfd is '饱和通量密度：dBW/m2';
comment on column resource_pool.sfp is 'sfp';
comment on column resource_pool.erip is '有效辐射功率：dBW';
comment on column resource_pool.gt is 'G/T值：dB/K';
comment on column resource_pool.cbi is '载波输入回退：dB';
comment on column resource_pool.cbo is '载波输出回退：dB';
comment on column resource_pool.gain is 'gain';
comment on column resource_pool.compensate_in is '输入功率补偿';
comment on column resource_pool.compensate_out is '输出功率补偿';
comment on column resource_pool.tspd_type is '转发器类型';
comment on column resource_pool.down_beam_type is '下行波束类型';
comment on column resource_pool.down_beam_coverage is '下行波束覆盖范围';
comment on column resource_pool.down_tunnel_type is '下行通道类型';
comment on column resource_pool.down_freq_type is '下行频段类型';
comment on column resource_pool.down_polarization is '下行极化方式';
comment on column resource_pool.up_beam_type is '上行波束类型';
comment on column resource_pool.up_beam_coverage is '上行波束覆盖范围';
comment on column resource_pool.up_tunnel_type is '上行通道类型';
comment on column resource_pool.up_freq_type is '上行频段类型';
comment on column resource_pool.up_polarization is '上行极化方式';
comment on column resource_pool.occupy_dept is '占用单位';
comment on column resource_pool.occupy_deptName is '占用单位名称';
comment on column resource_pool.owner_dept is '归属单位';
comment on column resource_pool.owner_deptName is '归属单位名称';
comment on column resource_pool.from_dept is '来源单位 a>b>c a为归属单位 为来源单位';
comment on column resource_pool.form_deptname is '来源单位名称';
comment on column resource_pool.form_time is '来源时间';
comment on column resource_pool.form_usage is '来源记录';
comment on column resource_pool.form_flow is '来源流程';
comment on column resource_pool.form_task is '来源任务';
comment on column resource_pool.form_duty is '来源事务';
comment on column resource_pool.create_user is '创建人';
comment on column resource_pool.create_username is '创建人名称';
comment on column resource_pool.create_dept is '创建单位';
comment on column resource_pool.create_deptname is '创建单位名称';
comment on column resource_pool.create_time is '创建时间';
comment on column resource_pool.update_user is '更新人';
comment on column resource_pool.update_username is '更新人名称';
comment on column resource_pool.update_dept is '更新单位';
comment on column resource_pool.update_deptname is '更新单位名称';
comment on column resource_pool.update_time is '更新时间';

drop sequence if exists sequence_usage;
create sequence sequence_usage increment by 1 minvalue 1 no maxvalue start with 1;

-- 8. 使用记录 resource_usage
drop table if exists resource_usage;
create table resource_usage
(
    id                    bigserial primary key,
    usage_id              int8,
    usage_status          int4      default 100,
    usage_type            int4,
    sat_id                int4,
    sat_name              varchar(256),
    down_beam_id          int4,
    down_beam_name        varchar(256),
    down_tunnel_id        int4,
    down_tunnel_name      varchar(256),
    tspd_id               int4,
    tspd_name             varchar(256),
    up_beam_id            int4,
    up_beam_name          varchar(256),
    up_tunnel_id          int4,
    up_tunnel_name        varchar(256),
    sat_version         int8 default 0,
    tspd_version        int8 default 0,
    up_beam_version     int8 default 0,
    up_tunnel_version   int8 default 0,
    down_beam_version   int8 default 0,
    down_tunnel_version int8 default 0,
    time_begin            timestamp,
    time_end              timestamp,
    timewidth             int8      default -1,
    down_freq_begin       int8      default -1,
    down_freq_end         int8      default -1,
    bandwidth             int8      default -1,
    freq_diff             int8      default -1,
    up_freq_begin         int8      default -1,
    up_freq_end           int8      default -1,
    usage_unit            int8,
    sfd                   numeric(18, 2),
    sfp                   numeric(18, 2),
    erip                  numeric(18, 2),
    gt                    numeric(18, 2),
    cbi                   int4,
    cbo                   int4,
    gain                  numeric(18, 2),
    compensate_in         numeric(18, 2),
    compensate_out        numeric(18, 2),
    tspd_type             int4,
    down_beam_type        int4,
    down_tunnel_type      int4,
    down_freq_type        int4,
    down_polarization     int4,
    up_beam_type          int4,
    up_tunnel_type        int4,
    up_freq_type          int4,
    up_polarization       int4,
    src_id                int8,
    src_type              int2,
    occupy_dept           varchar(64),
    occupy_deptname       varchar(256),
    owner_dept            varchar(64),
    owner_deptName        varchar(256),
    from_dept             varchar(64),
    form_deptname         varchar(256),
    form_time             timestamp,
    form_usage            int8,
    form_flow             varchar(64),
    form_task             varchar(64),
    form_duty             varchar(64),
    merge_id              int8         default -1,
    origin_id             int8         default -1,
    prev_id               int8         default -1,
    prev_usage_id         int8         default -1,
    prev_times            int4         default 0,
    prev_option           int4         default -1,
    prev_status           int4         default -1,
    usage_dept            varchar(64),
    usage_deptname        varchar(256),
    task_id               varchar(64),
    task_name             varchar(256),
    system_id             int4         default -1,
    net_no                varchar(64),
    net_mode              int4         default -1,
    duty_id               varchar(64),
    flow_id               varchar(64),
    plan_id               int8         default -1,
    create_user           varchar(256),
    create_username       varchar(256),
    create_dept           varchar(256),
    create_deptname       varchar(256),
    create_time           timestamp,
    update_user           varchar(256),
    update_username       varchar(256),
    update_dept           varchar(256),
    update_deptname       varchar(256),
    update_time           timestamp
);
comment on table resource_usage is '使用记录';
comment on column resource_usage.id is '主键';
comment on column resource_usage.usage_id is '使用id';
comment on column resource_usage.usage_status is '使用状态';
comment on column resource_usage.usage_type is '使用类型';
comment on column resource_usage.sat_id is '卫星id';
comment on column resource_usage.sat_name is '卫星名称';
comment on column resource_usage.down_beam_id is '下行波束id';
comment on column resource_usage.down_beam_name is '下行波束名称';
comment on column resource_usage.down_tunnel_id is '下行通道id';
comment on column resource_usage.down_tunnel_name is '下行通道名称';
comment on column resource_usage.tspd_id is '转发器id';
comment on column resource_usage.tspd_name is '转发器名称';
comment on column resource_usage.up_beam_id is '上行波束id';
comment on column resource_usage.up_beam_name is '上行波束名称';
comment on column resource_usage.up_tunnel_id is '上行通道id';
comment on column resource_usage.up_tunnel_name is '上行通道名称';
comment on column resource_usage.sat_version is '卫星信息版本';
comment on column resource_usage.tspd_version is '转发器信息版本';
comment on column resource_usage.up_beam_version is '上行波束信息版本';
comment on column resource_usage.up_tunnel_version is '上行通道信息版本';
comment on column resource_usage.down_beam_version is '下行波束信息版本';
comment on column resource_usage.down_tunnel_version is '下行通道信息版本';
comment on column resource_usage.time_begin is '起始时间';
comment on column resource_usage.time_end is '终止时间';
comment on column resource_usage.timewidth is '时宽';
comment on column resource_usage.down_freq_begin is '下行起始频点';
comment on column resource_usage.down_freq_end is '下行终止频点';
comment on column resource_usage.bandwidth is '带宽';
comment on column resource_usage.freq_diff is '频差';
comment on column resource_usage.up_freq_begin is '上行起始频点';
comment on column resource_usage.up_freq_end is '上行终止频点';
comment on column resource_usage.usage_unit is '最小使用单位(子带)';
comment on column resource_usage.sfd is '饱和通量密度：dBW/m2';
comment on column resource_usage.sfp is 'sfp';
comment on column resource_usage.erip is '有效辐射功率：dBW';
comment on column resource_usage.gt is 'G/T值：dB/K';
comment on column resource_usage.cbi is '载波输入回退：dB';
comment on column resource_usage.cbo is '载波输出回退：dB';
comment on column resource_usage.gain is 'gain';
comment on column resource_usage.compensate_in is '输入功率补偿';
comment on column resource_usage.compensate_out is '输出功率补偿';
comment on column resource_usage.tspd_type is '转发器类型';
comment on column resource_usage.down_beam_type is '下行波束类型';
comment on column resource_usage.down_tunnel_type is '下行通道类型';
comment on column resource_usage.down_freq_type is '下行频段类型';
comment on column resource_usage.down_polarization is '下行极化方式';
comment on column resource_usage.up_beam_type is '上行波束类型';
comment on column resource_usage.up_tunnel_type is '上行通道类型';
comment on column resource_usage.up_freq_type is '上行频段类型';
comment on column resource_usage.up_polarization is '上行极化方式';
comment on column resource_usage.src_id is '资源id';
comment on column resource_usage.src_type is '资源类型';
comment on column resource_usage.occupy_dept is '占用单位';
comment on column resource_usage.occupy_deptName is '占用单位名称';
comment on column resource_usage.owner_dept is '归属单位';
comment on column resource_usage.owner_deptName is '归属单位名称';
comment on column resource_usage.from_dept is '来源单位 a>b>c a为归属单位 为来源单位';
comment on column resource_usage.form_deptname is '来源单位名称';
comment on column resource_usage.form_time is '来源时间';
comment on column resource_usage.form_usage is '来源记录';
comment on column resource_usage.form_flow is '来源流程';
comment on column resource_usage.form_task is '来源任务';
comment on column resource_usage.form_duty is '来源事务';
comment on column resource_usage.merge_id is '合并记录id';
comment on column resource_usage.origin_id is '调整原始id';
comment on column resource_usage.prev_id is '调整前id';
comment on column resource_usage.prev_usage_id is '调整前usageId';
comment on column resource_usage.prev_times is '调整次数';
comment on column resource_usage.prev_option is '上一步动作';
comment on column resource_usage.prev_status is '上一步状态';
comment on column resource_usage.usage_dept is '使用单位';
comment on column resource_usage.usage_deptname is '使用单位名称';
comment on column resource_usage.task_id is '任务id';
comment on column resource_usage.task_name is '任务名称';
comment on column resource_usage.system_id is '网系标识';
comment on column resource_usage.net_no is '子网标识';
comment on column resource_usage.net_mode is '组网模式';
comment on column resource_usage.duty_id is '事务id';
comment on column resource_usage.flow_id is '流程id';
comment on column resource_usage.plan_id is '预案id';
comment on column resource_usage.create_user is '创建人';
comment on column resource_usage.create_username is '创建人名称';
comment on column resource_usage.create_dept is '创建单位';
comment on column resource_usage.create_deptname is '创建单位名称';
comment on column resource_usage.create_time is '创建时间';
comment on column resource_usage.update_user is '更新人';
comment on column resource_usage.update_username is '更新人名称';
comment on column resource_usage.update_dept is '更新单位';
comment on column resource_usage.update_deptname is '更新单位名称';
comment on column resource_usage.update_time is '更新时间';

-- 9. 使用历史 history_usage
drop table if exists history_usage;
create table history_usage
(
    id                    bigserial primary key,
    usage_id              int8,
    usage_status          int4      default 100,
    usage_type            int4,
    sat_id                int4,
    sat_name              varchar(256),
    down_beam_id          int4,
    down_beam_name        varchar(256),
    down_tunnel_id        int4,
    down_tunnel_name      varchar(256),
    tspd_id               int4,
    tspd_name             varchar(256),
    up_beam_id            int4,
    up_beam_name          varchar(256),
    up_tunnel_id          int4,
    up_tunnel_name        varchar(256),
    sat_version         int8 default 0,
    tspd_version        int8 default 0,
    up_beam_version     int8 default 0,
    up_tunnel_version   int8 default 0,
    down_beam_version   int8 default 0,
    down_tunnel_version int8 default 0,
    time_begin            timestamp,
    time_end              timestamp,
    timewidth             int8      default -1,
    down_freq_begin       int8      default -1,
    down_freq_end         int8      default -1,
    bandwidth             int8      default -1,
    freq_diff             int8      default -1,
    up_freq_begin         int8      default -1,
    up_freq_end           int8      default -1,
    usage_unit            int8,
    sfd                   numeric(18, 2),
    sfp                   numeric(18, 2),
    erip                  numeric(18, 2),
    gt                    numeric(18, 2),
    cbi                   int4,
    cbo                   int4,
    gain                  numeric(18, 2),
    compensate_in         numeric(18, 2),
    compensate_out        numeric(18, 2),
    tspd_type             int4,
    down_beam_type        int4,
    down_tunnel_type      int4,
    down_freq_type        int4,
    down_polarization     int4,
    up_beam_type          int4,
    up_tunnel_type        int4,
    up_freq_type          int4,
    up_polarization       int4,
    src_id                int8,
    src_type              int2,
    occupy_dept           varchar(64),
    occupy_deptname       varchar(256),
    owner_dept            varchar(64),
    owner_deptName        varchar(256),
    from_dept             varchar(64),
    form_deptname         varchar(256),
    form_time             timestamp,
    form_usage            int8,
    form_flow             varchar(64),
    form_task             varchar(64),
    form_duty             varchar(64),
    merge_id              int8         default -1,
    origin_id             int8         default -1,
    prev_id               int8         default -1,
    prev_usage_id         int8         default -1,
    prev_times            int4         default 0,
    prev_option           int4         default -1,
    prev_status           int4         default -1,
    usage_dept            varchar(64),
    usage_deptname        varchar(256),
    task_id               varchar(64),
    task_name             varchar(256),
    system_id             int4         default -1,
    net_no                varchar(64),
    net_mode              int4         default -1,
    duty_id               varchar(64),
    flow_id               varchar(64),
    plan_id               int8         default -1,
    create_user           varchar(256),
    create_username       varchar(256),
    create_dept           varchar(256),
    create_deptname       varchar(256),
    create_time           timestamp,
    update_user           varchar(256),
    update_username       varchar(256),
    update_dept           varchar(256),
    update_deptname       varchar(256),
    update_time           timestamp
);
comment on table history_usage is '使用历史';
comment on column history_usage.id is '主键';
comment on column history_usage.usage_id is '使用id';
comment on column history_usage.usage_status is '使用状态';
comment on column history_usage.usage_type is '使用类型';
comment on column history_usage.sat_id is '卫星id';
comment on column history_usage.sat_name is '卫星名称';
comment on column history_usage.down_beam_id is '下行波束id';
comment on column history_usage.down_beam_name is '下行波束名称';
comment on column history_usage.down_tunnel_id is '下行通道id';
comment on column history_usage.down_tunnel_name is '下行通道名称';
comment on column history_usage.tspd_id is '转发器id';
comment on column history_usage.tspd_name is '转发器名称';
comment on column history_usage.up_beam_id is '上行波束id';
comment on column history_usage.up_beam_name is '上行波束名称';
comment on column history_usage.up_tunnel_id is '上行通道id';
comment on column history_usage.up_tunnel_name is '上行通道名称';
comment on column history_usage.sat_version is '卫星信息版本';
comment on column history_usage.tspd_version is '转发器信息版本';
comment on column history_usage.up_beam_version is '上行波束信息版本';
comment on column history_usage.up_tunnel_version is '上行通道信息版本';
comment on column history_usage.down_beam_version is '下行波束信息版本';
comment on column history_usage.down_tunnel_version is '下行通道信息版本';
comment on column history_usage.time_begin is '起始时间';
comment on column history_usage.time_end is '终止时间';
comment on column history_usage.timewidth is '时宽';
comment on column history_usage.down_freq_begin is '下行起始频点';
comment on column history_usage.down_freq_end is '下行终止频点';
comment on column history_usage.bandwidth is '带宽';
comment on column history_usage.freq_diff is '频差';
comment on column history_usage.up_freq_begin is '上行起始频点';
comment on column history_usage.up_freq_end is '上行终止频点';
comment on column history_usage.usage_unit is '最小使用单位(子带)';
comment on column history_usage.sfd is '饱和通量密度：dBW/m2';
comment on column history_usage.sfp is 'sfp';
comment on column history_usage.erip is '有效辐射功率：dBW';
comment on column history_usage.gt is 'G/T值：dB/K';
comment on column history_usage.cbi is '载波输入回退：dB';
comment on column history_usage.cbo is '载波输出回退：dB';
comment on column history_usage.gain is 'gain';
comment on column history_usage.compensate_in is '输入功率补偿';
comment on column history_usage.compensate_out is '输出功率补偿';
comment on column history_usage.tspd_type is '转发器类型';
comment on column history_usage.down_beam_type is '下行波束类型';
comment on column history_usage.down_tunnel_type is '下行通道类型';
comment on column history_usage.down_freq_type is '下行频段类型';
comment on column history_usage.down_polarization is '下行极化方式';
comment on column history_usage.up_beam_type is '上行波束类型';
comment on column history_usage.up_tunnel_type is '上行通道类型';
comment on column history_usage.up_freq_type is '上行频段类型';
comment on column history_usage.up_polarization is '上行极化方式';
comment on column history_usage.src_id is '资源id';
comment on column history_usage.src_type is '资源类型';
comment on column history_usage.occupy_dept is '占用单位';
comment on column history_usage.occupy_deptName is '占用单位名称';
comment on column history_usage.owner_dept is '归属单位';
comment on column history_usage.owner_deptName is '归属单位名称';
comment on column history_usage.from_dept is '来源单位 a>b>c a为归属单位 为来源单位';
comment on column history_usage.form_deptname is '来源单位名称';
comment on column history_usage.form_time is '来源时间';
comment on column history_usage.form_usage is '来源记录';
comment on column history_usage.form_flow is '来源流程';
comment on column history_usage.form_task is '来源任务';
comment on column history_usage.form_duty is '来源事务';
comment on column history_usage.merge_id is '合并记录id';
comment on column history_usage.origin_id is '调整原始id';
comment on column history_usage.prev_id is '调整前id';
comment on column history_usage.prev_usage_id is '调整前usageId';
comment on column history_usage.prev_times is '调整次数';
comment on column history_usage.prev_option is '上一步动作';
comment on column history_usage.prev_status is '上一步状态';
comment on column history_usage.usage_dept is '使用单位';
comment on column history_usage.usage_deptname is '使用单位名称';
comment on column history_usage.task_id is '任务id';
comment on column history_usage.task_name is '任务名称';
comment on column history_usage.system_id is '网系标识';
comment on column history_usage.net_no is '子网标识';
comment on column history_usage.net_mode is '组网模式';
comment on column history_usage.duty_id is '事务id';
comment on column history_usage.flow_id is '流程id';
comment on column history_usage.plan_id is '预案id';
comment on column history_usage.create_user is '创建人';
comment on column history_usage.create_username is '创建人名称';
comment on column history_usage.create_dept is '创建单位';
comment on column history_usage.create_deptname is '创建单位名称';
comment on column history_usage.create_time is '创建时间';
comment on column history_usage.update_user is '更新人';
comment on column history_usage.update_username is '更新人名称';
comment on column history_usage.update_dept is '更新单位';
comment on column history_usage.update_deptname is '更新单位名称';
comment on column history_usage.update_time is '更新时间';
