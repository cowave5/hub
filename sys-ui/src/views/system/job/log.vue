<template>
  <div class="app-container">
    <!--  筛选栏  -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="auto">
      <el-form-item label="任务名称" prop="taskName">
        <el-input v-model="queryParams.taskName" placeholder="请输入任务名称" clearable style="width: 180px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="执行结果" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择执行结果" clearable style="width: 180px">
          <el-option v-for="dict in dict.type.success_failed" :key="dict.value" :value="dict.value" :label="$t(dict.name)"/>
        </el-select>
      </el-form-item>
      <el-form-item label="执行时间">
        <el-date-picker v-model="dateRange" style="width: 340px" value-format="yyyy-MM-dd HH:mm:ss" type="datetimerange"
          range-separator="-" start-placeholder="开始时间" end-placeholder="结束时间"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!--  操作栏  -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete"
                   :disabled="multiple || !checkPermit(['sys:job:log:delete'])">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleClean"
                   :disabled="!checkPermit(['sys:job:log:delete'])">清空</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                   :disabled="!checkPermit(['sys:job:log:export'])">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-close" size="mini" @click="handleClose">关闭</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!--  列表数据  -->
    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange" :header-cell-style="{'text-align':'center'}">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" align="center" width="50" >
        <template slot-scope="scope">
          <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="任务名称" align="center" prop="taskName" width="160" :show-overflow-tooltip="true" />
      <el-table-column label="触发方式" align="center" prop="triggerType"/>
      <el-table-column label="调度时间" align="center" prop="triggerTime" width="160"/>
      <el-table-column label="执行状态" align="center" prop="triggerStatus"/>
      <el-table-column label="执行耗时[ms]" align="center" prop="handleCost"/>
      <el-table-column label="执行时间" align="center" prop="handleTime" width="160"/>
      <el-table-column label="详情" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)"
                     :disabled="!checkPermit(['sys:job:log:query'])">详细</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 日志详细 -->
    <el-dialog title="任务日志" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" label-width="auto" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="任务名称：">{{ form.taskName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务分组：">
              <template v-for="item in dict.type.quartz_group">
                <span v-if="form.taskGroup === item.code">{{ $t(item.name) }}</span>
              </template>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="开始时间：">{{ form.beginTime }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间：">{{ form.endTime }}</el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="执行结果：">
              <template v-for="item in dict.type.success_failed">
                <span v-if="form.status === item.value">{{ $t(item.name) }}</span>
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务耗时：">{{ form.costTime }}ms</el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="调用目标：">{{ form.invokeTarget }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="异常信息：" v-if="form.status == 1">{{ form.exceptionInfo }}</el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listJobLog, delJobLog, cleanJobLog } from "@/api/system/jobLog";
import {checkPermit} from "@/utils/permission";

export default {
  name: "JobLog",
  dicts: ['success_failed', 'quartz_group'],
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
      // 调度日志表格数据
      list: [],
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 表单参数
      form: {},
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        taskName: undefined,
        taskGroup: undefined,
        status: undefined
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    checkPermit,
    /** 日志列表 */
    getList() {
      this.loading = true;
      listJobLog(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.list = response.data.list;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    /** 返回 */
    handleClose() {
      const obj = { path: "/system/job" };
      this.$tab.closeOpenPage(obj);
    },
    /** 搜索 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.jobLogId);
      this.multiple = !selection.length;
    },
    /** 详情 */
    handleView(row) {
      this.open = true;
      this.form = row;
    },
    /** 删除 */
    handleDelete(row) {
      const jobLogIds = this.ids;
      this.$modal.confirm('确认删除所选日志？').then(function() {
        return delJobLog(jobLogIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 清空 */
    handleClean() {
      this.$modal.confirm('确认清空所有任务日志？').then(function() {
        return cleanJobLog();
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("日志已清空");
      }).catch(() => {});
    },
    /** 导出 */
    handleExport() {
      this.download('/quartz/api/v1/task/log/export', {
        ...this.queryParams
      }, `任务日志_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
