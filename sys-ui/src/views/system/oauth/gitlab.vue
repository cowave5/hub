<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="Gitlab账号" prop="userAccount" label-width="20">
        <el-input v-model="queryParams.userAccount" placeholder="请输入Gitlab账号"
                  clearable style="width: 180px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{$t('button.search')}}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('button.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-edit" size="mini" @click="handleConfig"
                   :disabled="!checkPermit(['oauth:gitlab:query'])">配置</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="list" :header-cell-style="{'text-align':'center'}">
      <el-table-column :label="$t(`label.index`)" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="用户名称" align="center" prop="userName" width="180" :show-overflow-tooltip="true" />
      <el-table-column label="用户账号" align="center" prop="userAccount" width="180" :show-overflow-tooltip="true" />
      <el-table-column label="用户角色" align="center" prop="roleName" width="180" :show-overflow-tooltip="true" />
      <el-table-column label="邮箱" align="center" prop="userEmail" width="280" :show-overflow-tooltip="true" />
      <el-table-column label="部门" align="center" prop="userDept" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
      <el-table-column :label="$t(`label.option`)" align="center" width="160" class-name="small-padding">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     :disabled="!checkPermit(['oauth:gitlab:user:delete'])">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 配置 -->
    <el-dialog title="Gitlab配置" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item prop="appId">
          <span slot="label">
            <el-tooltip content="Gitlab申请的应用id" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
            应用id:
          </span>
          <el-input v-model="form.appId" />
        </el-form-item>
        <el-form-item prop="appSecret">
          <span slot="label">
            <el-tooltip content="Gitlab申请的应用secret" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
            应用secret:
          </span>
          <el-input v-model="form.appSecret" />
        </el-form-item>
        <el-form-item label="Gitlab Url:" prop="authUrl">
          <el-input v-model="form.authUrl" placeholder="Gitlab地址，比如：https://gitlab.cowave.com" />
        </el-form-item>
        <el-form-item label="Redirect Url:" prop="redirectUrl" label-width="120px">
          <el-input v-model="form.redirectUrl" placeholder="应用回调地址，比如：http://admin.cowave.com/oauth/gitlab" />
        </el-form-item>
        <el-form-item label="用户角色:" prop="userRole">
          <el-select v-model="form.userRole" placeholder="授权用户默认角色" style="width: 100%;">
            <el-option v-for="item in roleOptions" :key="item.roleId" :label="item.roleName" :value="item.roleId"/>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-if="$i18n.locale==='zh'" v-for="dict in dict.type.sys_is_enable" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
            <el-radio v-if="$i18n.locale==='en'" v-for="dict in dict.type.sys_is_enable" :key="dict.value" :label="dict.value">{{dict.labelEn}}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm"
                   :disabled="!checkPermit(['oauth:gitlab:edit'])">保存</el-button>
        <el-button @click="cancel">取消</el-button>
      </div>
    </el-dialog>

    <!-- 修改用户角色 -->
    <el-dialog title="修改用户角色" :visible.sync="openRole" width="750px" append-to-body>
      <el-form ref="form" :model="roleForm" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户名称">
              <el-input v-model="roleForm.userName" disabled="disabled" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户账号">
              <el-input v-model="roleForm.userAccount" disabled="disabled" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="用户邮箱">
              <el-input v-model="roleForm.userEmail" disabled="disabled" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="用户部门">
              <el-input v-model="roleForm.userDept" disabled="disabled" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="用户角色">
              <el-select v-model="roleForm.userRole" style="width: 100%;">
                <el-option v-for="item in roleOptions" :key="item.roleId" :label="item.roleName" :value="item.roleId"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saveRole"
                   :disabled="!checkPermit(['oauth:gitlab:user:edit'])">保存</el-button>
        <el-button @click="cancelRole">取消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {changeUserRole, deleteUser, getConfig, listUser, saveConfig} from "@/api/system/oauth";
import {getRoles} from "@/api/system/user";
import {checkPermit} from "@/utils/permission";

export default {
  name: "oauth_gitlab",
  dicts: ['sys_is_enable'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 参数表格数据
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      openRole: false,
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        userAccount: undefined,
        appType: "gitlab",
      },
      // 表单参数
      form: {
        status: 0,
        appType: "gitlab",
        appId: undefined,
        appSecret: undefined,
        authUrl: undefined,
        redirectUrl: undefined,
      },
      roleForm: {},
      // 角色选项
      roleOptions: [],
    };
  },
  created() {
    this.getList();
    this.getRoleOptions();
  },
  computed: {
    rules() {
      return {
        appId: [
          { required: true, message: "应用id不能为空", trigger: "blur" }
        ],
        appSecret: [
          { required: true, message: "应用secret不能为空", trigger: "blur" }
        ],
        authUrl: [
          { required: true, message: "授权服务地址不能为空", trigger: "blur" }
        ],
        redirectUrl: [
          { required: true, message: "回调地址不能为空", trigger: "blur" }
        ],
        userRole: [
          { required: true, message: "用户角色不能为空", trigger: "blur" }
        ]
      };
    }
  },
  methods: {
    checkPermit,
    /** 获取角色选项 */
    getRoleOptions(){
      getRoles().then(resp => {
        this.roleOptions = resp.data.list
      });
    },
    /** 表单重置 */
    reset() {
      this.form = {
        status: 0,
        appType: "gitlab",
        appId: undefined,
        appSecret: undefined,
        authUrl: undefined,
        redirectUrl: undefined,
      };
      this.resetForm("form");
    },
    /** 搜索 */
    handleQuery() {
      this.queryParams.page = 1;
      this.queryParams.appType = "gitlab";
      this.getList();
    },
    /** 重置 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 列表 */
    getList() {
      this.loading = true;
      listUser(this.queryParams).then(response => {
          this.list = response.data.list;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    /** 配置 */
    handleConfig() {
      this.reset();
      this.open = true;
      getConfig(this.form.appType).then(response => {
        this.form = response.data;
        this.open = true;
      });
    },
    /** 取消 */
    cancel() {
      this.open = false;
    },
    /** 提交 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          saveConfig(this.form).then(response => {
            this.$modal.msgSuccess("保存成功");
            this.open = false;
            this.getList();
          });
        }
      });
    },
    /** 修改用户角色 */
    handleUpdate(row){
      this.roleForm = row;
      this.openRole = true;
    },
    /** 取消修改用户 */
    cancelRole(){
      this.openRole = false;
    },
    /** 保存用户角色 */
    saveRole(){
      changeUserRole(this.roleForm.id, this.roleForm.userRole).then(response => {
        this.openRole = false;
        this.$modal.msgSuccess("修改成功");
        this.getList();
      });
    },
    /** 删除用户 */
    handleDelete(row){
      deleteUser(row.id).then(response => {
        this.$modal.msgSuccess("删除成功");
        this.getList();
      });
    }
  }
};
</script>
