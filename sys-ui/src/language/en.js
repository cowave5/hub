export default {
  route: {
    title: "Cowave",
    dashboard: "Dashboard",
    cowave: "Cowave",
    common: {
      title: "common",
      readonly: "Change Readonly"
    },
    system: {
      title: "System Manage",
      user: {
        title: "User",
        query: "Query",
        new: "New",
        edit: "Edit",
        delete: "Delete",
        export: "Export",
        import: "Import",
        diagram: "Diagram",
        cache: "Refresh Cache",
        grant: "Grant Role",
        passwd: "Reset Password",
        status: "Change Status"
      },
      dept: {
        title: "Department",
        query: "Query",
        new: "New",
        edit: "Edit",
        delete: "Delete",
        export: "Export",
        diagram: "Diagram",
        cache: "Refresh Cache",
        positions: "Positions",
        members: "Members",
      },
      post: {
        title: "Post",
        query: "Query",
        new: "New",
        edit: "Edit",
        delete: "Delete",
        export: "Export",
        diagram: "Diagram",
        cache: "Refresh Cache",
      },
      role: {
        title: "Role",
        query: "Query",
        new: "New",
        edit: "Edit",
        delete: "Delete",
        export: "Export",
        menus: "Menus",
        dataScope: "Data Scope",
        grant: "Grant User"
      },
      menu: {
        title: "Menu",
        query: "Query",
        new: "New",
        edit: "Edit",
        delete: "Delete",
        export: "Export",
      },
      dict: {
        title: "Dictionary",
        query: "Query",
        new: "New",
        edit: "Edit",
        delete: "Delete",
        export: "Export",
        cache: "Refresh Cache",
        data: {
          title: "Dictionary Data",
          query: "Query",
          new: "New",
          edit: "Edit",
          delete: "Delete",
        }
      },
      config: {
        title: "Configuration",
        query: "Query",
        new: "New",
        edit: "Edit",
        delete: "Delete",
        export: "Export",
        cache: "Refresh Cache",
      }
    },
    monitor: {
      title: "System Monitor"
    },
    tool: {
      title: "System Tool"
    },
    notice: {
      title: "Notice"
    },
  },
  tag: {
    refresh: "refresh",
    close: "close",
    other: "close other tabs",
    left: "close tabs to the left",
    right: "close tabs to the right",
    all: "close all tabs"
  },
  content: {
    hide: "Hide",
    show: "Show",
    enable: "enable",
    disable: "disable",
    search_hide: "Hide Search",
    search_show: "Show Search",
    column_choose: "Choose Column",
    success: " success",
    failed: " failed",
    profile: "Your profile",
    layout: "Preference",
    logout: "Sign out",
    set: "set",
    cancel: "cancel"
  },
  button: {
    search: "Search",
    reset: "Reset",
    refresh: "Refresh",
    cache: "Refresh Cache",
    expand: "Expand",
    collapse: "Collapse",
    add: "New",
    edit: "Edit",
    delete: "Delete",
    input: "Import",
    output: "Export",
    confirm: "Confirm",
    cancel: "Cancel",
    save: "Save",
    close: "Close",
    upload: "Upload",
    more: "More",
    check: "Check All/Cancle All",
    parent: "Parent-Child"
  },
  label: {
    index: "Index",
    status: "Status",
    remark: "Remark",
    option: "Option",
    readonly: "Readonly",
    time_begin: "Time Begin",
    time_end: "Time End",
    date_begin: "Date Begin",
    date_end: "Date End",
    date_create: "Create Date"
  },
  theme: {
    title: "Theme",
    color: "Color",
    layout: "Layout",
    enable: "Enable",
    fix: "Fix",
    show: "Show",
    dynamic: "Dynamic Title"
  },
  msg: {
    success_create: "Create success",
    success_edit: "Edit success",
    success_delete: "Delete success",
    success_reset: "Reset success",
    success_grant: "Grant success",
    success_refresh: "Refresh success",
    logout: "Are you sure you want to sign out?"
  },
  user: {
    profile: "Profile",
    basic: "Information",
    info: "User",
    roles: "Roles",
    excel: "user",
    template: "template_user",
    avatar_failed: "Invalid file format, please upload the image type, such as: JPG, PNG",
    label: {
      id: "Id",
      name: "Name",
      account: "Account",
      passwd: "Password",
      phone: "Phone",
      email: "Email",
      sex: "Sex",
      rank: "Rank",
      status: "Status",
      dept: "Dept/Post",
      post: "Post",
      role: "Role",
      report: "Report to",
      pwd_old: "Old Password",
      pwd_new: "New Password",
      pwd_confirm: "Confirm Password",
      login_ip: "Login Ip",
      login_time: "Login Time",
    },
    placeholder: {
      dept_choose: "Choose Dept/Post",
      report: "Choose Report User",
      account: "Input User Account",
      passwd: "Input User Passwd",
      dept: "Input Department",
      name: "Input User Name",
      phone: "Input User Phone",
      email: "Input User Email",
      status: "Choose User Status",
      sex: "Choose Sex",
      rank: "Choose Rank",
      role: "Choose Role",
      post: "Choose Post",
      pwd_old: "Input old password",
      pwd_new: "Input new password",
      pwd_confirm: "Confirm new password"
    },
    dialog: {
      diagram: "User Diagram",
      title_new: "New User",
      title_edit: "Edit User",
      title_import: "Import User",
      title_pwd: "Reset Password",
      title_avatar: "Change Avatar",
      text_import1: "Drag files here, or",
      text_import2: " click here ",
      text_import3: "to upload",
      text_import4: "Whether to overwrite if user exist",
      text_import5: "Only accept files in xlsx and xls formats, ",
      text_import6: "Template Download",
      text_import7: "Import Result",
    },
    rules: {
      account: "user account can't be empty",
      name1: "user name can't be empty",
      name2: "user name length must be between 2 and 20",
      pwd1: "user password can't be empty",
      pwd2: "password length must be between 6 and 20",
      email: "invalid email",
      phone: "invalid phone number",
      pwd_old: "old password can't be empty",
      pwd_new: "new password can't be empty",
      pwd_confirm: "confirm password can't be empty",
      pwd_compare: "the two entered passwords doesn't match"
    },
    msg: {
      confirm_status: "Are you sure to {var1} user[{var2}]?",
      confirm_readonly: "Are you sure to {var1} user[{var2}] as readonly？",
      reset_pwd: "Input new password of user[{var1}]",
      confirm_delete: "Are you sure to delete user[{var1}]？",
      select_delete: "Are you sure to delete the selected user？"
    }
  },
  menu: {
    excel: "menu",
    label: {
      root: "root",
      name: "Name",
      status: "Status",
      icon: "Icon",
      order: "Order",
      component: "Component",
      permission: "Permission",
      parent: "Parent",
      type: "Type",
      frame: "Frame",
      visiable: "Visiable",
      path: "Path",
      cacheable: "Cacheable",
      param: "Param",
      visibility: "Visibility"
    },
    placeholder: {
      parent: "Choose Parent Menu",
      name: "Input Menu Name",
      status: "Choose Status",
      en: "Input English Name",
      icon: "Choose Menu Icon",
      path: "Input Route Path",
      param: "Input Route Param",
      permission: "Input Route Permission",
      component: "Input Component Path"
    },
    rules: {
      name: "menu name can't be empty",
      path: "route path can't be empty",
      order: "menu order can't be empty"
    },
    dialog: {
      title_new: "New Menu",
      title_edit: "Edit Menu"
    },
    content: {
      name: "The name supports internationalization and can be defined as the key in internationalized resource",
      status: "The disabled route will not appear in the sidebar and can't be accessed",
      frame: "The external route needs to start with `http(s)://`",
      visiable: "The hidden route will not appear in the sidebar but still can be accessed",
      cacheable: "The cached route will be cached by `keep-alive`, but it's `name` and address of component need to be consistent",
      param: "The default parameters when you access route，such as：`{\"id\": 1, \"name\": \"cowave\"}`",
      visibility: "The public menu can be accessed by anyone",
      permission: "The permission is required when access if the menu is protected",
      component: "The path of the route component, such as: system/user/index, which is in the views directory by default"
    },
    msg: {
      confirm_delete: "Are you sure to delete menu[{var1}]?",
      confirm_readonly: "Are you sure to {var1} menu[{var2}] as readonly？",
    }
  },
  config: {
    excel: "system_config",
    label: {
      name : "Name",
      key: "Key",
      value: "Value",
      remark: "Remark",
      default: "System",
      parser: "Parser",
      param: "Param"
    },
    placeholder: {
      name : "Input config name",
      key: "Input config key",
      value: "Input config value",
      parser: "Input class name of value parser",
      param: "Input param of value parser"
    },
    content: {
      parser: "Implementation class of ValueParser",
      param: "parameter type is string"
    },
    dialog: {
      title_new: "New Config",
      title_edit: "Edit Config"
    },
    rules: {
      name: "config name can't be empty",
      key: "config key can't be empty",
      value: "config value can't be empty"
    },
    msg: {
      confirm_delete: "Are you sure to delete config[{var1}]？",
      select_delete: "Are you sure to delete the selected config？"
    }
  },
  dict: {
    excel: "dictionary",
    label: {
      css: "Css-Type",
      default: "Default",
      name: "Name",
      english: "English",
      code: "Code",
      value: "Value",
      order: "Order",
      group: "Group",
      type: "Type",
      groupname: "Group Name",
      typename: "Type Name",
      groupcode: "Group Code",
      typecode: "Type Code",
    },
    placeholder: {
      groupname: "Input group name",
      typename: "Input type name",
      groupcode: "Input group code",
      typecode: "Input type code",
      name: "Input dictionary name",
      english: "Input english name",
      code: "Input dictionary code",
      value: "Input dictionary value",
    },
    rules: {
      groupname: "group name can't be empty",
      typename: "type name can't be empty",
      groupcode: "group code can't be empty",
      typecode: "type code can't be empty",
      name: "dictionary name can't be empty",
      english: "english name can't be empty",
      code: "dictionary code can't be empty",
      value: "dictionary value can't be empty",
    },
    dialog: {
      title_new: "New Dictionary",
      title_edit: "Edit Dictionary",
    },
    msg: {
      confirm_delete: "Are you sure to delete dictionary[{var1}]？The operation will delete all associated subdictionaries",
      select_delete: "Are you sure to delete the selected dictionary？The operation will delete all associated subdictionaries",
      type_confirm_readonly: "Are you sure to {var1} dictionary[{var2}] as readonly？",
    }
  },
  post: {
    excel: "post",
    label: {
      name: "Name",
      type: "Type",
      parent: "Parent-post",
      diagram: "Diagram",
      level: "Level",
      status: "Status"
    },
    placeholder: {
      name: "Input post name",
      type: "Choose post type",
      parent: "Choose parent post",
    },
    rules: {
      name: "post name can't be empty",
      type: "post type can't be empty",
      level: "post level can't be empty",
    },
    dialog: {
      diagram: "Post Diagram",
      title_new: "New Post",
      title_edit: "Edit Post",
    },
    msg: {
      confirm_delete: "Are you sure to delete post[{var1}]？",
      select_delete: "Are you sure to delete the selected post？",
      confirm_readonly: "Are you sure to {var1} post[{var2}] as readonly？",
    }
  },
  dept: {
    excel: "department",
    info: "Department",
    label: {
      name: "Name",
      phone: "Phone",
      leader: "Leader",
      addr: "Address",
      member: "Members",
      position: "Positions",
      parent: "Parent"

    },
    placeholder: {
      name: "Input department name",
      phone: "Input phone number",
      addr: "Input department address",
      parent: "Choose parent department"
    },
    dialog: {
      diagram: "Department Diagram",
      title_new: "New Department",
      title_edit: "Edit Department",
    },
    msg: {
      confirm_delete: "Are you sure to delete department[{var1}]？",
      confirm_readonly: "Are you sure to {var1} department[{var2}] as readonly？",
    },
    rules: {
      name: "department name can't be empty",
      parent: "parent department can't be empty"
    },
    position:{
      info: "Positions",
      name: "Position Name",
      type: "Position Type",
      default: "Set As Default Position"
    },
    user: {
      name: "User Name",
      rank: "User Rank",
      position: "Position",
      default: "Make User Default Position",
      leader: "Set As Department Leader"
    }
  },
  role: {
    menus: "Role Menus",
    scope: "Role Data Scope",
    menu_choose: "Choose: ",
    label: {
      id: "Id",
      name: "Role Name",
      code: "Role Code",
    },
    placeholder: {
      name: "Input role name",
      code: "Input role code",
    },
    msg: {
      confirm_delete: "Are you sure to delete role[{var1}]？",
      select_delete: "Are you sure to delete the selected role？",
      confirm_readonly: "Are you sure to {var1} role[{var2}] as readonly？",
      remove_success: "Remove Grant Success",
      grant_success: "Grant Success",
      unselect: "No user selected yet",
      confirm_remove: "Are you sure to remove the role of user[{var1}]?",
      select_remove: "Are you sure to remove the role of selected user？",
    },
    dialog: {
      new: "New Role",
      edit: "Edit Role",
    },
    rules: {
      name: "role name can't be empty",
      code: "role code can't be empty",
    },
    user: {
      label: {
        name: "User Name",
        phone: "User Phone",
        rank: "Rank",
        dept: "Department",
        post: "Post"
      },
      button: {
        add: "Add User",
        choose: "Choose User",
        remove: "Remove Grant"
      }
    }
  },
}
