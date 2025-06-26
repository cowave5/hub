<template>
  <div class="app-container">
    <h4 class="form-header h4">{{ $t('tenant.text.info') }}</h4>
    <el-form ref="form" :model="form" label-width="180px">
      <el-row>
        <el-col :span="8" :offset="2">
          <el-form-item :label="$t('tenant.label.id')">
            <el-input  v-model="form.tenantId" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="8" :offset="2">
          <el-form-item :label="$t('tenant.label.name')">
            <el-input v-model="form.tenantName" disabled />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <!--  操作栏  -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="createManager">
          {{ $t('tenant.button.manager_add') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain size="mini" icon="el-icon-circle-close" @click="removeSelectedManager"
                   :disabled="multiple || !checkPermit(['sys:tenant:manager:remove'])">
          {{ $t('tenant.button.manager_remove') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain size="mini" icon="el-icon-close" @click="handleClose">
          {{ $t('commons.button.close') }}
        </el-button>
      </el-col>
    </el-row>

    <!--  列表数据  -->
    <el-table :data="list" v-loading="loading" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{ (queryParams.page - 1) * queryParams.pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('user.label.account')" prop="userAccount" align="center"/>
      <el-table-column :label="$t('user.label.name')" prop="userName" align="center"/>
      <el-table-column :label="$t('commons.label.createTime')" prop="createTime" align="center">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.updateTime')" prop="updateTime" align="center">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-circle-close" @click="removeTenantManager(scope.row)"
                     :disabled="scope.row.userId === 1 || !checkPermit(['sys:tenant:manager:remove'])">
            {{ $t('commons.button.remove') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total"
                :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!--  新增管理员  -->
    <el-dialog v-drag :title="title" :visible.sync="open" width="500px" append-to-body >
      <el-form ref="managerForm" :model="manager" :rules="rules" label-width="100px" style="height: 420px">
        <el-form-item :label="$t('user.label.name')" prop="userName">
          <el-input v-model="manager.userName" :placeholder="$t('user.placeholder.name')" maxlength="30" />
        </el-form-item>
        <el-form-item :label="$t('user.label.account')" prop="userAccount">
          <el-input v-model="manager.userAccount" :placeholder="$t('user.placeholder.account')" maxlength="30" />
        </el-form-item>
        <el-form-item :label="$t('user.label.passwd')" prop="userPasswd">
          <el-input v-model="manager.userPasswd" :placeholder="$t('user.placeholder.passwd')" type="password" maxlength="20" show-password/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" :disabled="!checkPermit(['sys:tenant:manager:create'])">
          {{$t('commons.button.confirm')}}
        </el-button>
        <el-button @click="cancel">{{$t('commons.button.cancel')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { checkPermit } from '@/utils/permission'
import {createTenantManager, getTenant, listTenantManager, removeTenantManager} from "@/api/system/tenant";

export default {
  name: 'TenantManager',
  dicts: [],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中用户组
      userIds: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 对象信息
      form: {},
      // 总条数
      total: 0,
      // 用户表格数据
      list: [],
      // 查询参数
      tenantId: undefined,
      queryParams: {
        page: 1,
        pageSize: 10,
      },
      open: false,
      title: "新增管理员",
      manager: {}
    }
  },
  created() {
    const tenantId = this.$route.params && this.$route.params.tenantId
    if (tenantId) {
      this.tenantId = tenantId
      this.getList()
      getTenant(tenantId).then(response => {
        this.form = response.data
      })
    }
  },
  computed: {
    rules() {
      return {
        userAccount: [
          {required: true, message: this.$t('user.rules.account'), trigger: "blur"}
        ],
        userName: [
          {required: true, message: this.$t('user.rules.name'), trigger: "blur"},
          {min: 2, max: 20, message: this.$t('user.rules.name_len'), trigger: 'blur'}
        ],
        userPasswd: [
          {required: true, message: this.$t('user.rules.passwd'), trigger: "blur"},
          {min: 6, max: 20, message: this.$t('user.rules.passwd_len'), trigger: 'blur'}
        ]
      };
    },
  },
  methods: {
    checkPermit,
    /** 多选框 */
    handleSelectionChange(selection) {
      this.userIds = selection.map(item => item.userId)
      this.multiple = !selection.length
    },
    /** 返回 */
    handleClose() {
      const obj = { path: '/system/tenant' }
      this.$tab.closeOpenPage(obj)
    },
    /** 管理员列表 */
    getList() {
      this.loading = true
      listTenantManager(this.tenantId, this.queryParams).then(response => {
            this.list = response.data.list
            this.total = response.data.total
            this.loading = false
          }
      )
    },
    /** 取消管理员 */
    removeTenantManager(row) {
      const tenantId = this.tenantId;
      this.$modal.confirm(this.$t('tenant.confirm.remove_manager', { arg1: row.userName })).then(function() {
        return removeTenantManager({ userIds: [row.userId], tenantId: tenantId })
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('tenant.msg.remove_success'))
      }).catch(() => {
      })
    },
    /** 取消管理员 */
    removeSelectedManager() {
      const tenantId = this.tenantId;
      const userIds = this.userIds
      this.$modal.confirm(this.$t('tenant.confirm.remove_select')).then(function() {
        return removeTenantManager({ tenantId: tenantId, userIds: userIds })
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('tenant.msg.remove_success'))
      }).catch(() => {
      })
    },
    /** 创建管理员 */
    createManager() {
      this.manager = {
        tenantId: this.tenantId,
        userName: undefined,
        userAccount: undefined,
        userPasswd: undefined
      },
      this.open = true;
    },
    /** 取消 */
    cancel() {
      this.open = false;
    },
    /** 提交 */
    submitForm: function() {
      this.$refs["managerForm"].validate(valid => {
        createTenantManager(this.manager).then(() => {
          this.$modal.msgSuccess(this.$t('commons.msg.success.create'));
          this.open = false;
          this.getList();
        });
      })
    }
  }
}
</script>
