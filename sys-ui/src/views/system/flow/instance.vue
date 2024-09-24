<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="">
        <el-select v-model="queryParams.active" placeholder="" @change="getList">
          <el-option :key="true" label="活动流程" :value="true"/>
          <el-option :key="false" label="历史流程" :value="false"/>
        </el-select>
      </el-form-item>
      <el-form-item label="流程类型">
        <el-select v-model="queryParams.processKey" placeholder="选择流程类型">
          <el-option v-for="flow in flowOptions" :key="flow.key" :label="`${flow.name}`" :value="flow.key"/>
        </el-select>
      </el-form-item>
      <el-form-item label="流程发起时间" label-width="120">
        <el-date-picker v-model="dateRange" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="datetimerange"
                        range-separator="-" :start-placeholder="$t(`label.date_begin`)" :end-placeholder="$t(`label.date_end`)"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{$t('button.search')}}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('button.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete"
                   :disabled="multiple">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="list"
              @selection-change="handleSelectionChange" :header-cell-style="{'text-align':'center'}">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t(`label.index`)" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="流程id" align="center" prop="instanceId" width="300" :show-overflow-tooltip="true" />
      <el-table-column label="流程名称" align="center" prop="instanceName" :show-overflow-tooltip="true" />
      <el-table-column label="发起人" align="center" prop="startUserName" :show-overflow-tooltip="true" />
      <el-table-column label="开始时间" align="center" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.beginTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="!queryParams.active" label="结束时间" align="center" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="queryParams.active" label="是否挂起" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.suspended">{{ '是' }}</span>
          <span v-else>{{ '否' }}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="!queryParams.active" label="是否结束" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.ended">{{ '是' }}</span>
          <span v-else>{{ '否' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="当前节点 | 办理人" align="center" width="200" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <div v-for="task in scope.row.taskList">{{ task.taskName }} | {{ task.assigneeName }}</div>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`label.option`)" align="center" class-name="small-padding" width="340">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleDiagram(scope.row)"><svg-icon icon-class="flowhistory"/>流程进度</el-button>
          <el-button size="mini" type="text" @click="handleHistory(scope.row)"><svg-icon icon-class="history"/>流程记录</el-button>
          <el-button size="mini" type="text" @click="handleVariable(scope.row)"><svg-icon icon-class="var"/>流程变量</el-button>
          <el-button v-if="queryParams.active && !scope.row.suspended && !scope.row.ended" size="mini" type="text" @click="handleSuspend(scope.row)"><svg-icon icon-class="suspend"/>挂起</el-button>
          <el-button v-if="queryParams.active && scope.row.suspended && !scope.row.ended" size="mini" type="text" @click="handleActivate(scope.row)"><svg-icon icon-class="activate"/>激活</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 流程进度 -->
    <el-dialog title="流程进度" :visible.sync="diagram.open" width="70%"  append-to-body>
      <el-image :src="diagram.src"  fit="contain"/>
    </el-dialog>

    <!-- 流程历史 -->
    <el-dialog title="流程历史" :visible.sync="history.open" width="75%" append-to-body>
      <el-table v-loading="history.loading" :data="history.list" :header-cell-style="{'text-align':'center'}">
        <el-table-column :label="$t(`label.index`)" type="index" align="center" width="55">
          <template slot-scope="scope">
            <span>{{(history.page - 1) * history.pageSize + scope.$index + 1}}</span>
          </template>
        </el-table-column>
        <el-table-column label="任务id" align="center" prop="taskId" width="300" :show-overflow-tooltip="true" />
        <el-table-column label="任务名称" align="center" prop="taskName" :show-overflow-tooltip="true" />
        <el-table-column label="开始时间" align="center">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.startTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="结束时间" align="center">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.endTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="办理人" align="center" prop="assigneeName" :show-overflow-tooltip="true" />
        <el-table-column label="备注" align="center" prop="comment" width="280" :show-overflow-tooltip="true" />
      </el-table>
      <pagination v-show="history.total>0" :total="history.total" :page.sync="history.page" :limit.sync="history.pageSize" @pagination="getHistoryList"/>
    </el-dialog>

    <!-- 流程变量 -->
    <el-dialog title="流程变量" :visible.sync="variable.open" width="60%" append-to-body>
        <el-table v-loading="variable.loading" :data="variable.list" :header-cell-style="{'text-align':'center'}">
          <el-table-column :label="$t(`label.index`)" type="index" align="center" width="55">
            <template slot-scope="scope">
              <span>{{(variable.page - 1) * variable.pageSize + scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="更新时间" align="center">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.lastUpdatedTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="变量名称" align="center" prop="variableName" :show-overflow-tooltip="true" />
          <el-table-column label="变量类型" align="center" prop="variableTypeName" :show-overflow-tooltip="true" />
          <el-table-column label="变量值" align="center" prop="value" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <el-input v-if="scope.row.edit" v-model="scope.row.value"/>
              <span v-else>{{ scope.row.value }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button v-if="scope.row.edit" type="text" size="medium" @click="confirmValue(scope.row, 'formData')">
                <i class="el-icon-check" aria-hidden="true"/>
              </el-button>
              <div v-else>
                <el-button type="text" size="medium" @click="editValue(scope.row)">
                  <i class="el-icon-edit" aria-hidden="true"/>
                </el-button>
                <el-button type="text" size="medium" @click="deleteValue(scope.row, scope.$index)">
                  <i class="el-icon-delete" aria-hidden="true"/>
                </el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
      <pagination v-show="variable.total>0" :total="variable.total" :page.sync="variable.page" :limit.sync="variable.pageSize" @pagination="getVariableList"/>
    </el-dialog>
  </div>
</template>

<script>
import {checkPermit} from "@/utils/permission";
import {addModel, deleteModel, deployModel, listModel} from "@/api/system/flow/modeler";
import {
  instanceList,
  instanceProgress,
  instanceHistory,
  instanceVariable,
  instanceVarEdit, instanceVarDelete, instanceDelete, instanceSuspend, instanceActivate
} from "@/api/system/flow/instance";
import {optionsDeploy} from "@/api/system/flow/deploy";

export default {
  name: "flow-Modler",
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
      // 表格数据
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        processKey: undefined,
        active: true
      },
      // 表单参数
      form: {},
      // 流程进度
      diagram: {
        open: false,
        src: undefined
      },
      // 流程历史
      history: {
        open: false,
        loading: true,
        processInstanceId: undefined,
        list: [],
        total: 0,
        page: 1,
        pageSize: 10
      },
      // 流程变量
      valueData: {},
      variable: {
        open: false,
        loading: true,
        processInstanceId: undefined,
        list: [],
        total: 0,
        page: 1,
        pageSize: 10
      },
      // 流程选项
      flowOptions: [],
    };
  },
  created() {
    this.getList();
    this.getFlowOptions();
  },
  computed: {
    rules() {
      return {

      };
    }
  },
  methods: {
    checkPermit,
    /** 表单重置 */
    reset() {
      this.form = {
        key: undefined,
        name: undefined,
        category: undefined,
        description: undefined
      };
      this.resetForm("form");
    },
    /** 流程类型 */
    getFlowOptions(){
      optionsDeploy().then(response => {
        this.flowOptions = response.data;
      });
    },
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.instanceId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 搜索 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置 */
    resetQuery() {
      this.dateRange = [];
      this.queryParams.processKey = undefined;
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 列表 */
    getList() {
      this.loading = true;
      instanceList(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.list = response.data.list;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    /** 历史列表 */
    getHistoryList() {
      this.history.loading = true;
      instanceHistory(this.history).then(response => {
        this.history.list = response.data.list;
        this.history.total = response.data.total;
        this.history.loading = false;
        this.history.open = true;
      });
    },
    /** 流程变量 */
    getVariableList() {
      this.variable.loading = true;
      instanceVariable(this.variable).then(response => {
        this.variable.list = response.data.list;
        this.variable.total = response.data.total;
        this.variable.loading = false;
        this.variable.open = true;
      });
    },
    /** 变量修改 */
    editValue(row) {
      row.edit = true;
    },
    /** 变量修改 */
    confirmValue(row) {
      instanceVarEdit(row).then(response => {
        row.edit = false;
      });
    },
    /** 变量删除 */
    deleteValue(row) {
      instanceVarDelete(row).then(response => {
        this.getVariableList();
      });
    },
    /** 流程进度 */
    handleDiagram(row){
      instanceProgress(row.instanceId).then(response => {
        this.diagram.src = 'data:image/jpg;base64,' + response.data;
        this.diagram.open = true;
      });
    },
    /** 流程历史 */
    handleHistory(row){
      this.history.processInstanceId = row.instanceId;
      this.history.page = 1;
      this.history.pageSize = 10;
      this.getHistoryList();
    },
    /** 流程变量 */
    handleVariable(row){
      this.variable.processInstanceId = row.instanceId;
      this.variable.page = 1;
      this.variable.pageSize = 10;
      this.getVariableList();
    },
    /** 流程删除 */
    handleDelete(row) {
      const ids = row.instanceId || this.ids;
      const msg = row.instanceId ? "确认删除流程[" + row.instanceName + "]" : "确认删除所选流程";
      this.$modal.confirm(msg).then(function() {
        return instanceDelete(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t(`msg.success_delete`));
      }).catch(() => {});
    },
    /** 流程挂起 */
    handleSuspend(row) {
      instanceSuspend(row.instanceId).then(response => {
        this.getList();
        this.$modal.msgSuccess("流程已挂起");
      });
    },
    /** 流程激活 */
    handleActivate(row) {
      instanceActivate(row.instanceId).then(response => {
        this.getList();
        this.$modal.msgSuccess("流程已激活");
      });
    }
  }
};
</script>
