<template>
  <div class="app-container">
     <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item :label="$t(`role.user.label.name`)" prop="userName">
        <el-input v-model="queryParams.userName" :placeholder="$t(`user.placeholder.name`)" clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item :label="$t(`role.user.label.phone`)" prop="userPhone">
        <el-input v-model="queryParams.userPhone" :placeholder="$t(`user.placeholder.phone`)" clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{$t('button.search')}}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('button.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="openSelectUser">{{$t('role.user.button.add')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-circle-close" size="mini" @click="cancelAuthUserAll"
                   :disabled="multiple || !checkPermit(['sys:role:grant'])">{{$t('role.user.button.remove')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-close" size="mini" @click="handleClose">{{$t('button.close')}}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
      <el-table-column :selectable='selectable' type="selection" width="55" align="center"/>
      <el-table-column :label="$t(`role.user.label.name`)" prop="userName" :show-overflow-tooltip="true" />
      <el-table-column :label="$t(`role.user.label.phone`)" :show-overflow-tooltip="true" />
      <el-table-column :label="$t(`role.user.label.rank`)" prop="rank" :show-overflow-tooltip="true" >
        <template slot-scope="{row: {rank}}">
          <template v-for="item in dict.type.post_level">
            <span v-if="rank === item.value && $i18n.locale==='zh'">{{ item.value }}/{{ item.label }}</span>
            <span v-if="rank === item.value && $i18n.locale==='en'">{{ item.value }}/{{ item.labelEn }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`role.user.label.dept`)" prop="deptName" :show-overflow-tooltip="true" />
      <el-table-column :label="$t(`role.user.label.post`)" prop="postName" :show-overflow-tooltip="true" />
      <el-table-column :label="$t(`label.option`)" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope" v-if="scope.row.userId > 1">
          <el-button size="mini" type="text" icon="el-icon-circle-close" @click="cancelAuthUser(scope.row)"
                     :disabled="!checkPermit(['sys:role:grant'])">{{$t('role.user.button.remove')}}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>
    <select-user ref="select" :roleId="queryParams.roleId" @ok="handleQuery" />
  </div>
</template>

<script>
import { allocatedUserList, authUserCancel } from "@/api/system/role";
import selectUser from "./selectUser";
import {checkPermit} from "@/utils/permission";

export default {
  name: "AuthUser",
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
    };
  },
  created() {
    const roleId = this.$route.params && this.$route.params.roleId;
    if (roleId) {
      this.queryParams.roleId = roleId;
      this.getList();
    }
  },
  methods: {
    checkPermit,
    selectable(row, rowIndex){
      return row.userId !== 1;
    },
    /** 多选框 */
    handleSelectionChange(selection) {
      this.userIds = selection.map(item => item.userId)
      this.multiple = !selection.length
    },
    /** 搜索 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 返回 */
    handleClose() {
      const obj = { path: "/system/role" };
      this.$tab.closeOpenPage(obj);
    },
    /** 选择用户 */
    openSelectUser() {
      this.$refs.select.show();
    },
    /** 用户列表 */
    getList() {
      this.loading = true;
      allocatedUserList(this.queryParams).then(response => {
          this.userList = response.data.list;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    /** 取消授权 */
    cancelAuthUser(row) {
      const roleId = this.queryParams.roleId;
      this.$modal.confirm(this.$t(`role.msg.confirm_remove`, { var1: row.userName })).then(function() {
        return authUserCancel({ userIds: [row.userId], roleId: roleId });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t(`role.msg.remove_success`));
      }).catch(() => {});
    },
    /** 批量取消授权 */
    cancelAuthUserAll() {
      const roleId = this.queryParams.roleId;
      const userIds = this.userIds;
      this.$modal.confirm(this.$t(`role.msg.select_remove`)).then(function() {
        return authUserCancel({ roleId: roleId, userIds: userIds });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t(`role.msg.remove_success`));
      }).catch(() => {});
    }
  }
};
</script>
