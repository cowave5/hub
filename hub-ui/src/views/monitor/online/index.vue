<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="auto">
      <el-form-item label="登录账号" prop="userAccount">
        <el-input v-model="queryParams.userAccount" placeholder="请输入用户名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="访问时间" label-width="100px">
        <el-date-picker v-model="dateRange" style="width: 260px" value-format="yyyy-MM-dd HH:mm:ss" type="datetimerange" range-separator="-"
                        start-placeholder="开始时间" end-placeholder="结束时间"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="list.slice((page-1)*pageSize,page*pageSize)" style="width: 100%;">
      <el-table-column type="expand">
        <template #default="props">
          <el-table v-if="props.row.accessList && props.row.accessList.length" :data="props.row.accessList" style="width: 100%">
            <el-table-column label="" width="50" />
            <el-table-column label="" width="120" />
            <el-table-column label="" width="120" />
            <el-table-column label="" width="160" />
            <el-table-column label="" width="160" />
            <el-table-column label="" width="140" />
            <el-table-column label="访问令牌" align="center" width="280" :show-overflow-tooltip="true">
              <template #default="scope">
                <span :class="{
                  'revoked-token': scope.row.revoked > 0,
                  'active-token': scope.row.revoked === 0 && scope.row.refreshId === props.row.refreshId }">
                  {{ scope.row.accessId }}
                </span>
              </template>
            </el-table-column>
            <el-table-column label="访问时间" align="center" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.accessTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="访问ip" align="center" prop="accessIp" width="180" :show-overflow-tooltip="true" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDeleteAccess(scope.row)"
                           :disabled="!checkPermit(['monitor:online:force'])"> 删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </template>
      </el-table-column>

      <el-table-column label="序号" type="index" align="center" width="120">
        <template slot-scope="scope">
          <span>{{(page - 1) * pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="令牌类型" align="center" prop="authType" width="120" />
      <el-table-column label="登录账号" align="center" prop="userAccount" width="160" :show-overflow-tooltip="true" />
      <el-table-column label="登录名称" align="center" prop="userName" width="160" :show-overflow-tooltip="true" />
      <el-table-column label="登录集群" align="center" prop="cluster" width="140" />
      <el-table-column label="令牌id" align="center" prop="refreshId" width="280" :show-overflow-tooltip="true" />
      <el-table-column label="登录时间" align="center" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.loginTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="登录ip" align="center" prop="loginIp" width="180" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleForceQuit(scope.row)"
                     :disabled="!checkPermit(['monitor:online:force'])">
            <svg-icon icon-class="quit"/> 强制退出</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="page" :limit.sync="pageSize" />
  </div>
</template>

<script>
import {list, revokeAccess, revokeRefresh} from "@/api/monitor/online";
import {checkPermit} from "@/utils/permission";

export default {
  name: "Online",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      page: 1,
      pageSize: 10,
      // 查询参数
      queryParams: {
        ipaddr: undefined,
        userName: undefined
      },
      // 日期范围
      dateRange: [],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    checkPermit,
    /** 查询登录日志列表 */
    getList() {
      this.loading = true;
      list(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.list = response.data.list;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.page = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 删除令牌 */
    handleDeleteAccess(row) {
      this.$modal.confirm('确认删除访问令牌？').then(function() {
        return revokeAccess(row.accessType, row.userAccount, row.accessId);
      }).then(() => {
        this.$modal.msgSuccess("删除成功");
        this.getList();
      }).catch(() => {});
    },
    /** 强制退出 */
    handleForceQuit(row) {
      this.$modal.confirm('确认强退用户["' + row.userName + '"]？').then(function() {
        return revokeRefresh(row.authType, row.userAccount);
      }).then(() => {
        this.$modal.msgSuccess("强退成功");
        this.getList();
      }).catch(() => {});
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.revoked-token {
  color: #9b9595;
}
.active-token {
  color: #164c9f;
}
</style>
