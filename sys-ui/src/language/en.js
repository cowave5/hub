export default {
  commons: {
    label: {
      index: "Index",
      status: "Status",
      remark: "Remark",
      options: "Options",
      createUser: "Create User",
      createTime: "Create Time",
      updateTime: "Update Time",
      beginDate: "Begin Date",
      endDate: "End Date",
    },
    button: {
      refresh: "Refresh",
      search: "Search",
      search_hide: "Hide Search",
      search_show: "Show Search",
      reset: "Reset",
      hide: "Hide",
      show: "Show",
      enable: "Enable",
      disable: "Disable",
      save: "Save",
      cancel: "Cancel",
      confirm: "Confirm",
      close: "Close",
      status: 'Change Status',
      query: "Query",
      detail: "Detail",
      create: "New",
      edit: "Edit",
      delete: "Delete",
      clean: "Clean",
      remove: "Remove",
      preview: "Preview",
      download: "Download",
      publish: "Publish",
      recall: "Recall",
      exec: "Execute",
      config: "Config",
      test: "Test",
      quit: "Quit",
      upload: "Upload",
      import: "Import",
      export: "Export",
      cache: "Refresh Cache",
      diagram: "Diagram",
      expand: "Expand",
      collapse: "Collapse",
      more: "More",
      logout: "Sign out",
      check: "Check All/Cancel All",
      parent: "Parent-Child",
      select_column: "Select Column",
    },
    menu: {
      root: "Cowave",
      cowave: "Cowave",
      dashboard: "Dashboard",
      meter: {
        root: "Metersphere",
        ui: "Ui Test",
        form: "Form Builder",
        template: {
          root: "Code Template",
          application: "Application",
          model: "Model",
          database: "Database",
          table: "Table"
        }
      },
      flow: {
        root: "Workflow",
        manage: 'Configuration',
        model: 'Design',
        deploy: 'Deploy',
        instance: 'Instance',
        owner: {
          task: 'Workbench',
          leave: 'Leave',
          meeting: 'Meeting',
          purchase: 'Purchase'
        }
      },
      monitor: {
        root: "System Monitor",
        actuator: "Actuator",
        grafana: "Grafana",
        prometheus: "prometheus",
        alert: "Alarm",
        nacos: "Nacos",
        log: "Operation Log",
        online: "Online user",
        cache: "Redis Cache",
      },
      sys: {
        root: "System Manage",
        tenant: "Tenant",
        user: 'User',
        role: "Role",
        menu: "Menu",
        scope: "Data Scope",
        attach: "Attachment",
        dept: "Dept",
        post: "Post",
        dict: "Dictionary",
        config: "Config",
        ldap: "Ldap",
        doc: {
          api: "Api Doc",
          admin: "System Manage",
          job: "Schedule Job",
          meter: "Meter Manage"
        },
        schedule: {
          root: "Schedule Task",
          refresh: "Refresh",
          logQuery: "Log Query",
          logExport: "Log Export",
          logDelete: "Log Delete"
        },
        oauth2: {
          root: "OAuth2",
          client: "Applications",
          gitlab: "Gitlab",
          userQuery: "User Query",
          userEdit: "User Edit",
          userDelete: "User Delete"
        },
      },
    },
    confirm: {
        logout: 'Sure to sign out?'
    },
    msg: {
      success: {
        create: 'Create success',
        edit: 'Edit success',
        delete: 'Delete success',
        reset: 'Reset success',
        grant: 'Grant success',
        refresh: 'Refresh success',
      },
    },
    theme: {
      title: 'Theme',
      color: 'Color',
      enable: 'Enable',
      fix: 'Fix',
      show: 'Show',
      dynamic: 'Dynamic Title',
      preference: "Preference",
      profile: "Profile",
      notice: "Notice",
      token: "Access Tokens",
    },
    frame: {
      layout: 'Layout',
      refresh: 'refresh',
      close: 'close',
      close_all: 'close all tabs',
      close_other: 'close other tabs',
      close_left: 'close tabs to the left',
      close_right: 'close tabs to the right',
    },
    text: {
      limit: "Limit",
      format: "Format"
    }
  },
  tenant: {
    title: {
      system: 'Cowave',
      cowave: 'Cowave'
    },
    label: {
      id: "Tenant Key",
      name: "Tenant Name",
      title: "Tenant Title",
      logo: "Logo",
      phone: "Phone",
      email: "Email",
      contact: "Contact",
      staff: "Staff Size",
      addr: "Address",
      status: "Status",
      expire: "Expire",
    },
    button: {
      manager: 'Managers',
      manager_add: "New Managers",
      manager_remove: "Remove Managers",
    },
    dialog: {
      new: "New Tenant",
      edit: "Edit Tenant",
    },
    rules: {
      id: "Tenant key can't be empty",
      title: "Tenant title can't be empty",
      name: "Tenant name can't be empty",
    },
    confirm: {
      status_enable: "Sure to enable tenant \"{arg1}\" ?",
      status_disable: "Sure to disable tenant \"{arg1}\" ?",
      remove_select: "Sure to remove the selected manager ?",
      remove_manager: "Sure to remove manager \"{arg1}\" ?",
    },
    text: {
      info: "Tenant Info"
    },
    msg: {
      remove_success: "Remove manager success",
    },
  },
  user: {
    label: {
      id: "Id",
      name: "Name",
      account: "Account",
      passwd: "Password",
      sex: "Sex",
      phone: "Phone",
      email: "Email",
      rank: "Rank",
      status: "Status",
      dept: "Dept/Post",
      post: "Post",
      role: "Role",
      report: "Report to",
      pwd_old: "Old Password",
      pwd_new: "New Password",
      pwd_confirm: "Confirm Password",
    },
    button: {
      grant: 'Grant Role',
      passwd: 'Reset Password',
      add: "Add User",
      remove: "Remove User",
      select: "Select User"
    },
    placeholder: {
      account: "Input User Account",
      passwd: "Input User Passwd",
      name: "Input User Name",
      phone: "Input User Phone",
      email: "Input User Email",
      status: "Select User Status",
      sex: "Select Sex",
      dept: "Select Dept/Post",
      role: "Select Role",
      rank: "Select Rank",
      post: "Select Post",
      report: "Select Report User",
      pwd_old: "Input old password",
      pwd_new: "Input new password",
      pwd_confirm: "Confirm new password"
    },
    dialog: {
      diagram: "User Diagram",
      new: "New User",
      edit: "Edit User",
      passwd: "Reset Password",
      avatar: "Change Avatar",
      import: "Import User",
      import_text1: "Drag files here, or",
      import_text2: " click here ",
      import_text3: "to upload",
      import_text4: "Whether to overwrite if user exist",
      import_text5: "Only accept files in xlsx and xls formats, ",
      import_text6: "Template Download",
      import_text7: "Import Result",
    },
    rules: {
      account: "User account can't be empty",
      name: "User name can't be empty",
      name_len: "User name length must be between 2 and 20",
      passwd: "User password can't be empty",
      passwd_len: "Password length must be between 6 and 20",
      email: "Invalid email",
      phone: "Invalid phone number",
      pwd_old: "Old password can't be empty",
      pwd_new: "New password can't be empty",
      pwd_confirm: "Confirm password can't be empty",
      pwd_compare: "The two entered passwords doesn't match"
    },
    confirm: {
      status_enable: "Sure to enable user \"{arg1}\" ?",
      status_disable: "Sure to disable user \"{arg1}\" ?",
      delete: "Sure to delete user \"{arg1}\" ?",
      delete_select: "Sure to delete the selected user ?",
      remove: "Sure to remove user \"{arg1}\" ?",
      remove_select: "Sure to remove the selected user ?",
      passwd: "Input new password of user \"{arg1}\"",
    },
    msg: {
      avatar_failed: "Invalid file format, please upload the image type, such as: JPG, PNG"
    },
    text: {
      profile: "Profile",
      info: "User Info",
      basic: "Basic Info",
      mfa: "MFA Config",
      data: "user",
      list: "Members",
      template: "user_template",
      unselect: "No user selected yet",
    },
  },
  role: {
    label: {
      id: "Id",
      name: "Role Name",
      code: "Role Code",
    },
    button: {
      menus: 'Menus',
      scope: 'Data Scope',
      remove: "Remove Grant",
      members: 'Members',
      members_grant: 'Members remove',
      members_cancel: 'Members Grant',
    },
    placeholder: {
      name: "Input role name",
      code: "Input role code",
    },
    dialog: {
      new: "New Role",
      edit: "Edit Role",
    },
    rules: {
      name: "role name can't be empty",
      code: "role code can't be empty",
    },
    confirm: {
      delete: "Sure to delete role \"{arg1}\" ?",
      delete_select: "Sure to delete the selected role ?",
      remove_grant: "Sure to remove the role of user \"{arg1}\" ?",
      remove_grant_select: "Sure to remove the role of selected user ?",
    },
    msg: {
      grant: "Grant Success",
      remove_grant: "Remove Grant Success",
    },
    text: {
      info: "Role Info",
      members: "Members",
      data: "role"
    },
  },
  menu: {
    label: {
      root: "root",
      name: "Name",
      tenant: "Tenant",
      module: "Module",
      status: "Status",
      icon: "Icon",
      order: "Order",
      component: "Component",
      permission: "Permission",
      parent: "Parent",
      type: "Type",
      frame: "Frame",
      visible: "Visible",
      path: "Path",
      cacheable: "Cacheable",
      param: "Param",
      visibility: "Visibility",
      shared: "Shared Menu"
    },
    button: {
      select: "Select: ",
    },
    placeholder: {
      name: "Input Menu Name",
      status: "Select Menu Status",
      parent: "Select Parent Menu",
      icon: "Select Menu Icon",
      path: "Input Route Path",
      param: "Input Route Param",
      permission: "Input Route Permission",
      component: "Input Component Path"
    },
    rules: {
      name: "menu name can't be empty",
      path: "route path can't be empty",
      order: "menu order can't be empty",
      tenant: "menu tenant can't be empty"
    },
    dialog: {
      new: "New Menu",
      edit: "Edit Menu"
    },
    confirm: {
      delete: "Sure to delete menu \"{arg1}\" ?",
    },
    content: {
      name: "The name supports internationalization and can be defined as the key in internationalized resource",
      module: "For menus and buttons, filter the data permission options",
      status: "The disabled route will not appear in the sidebar and can't be accessed",
      frame: "The external route needs to start with `http(s)://`",
      visible: "The hidden route will not appear in the sidebar but still can be accessed",
      cacheable: "The cached route will be cached by `keep-alive`, but it's `name` and address of component need to be consistent",
      param: "The default parameters when you access route，such as：`{\"id\": 1, \"name\": \"cowave\"}`",
      visibility: "The public menu can be accessed by anyone",
      permission: "The permission is required when access if the menu is protected",
      component: "The path of the route component, such as: system/user/index, which is in the views directory by default"
    },
    text: {
      data: "menu",
    },
  },
  dept: {
    label: {
      name: "Name",
      phone: "Phone",
      leader: "Leader",
      addr: "Address",
      parent: "Parent"
    },
    button: {
      members: "Members",
      members_add: "Add Members",
      members_remove: "Remove Members",
      positions: "Positions",
      positions_add: "Add Positions",
      positions_remove: "Remove Positions",
    },
    placeholder: {
      name: "Input department name",
      phone: "Input phone number",
      addr: "Input department address",
    },
    dialog: {
      diagram: "Department Diagram",
      new: "New Department",
      edit: "Edit Department",
    },
    rules: {
      name: "department name can't be empty",
      parent: "parent department can't be empty"
    },
    confirm: {
      delete: "Sure to delete department \"{arg1}\" ?",
    },
    text: {
      name: "Department",
      info: "Dept Info",
      data: "department",
      leader: "Department Leader",
      default_post: "Default Post",
      default_user_post: "User Default Post",
    },
  },
  post: {
    label: {
      name: "Name",
      type: "Type",
      parent: "Parent Post",
      level: "Level",
      status: "Status",
    },
    button: {
      add: "Add Post",
      remove: "Remove Post",
      select: "Select Post"
    },
    placeholder: {
      name: "Input post name",
      type: "Select post type",
      parent: "Select parent post",
    },
    dialog: {
      diagram: "Post Diagram",
      new: "New Post",
      edit: "Edit Post",
    },
    rules: {
      name: "post name can't be empty",
      type: "post type can't be empty",
      level: "post level can't be empty",
    },
    confirm: {
      delete: "Sure to delete post \"{arg1}\" ?",
      delete_select: "Sure to delete the selected post ?",
      remove: "Sure to remove post \"{arg1}\" ?",
      remove_select: "Sure to remove the selected post ?",
    },
    text: {
      name: "Post",
      list: "Positions",
      data: "post",
      unselect: "No post selected yet",
    },
  },
  dict: {
    label: {
      name: "Name",
      code: "Code",
      type: "Type",
      order: "Order",
      css: "Css",
      value: "Value",
      valueType: "Value Type",
      parser: "Value Parser",
      default: "Default",
      parent: "Parent Dict"
    },
    placeholder: {
      name: "Input dictionary name",
      code: "Input dictionary code",
      value: "Input dictionary value",
      parser: "Input class name of dictionary value parser",
    },
    dialog: {
      new: "New Dictionary",
      edit: "Edit Dictionary",
    },
    button: {
      reset: "Reset Cache",
    },
    rules: {
      groupcode: "group code can't be empty",
      typecode: "type code can't be empty",
      name: "dictionary name can't be empty",
      english: "english name can't be empty",
      code: "dictionary code can't be empty",
      value: "dictionary value can't be empty",
    },
    confirm: {
      delete: "Sure to delete dictionary \"{arg1}\" ？This will delete all associated sub dictionaries",
      delete_select: "Sure to delete the selected dictionary？This will delete all associated sub dictionaries",
    },
    text: {
      data: "dictionary",
      parser: "If the value type is 'custom', a CustomValueParser implementation should be provided"
    },
    name: {
      root: "Root Dict",
      sys: "System dict",
      yes_no: "Yes/No",
      yes: "Yes",
      no: "No",
      enable_disable: "Enable/Disable",
      enable: "Enable",
      disable: "Disable",
      success_failed: "Success/Failed",
      success: "Success",
      failed: "Failed",
      show_hide: "Show/Hide",
      show: "Show",
      hide: "Hide",
      public_protected: "Public/Protected",
      public: "Public",
      protected: "Protected",

      attach_type: "Attach Type",
      avatar: "Avatar",
      logo: "Logo",
      image: "Image",

      flow: "Flow dict",
      leave: "Leave Type",
      annual: "Annual Leave",
      personal: "Personal Leave",
      sick: "Sick Leave",
      bereavement: "Bereavement Leave",
      maternity: "Maternity Leave",

      user: "User dict",
      sex: "User Sex",
      post_type: "Post Type",
      post_level: "Post Level",
      male: "Male",
      female: "Female",
      unknown: "Unknown",
      M: "Management",
      T: "Technical",
      A: "Administrative",
      S: "Sales",
      F: "Finance",
      T0: "Trainee Engineer",
      T1: "Junior Engineer",
      T2: "Mid-level Engineer",
      T3: "Senior Engineer",
      T4: "Principal Engineer",
      T5: "Specialist",
      T6: "Senior Specialist",
      T7: "Principal Specialist",
      M0: "Intern",
      M1: "Commissioner",
      M2: "Senior Commissioner",
      M3: "Manager",
      M4: "Senior Manager",
      M5: "Director",
      M6: "Senior Director",
      M7: "executives",

      notice: "Notice dict",
      notice_status: "notice status",
      notice_type: "notice type",
      notice_level: "notice level",
      notice_common: "common",
      notice_urgent: "urgent",
      notice_draft: "draft",
      notice_publish: "published",
      notice_recall: "recall",
      notice_announcement: "announcement",
      notice_notice: "notice",
      notice_press: "press",

      op_action: "OP Action",
      op_create: "create",
      op_delete: "delete",
      op_edit: "edit",
      op_status: "status change",
      op_passwd: "passwd change",
      op_grant: "role change",
      op_login: "login",
      op_login_oauth: "OAuth Authorization",
      op_logout: "logout",
      op_logout_force: "force logout",

      domain_module: "Domain Module",

      domain_system: "system",
      module_auth: "Authorization",
      module_tenant: "Tenant",
      module_user: "User",
      module_dept: "Dept",
      module_role: "Role",
      module_post: "Post",
      module_menu: "Menu",
      module_scope: "Data Scope",
      module_dict: "Dictionary",
      module_config: "Config",
      module_task: "Task",
      module_ldap: "Ldap",
      module_oauth: "oAuth",
      module_notice: "Notice",
      module_attach: "Attach",

      domain_monitor: "monitor",
      module_online: "Online user",
      module_oplog: "Operation log",

      domain_flow: "flow",

      domain_meter: "meter",

      menu: "Menu dict",
      menu_type: "Menu Type",
      menu_cache: "Menu Cache",
      menu_frame: "Menu Internal/External",
      menu_cache_1: "Cache",
      menu_cache_0: "NoCache",
      menu_internal: "Internal",
      menu_external: "External",
      menu_M: "Catalog",
      menu_C: "Menu",
      menu_B: "Button",

      job: "Schedules",
      quartz_group: "task group",
      quartz_default: "quartz default",
      quartz_test: "quartz test",
    }
  },
  config: {
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
    button: {
      reset: "Reset Config",
    },
    dialog: {
      new: "New Config",
      edit: "Edit Config"
    },
    rules: {
      name: "config name can't be empty",
      key: "config key can't be empty",
      value: "config value can't be empty"
    },
    confirm: {
      delete: "Sure to delete config \"{arg1}\" ?",
      delete_select: "Sure to delete the selected config ?",
    },
    text: {
      parser: "Implementation class of ValueParser",
      param: "parameter type is string",
      data: "system_param",
    },
  },
  notice: {
    label: {
      title: "Title",
      type: "Type",
      level: "Level",
      publishTime: "Publish Time",
      read: "Read Statistics"
    },
    placeholder: {
      title: "Input Title",
      type: "Select Type",
      status: "Select Status"
    }
  },
  attach: {
    label: {
      tenant: "Tenant",
      type: "Attach Type",
      module: "Module",
      owner: "Owner",
      name: "Attach Name",
      size: "Attach Size",
      private: "Private",
      expire: "Expire Time",
    },
    placeholder: {
      tenant: "Select Tenant",
      type: "Select Attach Type",
    },
    confirm: {
      delete_select: "Sure to delete the selected attach ?",
    },
  },
  scope: {
    label: {
      module: "Scope Module",
      name: "Scope name",
    },
    button: {
      scope: "Scope",
    },
    placeholder: {
      module: "Select Scope Module",
    },
    dialog: {
      new: "New Data Scope",
      edit: "Edit Data Scope"
    },
    rules: {
      name: "scope name can't be empty",
      module: "scope module can't be empty",
    },
    confirm: {
      delete_select: "Sure to delete the selected scope ?",
      status_enable: "Sure to enable data scope \"{arg1}\" ?",
      status_disable: "Sure to disable date scope \"{arg1}\" ?",
    },
  }
}
