<template>
  <el-dialog :title="$t('user.button.select')" :visible.sync="visible" width="900px" top="5vh" append-to-body>
    <!-- 筛选栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
      <el-form-item :label="$t('user.label.name')" prop="userName">
        <el-input v-model="queryParams.userName" :placeholder="$t('user.placeholder.name')"
                  clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item :label="$t('user.label.phone')" prop="userPhone">
        <el-input v-model="queryParams.userPhone" :placeholder="$t('user.placeholder.phone')"
                  clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="mini" icon="el-icon-search" @click="handleQuery">
          {{$t('commons.button.search')}}
        </el-button>
        <el-button size="mini" icon="el-icon-refresh" @click="resetQuery">
          {{$t('commons.button.reset')}}
        </el-button>
      </el-form-item>
    </el-form>

    <!--  列表数据  -->
    <el-row>
      <el-table @row-click="clickRow" ref="table" :data="userList" @selection-change="selectRow" height="480px">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column :label="$t('user.label.name')" prop="userName" :show-overflow-tooltip="true" />
        <el-table-column :label="$t('user.label.phone')" prop="userPhone" :show-overflow-tooltip="true" />
        <el-table-column :label="$t('user.label.rank')" prop="userRank" :show-overflow-tooltip="true" >
          <template slot-scope="{row: {userRank}}">
            <template v-for="item in dict.type.post_level">
              <span v-if="userRank === item.code">{{ item.code }}/{{ $t(item.name) }}</span>
            </template>
          </template>
        </el-table-column>
        <el-table-column :label="$t('dept.text.name')" prop="deptName" :show-overflow-tooltip="true" />
        <el-table-column :label="$t('post.text.name')" prop="postName" :show-overflow-tooltip="true" />
      </el-table>
      <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>
    </el-row>

    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="handleSelectUser" :disabled="!checkPermit(['sys:role:members'])">
        {{$t('commons.button.confirm')}}
      </el-button>
      <el-button @click="visible = false">{{$t('commons.button.cancel')}}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getUnAuthedUser, grantRoleUser } from "@/api/system/role";
import {checkPermit} from "@/utils/permission";
export default {
  dicts: ['post_level'],
  props: {
    roleId: {
      type: [Number, String]
    }
  },
  data() {
    return {
      // 遮罩层
      visible: false,
      // 选中数组值
      userIds: [],
      // 总条数
      total: 0,
      // 未授权用户数据
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
  methods: {
    checkPermit,
    /** 显示弹框 */
    show() {
      this.queryParams.roleId = this.roleId;
      this.getList();
      this.visible = true;
    },
    /** 点击行 */
    clickRow(row) {
      this.$refs.table.toggleRowSelection(row);
    },
    /** 多选框 */
    selectRow(selection) {
      this.userIds = selection.map(item => item.userId);
    },
    /** 列表数据 */
    getList() {
      getUnAuthedUser(this.queryParams).then(res => {
        this.userList = res.data.list;
        this.total = res.data.total;
      });
    },
    /** 搜索 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置 */
    resetQuery() {
      this.$refs.queryForm.resetFields();
      this.handleQuery();
    },
    /** 授权用户 */
    handleSelectUser() {
      const roleId = this.queryParams.roleId;
      const userIds = this.userIds;
      if (userIds === undefined || userIds.length === 0) {
        this.$modal.msgError(this.$t('user.text.unselect'));
        return;
      }
      grantRoleUser({ roleId: roleId, userIds: userIds }).then(res => {
        if (res.code === "200") {
          this.$modal.msgSuccess(this.$t('role.msg.grant'));
          this.visible = false;
          this.$emit("ok");
        }
      });
    }
  }
};
</script>
