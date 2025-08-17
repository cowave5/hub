<template>
  <div class="app-container">
    <h4 class="form-header h4">{{$t('user.text.info')}}</h4>
    <el-form ref="form" :model="form" label-width="80px">
      <el-row>
        <el-col :span="8" :offset="2">
          <el-form-item :label="$t('user.label.account')" prop="userAccount">
            <el-input  v-model="form.userAccount" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="8" :offset="2">
          <el-form-item :label="$t('user.label.name')" prop="userName">
            <el-input v-model="form.userName" disabled />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <h4 class="form-header h4">{{$t('role.text.info')}}</h4>
    <el-table v-loading="loading" :row-key="getRowKey" :data="roles.slice((page-1)*pageSize,page*pageSize)"
              @row-click="clickRow" ref="table" @selection-change="handleSelectionChange">
      <el-table-column type="selection" :reserve-selection="true" width="50"></el-table-column>
      <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(page - 1) * pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('role.label.name')" align="center" prop="roleName" />
      <el-table-column :label="$t('role.label.code')" align="center" prop="roleCode" />
      <el-table-column :label="$t('commons.label.createTime')" align="center" prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="page" :limit.sync="pageSize" />

    <el-form label-width="100px">
      <el-form-item style="text-align: center;margin-left:-120px;margin-top:30px;">
        <el-button type="primary" @click="submitForm()">{{$t('commons.button.confirm')}}</el-button>
        <el-button @click="close()">{{$t('commons.button.cancel')}}</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getUserInfo, updateUserRoles} from "@/api/system/user";
import { getRoleList } from '@/api/system/role'

export default {
  name: "userRole",
  data() {
    return {
       // 遮罩层
      loading: true,
      // 分页信息
      total: 0,
      page: 1,
      pageSize: 10,
      // 选中角色编号
      roleIds:[],
      // 角色信息
      roles: [],
      // 用户信息
      form: {}
    };
  },
  created() {
    const userId = this.$route.params && this.$route.params.userId;
    if (userId) {
      this.loading = true;
      getRoleList().then((response) => {
        this.roles = response.data.list
        this.total = response.data.total;
        getUserInfo(userId).then((resp) => {
          this.form.userAccount = resp.data.userAccount;
          this.form.userName = resp.data.userName;
          this.form.userId = resp.data.userId;
          this.roleIds = resp.data.roleIds;
          this.$nextTick(() => {
            this.roles.forEach((row) => {
              if (resp.data.roleIds.indexOf(row.roleId) !== -1) {
                this.$refs.table.toggleRowSelection(row);
              }
            });
          });
          this.loading = false;
        });
      });
    }
  },
  methods: {
    /** 单击选中行 */
    clickRow(row) {
      this.$refs.table.toggleRowSelection(row);
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.roleIds = selection.map((item) => item.roleId);
    },
    /** 保存选中的数据编号 */
    getRowKey(row) {
      return row.roleId;
    },
    /** 提交 */
    submitForm() {
      const userId = this.form.userId;
      const userName = this.form.userName;
      const roleIds = this.roleIds;
      updateUserRoles(userId, userName, roleIds).then(() => {
        this.$modal.msgSuccess(this.$t('commons.msg.success.grant'));
        this.close();
      });
    },
    /** 关闭 */
    close() {
      const obj = { path: "/system/user" };
      this.$tab.closeOpenPage(obj);
    },
  },
};
</script>
