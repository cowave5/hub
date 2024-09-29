<template>
  <div class="app-container">
    <h4 class="form-header h4">{{ $t('role.text.info') }}</h4>
    <el-form ref="form" :model="form" label-width="180px">
      <el-row>
        <el-col :span="8" :offset="2">
          <el-form-item :label="$t('role.label.name')">
            <el-input  v-model="form.roleName" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="8" :offset="2">
          <el-form-item :label="$t('role.label.code')">
            <el-input v-model="form.roleCode" disabled />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <!--  筛选栏  -->
    <h4 class="form-header h4">{{ $t('role.text.members') }}</h4>
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" v-show="showSearch">
      <el-form-item :label="$t('user.label.name')" prop="userName">
        <el-input v-model="queryParams.userName" :placeholder="$t('user.placeholder.name')"
                  clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item :label="$t('user.label.phone')" prop="userPhone">
        <el-input v-model="queryParams.userPhone" :placeholder="$t('user.placeholder.phone')"
                  clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="mini" icon="el-icon-search"
                   @click="handleQuery">{{ $t('commons.button.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini"
                   @click="resetQuery">{{ $t('commons.button.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <!--  操作栏  -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="openSelectUser">
          {{ $t('user.button.add') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain size="mini" icon="el-icon-circle-close" @click="cancelAuthUserAll"
                   :disabled="multiple || !checkPermit(['sys:role:members'])">
          {{ $t('role.button.remove') }}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain size="mini" icon="el-icon-close" @click="handleClose">
          {{ $t('commons.button.close') }}
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!--  列表数据  -->
    <el-table :data="userList" v-loading="loading" @selection-change="handleSelectionChange">
      <el-table-column :selectable="selectable" type="selection" width="55" align="center"/>
      <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{ (queryParams.page - 1) * queryParams.pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('user.label.name')" prop="userName" align="center"/>
      <el-table-column :label="$t('user.label.phone')" prop="userPhone" align="center"/>
      <el-table-column :label="$t('user.label.rank')" prop="userRank" align="center">
        <template slot-scope="{row: {userRank}}">
          <template v-for="item in dict.type.post_level">
            <span v-if="userRank === item.code">{{ item.code }}/{{ $t(item.name) }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column :label="$t('dept.text.name')" prop="deptName" align="center"/>
      <el-table-column :label="$t('post.text.name')" prop="postName" align="center"/>
      <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope" v-if="scope.row.userId > 1">
          <el-button size="mini" type="text" icon="el-icon-circle-close" @click="cancelAuthUser(scope.row)"
                     :disabled="!checkPermit(['sys:role:members'])">
            {{ $t('role.button.remove') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total"
                :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>
    <select-user ref="select" :role-id="queryParams.roleId" @ok="handleQuery"/>
  </div>
</template>

<script>
import { getAuthedUser, cancelRoleUser, getRoleInfo } from '@/api/system/role'
import selectUser from './selectUser'
import { checkPermit } from '@/utils/permission'

export default {
  name: 'AuthUser',
  dicts: ['post_level'],
  components: { selectUser },
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
      userList: [],
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        roleId: undefined,
        userName: undefined,
        userPhone: undefined
      }
    }
  },
  created() {
    const roleId = this.$route.params && this.$route.params.roleId
    if (roleId) {
      this.queryParams.roleId = roleId
      this.getList()
      getRoleInfo(roleId).then(response => {
        this.form = response.data
      })
    }
  },
  methods: {
    checkPermit,
    selectable(row, rowIndex) {
      return row.userId !== 1
    },
    /** 多选框 */
    handleSelectionChange(selection) {
      this.userIds = selection.map(item => item.userId)
      this.multiple = !selection.length
    },
    /** 搜索 */
    handleQuery() {
      this.queryParams.page = 1
      this.getList()
    },
    /** 重置 */
    resetQuery() {
      this.$refs.queryForm.resetFields();
      this.handleQuery()
    },
    /** 返回 */
    handleClose() {
      const obj = { path: '/system/role' }
      this.$tab.closeOpenPage(obj)
    },
    /** 选择用户 */
    openSelectUser() {
      this.$refs.select.show()
    },
    /** 用户列表 */
    getList() {
      this.loading = true
      getAuthedUser(this.queryParams).then(response => {
            this.userList = response.data.list
            this.total = response.data.total
            this.loading = false
          }
      )
    },
    /** 取消授权 */
    cancelAuthUser(row) {
      const roleId = this.queryParams.roleId
      this.$modal.confirm(this.$t('role.confirm.remove_grant', { arg1: row.userName })).then(function() {
        return cancelRoleUser({ userIds: [row.userId], roleId: roleId })
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('role.msg.remove_grant'))
      }).catch(() => {
      })
    },
    /** 批量取消授权 */
    cancelAuthUserAll() {
      const roleId = this.queryParams.roleId
      const userIds = this.userIds
      this.$modal.confirm(this.$t('role.confirm.remove_grant_select')).then(function() {
        return cancelRoleUser({ roleId: roleId, userIds: userIds })
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('role.msg.remove_grant'))
      }).catch(() => {
      })
    }
  }
}
</script>
