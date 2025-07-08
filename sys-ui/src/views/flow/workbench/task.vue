<template>
  <div class="app-container">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="所有任务" name="allTask">
        <el-row>
          <el-form :model="allParams" ref="allForm" size="small" :inline="true" v-show="showSearch" label-width="auto" >
            <el-form-item label="任务流程">
              <el-select v-model="allParams.processKey" placeholder="选择流程类型">
                <el-option v-for="flow in flowOptions" :key="flow.key" :label="`${flow.name}`" :value="flow.key"/>
              </el-select>
            </el-form-item>
            <el-form-item label="任务开始时间" label-width="120">
              <el-date-picker v-model="allDateRange" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="datetimerange"
                              range-separator="-" :start-placeholder="$t('commons.label.beginDate')" :end-placeholder="$t('commons.label.endDate')"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleAllQuery">{{$t('commons.button.search')}}</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetAllQuery">{{$t('commons.button.reset')}}</el-button>
            </el-form-item>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getAllTasks"/>
          </el-form>
        </el-row>
        <el-table v-loading="allLoading" :data="allTasks"
                  @selection-change="handleSelectionChange" :header-cell-style="{'text-align':'center'}">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="55">
            <template slot-scope="scope">
              <span>{{(allParams.page - 1) * allParams.pageSize + scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column label="任务名称" align="center" prop="taskName" :show-overflow-tooltip="true" />
          <el-table-column label="任务开始时间" align="center" prop="createTime" width="200">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.beginTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="流程名称" align="center" prop="processName" :show-overflow-tooltip="true" />
          <el-table-column label="流程开始时间" align="center" prop="createTime" width="200">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.startTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="发起人" align="center" prop="starterName" />
          <el-table-column label="办理人" align="center" prop="assigneeName" :show-overflow-tooltip="true" />
          <el-table-column label="任务截止时间" align="center" prop="createTime" width="200">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.dueTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding fixed-width" width="280">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="handleAssignee(scope.row)"><svg-icon icon-class="flow-admin"/>修改办理人</el-button>
              <el-button size="mini" type="text" @click="handleHistory(scope.row)"><svg-icon icon-class="history"/>办理记录</el-button>
              <el-button size="mini" type="text" @click="handlePress(scope.row)"><svg-icon icon-class="urge"/>催办</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="allTotal>0" :total="allTotal" :page.sync="allParams.page" :limit.sync="allParams.pageSize" @pagination="getAllTasks"/>
      </el-tab-pane>

      <el-tab-pane label="我的待办" name="myTask">
        <el-row>
          <el-form :model="myParams" ref="myForm" size="small" :inline="true" v-show="showSearch" label-width="auto" >
            <el-form-item label="任务流程">
              <el-select v-model="myParams.processKey" placeholder="选择流程类型">
                <el-option v-for="flow in flowOptions" :key="flow.key" :label="`${flow.name}`" :value="flow.key"/>
              </el-select>
            </el-form-item>
            <el-form-item label="任务开始时间" label-width="120">
              <el-date-picker v-model="myDateRange" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="datetimerange"
                              range-separator="-" :start-placeholder="$t('commons.label.beginDate')" :end-placeholder="$t('commons.label.endDate')"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleMyQuery">{{$t('commons.button.search')}}</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetMyQuery">{{$t('commons.button.reset')}}</el-button>
            </el-form-item>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getMyTasks"/>
          </el-form>
        </el-row>
        <el-table v-loading="myLoading" :data="myTasks"
                  @selection-change="handleSelectionChange" :header-cell-style="{'text-align':'center'}">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="55">
            <template slot-scope="scope">
              <span>{{(myParams.page - 1) * myParams.pageSize + scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column label="任务名称" align="center" prop="taskName" :show-overflow-tooltip="true" />
          <el-table-column label="任务开始时间" align="center" prop="createTime" width="200">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.beginTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="流程名称" align="center" prop="processName" :show-overflow-tooltip="true" />
          <el-table-column label="流程开始时间" align="center" prop="createTime" width="200">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.startTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="发起人" align="center" prop="starterName" />
          <el-table-column label="办理人" align="center" prop="assigneeName" :show-overflow-tooltip="true" />
          <el-table-column label="任务截止时间" align="center" prop="createTime" width="200">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.dueTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleForm(scope.row, false)">办理</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="myTotal>0" :total="myTotal" :page.sync="myParams.page" :limit.sync="myParams.pageSize" @pagination="getMyTasks"/>
      </el-tab-pane>

      <el-tab-pane label="我办理的" name="myHistory">
        <el-row>
          <el-form :model="historyParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="auto" >
            <el-form-item label="任务流程">
              <el-select v-model="historyParams.processKey" placeholder="选择流程类型">
                <el-option v-for="flow in flowOptions" :key="flow.key" :label="`${flow.name}`" :value="flow.key"/>
              </el-select>
            </el-form-item>
            <el-form-item label="办理时间" label-width="120">
              <el-date-picker v-model="historyDateRange" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="datetimerange"
                              range-separator="-" :start-placeholder="$t('commons.label.beginDate')" :end-placeholder="$t('commons.label.endDate')"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleHistoryQuery">{{$t('commons.button.search')}}</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetHistoryQuery">{{$t('commons.button.reset')}}</el-button>
            </el-form-item>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getHistoryTasks"/>
          </el-form>
        </el-row>
        <el-table v-loading="historyLoading" :data="historyTasks"
                  @selection-change="handleSelectionChange" :header-cell-style="{'text-align':'center'}">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="55">
            <template slot-scope="scope">
              <span>{{(historyParams.page - 1) * historyParams.pageSize + scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column label="任务名称" align="center" prop="taskName" :show-overflow-tooltip="true" />
          <el-table-column label="任务开始时间" align="center" prop="createTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.beginTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="流程名称" align="center" prop="processName" :show-overflow-tooltip="true" />
          <el-table-column label="流程开始时间" align="center" prop="createTime" width="200">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.startTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="发起人" align="center" prop="starterName" />
          <el-table-column label="办理人" align="center" prop="assigneeName" :show-overflow-tooltip="true" />
          <el-table-column label="办理时间" align="center" prop="endTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.endTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="备注" align="center" prop="comment" :show-overflow-tooltip="true" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="handleForm(scope.row, true)"><svg-icon icon-class="info"/>办理详情</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="historyTotal>0" :total="historyTotal" :page.sync="historyParams.page" :limit.sync="historyParams.pageSize" @pagination="getHistoryTasks"/>
      </el-tab-pane>
    </el-tabs>

    <!-- 办理记录 -->
    <el-dialog title="流程历史" :visible.sync="history.open" width="75%" append-to-body>
      <el-table v-loading="history.loading" :data="history.list" :header-cell-style="{'text-align':'center'}">
        <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="55">
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

    <!-- 修改办理人 -->
    <el-dialog title="修改办理人" :visible.sync="assignee.open" width="750px" append-to-body >
      <el-form ref="form" :model="form" :rules="rules" label-width="110px" >
        <el-row>
          <el-col :span="12">
            <el-form-item label="选择办理人">
              <treeselect v-model="assignee.userId" :options="assignee.userOptions"
                          :multiple="false" :disable-branch-nodes="true" placeholder="" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-button type="primary" @click="changeAssignee" style="margin-left: 40px;">确认</el-button>
            <el-button @click="cancelAssignee" style="margin-left: 10px;">取消</el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-dialog>

    <!--  办理表单  -->
    <leave-dept v-if="formKey === 'leave-dept'" ref="leave-dept" @ok="getMyTasks" />
    <leave-dept-reject v-if="formKey === 'leave-dept-reject'" ref="leave-dept-reject" @ok="getMyTasks" />
    <leave-hr v-if="formKey === 'leave-hr'" ref="leave-hr" @ok="getMyTasks" />
    <leave-hr-reject v-if="formKey === 'leave-hr-reject'" ref="leave-hr-reject" @ok="getMyTasks" />
    <meeting-sign v-if="formKey === 'meeting-sign'" ref="meeting-sign" @ok="getMyTasks" />
    <meeting-summary v-if="formKey === 'meeting-summary'" ref="meeting-summary" @ok="getMyTasks" />
    <purchase-manager v-if="formKey === 'purchase-manager'" ref="purchase-manager" @ok="getMyTasks" />
    <purchase-finance v-if="formKey === 'purchase-finance'" ref="purchase-finance" @ok="getMyTasks" />
    <purchase-general v-if="formKey === 'purchase-general'" ref="purchase-general" @ok="getMyTasks" />
    <purchase-cashier v-if="formKey === 'purchase-cashier'" ref="purchase-cashier" @ok="getMyTasks" />
    <purchase-consignee v-if="formKey === 'purchase-consignee'" ref="purchase-consignee" @ok="getMyTasks" />
    <purchase-apply v-if="formKey === 'purchase-apply'" ref="purchase-apply" @ok="getMyTasks" />
  </div>
</template>

<script>
import {checkPermit} from "@/utils/permission";
import {listAllTask, listHistoryTask, listMyTask, taskAssignee, taskPress} from "@/api/flow/task";
import {optionsDeploy} from "@/api/flow/manage/deploy";
import {instanceHistory} from "@/api/flow/manage/instance";
import {getDeptUserDiagram} from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "workbench/task",
  dicts: [],
  components: {
    Treeselect,
    leaveDept: ()=> import('./form/leaveDept.vue'),
    leaveDeptReject: ()=> import('./form/leaveDeptReject.vue'),
    leaveHr: ()=> import('./form/leaveHr.vue'),
    leaveHrReject: ()=> import('./form/leaveHrReject.vue'),
    meetingSign: ()=> import('./form/meetingSign.vue'),
    meetingSummary: ()=> import('./form/meetingSummary.vue'),
    purchaseManager: ()=> import('./form/purchaseManager.vue'),
    purchaseFinance: ()=> import('./form/purchaseFinance.vue'),
    purchaseGeneral: ()=> import('./form/purchaseGeneral.vue'),
    purchaseCashier: ()=> import('./form/purchaseCashier.vue'),
    purchaseConsignee: ()=> import('./form/purchaseConsignee.vue'),
    purchaseApply: ()=> import('./form/purchaseApply.vue'),
  },
  data() {
    return {
      activeTab: 'myTask',
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 我的待办
      myLoading: true,
      myTasks: [],
      myTotal: 0,
      myDateRange: [],
      myParams: {
        page: 1,
        pageSize: 10
      },
      // 全部待办
      allLoading: true,
      allTasks: [],
      allTotal: 0,
      allDateRange: [],
      allParams: {
        page: 1,
        pageSize: 10
      },
      // 我办理的
      historyLoading: true,
      historyTasks: [],
      historyTotal: 0,
      historyDateRange: [],
      historyParams: {
        page: 1,
        pageSize: 10
      },
      // 表单key
      formKey: '',
      // 流程选项
      flowOptions: [],
      // 办理记录
      history: {
        open: false,
        loading: true,
        processInstanceId: undefined,
        list: [],
        total: 0,
        page: 1,
        pageSize: 10
      },
      // 修改办理人
      assignee: {
        open: false,
        userOptions: [],
        userId: undefined,
        taskId: undefined,

      },
    };
  },
  watch: {
    activeTab(tabName) {
      switch (tabName) {
        case 'allTask':
          this.getAllTasks();
          break;
        case 'myTask':
          this.getMyTasks();
          break;
        case 'myHistory':
          this.getHistoryTasks();
          break;
        default:
          break;
      }
    }
  },
  created() {
    this.getMyTasks();
    this.getFlowOptions();
    this.getUserOptions();
  },
  computed: {
    rules() {
      return {

      };
    }
  },
  methods: {
    checkPermit,
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.configId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 获取部门人员树 */
    getUserOptions(){
      getDeptUserDiagram().then(resp => {
        this.assignee.userOptions = resp.data
      });
    },
    /** 流程类型 */
    getFlowOptions(){
      optionsDeploy().then(response => {
        this.flowOptions = response.data;
      });
    },
    /** 重置全部待办 */
    resetAllQuery() {
      this.allDateRange = [];
      this.resetForm("allForm");
      this.handleAllQuery();
    },
    /** 搜索全部待办 */
    handleAllQuery() {
      this.allParams.page = 1;
      this.getAllTasks();
    },
    /** 全部待办 */
    getAllTasks() {
      this.allLoading = true;
      listAllTask(this.addDateRange(this.allParams, this.allDateRange)).then(response => {
          this.allTasks = response.data.list;
          this.allTotal = response.data.total;
          this.allLoading = false;
        }
      );
    },
    /** 重置我的待办 */
    resetMyQuery() {
      this.myDateRange = [];
      this.resetForm("myForm");
      this.handleMyQuery();
    },
    /** 搜索我的待办 */
    handleMyQuery() {
      this.myParams.page = 1;
      this.getMyTasks();
    },
    /** 我的待办 */
    getMyTasks() {
      this.myLoading = true;
      listMyTask(this.addDateRange(this.myParams, this.myDateRange)).then(response => {
          this.myTasks = response.data.list;
          this.myTotal = response.data.total;
          this.myLoading = false;
        }
      );
    },
    /** 重置我办理的 */
    resetHistoryQuery() {
      this.historyDateRange = [];
      this.resetForm("historyForm");
      this.handleHistoryQuery();
    },
    /** 搜索我的待办 */
    handleHistoryQuery() {
      this.historyParams.page = 1;
      this.getHistoryTasks();
    },
    /** 我办理的 */
    getHistoryTasks() {
      this.historyLoading = true;
      listHistoryTask(this.addDateRange(this.historyParams, this.historyDateRange)).then(response => {
          this.historyTasks = response.data.list;
          this.historyTotal = response.data.total;
          this.historyLoading = false;
        }
      );
    },
    /** 办理 */
    handleForm(row, complete) {
      row.complete = complete;
      this.formKey = row.formKey;
      setTimeout(()=>{
        this.$refs[row.formKey].show(row);
      });
    },
    /** 流程历史 */
    handleHistory(row){
      this.history.processInstanceId = row.processId;
      this.history.page = 1;
      this.history.pageSize = 10;
      this.getHistoryList();
    },
    /** 流程历史 */
    getHistoryList(){
      this.history.loading = true;
      instanceHistory(this.history).then(response => {
        this.history.list = response.data.list;
        this.history.total = response.data.total;
        this.history.loading = false;
        this.history.open = true;
      });
    },
    /** 修改办理人 */
    handleAssignee(row){
      this.assignee.userId = row.assignee;
      this.assignee.taskId = row.taskId;
      this.assignee.open = true;
    },
    /** 修改办理人 */
    changeAssignee(){
      taskAssignee(this.assignee.taskId, this.assignee.userId).then(() => {
        this.getAllTasks();
        this.$modal.msgSuccess("已修改任务办理人");
        this.assignee.open = false;
      });
    },
    /** 取消修改办理人 */
    cancelAssignee(){
      this.assignee.open = false;
    },
    /** 催办 */
    handlePress(row){
      taskPress(row.taskId).then(response => {
        this.$modal.msgSuccess("已催办");
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.vue-treeselect__multi-value {
  height: 24px;
  padding: 0 4px;
  line-height: 22px;
}

.vue-treeselect__multi-value-item-container {
  display: inline-block;
  padding-top: 0px;
  padding-right: 0px;
  vertical-align: top;
}

.vue-treeselect__multi-value-item {
  background-color: #f4f4f5;
  border-color: #e9e9eb;
  color: #909399;
}

.vue-treeselect__multi-value-item-container {
  display: inline-block;
  padding-right: 3px;
  vertical-align: top;
}

.vue-treeselect__value-remove {
  color: #606266;
  padding-left: 3px;
  border-left: 1px solid #fff;
  line-height: 0;
}
</style>
