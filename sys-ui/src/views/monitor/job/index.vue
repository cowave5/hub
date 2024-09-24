<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="任务名称" prop="jobName">
        <el-input v-model="queryParams.taskName" placeholder="请输入任务名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="任务分组" prop="jobGroup">
        <el-select v-model="queryParams.taskGroup" placeholder="请选择任务组名" clearable>
          <el-option v-for="dict in dict.type.quartz_group" :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   :disabled="!checkPermit(['monitor:quartz:new'])">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" @click="handleUpdate"
                   :disabled="single || !checkPermit(['monitor:quartz:edit'])">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete"
                   :disabled="multiple || !checkPermit(['monitor:quartz:delete'])">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                   :disabled="!checkPermit(['monitor:quartz:export'])">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-refresh" size="mini" @click="handleRefresh"
                   :disabled="!checkPermit(['monitor:quartz:refresh'])">重新加载</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="el-icon-s-operation" size="mini" @click="handleJobLog"
                   :disabled="!checkPermit(['monitor:quartz:log:query'])">任务日志</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="jobList" @selection-change="handleSelectionChange" :header-cell-style="{'text-align':'center'}">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" align="center" width="50" >
        <template slot-scope="scope">
          <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="任务名称" align="center" prop="taskName" width="100" :show-overflow-tooltip="true" />
      <el-table-column label="任务分组" align="center" prop="taskGroup" width="80" :show-overflow-tooltip="true">
        <template slot-scope="{row: {taskGroup}}">
          <template v-for="item in dict.type.quartz_group">
            <span v-if="taskGroup === item.value && $i18n.locale==='zh'">{{ item.label }}</span>
            <span v-if="taskGroup === item.value && $i18n.locale==='en'">{{ item.labelEn }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column label="调用目标" align="left" prop="invokeTarget" :show-overflow-tooltip="true" />
      <el-table-column label="优先级" align="center" prop="priority" width="60" />
      <el-table-column label="开始时间" align="center" prop="beginTime" width="160" />
      <el-table-column label="结束时间" align="center" prop="endTime" width="160" />
      <el-table-column label="cron"   align="center" prop="cron" width="100" :show-overflow-tooltip="true" />
      <el-table-column label="执行次数" align="center" prop="times" width="80" />
      <el-table-column label="上次执行时间" align="center" prop="timePrev" width="160" />
      <el-table-column label="下次执行时间" align="center" prop="timeNext" width="160" />
      <el-table-column label="状态" align="center" width="60" >
        <template slot-scope="scope">
          <el-switch :disabled="!checkPermit(['monitor:quartz:status'])" v-model="scope.row.status" :active-value=1 :inactive-value=0 @change="handleStatusChange(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     :disabled="!checkPermit(['monitor:quartz:delete'])">删除</el-button>
          <el-button size="mini" type="text" icon="el-icon-caret-right" @click="handleRun(scope.row)"
                     :disabled="!checkPermit(['monitor:quartz:exec'])">执行</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 添加或修改 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="任务名称" prop="taskName">
              <el-input v-model="form.taskName" placeholder="请输入任务名称" style="width: 260px"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务分组" prop="taskGroup">
              <el-select v-model="form.taskGroup" placeholder="请选择任务分组" style="width: 260px">
                <el-option v-for="dict in dict.type.quartz_group" :key="dict.value" :label="dict.label" :value="dict.value"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="开始时间" prop="beginTime">
              <el-date-picker v-model="form.beginTime" style="width: 260px"
                              value-format="yyyy-MM-dd HH:mm:ss" type="datetime" placeholder="开始时间"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endTime">
              <el-date-picker v-model="form.endTime" style="width: 260px"
                              value-format="yyyy-MM-dd HH:mm:ss" type="datetime" placeholder="结束时间"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item prop="invokeTarget">
              <span slot="label">
                调用目标
                <el-tooltip placement="top">
                  <div slot="content">
                    Bean调用示例：testTask.test1()
                    <br />Class调用示例：com.cowave.sys.quartz.task.test2('test')
                    <br />参数支持：string，bool，long，float，int
                  </div>
                  <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
              <el-input v-model="form.invokeTarget" placeholder="请输入调用目标字符串" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="cron表达式" prop="cron">
              <el-input v-model="form.cron" placeholder="请输入cron执行表达式">
                <template slot="append">
                  <el-button type="primary" @click="handleShowCron">
                    构建表达式
                    <i class="el-icon-time el-icon--right"></i>
                  </el-button>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="并发执行" prop="concurrent">
              <el-radio-group v-model="form.concurrent" size="small">
                <el-radio-button label="1">允许</el-radio-button>
                <el-radio-button label="0">禁止</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务优先级" prop="priority">
              <el-input-number v-model="form.priority" controls-position="right" :min="0" :max="5" style="width: 260px"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="执行策略" prop="policy">
              <el-radio-group v-model="form.policy" size="small">
                <el-radio-button label="1">
                  <el-tooltip placement="top">
                    <div slot="content">
                      立即执行一次，然后按照Cron定时计划执行
                    </div>
                    <i class="el-icon-question"></i>
                  </el-tooltip>
                  立即执行
                </el-radio-button>
                <el-radio-button label="2">
                  <el-tooltip placement="top">
                    <div slot="content">
                      什么都不做，等待下次Cron定时计划执行
                    </div>
                    <i class="el-icon-question"></i>
                  </el-tooltip>
                  计划执行
                </el-radio-button>
                <el-radio-button label="-1">
                  <el-tooltip placement="top">
                    <div slot="content">
                      将之前所有错过的执行全都补上
                    </div>
                    <i class="el-icon-question"></i>
                  </el-tooltip>
                  补偿执行
                </el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm"
                   :disabled="!checkPermit(['monitor:quartz:edit'])">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="Cron表达式" :visible.sync="openCron" append-to-body destroy-on-close>
      <crontab @hide="openCron=false" @fill="crontabFill" :expression="expression"></crontab>
    </el-dialog>
  </div>
</template>

<script>
import {listJob, getJob, delJob, addJob, updateJob, runJob, changeJobStatus, refreshJob} from "@/api/monitor/job";
import Crontab from '@/components/Crontab'
import {checkPermit} from "@/utils/permission";

export default {
  components: { Crontab },
  name: "Job",
  dicts: ['quartz_group'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 定时任务表格数据
      jobList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示Cron表达式弹出层
      openCron: false,
      // 传入的表达式
      expression: "",
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        taskName: undefined,
        taskGroup: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        taskName: [
          { required: true, message: "任务名称不能为空", trigger: "blur" }
        ],
        invokeTarget: [
          { required: true, message: "调用目标字符串不能为空", trigger: "blur" }
        ],
        cron: [
          { required: true, message: "cron表达式不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    checkPermit,
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        taskName: undefined,
        taskGroup: undefined,
        invokeTarget: undefined,
        cron: undefined,
        policy: 1,
        priority: 5,
        concurrent: 0
      };
      this.resetForm("form");
    },
    /** 搜索 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置 */
    resetQuery() {
      this.queryParams = {
         page: 1,
          pageSize: 10,
          taskName: undefined,
          taskGroup: undefined,
      },
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 任务列表 */
    getList() {
      this.loading = true;
      listJob(this.queryParams).then(response => {
        this.jobList = response.data.list;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 取消 */
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 状态修改 */
    handleStatusChange(row) {
      let text = row.status === 1 ? "启用" : "停用";
      this.$modal.confirm('确认' + text + '任务[' + row.taskName + ']？').then(function() {
        return changeJobStatus(row.id, row.status);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        row.status = row.status === 1 ? 0 : 1;
      });
    },
    /** 立即执行 */
    handleRun(row) {
      this.$modal.confirm('确认立即执行任务[' + row.taskName + ']？').then(function() {
        return runJob(row.id);
      }).then(() => {
        this.$modal.msgSuccess('任务[' + row.taskName + ']执行成功');
      }).catch(() => {});
    },
    /** cron表达式 */
    handleShowCron() {
      this.expression = this.form.cron;
      this.openCron = true;
    },
    /** cron表达式确定 */
    crontabFill(value) {
      this.form.cron = value;
    },
    /** 任务日志 */
    handleJobLog() {
      this.$router.push({ path: '/monitor/job-log/index' })
    },
    /** 新增 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增任务";
    },
    /** 修改 */
    handleUpdate(row) {
      this.reset();
      const jobId = row.id || this.ids;
      getJob(jobId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改任务";
      });
    },
    /** 提交 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            updateJob(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addJob(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除 */
    handleDelete(row) {
      const jobIds = row.id || this.ids;
      const msg = row.id ? '确认删除任务[' + row.taskName + ']?' : '确认删除所选任务？';
      this.$modal.confirm(msg).then(function() {
        return delJob(jobIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 重新加载 */
    handleRefresh() {
      this.$modal.confirm('确认重新加载所有任务？').then(function() {
        return refreshJob();
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("重新加载完成");
      }).catch(() => {});
    },
    /** 导出 */
    handleExport() {
      this.download('/quartz/api/v1/task/export', {
        ...this.queryParams
      }, `定时任务_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
