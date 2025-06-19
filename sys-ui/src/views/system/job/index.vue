<template>
  <div class="app-container">
    <!--  筛选栏  -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="任务名称" prop="taskName">
        <el-input v-model="queryParams.taskName" placeholder="请输入任务名称" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="任务类型" prop="taskType">
        <el-select v-model="queryParams.taskType" placeholder="请选择任务类型" clearable>
          <el-option label="Java实例" value="BEAN"/>
          <el-option label="Groovy脚本" value="GROOVY"/>
          <el-option label="Python脚本" value="PYTHON"/>
          <el-option label="Php脚本" value="PHP"/>
          <el-option label="Nodejs脚本" value="NODEJS"/>
          <el-option label="Shell脚本" value="SHELL"/>
        </el-select>
      </el-form-item>
      <el-form-item label="开始时间" prop="taskBeginTime">
        <el-date-picker v-model="queryParams.taskBeginTime" clearable style="width: 100%"
                        value-format="yyyy-MM-dd" type="datetime" placeholder=""/>
      </el-form-item>
      <el-form-item label="结束时间" prop="taskEndTime">
        <el-date-picker v-model="queryParams.taskEndTime" clearable style="width: 100%"
                        value-format="yyyy-MM-dd" type="datetime" placeholder=""/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!--  操作栏  -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   :disabled="!checkPermit(['sys:job:create'])">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" @click="handleUpdate"
                   :disabled="single || !checkPermit(['sys:job:edit'])">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete"
                   :disabled="multiple || !checkPermit(['sys:job:delete'])">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                   :disabled="!checkPermit(['sys:job:export'])">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="el-icon-s-operation" size="mini" @click="handleJobLog"
                   :disabled="!checkPermit(['sys:job:log:query'])">任务日志</el-button>
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
      <el-table-column label="任务名称" align="center" prop="taskName" :show-overflow-tooltip="true" />
      <el-table-column label="任务类型" align="center" prop="taskType" :show-overflow-tooltip="true" />
      <el-table-column label="调用目标" align="center" prop="handlerName" :show-overflow-tooltip="true" />
      <el-table-column label="开始时间" align="center" prop="taskBeginTime" width="160" />
      <el-table-column label="结束时间" align="center" prop="taskEndTime" width="160" />
      <el-table-column label="调度类型" align="center" prop="triggerType" width="100" />
      <el-table-column label="调度参数" align="center" prop="triggerParam" width="100" />
      <el-table-column label="调度成功次数" align="center" prop="times" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.triggerSuccessTimes }} / {{ scope.row.triggerTimes }}</span>
        </template>
      </el-table-column>
      <el-table-column label="上次调度时间" align="center" prop="triggerPrev" width="160" />
      <el-table-column label="下次调度时间" align="center" prop="triggerNext" width="160" />
      <el-table-column label="状态" align="center" width="60" >
        <template slot-scope="scope">
          <el-switch :disabled="!checkPermit(['sys:job:status'])" v-model="scope.row.status" :active-value=1 :inactive-value=0 @change="handleStatusChange(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     :disabled="!checkPermit(['sys:job:delete'])">删除</el-button>
          <el-button size="mini" type="text" icon="el-icon-caret-right" @click="handleExec(scope.row)"
                     :disabled="!checkPermit(['sys:job:exec'])">执行</el-button>
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
            <el-form-item label="调度类型" prop="triggerType">
              <el-radio-group v-model="form.triggerType" size="small">
                <el-radio-button label="CRON">CRON</el-radio-button>
                <el-radio-button label="FIX_RATE">固定频率</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item v-if="form.triggerType === 'CRON'" label="cron表达式" prop="triggerParam">
              <el-input v-model="form.triggerParam" placeholder="请输入cron执行表达式">
                <template slot="append">
                  <el-button type="primary" @click="handleShowCron">
                    构建表达式
                    <i class="el-icon-time el-icon--right"></i>
                  </el-button>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item v-if="form.triggerType === 'FIX_RATE'" label="固定间隔时长" prop="triggerParam">
              <el-input-number v-model="form.triggerParam" controls-position="right" :min="0" style="width: 100%"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="开始时间" prop="taskBeginTime">
              <el-date-picker v-model="form.taskBeginTime" style="width: 100%"
                              value-format="yyyy-MM-dd HH:mm:ss" type="datetime" placeholder="开始时间"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="taskEndTime">
              <el-date-picker v-model="form.taskEndTime" style="width: 100%"
                              value-format="yyyy-MM-dd HH:mm:ss" type="datetime" placeholder="结束时间"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="任务超时" prop="taskOverTime">
              <el-input-number v-model="form.taskOverTime" controls-position="right" :min="0" style="width: 100%"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="过期策略" prop="misfireStrategy">
              <el-select v-model="form.misfireStrategy" style="width: 100%">
                <el-option label="忽略" value="IGNORE"/>
                <el-option label="立即执行" value="FIRE_NOW"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="路由策略" prop="routeStrategy">
              <el-select v-model="form.routeStrategy" style="width: 100%">
                <el-option label="轮询" value="ROUND"/>
                <el-option label="随机" value="RANDOM"/>
                <el-option label="Hash散列" value="CONSISTENT_HASH"/>
                <el-option label="故障转移" value="FAIL_OVER"/>
                <el-option label="忙碌转移" value="BUSY_OVER"/>
                <el-option label="分片广播" value="SHARDING_BROADCAST"/>
                <el-option label="最不经常使用" value="LEAST_FREQUENTLY_USED"/>
                <el-option label="最久未使用" value="LEAST_RECENTLY_USED"/>
                <el-option label="取第一个" value="FIRST"/>
                <el-option label="取最后一个" value="LAST"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="阻塞策略" prop="blockStrategy">
              <el-select v-model="form.blockStrategy" style="width: 100%">
                <el-option label="单机串行" value="SERIAL_EXECUTION"/>
                <el-option label="丢弃后续任务" value="DISCARD_LATER"/>
                <el-option label="覆盖之前任务" value="COVER_EARLY"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="任务类型" prop="taskType">
              <el-radio-group v-model="form.taskType" size="small">
                <el-radio-button label="BEAN">
                  <el-tooltip placement="top">
                    <div slot="content">
                      @Job声明的方法
                    </div>
                    <i class="el-icon-question"></i>
                  </el-tooltip>
                  Java实例
                </el-radio-button>
                <el-radio-button label="GROOVY">Groovy脚本</el-radio-button>
                <el-radio-button label="PYTHON">Python脚本</el-radio-button>
                <el-radio-button label="PHP">Php脚本</el-radio-button>
                <el-radio-button label="NODEJS">Nodejs脚本</el-radio-button>
                <el-radio-button label="SHELL">Shell脚本</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-col :span="12" v-if="form.taskType === 'BEAN'">
              <el-form-item label="调用目标" prop="handlerName">
                <el-input v-model="form.handlerName" style="width: 100%"/>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="form.taskType === 'BEAN'">
              <el-form-item label="调用参数" prop="handlerParam">
                <el-input v-model="form.handlerParam" style="width: 100%"/>
              </el-form-item>
            </el-col>
          </el-col>
          <el-col :span="24" v-if="form.taskType !== 'BEAN'">
            <el-form-item prop="glueSource">
              <span slot="label">
                脚本内容
                <el-tooltip placement="top">
                  <div slot="content">
                    示例：
                    <br />...
                  </div>
                  <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
              <el-input v-model="form.glueSource" type="textarea" placeholder="" :rows="10"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm"
                   :disabled="!checkPermit(['sys:job:edit'])">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="Cron表达式" :visible.sync="openCron" append-to-body destroy-on-close>
      <crontab @hide="openCron=false" @fill="crontabFill" :expression="expression"></crontab>
    </el-dialog>
  </div>
</template>

<script>
import {listJob, getJob, delJob, addJob, updateJob, execJob, switchJobStatus} from "@/api/system/job";
import Crontab from '@/components/Crontab/index.vue'
import {checkPermit} from "@/utils/permission";
export default {
  components: { Crontab },
  name: "Job",
  dicts: [],
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
      list: [],
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
        pageSize: 10
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
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
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
    /** 任务列表 */
    getList() {
      this.loading = true;
      listJob(this.queryParams).then(response => {
        this.list = response.data.list;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 状态修改 */
    handleStatusChange(row) {
      let text = row.status === 1 ? "启用" : "停用";
      this.$modal.confirm('确认' + text + '任务[' + row.taskName + ']？').then(function() {
        return switchJobStatus(row.id, row.status);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        row.status = row.status === 1 ? 0 : 1;
      });
    },
    /** 立即执行 */
    handleExec(row) {
      this.$modal.confirm('确认立即执行任务[' + row.taskName + ']？').then(function() {
        return execJob(row.id);
      }).then(() => {
        this.$modal.msgSuccess('任务[' + row.taskName + ']执行成功');
      }).catch(() => {});
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
    /** 导出 */
    handleExport() {
      this.download('/quartz/api/v1/task/export', {
        ...this.queryParams
      }, `定时任务_${new Date().getTime()}.xlsx`)
    },
    /** 任务日志 */
    handleJobLog() {
      this.$router.push({ path: '/system/job-log/index' })
    },
    /** 定时表达式 */
    handleShowCron() {
      this.expression = this.form.triggerParam;
      this.openCron = true;
    },
    /** 定时表达式确定 */
    crontabFill(value) {
      this.form.triggerParam = value;
    },
    /** 取消 */
    cancel() {
      this.open = false;
      this.reset();
    },





    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        taskName: undefined,
        taskType: 'BEAN',
        taskBeginTime: undefined,
        taskEndTime: undefined,
        triggerType: 'CRON',
        triggerParam: undefined,
        handlerName: undefined,
        handlerParam: undefined,
        glueSource: undefined,
        taskOverTime: 0,
        misfireStrategy: 'IGNORE',
        routeStrategy: 'ROUND',
        blockStrategy: 'SERIAL_EXECUTION',
      };
      this.resetForm("form");
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
  }
};
</script>
