--系统配置
INSERT INTO "hub_config" ("config_id", "tenant_id", "config_name", "config_key", "config_value", "value_parser", "value_type", "is_default", "remark", "create_by", "create_time", "update_by", "update_time") VALUES
(1, '#', '账号管理-初始密码', 'hub.initPassword', '123456', NULL, NULL, 1, '初始密码', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(2, '#', '账号自助-开启用户注册', 'hub.registerOnOff', 'true', NULL, 'bool', 1, '开启注册用户功能 true/false', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(3, '#', '账号自助-验证码开关', 'hub.captchaOnOff', 'true', NULL, 'bool', 1, '开启验证码功能 true/false', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(4, '#', '账号自助-验证码类型', 'hub.captchaType', 'math', NULL, NULL, 1, '验证码类型 math/char', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(5, 'system', '账号管理-初始密码', 'hub.initPassword', '123456', NULL, NULL, 1, '初始密码', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(6, 'system', '账号自助-开启用户注册', 'hub.registerOnOff', 'true', NULL, 'bool', 1, '开启注册用户功能 true/false', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(7, 'system', '账号自助-验证码开关', 'hub.captchaOnOff', 'true', NULL, 'bool', 1, '开启验证码功能 true/false', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(8, 'system', '账号自助-验证码类型', 'hub.captchaType', 'math', NULL, NULL, 1, '验证码类型 math/char', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(9, 'cowave', '账号管理-初始密码', 'hub.initPassword', '123456', NULL, NULL, 1, '初始密码', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(10, 'cowave', '账号自助-开启用户注册', 'hub.registerOnOff', 'true', NULL, 'bool', 1, '开启注册用户功能 true/false', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(11, 'cowave', '账号自助-验证码开关', 'hub.captchaOnOff', 'true', NULL, 'bool', 1, '开启验证码功能 true/false', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
(12, 'cowave', '账号自助-验证码类型', 'hub.captchaType', 'math', NULL, NULL, 1, '验证码类型 math/char', NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');
SELECT setval('hub_config_config_id_seq', (SELECT max(config_id) FROM hub_config));

--字典数据
INSERT INTO "hub_dict" ("parent_code", "dict_code", "dict_name", "dict_value", "value_parser", "value_type", "dict_order", "is_default", "css", "status", "remark", "create_by", "create_time", "update_by", "update_time") VALUES
('root', 'group', 'dict.name.root', 'group', NULL, NULL, 0, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

('group', 'domain_module', 'dict.name.domain_module', NULL, NULL, NULL, 0, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

('domain_module', 'domain_system', 'dict.name.domain_system', NULL, NULL, NULL, 1, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_auth', 'dict.name.module_auth', NULL, NULL, NULL, 1, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_tenant', 'dict.name.module_tenant', NULL, NULL, NULL, 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_user', 'dict.name.module_user', NULL, NULL, NULL, 3, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_role', 'dict.name.module_role', NULL, NULL, NULL, 4, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_dept', 'dict.name.module_dept', NULL, NULL, NULL, 5, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_post', 'dict.name.module_post', NULL, NULL, NULL, 6, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_menu', 'dict.name.module_menu', NULL, NULL, NULL, 7, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_scope', 'dict.name.module_scope', NULL, NULL, NULL, 8, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_dict', 'dict.name.module_dict', NULL, NULL, NULL, 9, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_config', 'dict.name.module_config', NULL, NULL, NULL, 10, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_task', 'dict.name.module_task', NULL, NULL, NULL, 11, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_ldap', 'dict.name.module_ldap', NULL, NULL, NULL, 12, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_oauth', 'dict.name.module_oauth', NULL, NULL, NULL, 13, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_notice', 'dict.name.module_notice', NULL, NULL, NULL, 14, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_system', 'module_attach', 'dict.name.module_attach', NULL, NULL, NULL, 15, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

('domain_module', 'domain_monitor', 'dict.name.domain_monitor', NULL, NULL, NULL, 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_monitor', 'module_online', 'dict.name.module_online', NULL, NULL, NULL, 1, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('domain_monitor', 'module_oplog', 'dict.name.module_oplog', NULL, NULL, NULL, 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

('domain_module', 'domain_flow', 'dict.name.domain_flow', NULL, NULL, NULL, 3, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

('domain_module', 'domain_meter', 'dict.name.domain_meter', NULL, NULL, NULL, 4, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

('group', 'sys', 'dict.name.sys', NULL, NULL, NULL, 1, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

('sys', 'op_action', 'dict.name.op_action', NULL, NULL, NULL, 50, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('op_action', 'op_create', 'dict.name.op_create', NULL, NULL, NULL, 1, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('op_action', 'op_delete', 'dict.name.op_delete', NULL, NULL, NULL, 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('op_action', 'op_edit', 'dict.name.op_edit', NULL, NULL, NULL, 3, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('op_action', 'op_status', 'dict.name.op_status', NULL, NULL, NULL, 4, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('op_action', 'op_grant', 'dict.name.op_grant', NULL, NULL, NULL, 4, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('op_action', 'op_passwd', 'dict.name.op_passwd', NULL, NULL, NULL, 4, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('op_action', 'op_login', 'dict.name.op_login', NULL, NULL, NULL, 6, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('op_action', 'op_login_oauth', 'dict.name.op_login_oauth', NULL, NULL, NULL, 7, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('op_action', 'op_logout', 'dict.name.op_logout', NULL, NULL, NULL, 8, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('op_action', 'op_logout_force', 'dict.name.op_logout_force', NULL, NULL, NULL, 9, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

('sys', 'attach_type', 'dict.name.attach_type', NULL, NULL, NULL, 80, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('attach_type', 'image', 'dict.name.image', NULL, NULL, NULL, 1, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('attach_type', 'avatar', 'dict.name.avatar', NULL, NULL, NULL, 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('attach_type', 'logo', 'dict.name.logo', NULL, NULL, NULL, 3, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

('group', 'flow', 'dict.name.flow', NULL, NULL, NULL, 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

('flow', 'leave', 'dict.name.leave', NULL, NULL, NULL, 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('leave', 'annual', 'dict.name.annual', '1', NULL, 'int32', 1, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('leave', 'personal', 'dict.name.personal', '2', NULL, 'int32', 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('leave', 'sick', 'dict.name.sick', '3', NULL, 'int32', 3, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('leave', 'bereavement', 'dict.name.bereavement', '4', NULL, 'int32', 4, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('leave', 'maternity', 'dict.name.maternity', '5', NULL, 'int32', 5, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

('group', 'user', 'dict.name.user', NULL, NULL, NULL, 3, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

('user', 'post_type', 'dict.name.post_type', NULL, NULL, NULL, 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_type', 'M', 'dict.name.M', NULL, NULL, NULL, 1, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_type', 'T', 'dict.name.T', NULL, NULL, NULL, 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_type', 'A', 'dict.name.A', NULL, NULL, NULL, 3, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_type', 'S', 'dict.name.S', NULL, NULL, NULL, 4, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_type', 'F', 'dict.name.F', NULL, NULL, NULL, 5, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

('user', 'post_level', 'dict.name.post_level', NULL, NULL, NULL, 3, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'T0', 'dict.name.T0', NULL, NULL, NULL, 1, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'T1', 'dict.name.T1', NULL, NULL, NULL, 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'T2', 'dict.name.T2', NULL, NULL, NULL, 3, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'T3', 'dict.name.T3', NULL, NULL, NULL, 4, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'T4', 'dict.name.T4', NULL, NULL, NULL, 5, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'T5', 'dict.name.T5', NULL, NULL, NULL, 6, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'T6', 'dict.name.T6', NULL, NULL, NULL, 7, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'T7', 'dict.name.T7', NULL, NULL, NULL, 8, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'M0', 'dict.name.M0', NULL, NULL, NULL, 9, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'M1', 'dict.name.M1', NULL, NULL, NULL, 10, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'M2', 'dict.name.M2', NULL, NULL, NULL, 11, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'M3', 'dict.name.M3', NULL, NULL, NULL, 12, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'M4', 'dict.name.M4', NULL, NULL, NULL, 13, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'M5', 'dict.name.M5', NULL, NULL, NULL, 14, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'M6', 'dict.name.M6', NULL, NULL, NULL, 15, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('post_level', 'M7', 'dict.name.M7', NULL, NULL, NULL, 16, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

('group', 'job', 'dict.name.job', NULL, NULL, NULL, 5, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

('job', 'job_task', '任务类型', NULL, NULL, NULL, 1, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_task', 'job_task_bean', 'Java实例', 'BEAN', NULL, NULL, 1, 1, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_task', 'job_task_groovy', 'Groovy脚本', 'GROOVY', NULL, NULL, 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_task', 'job_task_python', 'Python脚本', 'PYTHON', NULL, NULL, 3, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_task', 'job_task_php', 'Php脚本', 'PHP', NULL, NULL, 4, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_task', 'job_task_nodejs', 'Nodejs脚本', 'NODEJS', NULL, NULL, 5, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_task', 'job_task_shell', 'Shell脚本', 'SHELL', NULL, NULL, 6, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

('job', 'job_route', '路由策略', NULL, NULL, NULL, 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_route', 'job_route_round', '轮询', 'ROUND', NULL, NULL, 1, 1, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_route', 'job_route_random', '随机', 'RANDOM', NULL, NULL, 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_route', 'job_route_hash', 'Hash散列', 'CONSISTENT_HASH', NULL, NULL, 3, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_route', 'job_route_fail', '故障转移', 'FAIL_OVER', NULL, NULL, 4, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_route', 'job_route_busy', '忙碌转移', 'BUSY_OVER', NULL, NULL, 5, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_route', 'job_route_shard', '分片广播', 'SHARDING_BROADCAST', NULL, NULL, 6, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_route', 'job_route_lfu', '最不经常使用', 'LEAST_FREQUENTLY_USED', NULL, NULL, 7, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_route', 'job_route_lru', '最久未使用', 'LEAST_RECENTLY_USED', NULL, NULL, 8, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_route', 'job_route_first', '取第一个', 'FIRST', NULL, NULL, 9, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_route', 'job_route_last', '取最后一个', 'LAST', NULL, NULL, 10, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

('job', 'job_block', '阻塞策略', NULL, NULL, NULL, 3, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_block', 'job_block_serial', '单机串行', 'SERIAL_EXECUTION', NULL, NULL, 1, 1, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_block', 'job_block_discard', '丢弃后续任务', 'DISCARD_LATER', NULL, NULL, 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_block', 'job_block_cover', '覆盖之前任务', 'COVER_EARLY', NULL, NULL, 3, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),

('job', 'job_misfire', '过期策略', NULL, NULL, NULL, 4, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_misfire', 'job_misfire_ignore', '忽略', 'IGNORE', NULL, NULL, 1, 1, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00'),
('job_misfire', 'job_misfire_fire', '立即执行', 'FIRE_NOW', NULL, NULL, 2, 0, NULL, 1, NULL, NULL, '2022-04-25 09:00:00', NULL, '2022-04-25 09:00:00');
