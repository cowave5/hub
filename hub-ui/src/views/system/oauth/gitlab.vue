<template>
  <div class="app-container">
    <!--  筛选栏  -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" @submit.native.prevent v-show="showSearch" label-width="auto">
      <el-form-item label="Gitlab账号" prop="userAccount" label-width="20">
        <el-input v-model="queryParams.userAccount" placeholder="请输入Gitlab账号"
                  clearable style="width: 180px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{$t('commons.button.search')}}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('commons.button.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <!--  操作栏  -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-edit" size="mini" @click="handleConfig"
                   :disabled="!checkPermit(['oauth:gitlab:query'])">配置</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!--  用户列表  -->
    <el-table v-loading="loading" :data="list" :header-cell-style="{'text-align':'center'}">
      <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="用户名称" align="center" prop="userName" width="180" :show-overflow-tooltip="true" />
      <el-table-column label="用户账号" align="center" prop="userAccount" width="180" :show-overflow-tooltip="true" />
      <el-table-column label="邮箱" align="center" prop="userEmail" width="280" :show-overflow-tooltip="true" />
      <el-table-column label="部门" align="center" prop="userDept" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180" />
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
        <el-form-item label="默认角色:" prop="roleCode">
          <el-select v-model="form.roleCode" placeholder="授权用户默认角色" style="width: 100%;">
            <el-option v-for="item in roleOptions" :key="item.roleCode" :label="item.roleName" :value="item.roleCode"/>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="item in enable_disable" :key="item.value" :label="item.value">{{$t(item.label)}}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm"
                   :disabled="!checkPermit(['oauth:gitlab:edit'])">保存</el-button>
        <el-button @click="cancel">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {changeGitlabRole, deleteGitlabUser, getConfig, listUser, saveConfig} from "@/api/system/oauth";
import {checkPermit} from "@/utils/permission";
import { getRoleList } from '@/api/system/role'
import { enable_disable } from '@/utils/constants';
export default {
  name: "oauth_gitlab",
  dicts: [],
  data() {
    return {
      enable_disable: enable_disable,
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
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        userAccount: undefined,
        serverType: "gitlab",
      },
      // 表单参数
      form: {
        status: 0,
        serverType: "gitlab",
        appId: undefined,
        appSecret: undefined,
        authUrl: undefined,
        redirectUrl: undefined,
      },
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
        roleCode: [
          { required: true, message: "用户角色不能为空", trigger: "blur" }
        ]
      };
    }
  },
  methods: {
    checkPermit,
    /** 获取角色选项 */
    getRoleOptions(){
      getRoleList().then(resp => {
        this.roleOptions = resp.data.list
      });
    },
    /** 表单重置 */
    reset() {
      this.form = {
        status: 0,
        serverType: "gitlab",
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
      this.queryParams.serverType = "gitlab";
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
      getConfig(this.form.serverType).then(response => {
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
  }
};
</script>
