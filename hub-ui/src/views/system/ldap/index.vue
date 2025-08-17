<template>
  <div class="app-container">
    <!--  筛选栏  -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="auto">
      <el-form-item label="Ldap账号" prop="userAccount" label-width="100">
        <el-input v-model="queryParams.ldapAccount" clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{$t('commons.button.search')}}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('commons.button.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <!--  操作栏  -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-edit" size="mini" @click="handleConfig">配置</el-button>
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
      <el-table-column label="用户账号" align="center" prop="userAccount" :show-overflow-tooltip="true"/>
      <el-table-column label="用户名称" align="center" prop="userName" :show-overflow-tooltip="true"/>
      <el-table-column label="手机" align="center" prop="userPhone" :show-overflow-tooltip="true"/>
      <el-table-column label="邮箱" align="center" prop="userEmail" width="200" :show-overflow-tooltip="true"/>
      <el-table-column label="部门" align="center" prop="userDept"/>
      <el-table-column label="岗位" align="center" prop="userPost"/>
      <el-table-column label="汇报对象" align="center" prop="userLeader"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 配置 -->
    <el-dialog :title="title" :visible.sync="open" width="750px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <h4>Ldap服务</h4>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户名:" prop="ldapUser">
              <el-input v-model="form.ldapUser" placeholder="例子: user@domain.name" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码:" prop="ldapPasswd">
              <el-input v-model="form.ldapPasswd" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="Ldap地址:" prop="ldapUrl">
              <el-input v-model="form.ldapUrl" placeholder="例如：ldap.example.com" />
            </el-form-item>
          </el-col>
        </el-row>
        <h4>Ldap模式</h4>
        <el-row>
          <el-col :span="24">
            <el-form-item label="基本DN:" prop="baseDn">
              <el-input v-model="form.baseDn" placeholder="从LDAP根节点搜索用户和组，例如: cn=users,dc=example,dc=com" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="用户DN:" prop="userDn">
              <el-input v-model="form.userDn" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="用户名属性:" prop="accountProperty">
              <el-input v-model="form.accountProperty" placeholder="在用户对象上使用的属性域，例如：cn、sAMAccountName" />
            </el-form-item>
          </el-col>
        </el-row>
        <h4>用户模式</h4>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户对象类:" prop="userClass">
              <el-input v-model="form.userClass" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户DN:" prop="infoProperty">
              <el-input v-model="form.infoProperty" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户姓名:" prop="nameProperty">
              <el-input v-model="form.nameProperty" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="上级用户:" prop="leaderProperty">
              <el-input v-model="form.leaderProperty" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户电话:" prop="phoneProperty">
              <el-input v-model="form.phoneProperty" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户邮箱:" prop="emailProperty">
              <el-input v-model="form.emailProperty" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户部门:" prop="deptProperty">
              <el-input v-model="form.deptProperty" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户岗位:" prop="postProperty">
              <el-input v-model="form.postProperty" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="默认角色:" prop="roleCode">
              <el-select v-model="form.roleCode" placeholder="Ldap用户默认角色" style="width: 100%;">
                <el-option v-for="item in roleOptions" :key="item.roleCode" :label="item.roleName" :value="item.roleCode"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleValid" :disabled="!checkPermit(['hub:ldap:edit'])">测试</el-button>
        <el-button @click="submitForm" :disabled="!checkPermit(['hub:ldap:edit'])">保存</el-button>
        <el-button @click="cancel">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getLdapConfig,
  getLdapUsers,
  saveLdapConfig,
  validLdapConfig
} from "@/api/system/ldap";
import {checkPermit} from "@/utils/permission";
import { getRoleList } from '@/api/system/role'

export default {
  name: "Config",
  dicts: [],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
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
        ldapAccount: undefined
      },
      // 表单参数
      form: {
        id: undefined,
        ldapUrl: undefined,
        ldapUser: undefined,
        ldapPasswd: undefined,
        baseDn: undefined,
        userDn: undefined,
        userClass: undefined,
        roleCode: undefined,
        accountProperty: undefined,
        nameProperty: undefined,
        leaderProperty: undefined,
        phoneProperty: undefined,
        emailProperty: undefined,
        deptProperty: undefined,
        postProperty: undefined,
        infoProperty: undefined,
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
        ldapUrl: [
          { required: true, message: "Ldap服务地址不能为空", trigger: "blur" }
        ],
        ldapUser: [
          { required: true, message: "Ldap用户名不能为空", trigger: "blur" }
        ],
        ldapPasswd: [
          { required: true, message: "Ldap密码不能为空", trigger: "blur" }
        ],
        baseDn: [
          { required: true, message: "基本DN不能为空", trigger: "blur" }
        ],
        accountProperty: [
          { required: true, message: "用户名属性不能为空", trigger: "blur" }
        ],
        userClass: [
          { required: true, message: "用户对象类不能为空", trigger: "blur" }
        ],
        roleCode: [
          { required: true, message: "用户角色不能为空", trigger: "blur" }
        ]
      };
    }
  },
  methods: {
    checkPermit,
    /** 表单重置 */
    reset() {
      this.form = {
        ldapUrl: undefined,
        ldapUser: undefined,
        ldapPasswd: undefined,
        baseDn: undefined,
        userDn: undefined,
        userClass: undefined,
        roleCode: undefined,
        accountProperty: undefined,
        nameProperty: undefined,
        leaderProperty: undefined,
        phoneProperty: undefined,
        emailProperty: undefined,
        deptProperty: undefined,
        postProperty: undefined,
        infoProperty: undefined,
      };
      this.resetForm("form");
    },
    /** 搜索 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置 */
    resetQuery() {
      this.queryParams = {
          page: 1,
          pageSize: 10
      };
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 配置 */
    handleConfig() {
      getLdapConfig().then(response => {
        this.form = response.data;
        if(this.form == null){
          this.reset();
        }
        this.open = true;
      });
    },
    /** 列表 */
    getList() {
      this.loading = true;
      getLdapUsers(this.queryParams).then(response => {
          this.list = response.data.list;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    /** 取消 */
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 提交 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          saveLdapConfig(this.form).then(() => {
            this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
            this.open = false;
            this.getList();
          });
        }
      });
    },
    /** 获取角色选项 */
    getRoleOptions(){
      getRoleList().then(resp => {
        this.roleOptions = resp.data.list
      });
    },
    /** 验证Ldap配置 */
    handleValid(){
      this.$refs["form"].validate(valid => {
        if (valid) {
          validLdapConfig(this.form).then(response => {
            this.$modal.msgSuccess("测试成功");
          });
        }
      });
    },
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-form h4 {
  height: 25px;
  line-height: 25px;
  font-size: 12px;
  font-weight: 600;
  margin: 0 10px 16px;
  /* padding: 0 10px; */
  border-bottom: 1px solid rgb(189, 205, 222);
  position: relative;
}

.el-form h4::before {
  position: absolute;
  content: "";
  top: 50%;
  transform: translateY(-50%);
  left: -10px;
  height: 14px;
  width: 4px;
  background-color: #a9c0e0;
  border-radius: 5px;
}
</style>
