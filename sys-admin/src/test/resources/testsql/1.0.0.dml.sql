--系统配置
INSERT INTO sys_config (config_name, config_key, config_value, value_parser, value_param, is_default, remark, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('账号管理-初始密码', 'sys.user.initPassword', '123456', NULL, NULL, 1, '初始密码', 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_config (config_name, config_key, config_value, value_parser, value_param, is_default, remark, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('主题风格-皮肤样式', 'sys.index.skinName', 'skin-blue', NULL, NULL, 1, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow', 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_config (config_name, config_key, config_value, value_parser, value_param, is_default, remark, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('主题风格-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', NULL, NULL, 0, '深色主题theme-dark，浅色主题theme-light', 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_config (config_name, config_key, config_value, value_parser, value_param, is_default, remark, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('账号自助-开启用户注册', 'sys.account.registerOnOff', 'true', 'com.cowave.commons.framework.helper.dict.DefaultValueParser', 'boolean', 1, '开启注册用户功能 true/false', 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_config (config_name, config_key, config_value, value_parser, value_param, is_default, remark, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('账号自助-验证码开关', 'sys.account.captchaOnOff', 'true', 'com.cowave.commons.framework.helper.dict.DefaultValueParser', 'boolean', 1, '开启验证码功能 true/false', 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_config (config_name, config_key, config_value, value_parser, value_param, is_default, remark, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('账号自助-验证码类型', 'sys.account.captchaType', 'math', NULL, NULL, 1, '验证码类型 math/char', 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');

--菜单数据
INSERT INTO sys_menu (parent_id, menu_name, menu_order, menu_permit, menu_path, menu_param, menu_type, menu_icon, component, menu_status, is_frame, is_cache, is_visible, is_protected, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES (0, 'route.system.title', 1, NULL, 'system', NULL, 'M', 'system', NULL, 1, 1, 1, 1, 1, '系统管理目录', 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2023-08-29 02:04:32.684');
INSERT INTO sys_menu (parent_id, menu_name, menu_order, menu_permit, menu_path, menu_param, menu_type, menu_icon, component, menu_status, is_frame, is_cache, is_visible, is_protected, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES (0, 'route.monitor.title', 2, NULL, 'monitor', NULL, 'M', 'monitor', NULL, 1, 1, 1, 1, 1, '系统监控目录', 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2023-08-09 07:02:51.069');
INSERT INTO sys_menu (parent_id, menu_name, menu_order, menu_permit, menu_path, menu_param, menu_type, menu_icon, component, menu_status, is_frame, is_cache, is_visible, is_protected, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES (0, 'route.tool.title', 3, NULL, 'tool', NULL, 'M', 'tool', NULL, 1, 1, 1, 1, 1, '系统工具目录', 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2023-08-29 02:10:56.743');
INSERT INTO sys_menu (parent_id, menu_name, menu_order, menu_permit, menu_path, menu_param, menu_type, menu_icon, component, menu_status, is_frame, is_cache, is_visible, is_protected, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES (0, 'route.cowave', 9, NULL, 'https://www.cowave.com/', NULL, 'C', 'guide', NULL, 1, 0, 1, 1, 0, '控维官网', 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2023-08-10 06:45:14.541');
INSERT INTO sys_menu (parent_id, menu_name, menu_order, menu_permit, menu_path, menu_param, menu_type, menu_icon, component, menu_status, is_frame, is_cache, is_visible, is_protected, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES (1, 'route.system.user.title', 1, 'sys:user:query', 'user', NULL, 'C', 'user', 'system/user/index', 1, 1, 1, 1, 1, '用户管理菜单', 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2023-08-29 02:04:54.33');
INSERT INTO sys_menu (parent_id, menu_name, menu_order, menu_permit, menu_path, menu_param, menu_type, menu_icon, component, menu_status, is_frame, is_cache, is_visible, is_protected, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES (1, 'route.system.role.title', 4, 'sys:role:query', 'role', NULL, 'C', 'peoples', 'system/role/index', 1, 1, 1, 1, 1, '角色管理菜单', 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2023-08-29 02:07:43.946');
INSERT INTO sys_menu (parent_id, menu_name, menu_order, menu_permit, menu_path, menu_param, menu_type, menu_icon, component, menu_status, is_frame, is_cache, is_visible, is_protected, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES (1, 'route.system.menu.title', 5, 'sys:menu:query', 'menu', NULL, 'C', 'tree-table', 'system/menu/index', 1, 1, 1, 1, 1, '菜单管理菜单', 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_menu (parent_id, menu_name, menu_order, menu_permit, menu_path, menu_param, menu_type, menu_icon, component, menu_status, is_frame, is_cache, is_visible, is_protected, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES (1, 'route.system.dept.title', 2, 'sys:dept:query', 'dept', NULL, 'C', 'tree', 'system/dept/index', 1, 1, 1, 1, 1, '部门管理菜单', 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2023-08-29 02:06:04.739');
INSERT INTO sys_menu (parent_id, menu_name, menu_order, menu_permit, menu_path, menu_param, menu_type, menu_icon, component, menu_status, is_frame, is_cache, is_visible, is_protected, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES (1, 'route.system.post.title', 3, 'sys:post:query', 'post', NULL, 'C', 'post', 'system/post/index', 1, 1, 1, 1, 1, '岗位管理菜单', 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_menu (parent_id, menu_name, menu_order, menu_permit, menu_path, menu_param, menu_type, menu_icon, component, menu_status, is_frame, is_cache, is_visible, is_protected, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES (1, 'route.system.dict.title', 6, 'sys:dict:query', 'dict', NULL, 'C', 'dict', 'system/dict/index', 1, 1, 1, 1, 1, '字典管理菜单', 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_menu (parent_id, menu_name, menu_order, menu_permit, menu_path, menu_param, menu_type, menu_icon, component, menu_status, is_frame, is_cache, is_visible, is_protected, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES (1, 'route.system.config.title', 7, 'sys:config:query', 'config', NULL, 'C', 'edit', 'system/config/index', 1, 1, 1, 1, 1, '参数设置菜单', 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_menu (parent_id, menu_name, menu_order, menu_permit, menu_path, menu_param, menu_type, menu_icon, component, menu_status, is_frame, is_cache, is_visible, is_protected, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES (0, 'route.notice.title', 4, '', 'notice', NULL, 'C', 'message', 'system/notice/index', 1, 1, 1, 1, 0, '通知公告菜单', 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2023-08-15 16:07:25.603');
INSERT INTO sys_menu (parent_id, menu_name, menu_order, menu_permit, menu_path, menu_param, menu_type, menu_icon, component, menu_status, is_frame, is_cache, is_visible, is_protected, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES (2, '系统日志', 5, NULL, 'log', NULL, 'C', 'form', 'monitor/operlog/index', 1, 1, 1, 1, 1, '日志管理菜单', 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_menu (parent_id, menu_name, menu_order, menu_permit, menu_path, menu_param, menu_type, menu_icon, component, menu_status, is_frame, is_cache, is_visible, is_protected, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES (2, '在线用户', 4, 'monitor:online:list', 'online', NULL, 'C', 'online', 'monitor/online/index', 1, 1, 1, 1, 1, '在线用户菜单', 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_menu (parent_id, menu_name, menu_order, menu_permit, menu_path, menu_param, menu_type, menu_icon, component, menu_status, is_frame, is_cache, is_visible, is_protected, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES (0, '定时任务', 5, 'monitor:quartz:query', 'monitor/job', NULL, 'C', 'job', 'monitor/job/index', 1, 1, 1, 1, 1, '定时任务菜单', 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2023-08-10 06:41:40.275');
INSERT INTO sys_menu (parent_id, menu_name, menu_order, menu_permit, menu_path, menu_param, menu_type, menu_icon, component, menu_status, is_frame, is_cache, is_visible, is_protected, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES (2, '缓存监控', 3, 'monitor:cache:list', 'cache', NULL, 'C', 'redis', 'monitor/cache/index', 1, 1, 1, 1, 1, '缓存监控菜单', 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_menu (parent_id, menu_name, menu_order, menu_permit, menu_path, menu_param, menu_type, menu_icon, component, menu_status, is_frame, is_cache, is_visible, is_protected, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES (3, '表单构建', 2, 'tool:build:list', 'build', NULL, 'C', 'build', 'tool/build/index', 1, 1, 1, 1, 1, '表单构建菜单', 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_menu (parent_id, menu_name, menu_order, menu_permit, menu_path, menu_param, menu_type, menu_icon, component, menu_status, is_frame, is_cache, is_visible, is_protected, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES (3, '接口文档', 1, 'tool:smart:list', 'smart', NULL, 'M', 'swagger', '', 1, 1, 1, 1, 1, '系统接口菜单', 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');

--字典数据
INSERT INTO sys_dict (parent_code, dict_code, dict_label, dict_en, dict_value, value_parser, value_param, dict_order, is_default, css, status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('dict_root', 'dict_group', '字典分组', 'Dict Group', 'dict_group', NULL, NULL, 0, 0, NULL, 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_dict (parent_code, dict_code, dict_label, dict_en, dict_value, value_parser, value_param, dict_order, is_default, css, status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('dict_group', 'dict_sys', '系统字典', 'sys dict', 'dict_sys', NULL, NULL, 1, 0, NULL, 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_dict (parent_code, dict_code, dict_label, dict_en, dict_value, value_parser, value_param, dict_order, is_default, css, status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('dict_sys', 'sys_is_success', '是否成功', 'Success/Failed', 'sys_is_success', NULL, NULL, 2, 0, NULL, 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_dict (parent_code, dict_code, dict_label, dict_en, dict_value, value_parser, value_param, dict_order, is_default, css, status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('dict_sys', 'sys_is_enable', '是否启用', 'Enable/Disable', 'sys_is_enable', NULL, NULL, 2, 0, NULL, 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_dict (parent_code, dict_code, dict_label, dict_en, dict_value, value_parser, value_param, dict_order, is_default, css, status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('dict_sys', 'sys_is_protected', '是否受保护的', 'Public/Protected', 'sys_is_protected', NULL, NULL, 2, 0, NULL, 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_dict (parent_code, dict_code, dict_label, dict_en, dict_value, value_parser, value_param, dict_order, is_default, css, status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('dict_sys', 'sys_yes_no', '是否', 'Yes/No', 'sys_yes_no', NULL, NULL, 2, 0, NULL, 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_dict (parent_code, dict_code, dict_label, dict_en, dict_value, value_parser, value_param, dict_order, is_default, css, status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('dict_sys', 'sys_show_hide', '显示隐藏', 'Show/Hide', 'sys_show_hide', NULL, NULL, 2, 0, NULL, 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_dict (parent_code, dict_code, dict_label, dict_en, dict_value, value_parser, value_param, dict_order, is_default, css, status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('sys_is_success', 'sys_success', '成功', 'Success', '1', 'com.cowave.commons.framework.helper.dict.DefaultValueParser', 'int', 16, 1, 'success', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2023-08-11 14:22:07.256');
INSERT INTO sys_dict (parent_code, dict_code, dict_label, dict_en, dict_value, value_parser, value_param, dict_order, is_default, css, status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('sys_is_success', 'sys_failed', '失败', 'Failed', '0', 'com.cowave.commons.framework.helper.dict.DefaultValueParser', 'int', 17, 0, 'danger', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_dict (parent_code, dict_code, dict_label, dict_en, dict_value, value_parser, value_param, dict_order, is_default, css, status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('sys_is_enable', 'sys_enable', '启用', 'Enable', '1', 'com.cowave.commons.framework.helper.dict.DefaultValueParser', 'int', 18, 1, 'primary', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_dict (parent_code, dict_code, dict_label, dict_en, dict_value, value_parser, value_param, dict_order, is_default, css, status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('sys_is_enable', 'sys_disable', '停用', 'Disable', '0', 'com.cowave.commons.framework.helper.dict.DefaultValueParser', 'int', 19, 0, 'danger', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_dict (parent_code, dict_code, dict_label, dict_en, dict_value, value_parser, value_param, dict_order, is_default, css, status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('sys_is_protected', 'sys_protected', '受保护的', 'Protected', '1', 'com.cowave.commons.framework.helper.dict.DefaultValueParser', 'int', 21, 1, 'primary', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_dict (parent_code, dict_code, dict_label, dict_en, dict_value, value_parser, value_param, dict_order, is_default, css, status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('sys_is_protected', 'sys_public', '公开的', 'Public', '0', 'com.cowave.commons.framework.helper.dict.DefaultValueParser', 'int', 20, 0, 'success', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_dict (parent_code, dict_code, dict_label, dict_en, dict_value, value_parser, value_param, dict_order, is_default, css, status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('sys_yes_no', 'sys_yes', '是', 'Yes', '1', 'com.cowave.commons.framework.helper.dict.DefaultValueParser', 'int', 22, 1, 'primary', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_dict (parent_code, dict_code, dict_label, dict_en, dict_value, value_parser, value_param, dict_order, is_default, css, status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('sys_yes_no', 'sys_no', '否', 'No', '0', 'com.cowave.commons.framework.helper.dict.DefaultValueParser', 'int', 23, 0, 'danger', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_dict (parent_code, dict_code, dict_label, dict_en, dict_value, value_parser, value_param, dict_order, is_default, css, status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('sys_show_hide', 'sys_show', '显示', 'Show', '1', 'com.cowave.commons.framework.helper.dict.DefaultValueParser', 'int', 24, 1, 'primary', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_dict (parent_code, dict_code, dict_label, dict_en, dict_value, value_parser, value_param, dict_order, is_default, css, status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('sys_show_hide', 'sys_hide', '隐藏', 'Hide', '0', 'com.cowave.commons.framework.helper.dict.DefaultValueParser', 'int', 25, 0, 'danger', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');

--部门数据
INSERT INTO sys_dept (dept_name, dept_short, dept_addr, dept_phone, remark, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES
('控维通信', NULL, NULL, '15888888888', NULL, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00'),
('南京总公司', NULL, NULL, '15888888888', NULL, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00'),
('北京分公司', NULL, NULL, '15888888888', NULL, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00'),
('研发部门', NULL, NULL, '15888888888', NULL, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00'),
('销售部门', NULL, NULL, '15888888888', NULL, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00'),
('财务部门', NULL, NULL, '15888888888', NULL, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00'),
('人事部', NULL, NULL, '15888888888', NULL, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00'),
('行政部门', NULL, NULL, '15888888888', NULL, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00'),
('运营部门', NULL, NULL, '15888888888', NULL, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00'),
('市场部门', NULL, NULL, '15888888888', NULL, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00'),
('销售部门[北京]', NULL, NULL, '15888888888', NULL, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00'),
('市场部门[北京]', NULL, NULL, '15888888888', NULL, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00'),
('公共部门', NULL, NULL, '15888888888', NULL, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');


--岗位数据
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('总经理', 1, 'M', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('CTO技术总监', 1, 'M', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('CEO行政总监', 1, 'M', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('CFO财务总监', 1, 'M', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('COS销售总监', 1, 'M', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('COO运营总监', 1, 'M', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('CHO人力资源总监', 1, 'M', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('前台接待', 1, 'A', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('行政专员', 1, 'A', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('行政主管', 1, 'A', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('市场专员', 1, 'A', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('运营经理', 1, 'A', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('销售专员', 1, 'S', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('销售经理', 1, 'S', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('招聘专员', 1, 'A', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('招聘主管', 1, 'A', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('出纳员', 1, 'F', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('会计师', 1, 'F', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('研发主管', 1, 'M', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('产品经理', 1, 'M', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('项目经理', 1, 'M', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('系统架构师', 1, 'T', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('硬件工程师', 1, 'T', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('运维工程师', 1, 'T', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('测试工程师', 1, 'T', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('嵌入式工程师', 1, 'T', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('UI设计师', 1, 'T', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('前端工程师', 1, 'T', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('Python工程师', 1, 'T', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_post (post_name, post_level, post_type, post_status, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('Java工程师', 1, 'T', 1, NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');

--角色数据
INSERT INTO sys_role (role_code, role_name, role_type, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('sysAdmin', '系统管理员', 'sys', NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_role (role_code, role_name, role_type, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('flowAdmin', '流程管理员', 'flow', NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_role (role_code, role_name, role_type, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('common', '普通角色', 'sys', NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');

--用户数据
INSERT INTO sys_user (user_name, user_account, user_passwd, user_sex, user_phone, user_email, user_status, rank, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('系统管理员', 'sysAdmin', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 0, NULL, 'sysAdmin@cowave.com', 1, NULL, NULL, 1, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_user (user_name, user_account, user_passwd, user_sex, user_phone, user_email, user_status, rank, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('赵云', 'zhaoyun', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 0, '13288888888', 'zhaoyun@cowave.com', 1, 'M7', NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_user (user_name, user_account, user_passwd, user_sex, user_phone, user_email, user_status, rank, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('黄忠', 'huangzhong', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 0, '13288888888', 'huangzhong@cowave.com', 1, 'M7', NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_user (user_name, user_account, user_passwd, user_sex, user_phone, user_email, user_status, rank, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('刘备', 'liubei', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 0, '13288888888', 'liubei@cowave.com', 1, 'M7', NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_user (user_name, user_account, user_passwd, user_sex, user_phone, user_email, user_status, rank, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('诸葛亮', 'zhugeliang', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 0, '13288888888', 'zhugeliang@cowave.com', 1, 'M7', NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_user (user_name, user_account, user_passwd, user_sex, user_phone, user_email, user_status, rank, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('关羽', 'guanyu', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 0, '13288888888', 'guanyu@cowave.com', 1, 'M7', NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_user (user_name, user_account, user_passwd, user_sex, user_phone, user_email, user_status, rank, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('张飞', 'zhangfei', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 0, '13288888888', 'zhangfei@cowave.com', 1, 'M7', NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO sys_user (user_name, user_account, user_passwd, user_sex, user_phone, user_email, user_status, rank, remark, read_only, create_user, create_dept, create_time, update_user, update_dept, update_time) VALUES ('马超', 'machao', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 0, '13288888888', 'machao@cowave.com', 1, 'M7', NULL, 0, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');

--部门岗位
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (1, 1);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (1, 2);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (1, 3);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (1, 4);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (1, 5);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (1, 6);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (1, 7);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (8, 8);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (8, 9);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (8, 10);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (9, 12);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (5, 13);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (5, 14);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (7, 15);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (7, 16);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (6, 17);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (6, 18);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (4, 19);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (4, 20);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (4, 21);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (4, 22);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (4, 23);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (4, 24);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (4, 25);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (4, 26);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (4, 27);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (4, 28);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (4, 29);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (4, 30);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (10, 11);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (12, 11);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (11, 13);
INSERT INTO sys_dept_post (dept_id, post_id) VALUES (11, 14);

--部门关系
INSERT INTO sys_dept_tree (dept_id, parent_id, tree_type) VALUES (1, 0, 1);
INSERT INTO sys_dept_tree (dept_id, parent_id, tree_type) VALUES (2, 1, 1);
INSERT INTO sys_dept_tree (dept_id, parent_id, tree_type) VALUES (3, 1, 1);
INSERT INTO sys_dept_tree (dept_id, parent_id, tree_type) VALUES (4, 2, 1);
INSERT INTO sys_dept_tree (dept_id, parent_id, tree_type) VALUES (5, 2, 1);
INSERT INTO sys_dept_tree (dept_id, parent_id, tree_type) VALUES (6, 2, 1);
INSERT INTO sys_dept_tree (dept_id, parent_id, tree_type) VALUES (7, 2, 1);
INSERT INTO sys_dept_tree (dept_id, parent_id, tree_type) VALUES (8, 2, 1);
INSERT INTO sys_dept_tree (dept_id, parent_id, tree_type) VALUES (9, 2, 1);
INSERT INTO sys_dept_tree (dept_id, parent_id, tree_type) VALUES (10, 2, 1);
INSERT INTO sys_dept_tree (dept_id, parent_id, tree_type) VALUES (13, 2, 1);
INSERT INTO sys_dept_tree (dept_id, parent_id, tree_type) VALUES (11, 3, 1);
INSERT INTO sys_dept_tree (dept_id, parent_id, tree_type) VALUES (12, 3, 1);
INSERT INTO sys_dept_tree (dept_id, parent_id, tree_type) VALUES (13, 3, 1);

--岗位关系
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (2, 1, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (3, 1, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (4, 1, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (5, 1, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (6, 1, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (7, 1, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (10, 3, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (12, 6, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (14, 5, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (16, 7, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (17, 4, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (18, 4, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (19, 2, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (1, 0, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (15, 16, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (11, 12, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (13, 14, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (8, 10, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (9, 10, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (27, 21, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (28, 21, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (29, 21, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (30, 21, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (21, 19, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (20, 19, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (25, 21, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (22, 19, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (23, 21, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (24, 21, 1);
INSERT INTO sys_post_tree (post_id, parent_id, tree_type)VALUES (26, 21, 1);

--用户部门岗位
INSERT INTO sys_user_dept (id, user_id, dept_id, post_id, is_default, is_leader) VALUES (1, 2, 1, 1, 1, 1);
INSERT INTO sys_user_dept (id, user_id, dept_id, post_id, is_default, is_leader) VALUES (2, 3, 1, 2, 0, 0);
INSERT INTO sys_user_dept (id, user_id, dept_id, post_id, is_default, is_leader) VALUES (3, 4, 1, 3, 0, 0);
INSERT INTO sys_user_dept (id, user_id, dept_id, post_id, is_default, is_leader) VALUES (4, 5, 1, 6, 0, 0);
INSERT INTO sys_user_dept (id, user_id, dept_id, post_id, is_default, is_leader) VALUES (6, 7, 1, 4, 0, 0);
INSERT INTO sys_user_dept (id, user_id, dept_id, post_id, is_default, is_leader) VALUES (5, 6, 1, 7, 0, 0);
INSERT INTO sys_user_dept (id, user_id, dept_id, post_id, is_default, is_leader) VALUES (7, 8, 1, 5, 0, 0);
INSERT INTO sys_user_dept (id, user_id, dept_id, post_id, is_default, is_leader) VALUES (8, 3, 4, 19, 1, 1);
INSERT INTO sys_user_dept (id, user_id, dept_id, post_id, is_default, is_leader) VALUES (9, 4, 8, 10, 1, 1);
INSERT INTO sys_user_dept (id, user_id, dept_id, post_id, is_default, is_leader) VALUES (10, 5, 9, 12, 1, 1);
INSERT INTO sys_user_dept (id, user_id, dept_id, post_id, is_default, is_leader) VALUES (11, 7, 6, 18, 1, 1);
INSERT INTO sys_user_dept (id, user_id, dept_id, post_id, is_default, is_leader) VALUES (12, 6, 7, 16, 1, 1);
INSERT INTO sys_user_dept (id, user_id, dept_id, post_id, is_default, is_leader) VALUES (13, 8, 5, 14, 1, 1);

SELECT setval('sys_user_dept_id_seq', (SELECT max(id) FROM sys_user_dept));

--用户角色
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 2);
INSERT INTO sys_user_role (user_id, role_id) VALUES (2, 3);
INSERT INTO sys_user_role (user_id, role_id) VALUES (3, 3);
INSERT INTO sys_user_role (user_id, role_id) VALUES (4, 3);
INSERT INTO sys_user_role (user_id, role_id) VALUES (5, 3);
INSERT INTO sys_user_role (user_id, role_id) VALUES (6, 3);
INSERT INTO sys_user_role (user_id, role_id) VALUES (7, 3);
INSERT INTO sys_user_role (user_id, role_id) VALUES (8, 3);

--用户关系
INSERT INTO sys_user_tree (user_id, parent_id, tree_type) VALUES (2, 0, 1);
INSERT INTO sys_user_tree (user_id, parent_id, tree_type) VALUES (3, 2, 1);
INSERT INTO sys_user_tree (user_id, parent_id, tree_type) VALUES (4, 2, 1);
INSERT INTO sys_user_tree (user_id, parent_id, tree_type) VALUES (5, 2, 1);
INSERT INTO sys_user_tree (user_id, parent_id, tree_type) VALUES (6, 2, 1);
INSERT INTO sys_user_tree (user_id, parent_id, tree_type) VALUES (7, 2, 1);
INSERT INTO sys_user_tree (user_id, parent_id, tree_type) VALUES (8, 2, 1);

--普通角色权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (3, 1);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (3, 5);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (3, 8);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (3, 9);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (3, 6);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (3, 7);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (3, 10);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (3, 11);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (3, 2);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (3, 14);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (3, 13);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (3, 15);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (3, 22);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (3, 3);
INSERT INTO sys_role_menu (role_id, menu_id) VALUES (3, 21);

INSERT INTO sys_notice(notice_title,content)VALUES ('x', 'xx');
