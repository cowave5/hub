<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="Ldap名称" prop="configName" label-width="100">
        <el-input v-model="queryParams.ldapName" clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{$t('button.search')}}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('button.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   :disabled="!checkPermit(['sys:ldap:new'])">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" @click="handleUpdate"
                   :disabled="single || !checkPermit(['sys:ldap:edit'])">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete"
                   :disabled="multiple || !checkPermit(['sys:ldap:delete'])">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="list"
              @selection-change="handleSelectionChange" :header-cell-style="{'text-align':'center'}">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t(`label.index`)" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="Ldap名称" align="center" prop="ldapName" width="160" :show-overflow-tooltip="true" />
      <el-table-column label="Ldap地址" align="center" prop="ldapUrl" width="200" :show-overflow-tooltip="true" />
      <el-table-column label="用户名" align="center" prop="ldapUser" width="240" :show-overflow-tooltip="true" />
      <el-table-column label="基本DN" align="center" prop="baseDn" :show-overflow-tooltip="true" />
      <el-table-column label="默认角色" align="center" prop="roleName" width="120"/>
      <el-table-column label="是否启用" align="center" width="80">
        <template slot-scope="scope">
          <el-switch :disabled="!checkPermit(['sys:ldap:edit'])" v-model="scope.row.ldapStatus" :active-value=1 :inactive-value=0 @change="handleStatusChange(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`label.option`)" align="center" class-name="small-padding" width="160">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     :disabled="!checkPermit(['sys:ldap:delete'])">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="750px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <h4>Ldap服务</h4>
        <el-row>
          <el-col :span="24">
            <el-form-item label="Ldap名称:" prop="ldapName">
              <el-input v-model="form.ldapName" placeholder="Ldap服务名称" />
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
        <h4>Ldap模式</h4>
        <el-row>
          <el-col :span="24">
            <el-form-item label="用户名属性:" prop="accountProperty">
              <el-input v-model="form.accountProperty" placeholder="在用户对象上使用的属性域，例如：cn、sAMAccountName" />
            </el-form-item>
          </el-col>
        </el-row>
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
            <el-form-item label="用户角色:" prop="userRole">
              <el-select v-model="form.userRole" placeholder="Ldap用户默认角色" style="width: 100%;">
                <el-option v-for="item in roleOptions" :key="item.roleId" :label="item.roleName" :value="item.roleId"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleValid"
                   :disabled="!checkPermit(['sys:ldap:edit'])">测试</el-button>
        <el-button @click="submitForm"
                   :disabled="!checkPermit(['sys:ldap:edit'])">保存</el-button>
        <el-button @click="cancel">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {addLdap, changeStatus, delLdap, infoLdap, listLdap, updateLdap, validLdap} from "@/api/system/ldap";
import {getRoles} from "@/api/system/user";
import {checkPermit} from "@/utils/permission";

export default {
  name: "Config",
  dicts: ['sys_yes_no'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
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
        ldapName: undefined
      },
      // 表单参数
      form: {
        id: undefined,
        ldapName: undefined,
        ldapUrl: undefined,
        ldapUser: undefined,
        ldapPasswd: undefined,
        baseDn: undefined,
        userDn: undefined,
        userClass: undefined,
        userRole: undefined,
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
        ldapName: [
          { required: true, message: "Ldap服务名称不能为空", trigger: "blur" }
        ],
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
        userRole: [
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
        id: undefined,
        ldapName: undefined,
        ldapUrl: undefined,
        ldapUser: undefined,
        ldapPasswd: undefined,
        baseDn: undefined,
        userDn: undefined,
        userClass: undefined,
        userRole: undefined,
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
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
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
          pageSize: 10,
          ldapName: undefined
      };
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 列表 */
    getList() {
      this.loading = true;
      listLdap(this.queryParams).then(response => {
          this.list = response.data.list;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    /** 新增 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增Ldap";
    },
    /** 修改 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      infoLdap(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改Ldap";
      });
    },
    /** 删除 */
    handleDelete(row) {
      const idarray = row.id || this.ids;
      const msg = row.id ? "确认删除Ldap配置[" + row.ldapName + "]" : "确认删除所选Ldap配置";
      this.$modal.confirm(msg).then(function() {
        return delLdap(idarray);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t(`msg.success_delete`));
      }).catch(() => {});
    },
    /** 取消 */
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 提交 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            updateLdap(this.form).then(response => {
              this.$modal.msgSuccess(this.$t(`msg.success_edit`));
              this.open = false;
              this.getList();
            });
          } else {
            addLdap(this.form).then(response => {
              this.$modal.msgSuccess(this.$t(`msg.success_create`));
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 获取角色选项 */
    getRoleOptions(){
      getRoles().then(resp => {
        this.roleOptions = resp.data.list
      });
    },
    /** 修改状态 */
    handleStatusChange(row){
      let text = row.ldapStatus === 1 ? "启用" : "停用";
      const msg = "确认" + text + "Ldap配置[" + row.ldapName + "]";
      this.$modal.confirm(msg).then(function() {
        return changeStatus(row.id, row.ldapStatus);
      }).then(() => {
        this.$modal.msgSuccess(text + this.$t(`content.success`));
      }).catch(function() {
        row.ldapStatus = row.ldapStatus === 0 ? 1 : 0;
      });
    },
    /** 修改Ldap配置 */
    handleValid(){
      this.$refs["form"].validate(valid => {
        if (valid) {
          validLdap(this.form).then(response => {
            this.$modal.msgSuccess("测试成功");
          });
        }
      });
    }
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
