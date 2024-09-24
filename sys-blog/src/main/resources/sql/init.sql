-- 1.文章信息
DROP TABLE IF EXISTS post_info;
CREATE TABLE post_info
(
    id          bigserial primary key,
    title       varchar(64),
    summary     varchar(500),
    image       varchar(512),
    pwd         varchar(64),
    slider      int2 default 0,
    status      int2 default 1,
    featured    int2 default 0,
    original    int2 default 1,
    views       int8 default 0,
    favors      int8 default 0,
    channel_id  int8,
    category_id int8,
    create_user varchar(64),
    create_time timestamp default CURRENT_TIMESTAMP,
    update_time timestamp default CURRENT_TIMESTAMP
);
comment on table post_info is '博客信息';
comment on column post_info.id is 'id';
comment on column post_info.title is '标题';
comment on column post_info.summary is '摘要';
comment on column post_info.image is '封面';
comment on column post_info.pwd is '文章密码';
comment on column post_info.slider is '是否作为幻灯片';
comment on column post_info.status is '是否可见';
comment on column post_info.featured is '是否推荐';
comment on column post_info.original is '是否原创';
comment on column post_info.views is '浏览数';
comment on column post_info.favors is '点赞数';
comment on column post_info.channel_id is '专栏id';
comment on column post_info.category_id is '分类id';
comment on column post_info.create_time is '创建时间';
comment on column post_info.update_time is '更新时间';

-- 2.文章内容
DROP TABLE IF EXISTS post_content;
CREATE TABLE post_content
(
    id      int8 primary key,
    content text,
    editor  varchar(16) default 'tinymce'
);

-- 3.文章标签
DROP TABLE IF EXISTS post_tag;
CREATE TABLE post_tag
(
    id      bigserial primary key,
    post_id int8,
    tag_name  varchar(64)
);

-- 4. 文章评论
DROP TABLE IF EXISTS post_comment;
CREATE TABLE post_comment
(
    id               bigserial primary key,
    p_id             int8,
    ancestors        varchar(255),
    post_id          int8,
    email            varchar(64),
    nick_name        varchar(64),
    avatar           varchar(255),
    love             char(4),
    status           int2      default 1,
    create_time      timestamp,
    examine_by       varchar(64),
    examine_time     timestamp,
    ip               varchar(64),
    parent_nick_name varchar(255),
    content          varchar(2000),
    ip_addr          varchar(60)
);
comment on table post_comment is '留言';
comment on column post_comment.id is 'id';
comment on column post_comment.p_id is 'pid';
comment on column post_comment.ancestors is '祖级列表';
comment on column post_comment.post_id is '博客id';
comment on column post_comment.email is '邮箱';
comment on column post_comment.nick_name is '昵称';
comment on column post_comment.avatar is '头像';
comment on column post_comment.love is '点赞';
comment on column post_comment.status is '状态';
comment on column post_comment.create_time is '创建时间';
comment on column post_comment.examine_by is '审核人';
comment on column post_comment.examine_time is '审核时间';
comment on column post_comment.ip is 'ip';
comment on column post_comment.ip_addr is 'ip地址';
comment on column post_comment.parent_nick_name is '父级名称';
comment on column post_comment.content is '内容';

-- 5.导航栏
drop table if exists info_navigation;
CREATE TABLE info_navigation
(
    id          serial primary key,
    pid         int4      default 0,
    name        varchar(64),
    icon        varchar(128),
    sort        int4,
    status      int2      default 1,
    type        int4      default 0,
    page_url    varchar(256),
    description varchar(256),
    create_time timestamp default CURRENT_TIMESTAMP,
    update_time timestamp default CURRENT_TIMESTAMP
);
comment on table info_navigation is '导航栏';
comment on column info_navigation.id is 'id';
comment on column info_navigation.pid is '父级id';
comment on column info_navigation.name is '名称';
comment on column info_navigation.description is '描述';
comment on column info_navigation.sort is '排序';
comment on column info_navigation.icon is '图标';
comment on column info_navigation.status is '状态';
comment on column info_navigation.type is '类型 （0目录/1页面）';
comment on column info_navigation.page_url is '页面地址';
comment on column info_navigation.create_time is '创建时间';
comment on column info_navigation.update_time is '更新时间';

-- 6. 文章分类
DROP TABLE IF EXISTS info_category;
CREATE TABLE info_category
(
    id        bigserial primary key,
    name      varchar(64),
    image varchar(128),
    weight    int8 default 0
);
comment on table info_category is '文章分类';
comment on column info_category.id is 'id';
comment on column info_category.name is '名称';
comment on column info_category.image is '图片链接';

-- 7. 文章专栏
DROP TABLE IF EXISTS info_channel;
CREATE TABLE info_channel
(
    id        bigserial primary key,
    name      varchar(64),
    image varchar(128),
    weight    int8 default 0
);
comment on table info_channel is '文章专栏';
comment on column info_channel.id is 'id';
comment on column info_channel.name is '名称';
comment on column info_channel.image is '图片链接';

-- 8.友情链接
DROP TABLE IF EXISTS info_link;
CREATE TABLE info_link
(
    id               bigserial primary key ,
    link_name        varchar(255),
    link_logo        varchar(255),
    link_url         varchar(255),
    link_description varchar(255),
    status           int2,
    create_by        varchar(64),
    create_time      timestamp,
    update_by        varchar(64),
    update_time      timestamp,
    remark           varchar(500)
);
comment on table info_link is '链接';
comment on column info_link.id is 'id';
comment on column info_link.link_name is '名称';
comment on column info_link.link_logo is 'logo';
comment on column info_link.link_url is 'url地址';
comment on column info_link.link_description is '链接描述';
comment on column info_link.status is '状态：0正常，1隐藏';
comment on column info_link.create_by is '创建者';
comment on column info_link.create_time is '创建时间';
comment on column info_link.update_by is '更新者';
comment on column info_link.update_time is '更新时间';
comment on column info_link.remark is '备注';

-- 9.动态
DROP TABLE IF EXISTS info_note;
CREATE TABLE info_note
(
    id           bigserial primary key,
    note_title   varchar(64),
    note_image   varchar(255),
    note_content text,
    note_summary varchar(256),
    note_type    varchar(10),
    note_status  char(1),
    is_Public    char(1),
    remark       varchar(255),
    author_id    int8,
    create_time  timestamp,
    update_time  timestamp
);
comment on table info_note is '动态信息';
comment on column info_note.id is 'id';
comment on column info_note.note_title is '标题';
comment on column info_note.note_image is '图片';
comment on column info_note.note_content is '内容';
comment on column info_note.note_summary is '摘要';
comment on column info_note.note_type is '类型';
comment on column info_note.note_status is '状态';
comment on column info_note.is_Public is '是否公共，1公共 0私有';
comment on column info_note.remark is '备注';
comment on column info_note.author_id is '作者id';
comment on column info_note.create_time is '创建时间';
comment on column info_note.update_time is '更新时间';

INSERT INTO info_navigation (pid, name, description, sort, icon, status, create_time, update_time, type, page_url) VALUES (0, '首页', '', 0, 'fa fa-home', 1, '2022-09-02 16:24:24', '2022-09-02 16:24:24', 1, '/blog');
INSERT INTO info_navigation (pid, name, description, sort, icon, status, create_time, update_time, type, page_url) VALUES (4, '归档', '', 11, 'fa fa-coffee', 1, '2018-01-14 21:34:57', '2022-02-12 19:40:52', 1, '/blog/timeArchives');
INSERT INTO info_navigation (pid, name, description, sort, icon, status, create_time, update_time, type, page_url) VALUES (0, '文档', '', 2, 'fa fa-book', 1, '2018-01-20 22:28:03', '2022-02-08 16:27:11', 0, '');
INSERT INTO info_navigation (pid, name, description, sort, icon, status, create_time, update_time, type, page_url) VALUES (0, '文章', '', 1, 'fa fa-folder-open-o', 1, '2018-01-20 22:28:03', '2022-02-08 16:27:11', 0, '');
INSERT INTO info_navigation (pid, name, description, sort, icon, status, create_time, update_time, type, page_url) VALUES (4, '时间轴', '', 13, 'fa fa-calendar-check-o', 1, '2022-01-02 21:58:34', '2022-01-03 18:55:59', 1, '/blog/focus');
INSERT INTO info_navigation (pid, name, description, sort, icon, status, create_time, update_time, type, page_url) VALUES (3, '源码', '', 1, 'fa fa-code-fork', 1, '2022-02-08 16:28:02', '2022-02-08 16:28:02', 1, 'https://gitlab.cowave.com/commons/demo/demo-sys/sys-blog');
INSERT INTO info_navigation (pid, name, description, sort, icon, status, create_time, update_time, type, page_url) VALUES (4, '标签', '', 12, 'fa fa-bookmark-o', 1, '2022-03-11 13:20:21', '2022-03-11 13:20:20', 1, '/blog/tags');
INSERT INTO info_navigation (pid, name, description, sort, icon, status, create_time, update_time, type, page_url) VALUES (0, '动态', '', 5, 'fa fa-hacker-news', 1, '2022-03-12 19:30:23', '2022-03-12 19:31:01', 1, '/blog/dynamics');
INSERT INTO info_navigation (pid, name, description, sort, icon, status, create_time, update_time, type, page_url) VALUES (0, '留言板', '', 3, 'fa fa-comments', 1, '2022-03-16 17:04:53', '2022-03-16 17:04:53', 1, '/blog/comments');
INSERT INTO info_navigation (pid, name, description, sort, icon, status, create_time, update_time, type, page_url) VALUES (0, '热搜', '', 6, 'fa fa-fire', 1, '2022-03-23 20:49:26', '2022-03-26 18:56:51', 1, '/blog/news');
INSERT INTO info_navigation (pid, name, description, sort, icon, status, create_time, update_time, type, page_url) VALUES (0, '聊天室', '', 4, 'glyphicon glyphicon-comment', 1, '2023-10-21 13:00:41', '2023-10-21 13:00:41', 1, '/blog/chatRoom');
INSERT INTO info_navigation (pid, name, description, sort, icon, status, create_time, update_time, type, page_url) VALUES (0, '我的', '', 7, 'fa fa-user-circle', 1, '2022-03-23 20:49:26', '2022-03-26 18:56:51', 0, '');
INSERT INTO info_navigation (pid, name, description, sort, icon, status, create_time, update_time, type, page_url) VALUES (12, '博客', '', 1, 'fa fa-file-o', 1, '2022-03-23 20:49:26', '2022-03-26 18:56:51', 1, '/blog/own/post');
INSERT INTO info_navigation (pid, name, description, sort, icon, status, create_time, update_time, type, page_url) VALUES (12, '分类', '', 2, 'fa fa-folder-o', 1, '2022-03-23 20:49:26', '2022-03-26 18:56:51', 1, '/blog/own/category');
INSERT INTO info_navigation (pid, name, description, sort, icon, status, create_time, update_time, type, page_url) VALUES (12, '专栏', '', 3, 'fa fa-bookmark-o', 1, '2022-03-23 20:49:26', '2022-03-26 18:56:51', 1, '/blog/own/channel');

INSERT INTO info_category (name, image) VALUES ('使用笔记', NULL);

INSERT INTO info_channel (name, image) VALUES ('测试项目', NULL);

INSERT INTO info_note (note_title, author_id, note_image, note_content, note_summary, note_type, note_status, is_Public, create_time, update_time, remark) VALUES ('说说那些最受欢迎的emoji表情（2019）', 1, NULL, '<h1 class="h2 pt2 pb2 center bold" style="box-sizing: inherit; margin-top: 0px; margin-bottom: 0px; padding-top: var(--space-2); padding-right: 0px; padding-bottom: var(--space-2); padding-left: 0px; font-size: var(--h2); font-weight: var(--bold-font-weight,bold); text-align: center; color: rgb(74, 74, 74); font-family: BlinkMacSystemFont, -apple-system, &quot;Segoe UI&quot;, Roboto, Oxygen, Ubuntu, Cantarell, &quot;Fira Sans&quot;, &quot;Droid Sans&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, sans-serif;">说说那些最受欢迎的emoji表情（2019）</h1><p><time class="block mb2 center" datetime="2020-03-18T16:00:00.000Z" style="box-sizing: inherit; color: rgb(119, 119, 119); font-size: 0.8em; text-align: center; margin-bottom: 1.5rem; font-family: BlinkMacSystemFont, -apple-system, &quot;Segoe UI&quot;, Roboto, Oxygen, Ubuntu, Cantarell, &quot;Fira Sans&quot;, &quot;Droid Sans&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, sans-serif;">2020-03-18</time></p><article class="content is-medium px2" style="box-sizing: inherit; padding-left: var(--space-2); padding-right: var(--space-2); font-size: 1.25rem; color: rgb(74, 74, 74); font-family: BlinkMacSystemFont, -apple-system, &quot;Segoe UI&quot;, Roboto, Oxygen, Ubuntu, Cantarell, &quot;Fira Sans&quot;, &quot;Droid Sans&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, sans-serif;"><p style="box-sizing: inherit; margin-bottom: 1em; padding: 0px; line-height: 1.8; text-indent: 2em;">“当我们了解客户的情绪时，我们可以更好地满足他们的需求。”这是2020年全球情感报告的目的。那如何来收集客户的情绪数据呢？互联网的普及以及emoji表情的使用使得这个问题可以很好的解决了。研究者们分析了2019年1月的社交网络的上的emoji表情使用数据，那么现在大家一定很好奇，世界上最受欢迎的表情是哪些呢？</p><h4 style="box-sizing: inherit; margin: 0px 0px 0.8em; padding: 0px; font-size: 1.25em; color: rgb(54, 54, 54); line-height: 1.125;">最常用的表情<img src="https://emojixd.com/uploads/e7cd9d72e9af4636874a02970de1446a.png" alt="表情图" style="text-indent: 2em; color: rgb(74, 74, 74); font-size: 1.25rem; box-sizing: inherit; height: auto; max-width: 100%;"></h4><p style="box-sizing: inherit; margin-bottom: 1em; padding: 0px; line-height: 1.8; text-indent: 2em;">前3个表情符号：<a href="https://emojixd.com/x7q0" target="_blank" title="笑哭了" style="box-sizing: inherit; color: rgb(50, 115, 220);"><img class="emoji" draggable="false" alt="😂" src="https://twemoji.maxcdn.com/v/12.1.3/72x72/1f602.png" style="box-sizing: inherit; height: auto; max-width: 100%; font-family: &quot;apple color emoji&quot;, &quot;segoe ui emoji&quot;, &quot;noto color emoji&quot;, &quot;android emoji&quot;, emojisymbols, &quot;emojione mozilla&quot;, &quot;twemoji mozilla&quot;, &quot;segoe ui symbol&quot;; width: 2rem; padding: 0px 0.2rem; vertical-align: text-bottom;"></a>，<a href="https://emojixd.com/x7ry" target="_blank" title="放声大哭" style="box-sizing: inherit; color: rgb(50, 115, 220);"><img class="emoji" draggable="false" alt="😭" src="https://twemoji.maxcdn.com/v/12.1.3/72x72/1f62d.png" style="box-sizing: inherit; height: auto; max-width: 100%; font-family: &quot;apple color emoji&quot;, &quot;segoe ui emoji&quot;, &quot;noto color emoji&quot;, &quot;android emoji&quot;, emojisymbols, &quot;emojione mozilla&quot;, &quot;twemoji mozilla&quot;, &quot;segoe ui symbol&quot;; width: 2rem; padding: 0px 0.2rem; vertical-align: text-bottom;"></a>，<a href="https://emojixd.com/x7tb" target="_blank" title="红心" style="box-sizing: inherit; color: rgb(50, 115, 220);"><img class="emoji" draggable="false" alt="❤️" src="https://twemoji.maxcdn.com/v/12.1.3/72x72/2764.png" style="box-sizing: inherit; height: auto; max-width: 100%; font-family: &quot;apple color emoji&quot;, &quot;segoe ui emoji&quot;, &quot;noto color emoji&quot;, &quot;android emoji&quot;, emojisymbols, &quot;emojione mozilla&quot;, &quot;twemoji mozilla&quot;, &quot;segoe ui symbol&quot;; width: 2rem; padding: 0px 0.2rem; vertical-align: text-bottom;"></a>。而且在样本中的所有国家<a href="https://emojixd.com/x7q0" target="_blank" title="笑哭了" style="box-sizing: inherit; color: rgb(50, 115, 220);"><img class="emoji" draggable="false" alt="😂" src="https://twemoji.maxcdn.com/v/12.1.3/72x72/1f602.png" style="box-sizing: inherit; height: auto; max-width: 100%; font-family: &quot;apple color emoji&quot;, &quot;segoe ui emoji&quot;, &quot;noto color emoji&quot;, &quot;android emoji&quot;, emojisymbols, &quot;emojione mozilla&quot;, &quot;twemoji mozilla&quot;, &quot;segoe ui symbol&quot;; width: 2rem; padding: 0px 0.2rem; vertical-align: text-bottom;"></a>都位居首位！哭笑不得，这个表情的确比言语可以更好的表达一种尴尬的语境和状态。女性最常用的5位是：<a href="https://emojixd.com/x7q0" target="_blank" title="笑哭了" style="box-sizing: inherit; color: rgb(50, 115, 220);"><img class="emoji" draggable="false" alt="😂" src="https://twemoji.maxcdn.com/v/12.1.3/72x72/1f602.png" style="box-sizing: inherit; height: auto; max-width: 100%; font-family: &quot;apple color emoji&quot;, &quot;segoe ui emoji&quot;, &quot;noto color emoji&quot;, &quot;android emoji&quot;, emojisymbols, &quot;emojione mozilla&quot;, &quot;twemoji mozilla&quot;, &quot;segoe ui symbol&quot;; width: 2rem; padding: 0px 0.2rem; vertical-align: text-bottom;"></a>，<a href="https://emojixd.com/x7tb" target="_blank" title="红心" style="box-sizing: inherit; color: rgb(50, 115, 220);"><img class="emoji" draggable="false" alt="❤️" src="https://twemoji.maxcdn.com/v/12.1.3/72x72/2764.png" style="box-sizing: inherit; height: auto; max-width: 100%; font-family: &quot;apple color emoji&quot;, &quot;segoe ui emoji&quot;, &quot;noto color emoji&quot;, &quot;android emoji&quot;, emojisymbols, &quot;emojione mozilla&quot;, &quot;twemoji mozilla&quot;, &quot;segoe ui symbol&quot;; width: 2rem; padding: 0px 0.2rem; vertical-align: text-bottom;"></a>，<a href="https://emojixd.com/x7ry" target="_blank" title="放声大哭" style="box-sizing: inherit; color: rgb(50, 115, 220);"><img class="emoji" draggable="false" alt="😭" src="https://twemoji.maxcdn.com/v/12.1.3/72x72/1f62d.png" style="box-sizing: inherit; height: auto; max-width: 100%; font-family: &quot;apple color emoji&quot;, &quot;segoe ui emoji&quot;, &quot;noto color emoji&quot;, &quot;android emoji&quot;, emojisymbols, &quot;emojione mozilla&quot;, &quot;twemoji mozilla&quot;, &quot;segoe ui symbol&quot;; width: 2rem; padding: 0px 0.2rem; vertical-align: text-bottom;"></a>，<a href="https://emojixd.com/x7q7" target="_blank" title="花痴" style="box-sizing: inherit; color: rgb(50, 115, 220);"><img class="emoji" draggable="false" alt="😍" src="https://twemoji.maxcdn.com/v/12.1.3/72x72/1f60d.png" style="box-sizing: inherit; height: auto; max-width: 100%; font-family: &quot;apple color emoji&quot;, &quot;segoe ui emoji&quot;, &quot;noto color emoji&quot;, &quot;android emoji&quot;, emojisymbols, &quot;emojione mozilla&quot;, &quot;twemoji mozilla&quot;, &quot;segoe ui symbol&quot;; width: 2rem; padding: 0px 0.2rem; vertical-align: text-bottom;"></a>，<a href="https://emojixd.com/x7pz" target="_blank" title="笑得满地打滚" style="box-sizing: inherit; color: rgb(50, 115, 220);"><img class="emoji" draggable="false" alt="🤣" src="https://twemoji.maxcdn.com/v/12.1.3/72x72/1f923.png" style="box-sizing: inherit; height: auto; max-width: 100%; font-family: &quot;apple color emoji&quot;, &quot;segoe ui emoji&quot;, &quot;noto color emoji&quot;, &quot;android emoji&quot;, emojisymbols, &quot;emojione mozilla&quot;, &quot;twemoji mozilla&quot;, &quot;segoe ui symbol&quot;; width: 2rem; padding: 0px 0.2rem; vertical-align: text-bottom;"></a>；而男性最常用的是<a href="https://emojixd.com/x7q0" target="_blank" title="笑哭了" style="box-sizing: inherit; color: rgb(50, 115, 220);"><img class="emoji" draggable="false" alt="😂" src="https://twemoji.maxcdn.com/v/12.1.3/72x72/1f602.png" style="box-sizing: inherit; height: auto; max-width: 100%; font-family: &quot;apple color emoji&quot;, &quot;segoe ui emoji&quot;, &quot;noto color emoji&quot;, &quot;android emoji&quot;, emojisymbols, &quot;emojione mozilla&quot;, &quot;twemoji mozilla&quot;, &quot;segoe ui symbol&quot;; width: 2rem; padding: 0px 0.2rem; vertical-align: text-bottom;"></a>，<a href="https://emojixd.com/x7pz" target="_blank" title="笑得满地打滚" style="box-sizing: inherit; color: rgb(50, 115, 220);"><img class="emoji" draggable="false" alt="🤣" src="https://twemoji.maxcdn.com/v/12.1.3/72x72/1f923.png" style="box-sizing: inherit; height: auto; max-width: 100%; font-family: &quot;apple color emoji&quot;, &quot;segoe ui emoji&quot;, &quot;noto color emoji&quot;, &quot;android emoji&quot;, emojisymbols, &quot;emojione mozilla&quot;, &quot;twemoji mozilla&quot;, &quot;segoe ui symbol&quot;; width: 2rem; padding: 0px 0.2rem; vertical-align: text-bottom;"></a>，<a href="https://emojixd.com/x7ry" target="_blank" title="放声大哭" style="box-sizing: inherit; color: rgb(50, 115, 220);"><img class="emoji" draggable="false" alt="😭" src="https://twemoji.maxcdn.com/v/12.1.3/72x72/1f62d.png" style="box-sizing: inherit; height: auto; max-width: 100%; font-family: &quot;apple color emoji&quot;, &quot;segoe ui emoji&quot;, &quot;noto color emoji&quot;, &quot;android emoji&quot;, emojisymbols, &quot;emojione mozilla&quot;, &quot;twemoji mozilla&quot;, &quot;segoe ui symbol&quot;; width: 2rem; padding: 0px 0.2rem; vertical-align: text-bottom;"></a>，<a href="https://emojixd.com/x7tb" target="_blank" title="红心" style="box-sizing: inherit; color: rgb(50, 115, 220);"><img class="emoji" draggable="false" alt="❤️" src="https://twemoji.maxcdn.com/v/12.1.3/72x72/2764.png" style="box-sizing: inherit; height: auto; max-width: 100%; font-family: &quot;apple color emoji&quot;, &quot;segoe ui emoji&quot;, &quot;noto color emoji&quot;, &quot;android emoji&quot;, emojisymbols, &quot;emojione mozilla&quot;, &quot;twemoji mozilla&quot;, &quot;segoe ui symbol&quot;; width: 2rem; padding: 0px 0.2rem; vertical-align: text-bottom;"></a>，<a href="https://emojixd.com/x7wy" target="_blank" title="拇指向上" style="box-sizing: inherit; color: rgb(50, 115, 220);"><img class="emoji" draggable="false" alt="👍" src="https://twemoji.maxcdn.com/v/12.1.3/72x72/1f44d.png" style="box-sizing: inherit; height: auto; max-width: 100%; font-family: &quot;apple color emoji&quot;, &quot;segoe ui emoji&quot;, &quot;noto color emoji&quot;, &quot;android emoji&quot;, emojisymbols, &quot;emojione mozilla&quot;, &quot;twemoji mozilla&quot;, &quot;segoe ui symbol&quot;; width: 2rem; padding: 0px 0.2rem; vertical-align: text-bottom;"></a>。同时世界各地中，美国使用的负面表情最多（<a href="https://emojixd.com/x7ry" target="_blank" title="放声大哭" style="box-sizing: inherit; color: rgb(50, 115, 220);"><img class="emoji" draggable="false" alt="😭" src="https://twemoji.maxcdn.com/v/12.1.3/72x72/1f62d.png" style="box-sizing: inherit; height: auto; max-width: 100%; font-family: &quot;apple color emoji&quot;, &quot;segoe ui emoji&quot;, &quot;noto color emoji&quot;, &quot;android emoji&quot;, emojisymbols, &quot;emojione mozilla&quot;, &quot;twemoji mozilla&quot;, &quot;segoe ui symbol&quot;; width: 2rem; padding: 0px 0.2rem; vertical-align: text-bottom;"></a>,<a href="https://emojixd.com/x7s4" target="_blank" title="累死了" style="box-sizing: inherit; color: rgb(50, 115, 220);"><img class="emoji" draggable="false" alt="😩" src="https://twemoji.maxcdn.com/v/12.1.3/72x72/1f629.png" style="box-sizing: inherit; height: auto; max-width: 100%; font-family: &quot;apple color emoji&quot;, &quot;segoe ui emoji&quot;, &quot;noto color emoji&quot;, &quot;android emoji&quot;, emojisymbols, &quot;emojione mozilla&quot;, &quot;twemoji mozilla&quot;, &quot;segoe ui symbol&quot;; width: 2rem; padding: 0px 0.2rem; vertical-align: text-bottom;"></a>,<a href="https://emojixd.com/x7qv" target="_blank" title="翻白眼" style="box-sizing: inherit; color: rgb(50, 115, 220);"><img class="emoji" draggable="false" alt="🙄" src="https://twemoji.maxcdn.com/v/12.1.3/72x72/1f644.png" style="box-sizing: inherit; height: auto; max-width: 100%; font-family: &quot;apple color emoji&quot;, &quot;segoe ui emoji&quot;, &quot;noto color emoji&quot;, &quot;android emoji&quot;, emojisymbols, &quot;emojione mozilla&quot;, &quot;twemoji mozilla&quot;, &quot;segoe ui symbol&quot;; width: 2rem; padding: 0px 0.2rem; vertical-align: text-bottom;"></a>,&nbsp;<a href="https://emojixd.com/x7qn" target="_blank" title="想一想" style="box-sizing: inherit; color: rgb(50, 115, 220);"><img class="emoji" draggable="false" alt="🤔" src="https://twemoji.maxcdn.com/v/12.1.3/72x72/1f914.png" style="box-sizing: inherit; height: auto; max-width: 100%; font-family: &quot;apple color emoji&quot;, &quot;segoe ui emoji&quot;, &quot;noto color emoji&quot;, &quot;android emoji&quot;, emojisymbols, &quot;emojione mozilla&quot;, &quot;twemoji mozilla&quot;, &quot;segoe ui symbol&quot;; width: 2rem; padding: 0px 0.2rem; vertical-align: text-bottom;"></a>），澳大利亚喜爱使用积极的表情，由于文化的差异，中国的网民更偏爱咧嘴笑。可以说礼仪之邦的中国还是比较客套的。但是随着中国互联网社交媒体的发展，这个占比还是有很大变化的可能的。</p><h4 style="box-sizing: inherit; margin: 0px 0px 0.8em; padding: 0px; font-size: 1.25em; color: rgb(54, 54, 54); line-height: 1.125;">最常见的情感<img src="https://emojixd.com/uploads/5863322eaa3a43ad8cf6ff67a656af85.png" alt="最常见的情感（2020全球情感报告）" style="text-indent: 2em; color: rgb(74, 74, 74); font-size: 1.25rem; box-sizing: inherit; height: auto; max-width: 100%;"></h4><p style="box-sizing: inherit; margin-bottom: 1em; padding: 0px; line-height: 1.8; text-indent: 2em;">大家在网上最常见的就是分享快乐和悲伤了，而且这些表情中有一半以上是快乐的。所以说人们是很希望将自己的快乐分享的。当谈到快乐的谈话时，最常使用“爱”，“快乐”和“好”这样的短语，人们常常提到其他人祝他们生日快乐或称赞他们做得很好。这个也和之前的表情相对应。总体而言，与积极表情最相关的人是家人和朋友。而人们常常在愤怒的气氛中提到工作；当谈到恐惧时，可能是在即将来临的考试或是在面试中.......</p><p style="box-sizing: inherit; margin-bottom: 0px; padding: 0px; line-height: 1.8; text-indent: 2em;">通过分析人们常用的emoji可以发现，emoji的出现可以让人们更方便直接的表达自己的情感了，这是一件好事，更快速明了的沟通一定使得人与人之间的连接更加紧密，同时通过分析这些表情也可以对各行各业有着指导性的作用！</p></article>', '', '-1', '0', '0', '2022-03-18 17:50:39', NULL, NULL);
