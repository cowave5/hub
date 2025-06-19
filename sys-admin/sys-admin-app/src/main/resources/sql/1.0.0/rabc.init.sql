--部门数据
INSERT INTO "sys_dept" ("dept_id", "dept_code", "dept_name", "dept_short", "dept_addr", "dept_phone", "remark", "create_by", "create_time", "update_by", "update_time") VALUES
(1, 'Cowave', '控维通信', NULL, NULL, '15888888888', NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(2, NULL, '南京总公司', NULL, NULL, '15888888888', NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(3, NULL, '北京分公司', NULL, NULL, '15888888888', NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(4, NULL, '研发部门', NULL, NULL, '15888888888', NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(6, 'FD', '财务部门', NULL, NULL, '15888888888', NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(8, NULL, '行政部门', NULL, NULL, '15888888888', NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(10, NULL, '市场部门', NULL, NULL, '15888888888', NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(5, NULL, '销售部门', NULL, NULL, '15888888888', NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(7, 'HR', '人事部', NULL, NULL, '15888888888', NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(9, NULL, '运营部门', NULL, NULL, '15888888888', NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(11, NULL, '销售部门[北京]', NULL, NULL, '15888888888', NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(12, NULL, '市场部门[北京]', NULL, NULL, '15888888888', NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(13, NULL, '公共部门', NULL, NULL, '15888888888', NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00');
SELECT setval('sys_dept_dept_id_seq', (SELECT max(dept_id) FROM sys_dept));

--岗位数据
INSERT INTO "sys_post" ("post_id", "post_code", "post_name", "post_level", "post_type", "post_status", "remark", "create_by", "create_time", "update_by", "update_time") VALUES
(17, 'AC', '出纳员', 1, 'F', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(18, 'ACCT', '会计师', 1, 'F', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(1, 'GM', '总经理', 1, 'M', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(2, 'CTO', 'CTO技术总监', 1, 'M', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(3, 'CEO', 'CEO行政总监', 1, 'M', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(4, 'CFO', 'CFO财务总监', 1, 'M', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(5, 'COS', 'COS销售总监', 1, 'M', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(6, 'COO', 'COO运营总监', 1, 'M', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(7, 'CHO', 'CHO人力资源总监', 1, 'M', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(19, NULL, '研发主管', 1, 'M', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(20, NULL, '产品经理', 1, 'M', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(21, NULL, '项目经理', 1, 'M', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(22, NULL, '系统架构师', 1, 'T', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(23, NULL, '硬件工程师', 1, 'T', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(24, NULL, '运维工程师', 1, 'T', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(25, NULL, '测试工程师', 1, 'T', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(26, NULL, '嵌入式工程师', 1, 'T', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(27, NULL, 'UI设计师', 1, 'T', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(28, NULL, '前端工程师', 1, 'T', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(29, NULL, 'Python工程师', 1, 'T', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(30, NULL, 'Java工程师', 1, 'T', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(8, NULL, '前台接待', 1, 'A', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(9, NULL, '行政专员', 1, 'A', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(10, NULL, '行政主管', 1, 'A', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(13, NULL, '销售专员', 1, 'S', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(14, NULL, '销售经理', 1, 'S', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(15, NULL, '招聘专员', 1, 'A', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(16, NULL, '招聘主管', 1, 'A', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(11, NULL, '市场专员', 1, 'A', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(12, NULL, '运营经理', 1, 'A', 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00');
SELECT setval('sys_post_post_id_seq', (SELECT max(post_id) FROM sys_post));

--部门岗位
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (1, 1);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (1, 2);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (1, 3);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (1, 4);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (1, 5);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (1, 6);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (1, 7);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (8, 8);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (8, 9);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (8, 10);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (9, 12);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (5, 13);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (5, 14);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (7, 15);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (7, 16);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (6, 17);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (6, 18);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (4, 19);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (4, 20);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (4, 21);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (4, 22);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (4, 23);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (4, 24);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (4, 25);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (4, 26);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (4, 27);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (4, 28);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (4, 29);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (4, 30);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (10, 11);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (12, 11);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (11, 13);
INSERT INTO "sys_dept_post" ("dept_id", "post_id") VALUES (11, 14);

--部门关系
INSERT INTO "sys_dept_tree" ("dept_id", "parent_id", "tree_type") VALUES (1, 0, 1);
INSERT INTO "sys_dept_tree" ("dept_id", "parent_id", "tree_type") VALUES (2, 1, 1);
INSERT INTO "sys_dept_tree" ("dept_id", "parent_id", "tree_type") VALUES (3, 1, 1);
INSERT INTO "sys_dept_tree" ("dept_id", "parent_id", "tree_type") VALUES (4, 2, 1);
INSERT INTO "sys_dept_tree" ("dept_id", "parent_id", "tree_type") VALUES (5, 2, 1);
INSERT INTO "sys_dept_tree" ("dept_id", "parent_id", "tree_type") VALUES (6, 2, 1);
INSERT INTO "sys_dept_tree" ("dept_id", "parent_id", "tree_type") VALUES (7, 2, 1);
INSERT INTO "sys_dept_tree" ("dept_id", "parent_id", "tree_type") VALUES (8, 2, 1);
INSERT INTO "sys_dept_tree" ("dept_id", "parent_id", "tree_type") VALUES (9, 2, 1);
INSERT INTO "sys_dept_tree" ("dept_id", "parent_id", "tree_type") VALUES (10, 2, 1);
INSERT INTO "sys_dept_tree" ("dept_id", "parent_id", "tree_type") VALUES (13, 2, 1);
INSERT INTO "sys_dept_tree" ("dept_id", "parent_id", "tree_type") VALUES (11, 3, 1);
INSERT INTO "sys_dept_tree" ("dept_id", "parent_id", "tree_type") VALUES (12, 3, 1);
INSERT INTO "sys_dept_tree" ("dept_id", "parent_id", "tree_type") VALUES (13, 3, 1);

--岗位关系
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (2, 1, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (3, 1, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (4, 1, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (5, 1, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (6, 1, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (7, 1, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (10, 3, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (12, 6, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (14, 5, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (16, 7, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (17, 4, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (18, 4, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (19, 2, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (1, 0, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (15, 16, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (11, 12, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (13, 14, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (8, 10, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (9, 10, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (27, 21, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (28, 21, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (29, 21, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (30, 21, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (21, 19, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (20, 19, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (25, 21, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (22, 19, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (23, 21, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (24, 21, 1);
INSERT INTO "sys_post_tree" ("post_id", "parent_id", "tree_type") VALUES (26, 21, 1);

--用户数据
INSERT INTO "sys_user_admin" ("user_code", "user_name", "user_account", "user_passwd") VALUES
('admin-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '系统管理员', 'sysAdmin', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq');

INSERT INTO "sys_user" ("user_id", "user_code", "user_name", "user_account", "user_passwd", "user_sex", "user_phone", "user_email", "user_status", "user_rank", "remark", "create_by", "create_time", "update_by", "update_time") VALUES
(1, 'sys-56ae4df2-e9fd-4ece-a5cb-5893e3b29dc7', '刘备', 'liubei', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 0, '13288888888', 'liubei@cowave.com', 1, 'M7', NULL,  'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(2, 'sys-8d234c95-2d19-4ca0-ab13-76be013c3dc2', '诸葛亮', 'zhugeliang', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 0, '13288888888', 'zhugeliang@cowave.com', 1, 'M7', NULL,  'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(3, 'sys-a3cda9ab-737f-4bce-865b-df226338bf54', '关羽', 'guanyu', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 0, '13288888888', 'guanyu@cowave.com', 1, 'M7', NULL,  'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(4, 'sys-3960dc81-b4a9-4bce-8b37-cbdd2869e75d', '张飞', 'zhangfei', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 0, '13288888888', 'zhangfei@cowave.com', 1, 'M7', NULL,  'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(5, 'sys-8ea84a28-98a1-4f8f-80e7-0c333bac5eeb', '马超', 'machao', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 0, '13288888888', 'machao@cowave.com', 1, 'M7', NULL,  'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(6, 'sys-de1db190-d899-4f2e-b96f-5bdf9b954073', '赵云', 'zhaoyun', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 0, '13288888888', 'zhaoyun@cowave.com', 1, 'M7', NULL,  'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(7, 'sys-0a6f0c32-b1a9-4051-bd26-a0062acd58ae', '黄忠', 'huangzhong', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 0, '13288888888', 'huangzhong@cowave.com', 1, 'M7', NULL,  'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(8, 'sys-d0369ec0-a572-4fe2-879e-8ab969331ba4', '大乔', 'daqiao', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 1, '13288888888', 'daqiao@cowave.com', 1, 'M2', NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(9, 'sys-3268546f-9e08-4018-b8e7-8eb95ebe0679', '小乔', 'xiaoqiao', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 1, '13288888888', 'xiaoqiao@cowave.com', 1, 'M2', NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00');
SELECT setval('sys_user_user_id_seq', (SELECT max(user_id) FROM sys_user));

--角色数据
INSERT INTO "sys_role" ("role_id", "role_code", "role_name", "role_type", "remark", "create_by", "create_time", "update_by", "update_time") VALUES
(1, 'sysAdmin', '系统管理员', 'sys', NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(2, 'flowAdmin', '流程管理员', 'flow', NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(3, 'role-readonly', '只读用户', 'sys', NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00');
SELECT setval('sys_role_role_id_seq', (SELECT max(role_id) FROM sys_role));

--菜单数据
INSERT INTO "sys_menu" ("menu_id", "parent_id", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "create_by", "create_time", "update_by", "update_time") VALUES
(4, 0, 'commons.menu.cowave', 10, NULL, 'https://www.cowave.com', NULL, 'C', 'guide', NULL, 1, 0, 1, 1, 0, '控维官网', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-08-10 06:45:14.541'),

-- 系统管理
(1, 0, 'commons.menu.sys.root', 7, NULL, 'system', NULL, 'M', 'system', NULL, 1, 1, 1, 1, 1, '系统管理目录', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-08-29 02:04:32.684'),

-- 用户管理
(5, 1, 'commons.menu.sys.user', 1, 'sys:user:query', 'user', NULL, 'C', 'user', 'system/user/index', 1, 1, 1, 1, 1, '用户管理菜单', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-08-29 02:04:54.33'),
(22, 5, 'commons.button.query', 1, 'sys:user:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(23, 5, 'commons.button.create', 2, 'sys:user:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(24, 5, 'commons.button.edit', 3, 'sys:user:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(25, 5, 'commons.button.delete', 4, 'sys:user:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(26, 5, 'commons.button.export', 5, 'sys:user:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(27, 5, 'commons.button.import', 6, 'sys:user:import', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(88, 5, 'commons.button.diagram', 7, 'sys:user:diagram', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(89, 5, 'commons.button.cache', 8, 'sys:user:cache', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(85, 5, 'user.button.grant', 9, 'sys:user:grant', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(28, 5, 'user.button.passwd', 10, 'sys:user:passwd', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(112, 5, 'commons.button.status', 11, 'sys:user:status', '#', NULL, 'B', '#', NULL, 1, 1, 1, 0, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),

-- 角色管理
(6, 1, 'commons.menu.sys.role', 2, 'sys:role:query', 'role', NULL, 'C', 'peoples', 'system/role/index', 1, 1, 1, 1, 1, '角色管理菜单', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-08-29 02:07:43.946'),
(29, 6, 'commons.button.query', 1, 'sys:role:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(30, 6, 'commons.button.create', 2, 'sys:role:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(31, 6, 'commons.button.edit', 3, 'sys:role:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(32, 6, 'commons.button.delete', 4, 'sys:role:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(33, 6, 'commons.button.export', 5, 'sys:role:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(97, 6, 'role.button.menus', 6, 'sys:role:menus', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(98, 6, 'role.button.scope', 7, 'sys:role:scope', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(99, 6, 'role.button.members', 8, 'sys:role:members', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),

-- 部门管理
(8, 1, 'commons.menu.sys.dept', 3, 'sys:dept:query', 'dept', NULL, 'C', 'tree', 'system/dept/index', 1, 1, 1, 1, 1, '部门管理菜单', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-08-29 02:06:04.739'),
(39, 8, 'commons.button.query', 1, 'sys:dept:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(40, 8, 'commons.button.create', 2, 'sys:dept:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(41, 8, 'commons.button.edit', 3, 'sys:dept:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(42, 8, 'commons.button.delete', 4, 'sys:dept:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(90, 8, 'commons.button.export', 5, 'sys:dept:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(91, 8, 'commons.button.diagram', 6, 'sys:dept:diagram', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(92, 8, 'commons.button.cache', 7, 'sys:dept:cache', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(93, 8, 'dept.button.positions', 8, 'sys:dept:positions', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(94, 8, 'dept.button.members', 9, 'sys:dept:members', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),

-- 岗位管理
(9, 1, 'commons.menu.sys.post', 4, 'sys:post:query', 'post', NULL, 'C', 'post', 'system/post/index', 1, 1, 1, 1, 1, '岗位管理菜单', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(43, 9, 'commons.button.query', 1, 'sys:post:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(44, 9, 'commons.button.create', 2, 'sys:post:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(45, 9, 'commons.button.edit', 3, 'sys:post:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(46, 9, 'commons.button.delete', 4, 'sys:post:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(47, 9, 'commons.button.export', 5, 'sys:post:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(95, 9, 'commons.button.diagram', 6, 'sys:post:diagram', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(96, 9, 'commons.button.cache', 7, 'sys:post:cache', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),

-- 菜单管理
(7, 1, 'commons.menu.sys.menu', 5, 'sys:menu:query', 'menu', NULL, 'C', 'tree-table', 'system/menu/index', 1, 1, 1, 1, 1, '菜单管理菜单', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(34, 7, 'commons.button.query', 1, 'sys:menu:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(35, 7, 'commons.button.create', 2, 'sys:menu:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(36, 7, 'commons.button.edit', 3, 'sys:menu:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(37, 7, 'commons.button.delete', 4, 'sys:menu:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(100, 7, 'commons.button.export', 5, 'sys:menu:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),

-- 字典管理
(10, 1, 'commons.menu.sys.dict', 6, 'sys:dict:query', 'dict', NULL, 'C', 'dict', 'system/dict/index', 1, 1, 1, 1, 1, '字典管理菜单', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(48, 10, 'commons.button.query', 1, 'sys:dict:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(49, 10, 'commons.button.create', 2, 'sys:dict:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(50, 10, 'commons.button.edit', 3, 'sys:dict:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(51, 10, 'commons.button.delete', 4, 'sys:dict:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(52, 10, 'commons.button.export', 5, 'sys:dict:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(101, 10, 'commons.button.cache', 6, 'sys:dict:cache', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),

-- 系统配置
(11, 1, 'commons.menu.sys.config', 8, 'sys:config:query', 'config', NULL, 'C', 'param', 'system/config/index', 1, 1, 1, 1, 1, '参数设置菜单', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(53, 11, 'commons.button.query', 1, 'sys:config:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(54, 11, 'commons.button.create', 2, 'sys:config:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(55, 11, 'commons.button.edit', 3, 'sys:config:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(56, 11, 'commons.button.delete', 4, 'sys:config:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(57, 11, 'commons.button.export', 5, 'sys:config:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(102, 11, 'commons.button.cache', 6, 'sys:config:cache', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),

-- 定时任务
(15, 1, 'commons.menu.sys.schedule.root', 9, 'sys:job:query', 'job', NULL, 'C', 'job', 'system/job/index', 1, 1, 1, 1, 1, '定时任务菜单', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-08-10 06:41:40.275'),
(67, 15, 'commons.button.query', 1, 'sys:job:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(68, 15, 'commons.button.create', 2, 'sys:job:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(69, 15, 'commons.button.edit', 3, 'sys:job:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(70, 15, 'commons.button.delete', 4, 'sys:job:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(72, 15, 'commons.button.export', 5, 'sys:job:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(117, 15, 'commons.button.exec', 6, 'sys:job:exec', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(71, 15, 'commons.button.status', 7, 'sys:job:status', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(118, 15, 'commons.menu.sys.schedule.refresh', 8, 'sys:job:refresh', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(138, 15, 'commons.menu.sys.schedule.logQuery', 9, 'sys:job:log:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(140, 15, 'commons.menu.sys.schedule.logExport', 9, 'sys:job:log:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(139, 15, 'commons.menu.sys.schedule.logDelete', 9, 'sys:job:log:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),

-- api文档
(21, 1, 'commons.menu.sys.doc.api', 10, NULL, 'doc', NULL, 'M', 'api', '', 1, 1, 1, 1, 1, '系统接口菜单', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(115, 21, 'commons.menu.sys.doc.admin', 1, NULL, 'admin', NULL, 'C', 'interface', 'system/doc/admin', 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(116, 21, 'commons.menu.sys.doc.quartz', 2, NULL, 'quartz', NULL, 'C', 'interface', 'system/doc/quartz', 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(126, 21, 'commons.menu.sys.doc.meter', 3, NULL, 'meter', NULL, 'C', 'interface', 'system/doc/meter', 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-08-10 06:55:27.034', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-08-10 06:55:27.034'),

-- Ldap配置
(148, 1, 'commons.menu.sys.ldap', 11, 'sys:ldap:query', 'ldap', NULL, 'C', 'ldap', 'system/ldap/index', 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-18 13:57:36.631', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-18 13:59:53.624'),
(149, 148, 'commons.button.query', 1, 'sys:ldap:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-20 16:38:18.118', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-20 16:38:18.118'),
(150, 148, 'commons.button.create', 2, 'sys:ldap:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-20 16:39:01.56', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-20 16:39:01.56'),
(151, 148, 'commons.button.edit', 3, 'sys:ldap:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-20 16:39:27.646', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-20 16:39:27.646'),
(152, 148, 'commons.button.delete', 4, 'sys:ldap:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-20 16:39:59.614', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-20 16:39:59.614'),
(153, 148, 'commons.button.test', 5, 'sys:ldap:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-20 16:39:27.646', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-20 16:39:27.646'),
(154, 148, 'commons.button.status', 6, 'sys:ldap:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-20 16:39:27.646', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-20 16:39:27.646'),

-- Oauth2配置
(141, 1, 'commons.menu.sys.oauth2.root', 12, NULL, 'oauth', NULL, 'M', 'vscope', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-02 23:08:01.779', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-02 23:08:01.779'),

(169, 141, 'commons.menu.sys.oauth2.client', 1, 'oauth:client:query', 'client', NULL, 'C', 'app', 'system/oauth/client', 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2025-06-05 13:35:45.532', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2025-06-05 13:35:45.532'),
(170, 169, 'commons.button.query', 1, 'oauth:client:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-20 16:38:18.118', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-20 16:38:18.118'),
(171, 169, 'commons.button.create', 2, 'oauth:client:create', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-20 16:39:01.56', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-20 16:39:01.56'),
(172, 169, 'commons.button.delete', 3, 'oauth:client:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-20 16:39:59.614', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-20 16:39:59.614'),

(142, 141, 'commons.menu.sys.oauth2.gitlab', 2, 'sys:oauth:gitlab', 'gitlab', NULL, 'C', 'gitlab', 'system/oauth/gitlab', 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-02 23:10:39.323', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-03 07:20:04.089'),
(143, 142, 'commons.button.config', 1, 'oauth:gitlab:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-21 13:57:12.65', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-21 13:57:12.65'),
(144, 142, 'commons.button.query', 2, 'oauth:gitlab:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-21 13:57:12.65', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-21 13:57:12.65'),
(145, 142, 'commons.menu.sys.oauth2.userQuery', 3, 'oauth:gitlab:user:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-21 13:57:12.65', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-21 13:57:12.65'),
(146, 142, 'commons.menu.sys.oauth2.userEdit', 4, 'oauth:gitlab:user:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-21 13:57:12.65', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-21 13:57:12.65'),
(147, 142, 'commons.menu.sys.oauth2.userDelete', 5, 'oauth:gitlab:user:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-21 13:57:12.65', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-21 13:57:12.65'),

-- 系统监控
(2, 0, 'commons.menu.monitor.root', 6, NULL, 'monitor', NULL, 'M', 'monitor', NULL, 1, 1, 1, 1, 1, '系统监控目录', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-08-09 07:02:51.069'),

-- 在线用户
(14, 2, 'commons.menu.monitor.online', 1, 'monitor:online:query', 'online', NULL, 'C', 'online', 'monitor/online/index', 1, 1, 1, 1, 1, '在线用户菜单', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(64, 14, 'commons.button.query', 1, 'monitor:online:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(65, 14, 'commons.button.quit', 2, 'monitor:online:force', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),

-- 操作日志
(13, 2, 'commons.menu.monitor.log', 2, 'monitor:log:query', 'log', NULL, 'C', 'log', 'monitor/operlog/index', 1, 1, 1, 1, 1, '日志管理菜单', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(79, 13, 'commons.button.query', 1, 'monitor:log:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(80, 13, 'commons.button.delete', 2, 'monitor:log:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),
(81, 13, 'commons.button.export', 3, 'monitor:log:export', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, '', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),

-- 监控页面
(165, 2, 'commons.menu.monitor.nacos', 3, NULL, 'monitor-nacos', NULL, 'C', 'nacos', 'monitor/nacos/index', 1, 1, 1, 1, 0, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2025-04-08 11:04:22.486', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2025-04-08 11:04:22.486'),
(164, 2, 'commons.menu.monitor.actuator', 4, NULL, 'monitor-actuator', NULL, 'C', 'health', 'monitor/actuator/index', 1, 1, 1, 1, 0, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2025-04-08 11:04:22.486', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2025-04-08 11:04:22.486'),
(130, 2, 'commons.menu.monitor.alert', 5, NULL, 'monitor-alert', NULL, 'C', 'alert', 'monitor/alert/index', 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-08-10 08:10:04.878', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-08-10 08:33:56.584'),
(166, 2, 'commons.menu.monitor.grafana', 6, NULL, 'monitor-grafana', NULL, 'C', 'grafana', 'monitor/grafana/index', 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2025-04-09 19:46:29.889', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2025-04-09 19:56:08.975'),
(167, 2, 'commons.menu.monitor.prometheus', 7, NULL, 'monitor-prometheus', NULL, 'C', 'prometheus', 'monitor/prometheus/index', 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2025-04-09 19:46:29.889', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2025-04-09 19:56:08.975'),

-- 流程管理
(159, 0, 'commons.menu.flow.root', 2, NULL, 'flow', NULL, 'M', 'cascader', NULL, 1, 1, 1, 1, 0, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-08-10 06:43:00.55', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-08-10 07:32:09.243'),
(160, 159, 'commons.menu.flow.owner.task', 1, 'flow:task', 'task', NULL, 'C', 'task', 'flow/workbench/task', 1, 1, 1, 1, 0, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-02 23:10:39.323', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-03 07:20:04.089'),
(161, 159, 'commons.menu.flow.owner.leave', 2, 'flow:leave', 'leave', NULL, 'C', 'leave', 'flow/workbench/leave', 1, 1, 1, 1, 0, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-02 23:10:39.323', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-03 07:20:04.089'),
(162, 159, 'commons.menu.flow.owner.meeting', 3, 'flow:meeting', 'meeting', NULL, 'C', 'meeting', 'flow/workbench/meeting', 1, 1, 1, 1, 0, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-02 23:10:39.323', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-03 07:20:04.089'),
(163, 159, 'commons.menu.flow.owner.purchase', 4, 'flow:purchase', 'purchase', NULL, 'C', 'purchase', 'flow/workbench/purchase', 1, 1, 1, 1, 0, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-02 23:10:39.323', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-03 07:20:04.089'),

-- 流程配置
(155, 159, 'commons.menu.flow.manage', 12, NULL, 'manage', NULL, 'M', 'flow', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-02 23:08:01.779', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-02 23:08:01.779'),
(157, 155, 'commons.menu.flow.model', 1, 'flow:modeler', 'modeler', NULL, 'C', 'component', 'flow/modeler', 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-02 23:10:39.323', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-03 07:20:04.089'),
(158, 155, 'commons.menu.flow.deploy', 2, 'flow:deploy', 'deploy', NULL, 'C', 'deploy', 'flow/deploy', 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-02 23:10:39.323', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-03 07:20:04.089'),
(156, 155, 'commons.menu.flow.instance', 3, 'flow:instance', 'instance', NULL, 'C', 'flowinstance', 'flow/instance', 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-02 23:10:39.323', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2024-03-03 07:20:04.089'),

-- 软件测试
(124, 0, 'commons.menu.meter.root', 1, NULL, 'meter', NULL, 'M', 'meter', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-08-10 06:45:07.865', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-08-10 06:45:07.865'),

(168, 124, 'commons.menu.meter.ui', 1, NULL, 'meter-ui', NULL, 'C', 'meter_ui', 'meter/ui/index', 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2025-04-23 07:35:44.951', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2025-04-23 07:38:08.135'),

(19, 124, 'commons.menu.meter.form', 2, NULL, 'form', NULL, 'C', 'build', 'meter/form/index', 1, 1, 1, 1, 1, '表单构建菜单', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2022-04-25 09:00:00'),

-- 代码模板
(132, 124, 'commons.menu.meter.template.root', 3, NULL, 'template', NULL, 'M', 'code', NULL, 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-08-25 01:56:05.058', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-09-07 04:51:01.348'),
(134, 132, 'commons.menu.meter.template.application', 2, NULL, 'application', NULL, 'C', 'app', 'meter/template/application', 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-08-25 01:58:50.097', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-08-29 02:07:02.87'),
(135, 132, 'commons.menu.meter.template.model', 3, NULL, 'model', NULL, 'C', 'model', 'meter/template/model', 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-08-25 01:59:13.949', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-08-30 09:10:55.253'),
(136, 132, 'commons.menu.meter.template.database', 4, NULL, 'db', NULL, 'C', 'db', 'meter/template/db', 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-08-30 01:18:35.52', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-08-30 02:46:23.893'),
(137, 132, 'commons.menu.meter.template.table', 5, NULL, 'table', NULL, 'C', 'table', 'meter/template/table', 1, 1, 1, 1, 1, NULL, 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-08-30 02:46:06.24', 'sys-8a1f2a30-6136-4d2a-b883-70800c09ef0e', '2023-08-31 04:49:04.045');

SELECT setval('sys_menu_menu_id_seq', (SELECT max(menu_id) FROM sys_menu));

--用户部门岗位
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (1, 1, 1, 1, 1);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (2, 1, 2, 1, 0);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (3, 1, 3, 1, 0);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (4, 1, 6, 1, 0);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (6, 1, 4, 1, 0);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (5, 1, 7, 1, 0);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (7, 1, 5, 1, 0);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (2, 4, 19, 0, 1);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (3, 8, 10, 0, 1);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (4, 9, 12, 0, 1);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (6, 6, 18, 0, 1);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (5, 7, 16, 0, 1);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader") VALUES (7, 5, 14, 0, 1);

--用户角色
INSERT INTO "sys_user_role" ("user_id", "role_id") VALUES (1, 3);
INSERT INTO "sys_user_role" ("user_id", "role_id") VALUES (2, 3);
INSERT INTO "sys_user_role" ("user_id", "role_id") VALUES (3, 3);
INSERT INTO "sys_user_role" ("user_id", "role_id") VALUES (4, 3);
INSERT INTO "sys_user_role" ("user_id", "role_id") VALUES (5, 3);
INSERT INTO "sys_user_role" ("user_id", "role_id") VALUES (6, 3);
INSERT INTO "sys_user_role" ("user_id", "role_id") VALUES (7, 3);

--用户关系
INSERT INTO "sys_user_tree" ("user_id", "parent_id", "tree_type") VALUES (1, 0, 1);
INSERT INTO "sys_user_tree" ("user_id", "parent_id", "tree_type") VALUES (2, 2, 1);
INSERT INTO "sys_user_tree" ("user_id", "parent_id", "tree_type") VALUES (3, 2, 1);
INSERT INTO "sys_user_tree" ("user_id", "parent_id", "tree_type") VALUES (4, 2, 1);
INSERT INTO "sys_user_tree" ("user_id", "parent_id", "tree_type") VALUES (5, 2, 1);
INSERT INTO "sys_user_tree" ("user_id", "parent_id", "tree_type") VALUES (6, 2, 1);
INSERT INTO "sys_user_tree" ("user_id", "parent_id", "tree_type") VALUES (7, 2, 1);

--只读用户菜单
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 1);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 5);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 8);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 9);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 6);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 7);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 10);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 11);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 141);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 142);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 148);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 2);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 14);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 13);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 15);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 22);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 88);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 39);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 91);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 43);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 95);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 29);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 34);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 48);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 53);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 129);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 144);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 145);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 149);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 18);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 64);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 79);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 130);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 3);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 21);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 115);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 116);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 126);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 19);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 132);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 133);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 134);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 135);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 136);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 137);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 67);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 117);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 71);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 138);
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, 124);
