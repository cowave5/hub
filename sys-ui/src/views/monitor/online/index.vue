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
      <el-table-column label="序号" type="index" align="center">
        <template slot-scope="scope">
          <span>{{(page - 1) * pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="令牌id" align="center" prop="accessId" width="280" :show-overflow-tooltip="true" />
      <el-table-column label="令牌类型" align="center" prop="accessType"/>
      <el-table-column label="访问时间" align="center" prop="accessTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.accessTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="访问ip" align="center" prop="accessIp" :show-overflow-tooltip="true" />
      <el-table-column label="登录账号" align="center" prop="userAccount" :show-overflow-tooltip="true" />
      <el-table-column label="登录名称" align="center" prop="userName" :show-overflow-tooltip="true" />
      <el-table-column label="登录集群" align="center" prop="accessCluster" />
      <el-table-column label="登录时间" align="center" prop="loginTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.loginTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="登录ip" align="center" prop="accessIp" :show-overflow-tooltip="true" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleForceLogout(scope.row)"
                     :disabled="!checkPermit(['monitor:online:force'])">强退</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="page" :limit.sync="pageSize" />
  </div>
</template>

<script>
import { list, forceLogout } from "@/api/monitor/online";
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
    /** 强退按钮操作 */
    handleForceLogout(row) {
      this.$modal.confirm('确认强退用户["' + row.userName + '"]？').then(function() {
        return forceLogout(row.accessId);
      }).then(() => {
        this.$modal.msgSuccess("强退成功");
        this.getList();
      }).catch(() => {});
    }
  }
};
</script>

