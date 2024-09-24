<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="日志模块" prop="title">
        <el-select v-model="queryParams.groupCode" @change="handleGroupChange">
          <el-option v-for="item in groupOptions" :key="item.key" :value="item.key" :label="item.label"/>
        </el-select>
      </el-form-item>
      <el-form-item label="日志类型" prop="businessType">
        <el-select v-model="queryParams.typeCode">
          <el-option v-for="item in typeOptions" :key="item.key" :value="item.key" :label="item.label"/>
        </el-select>
      </el-form-item>
      <el-form-item label="操作人" prop="operName">
        <el-input v-model="queryParams.userName" placeholder="请输入操作人员" clearable style="width: 240px;" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="操作时间">
        <el-date-picker v-model="dateRange" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="datetimerange"
                        range-separator="-" start-placeholder="开始时间" end-placeholder="结束时间"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleClean"
                   :disabled="!checkPermit(['monitor:log:delete'])">清空</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete"
                   :disabled="multiple || !checkPermit(['monitor:log:delete'])">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                   :disabled="!checkPermit(['monitor:log:export'])">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table ref="tables" v-loading="loading" :data="list" @selection-change="handleSelectionChange" :header-cell-style="{'text-align':'center'}">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="日志模块" align="center" prop="groupName" />
      <el-table-column label="日志类型" align="center" prop="typeName"/>
      <el-table-column label="日志动作" align="center" prop="actionName"/>
      <el-table-column label="访问ip" align="center" prop="ip"/>
      <el-table-column label="访问路径" align="left" prop="url" width="180" :show-overflow-tooltip="true" />
      <el-table-column label="访问时间" align="center" prop="logTime" width="160"/>
      <el-table-column label="操作人" align="center" prop="userName"/>
      <el-table-column label="操作结果" align="center" prop="logStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_is_success" :value="scope.row.logStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="操作描述" align="center" prop="logDesc" width="240" :show-overflow-tooltip="true" />
      <el-table-column label="详情" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)"
                     v-hasPermi="['monitor:log:query']">详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>
    <log-detail v-if="viewKey === 'log-detail'" ref="log-detail" @ok="handleQuery" />
    <log-user v-if="viewKey === 'log-user'" ref="log-user" @ok="handleQuery" />
    <log-dept v-if="viewKey === 'log-dept'" ref="log-dept" @ok="handleQuery" />
    <log-role v-if="viewKey === 'log-role'" ref="log-role" @ok="handleQuery" />
    <log-post v-if="viewKey === 'log-post'" ref="log-post" @ok="handleQuery" />
  </div>
</template>

<script>
import {list, delOperlog, cleanOperlog, options} from "@/api/monitor/operlog";
import {checkPermit} from "@/utils/permission";
export default {
  name: "Operlog",
  dicts: ['sys_is_success'],
  components: { logDetail: ()=> import('./detail/logDetail.vue'), logUser: ()=> import('./detail/logUser.vue'), logDept: ()=> import('./detail/logDept.vue'), logRole: ()=> import('./detail/logRole.vue'), logPost: ()=> import('./detail/logPost.vue') },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        groupCode: undefined,
        typeCode: undefined,
        userName: undefined,
        beginTime: undefined,
        endTime: undefined
      },
      groupOptions: [],
      typeOptions: [],
      viewKey: '',
    };
  },
  created() {
    this.getOptions();
    this.getList();
  },
  methods: {
    checkPermit,
    getOptions() {
      options().then( response => {
        this.groupOptions = response.data;
      });
    },
    /** 选择分组 */
    handleGroupChange() {
      const selectedOption = this.groupOptions.find(item => item.key === this.queryParams.groupCode);
      if (selectedOption && selectedOption.children) {
        this.typeOptions = selectedOption.children;
      } else {
        this.typeOptions = [];
      }
      this.queryParams.typeCode = undefined;
    },
    /** 查询登录日志 */
    getList() {
      this.loading = true;
      list(this.addDateRange(this.queryParams, this.dateRange)).then( response => {
          this.list = response.data.list;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.queryParams = {
        page: 1,
          pageSize: 10,
          groupCode: undefined,
          typeCode: undefined,
          userName: undefined,
          beginTime: undefined,
          endTime: undefined
      },
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.multiple = !selection.length
    },
    /** 删除 */
    handleDelete() {
      const ids = this.ids;
      this.$modal.confirm('确认删除所选日志？').then(function() {
        return delOperlog(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 清空 */
    handleClean() {
      this.$modal.confirm('确认清空所有日志数据？').then(function() {
        return cleanOperlog();
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("清空成功");
      }).catch(() => {});
    },
    /** 导出 */
    handleExport() {
      this.download('/admin/api/v1/log/export', {
        ...this.queryParams
      }, `系统日志_${new Date().getTime()}.xlsx`)
    },
    /** 详情 */
    handleView(row) {
      this.viewKey = row.viewKey
      setTimeout(()=>{
        this.$refs[row.viewKey].show(row.id);
      });
    },
  }
};
</script>

