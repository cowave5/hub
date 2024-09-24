<template>
  <!-- 授权用户 -->
  <el-dialog :title="$t(`role.user.button.choose`)" :visible.sync="visible" width="900px" top="5vh" append-to-body>
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
      <el-form-item :label="$t(`role.user.label.name`)" prop="userName">
        <el-input v-model="queryParams.userName" :placeholder="$t(`user.placeholder.name`)" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item :label="$t(`role.user.label.phone`)" prop="userPhone">
        <el-input v-model="queryParams.userPhone" :placeholder="$t(`user.placeholder.phone`)" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{$t('button.search')}}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('button.reset')}}</el-button>
      </el-form-item>
    </el-form>
    <el-row>
      <el-table @row-click="clickRow" ref="table" :data="userList" @selection-change="handleSelectionChange" height="480px">
        <el-table-column type="selection" width="55"></el-table-column>
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
      </el-table>
      <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>
    </el-row>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="handleSelectUser"
                 :disabled="!checkPermit(['sys:role:grant'])">{{$t('button.confirm')}}</el-button>
      <el-button @click="visible = false">{{$t('button.cancel')}}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { unallocatedUserList, authUserSelectAll } from "@/api/system/role";
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
    handleSelectionChange(selection) {
      this.userIds = selection.map(item => item.userId);
    },
    /** 列表数据 */
    getList() {
      unallocatedUserList(this.queryParams).then(res => {
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 授权用户 */
    handleSelectUser() {
      const roleId = this.queryParams.roleId;
      const userIds = this.userIds;
      if (userIds === undefined || userIds.length === 0) {
        this.$modal.msgError(this.$t(`role.msg.unselect`));
        return;
      }
      authUserSelectAll({ roleId: roleId, userIds: userIds }).then(res => {
        if (res.code === "200") {
          this.$modal.msgSuccess(this.$t(`role.msg.grant_success`));
          this.visible = false;
          this.$emit("ok");
        }
      });
    }
  }
};
</script>
