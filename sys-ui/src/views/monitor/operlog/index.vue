<template>
  <div class="app-container">
    <!--  筛选栏  -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="auto">
      <el-form-item label="日志模块" prop="opModule">
        <el-select v-model="queryParams.opModule" @change="handleModuleChange">
          <el-option v-for="item in moduleOptions" :key="item.key" :value="item.key" :label="$t(item.label)"/>
        </el-select>
      </el-form-item>
      <el-form-item label="日志类型" prop="opType">
        <el-select v-model="queryParams.opType">
          <el-option v-for="item in typeOptions" :key="item.key" :value="item.key" :label="$t(item.label)"/>
        </el-select>
      </el-form-item>
      <el-form-item label="操作人" prop="opUser">
        <el-input v-model="queryParams.opUser" placeholder="请输入操作人员" clearable style="width: 240px;" @keyup.enter.native="handleQuery"/>
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

    <!--  操作栏  -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleClean"
                   :disabled="!checkPermit(['monitor:log:clean'])">清空</el-button>
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

    <!--  列表数据  -->
    <el-table ref="tables" v-loading="loading" :data="list" @selection-change="handleSelectionChange" :header-cell-style="{'text-align':'center'}">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="日志模块" align="center" prop="opModule">
        <template slot-scope="{row: {opModule}}">
          <template v-for="module in moduleOptions">
            <span v-if="opModule === module.key">{{ $t(module.label) }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column label="日志类型" align="center" prop="opType">
        <template slot-scope="{row: {opType, opModule}}">
          <template v-for="type in moduleOptions.find(item => item.key === opModule).children">
            <span v-if="opType === type.key">{{ $t(type.label) }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column label="日志动作" align="center" prop="opAction">
        <template slot-scope="{row: {opAction}}">
          <template v-for="action in dict.type.op_action">
            <span v-if="opAction === action.code">{{ $t(action.name) }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column label="日志描述" align="center" prop="opDesc" width="240" :show-overflow-tooltip="true" />
      <el-table-column label="访问ip" align="center" prop="ip"/>
      <el-table-column label="访问时间" align="center" prop="opTime" width="160"/>
      <el-table-column label="操作人" align="center" prop="access.accessUserName"/>
      <el-table-column label="操作结果" align="center" prop="opStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.success_failed" :value="scope.row.opStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="详情" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)"
                     v-hasPermi="['monitor:log:query']">详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>
    <component ref="infoRef" :is="infoView" @ok="handleQuery" />
  </div>
</template>

<script>
import {listOpLog, delOpLog, cleanOpLog} from "@/api/monitor/operlog";
import {checkPermit} from "@/utils/permission";
import {listTypeByGroup} from "@/api/system/dict";
export default {
  name: "Oplog",
  dicts: ['success_failed', 'op_action'],
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
        opModule: undefined,
        opType: undefined,
        opUser: undefined,
        beginTime: undefined,
        endTime: undefined
      },
      moduleOptions: [],
      typeOptions: [],
      viewKey: '',
      infoView: null
    };
  },
  created() {
    this.getOptions();
    this.getList();
  },
  methods: {
    checkPermit,
    getOptions() {
      listTypeByGroup("domain_module").then(response => {
        this.moduleOptions = response.data;
      });
    },
    /** 选择分组 */
    handleModuleChange() {
      const selectedOption = this.moduleOptions.find(item => item.key === this.queryParams.opModule);
      if (selectedOption && selectedOption.children) {
        this.typeOptions = selectedOption.children;
      } else {
        this.typeOptions = [];
      }
      this.queryParams.opType = undefined;
    },
    /** 查询登录日志 */
    getList() {
      this.loading = true;
      listOpLog(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
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
        return delOpLog(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 清空 */
    handleClean() {
      this.$modal.confirm('确认清空所有日志数据？').then(function() {
        return cleanOpLog();
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("清空成功");
      }).catch(() => {});
    },
    /** 导出 */
    handleExport() {
      this.download('/admin/api/v1/oplog/export', {
        ...this.queryParams
      }, `操作日志_${new Date().getTime()}.xlsx`)
    },
    /** 详情 */
    handleView(row) {
      let types = this.moduleOptions.find(item => item.key === row.opModule).children;
      if (types !== undefined) {
        let viewName = types.find(item => item.key === row.opType).value;
        if (viewName === undefined || viewName === null) {
          viewName = "log_view_" + row.opType;
        }
        this.infoView = this.getDialog(viewName)
        this.$nextTick(() => {
          this.$refs.infoRef.show(row)
        })
      }
    },
    getDialog(viewName) {
      return require(`@/views/monitor/operlog/detail/${viewName}.vue`).default;
    },
  }
};
</script>

