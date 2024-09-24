export default {
  route: {
    title: "控维通信",
    dashboard: "首页",
    cowave: "控维官网",
    common: {
      title: "通用权限",
      readonly: "只读控制"
    },
    system: {
      title: "系统管理",
      user: {
        title: "用户管理",
        query: "查询",
        new: "新增",
        edit: "修改",
        delete: "删除",
        export: "导出",
        import: "导入",
        diagram: "人员组织架构",
        cache: "刷新缓存",
        grant: "分配角色",
        passwd: "重置密码",
        status: "修改状态"
      },
      dept: {
        title: "部门管理",
        query: "查询",
        new: "新增",
        edit: "修改",
        delete: "删除",
        export: "导出",
        diagram: "部门组织架构",
        cache: "刷新缓存",
        positions: "岗位设置",
        members: "部门成员",
      },
      post: {
        title: "岗位管理",
        query: "查询",
        new: "新增",
        edit: "修改",
        delete: "删除",
        export: "导出",
        diagram: "岗位组织架构",
        cache: "刷新缓存",
      },
      role: {
        title: "角色管理",
        query: "查询",
        new: "新增",
        edit: "修改",
        delete: "删除",
        export: "导出",
        menus: "菜单权限",
        dataScope: "数据权限",
        grant: "分配用户"
      },
      menu: {
        title: "菜单管理",
        query: "查询",
        new: "新增",
        edit: "修改",
        delete: "删除",
        export: "导出",
      },
      dict: {
        title: "字典管理",
        query: "查询",
        new: "新增",
        edit: "修改",
        delete: "删除",
        export: "导出",
        cache: "刷新缓存",
        data: {
          title: "字典数据",
          query: "查询",
          new: "新增",
          edit: "修改",
          delete: "删除",
        }
      },
      config: {
        title: "系统参数",
        query: "查询",
        new: "新增",
        edit: "修改",
        delete: "删除",
        export: "导出",
        cache: "刷新缓存",
      }
    },
    monitor: {
      title: "系统监控"
    },
    tool: {
      title: "系统工具"
    },
    notice: {
      title: "通知公告"
    },
  },
  tag: {
    refresh: "刷新页面",
    close: "关闭页面",
    other: "关闭其他",
    left: "关闭左侧",
    right: "关闭右侧",
    all: "全部关闭"
  },
  content: {
    hide: "隐藏",
    show: "显示",
    enable: "启用",
    disable: "停用",
    search_hide: "隐藏搜索",
    search_show: "显示搜索",
    column_choose: "选择列",
    success: "成功",
    failed: "失败",
    profile: "个人中心",
    layout: "偏好设置",
    logout: "退出登录",
    set: "设置",
    cancel: "取消"
  },
  button: {
    search: "搜索",
    reset: "重置",
    refresh: "刷新",
    cache: "刷新缓存",
    expand: "展开",
    collapse: "折叠",
    add: "新增",
    edit: "修改",
    delete: "删除",
    input: "导入",
    output: "导出",
    confirm: "确定",
    cancel: "取消",
    save: "保存",
    close: "关闭",
    upload: "上传",
    more: "更多",
    check: "全选/全不选",
    parent: "父子联动"
  },
  label: {
    index: "序号",
    status: "状态",
    remark: "备注",
    option: "操作",
    readonly: "只读",
    time_begin: "开始时间",
    time_end: "结束时间",
    date_begin: "开始日期",
    date_end: "结束日期",
    date_create: "创建时间"
  },
  theme: {
    title: "主题风格",
    color: "主题颜色",
    layout: "系统布局",
    enable: "开启",
    fix: "固定",
    show: "显示",
    dynamic: "动态标题"
  },
  msg: {
    success_create: "新增成功",
    success_edit: "修改成功",
    success_delete: "删除成功",
    success_reset: "重置成功",
    success_grant: "授权成功",
    success_refresh: "刷新成功",
    logout: "确定注销并退出系统吗？"
  },
  user:{
    profile: "个人信息",
    basic: "基本信息",
    info: "用户信息",
    roles: "角色信息",
    excel: "用户数据",
    template: "用户数据模板",
    avatar_failed: "文件格式错误，请上传图片类型，如：JPG，PNG",
    label: {
      id: "用户Id",
      name: "用户名称",
      account: "用户账号",
      passwd: "用户密码",
      phone: "手机号码",
      email: "邮箱",
      sex: "性别",
      rank: "职级",
      status: "状态",
      login_ip: "登录IP",
      login_time: "登录时间",
      dept: "部门/岗位",
      post: "用户岗位",
      role: "用户角色",
      report: "汇报对象",
      pwd_old: "旧密码",
      pwd_new: "新密码",
      pwd_confirm: "确认密码",
    },
    placeholder: {
      dept_choose: "请选择部门/岗位",
      report: "请选择汇报对象",
      account: "请输入用户账号",
      passwd: "请输入用户密码",
      dept: "请输入部门名称",
      name: "请输入用户名称",
      phone: "请输入手机号码",
      email: "请输入邮箱地址",
      status: "选择用户状态",
      sex: "请选择性别",
      role: "请选择角色",
      rank: "请选择职级",
      post: "请选择岗位",
      pwd_old: "请输入旧密码",
      pwd_new: "请输入新密码",
      pwd_confirm: "请确认密码"
    },
    dialog: {
      diagram: "人员组织架构",
      title_new: "新增用户",
      title_edit: "修改用户",
      title_import: "导入用户",
      title_pwd: "修改密码",
      title_avatar: "修改头像",
      text_import1: "将文件拖到此处，或",
      text_import2: "点击上传",
      text_import3: "",
      text_import4: "是否更新已经存在的用户数据",
      text_import5: "仅允许导入xls、xlsx格式文件, ",
      text_import6: "下载模板",
      text_import7: "导入结果",
    },
    rules: {
      account: "用户账号不能为空",
      name1: "用户名称不能为空",
      name2: "用户名称长度必须介于 2 和 20 之间",
      pwd1: "用户密码不能为空",
      pwd2: "密码长度必须介于 6 和 20 之间",
      email: "请输入正确的邮箱地址",
      phone: "请输入正确的手机号码",
      pwd_old: "旧密码不能为空",
      pwd_new: "新密码不能为空",
      pwd_confirm: "确认密码不能为空",
      pwd_compare: "两次输入的密码不一致"
    },
    msg: {
      confirm_status: "确认要{var1}用户[{var2}]吗？",
      confirm_readonly: "确认要{var1}用户[{var2}]数据为只读吗？",
      reset_pwd: "请输入用户[{var1}]的新密码",
      confirm_delete: "确认删除用户[{var1}]？",
      select_delete: "确认删除所选用户？"
    }
  },
  menu: {
    excel: "菜单数据",
    label: {
      root: "根目录",
      name: "菜单名称",
      status: "菜单状态",
      order: "显示排序",
      component: "组件路径",
      permission: "权限标识",
      parent: "上级菜单",
      type: "菜单类型",
      icon: "菜单图标",
      frame: "是否内链",
      visiable: "是否显示",
      path: "路由地址",
      cacheable: "是否缓存",
      param: "路由参数",
      visibility: "访问控制"
    },
    placeholder: {
      parent: "选择上级菜单",
      name: "请输入菜单名称",
      status: "选择菜单状态",
      en: "请输入菜单英文名",
      icon: "选择菜单图标",
      path: "请输入路由地址",
      param: "请输入路由参数",
      permission: "请输入权限标识",
      component: "请输入组件路径"
    },
    rules: {
      name: "菜单名称不能为空",
      path: "路由地址不能为空",
      order: "菜单顺序不能为空"
    },
    dialog: {
      title_new: "新增菜单",
      title_edit: "修改菜单",
      parent: "选择上级菜单"
    },
    content: {
      name: "名称支持国际化，可以定义成国际化资源中的key",
      status: "停用的路由不会出现在侧边栏，也不能被访问",
      frame: "外链路由需要以`http(s)://`开头",
      visiable: "隐藏的路由不会出现在侧边栏，但仍然可以访问",
      cacheable: "缓存的路由会被`keep-alive`缓存，需要匹配组件的`name`和地址保持一致",
      param: "访问路由的默认参数，如：`{\"id\": 1, \"name\": \"cowave\"}`",
      visibility: "公开的菜单可以被任何人访问",
      permission: "如果菜单是受保护的，则访问时需要此权限定义",
      component: "访问的组件路径，如：system/user/index，默认在views目录下"
    },
    msg: {
      confirm_delete: "确认要删除菜单[{var1}]吗？",
      confirm_readonly: "确认要{var1}菜单[{var2}]数据为只读吗？",
    }
  },
  config: {
    excel: "系统参数",
    label: {
      name : "参数名称",
      key: "参数标识",
      value: "参数值",
      remark: "备注",
      default: "系统内置",
      parser: "值转换器",
      param: "转换参数"
    },
    placeholder: {
      name: "请输入参数名称",
      key: "请输入参数标识",
      value: "请输入参数值",
      parser: "请输入配置值转换器类名",
      param: "请输入配置值转换参数"
    },
    content: {
      parser: "ValueParser的实现类",
      param: "转换参数类型为字符串"
    },
    dialog: {
      title_new: "新增配置",
      title_edit: "修改配置"
    },
    rules: {
      name: "参数名称不能为空",
      key: "参数标识不能为空",
      value: "参数值不能为空"
    },
    msg: {
      confirm_delete: "确认删除配置[{var1}]？",
      select_delete: "确认删除所选配置？"
    }
  },
  dict: {
    excel: "字典数据",
    label: {
      css: "字典样式",
      default: "是否默认",
      name: "字典名称",
      english: "英文名称",
      code: "字典编码",
      value: "字典值",
      order: "字典排序",
      group: "字典分组",
      type: "字典类型",
      groupname: "分组名称",
      typename: "类型名称",
      groupcode: "分组编码",
      typecode: "类型编码",
    },
    placeholder: {
      groupname: "请输入分组名称",
      typename: "请输入类型名称",
      groupcode: "请输入分组编码",
      typecode: "请输入类型编码",
      name: "请输入字典名称",
      english: "请输入英文名称",
      code: "请输入字典编码",
      value: "请输入字典值",
    },
    rules: {
      groupname: "分组名称不能为空",
      typename: "类型名称不能为空",
      groupcode: "分组编码不能为空",
      typecode: "类型编码不能为空",
      name: "字典名称不能为空",
      english: "英文名称不能为空",
      code: "字典编码不能为空",
      value: "字典值不能为空",
    },
    dialog: {
      title_new: "新增字典",
      title_edit: "修改字典",
    },
    msg: {
      confirm_delete: "确认删除字典[{var1}]？删除操作将会删除所有关联的子字典！",
      select_delete: "确认删除所选字典？删除操作将会删除所有关联的子字典！",
      type_confirm_readonly: "确认要{var1}字典[{var2}]为只读吗？",
    }
  },
  post: {
    excel: "岗位数据",
    label: {
      name: "岗位名称",
      type: "岗位类型",
      parent: "上级岗位",
      diagram: "岗位组织架构",
      level: "岗位级别",
      status: "岗位状态"
    },
    placeholder: {
      name: "请输入岗位名称",
      type: "选择岗位类型",
      parent: "选择上级岗位",
    },
    rules: {
      name: "岗位名称不能为空",
      type: "岗位类型不能为空",
      level: "岗位级别不能为空",
    },
    dialog: {
      diagram: "岗位组织架构",
      title_new: "新增岗位",
      title_edit: "修改岗位",
    },
    msg: {
      confirm_delete: "确认删除岗位[{var1}]？",
      select_delete: "确认删除所选岗位？",
      confirm_readonly: "确认要{var1}岗位[{var2}]数据为只读吗？",
    },
  },
  dept: {
    excel: "部门数据",
    info: "部门信息",
    label: {
      name: "部门名称",
      phone: "部门电话",
      leader: "部门负责人",
      addr: "部门地址",
      member: "部门成员",
      position: "岗位设置",
      parent: "上级部门"
    },
    placeholder: {
      name: "请输入部门名称",
      phone: "请输入部门电话",
      addr: "请输入部门地址",
      parent: "选择上级部门"
    },
    dialog: {
      diagram: "部门组织架构",
      title_new: "新增部门",
      title_edit: "修改部门",
    },
    msg: {
      confirm_delete: "确认删除部门[{var1}]？",
      confirm_readonly: "确认要{var1}部门[{var2}]数据为只读吗？",
    },
    rules: {
      name: "部门名称不能为空",
      parent: "上级部门不能为空"
    },
    position:{
      info: "岗位信息",
      name: "岗位名称",
      type: "岗位类型",
      default: "设为默认岗位"
    },
    user: {
      name: "人员名称",
      rank: "人员职级",
      position: "岗位设置",
      default: "设为用户默认岗位",
      leader: "设为部门负责人"
    }
  },
  role: {
    menus: "菜单权限",
    scope: "数据权限",
    menu_choose: "选择菜单: ",
    label: {
      id: "角色Id",
      name: "角色名称",
      code: "角色编码",
    },
    placeholder: {
      name: "请输入角色名称",
      code: "请输入角色编码",
    },
    msg: {
      confirm_delete: "确认删除角色[{var1}]？",
      select_delete: "确认删除所选角色？",
      confirm_readonly: "确认要{var1}角色[{var2}]数据为只读吗？",
      remove_success: "取消授权成功",
      grant_success: "授权成功",
      unselect: "未选择用户",
      confirm_remove: "确认取消用户[{var1}]的角色？",
      select_remove: "确认取消所选用户的角色？",
    },
    dialog: {
      new: "新增角色",
      edit: "修改角色",
    },
    rules: {
      name: "角色名称不能为空",
      code: "角色编码不能为空"
    },
    user: {
      label: {
        name: "用户名称",
        phone: "手机号码",
        rank: "职级",
        dept: "部门",
        post: "岗位"
      },
      button: {
        add: "添加用户",
        choose: "选择用户",
        remove: "取消授权"
      }
    }
  },
}
