<template>
  <div class="app-container">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="所有请假" name="allLeave">
        <el-row>
          <el-form ref="allForm" :model="allParams" size="small" :inline="true" v-show="showSearch" label-width="auto">
            <el-form-item label="流程状态" prop="processStatus">
              <el-select v-model="allParams.processStatus" placeholder="选择流程状态">
                <el-option :key="1" :value="1" label="审批中" />
                <el-option :key="2" :value="2" label="已审批" />
                <el-option :key="0" :value="0" label="已撤销" />
              </el-select>
            </el-form-item>
            <el-form-item label="请假类型" prop="leaveType">
              <el-select v-model="allParams.leaveType" placeholder="选择请假类型">
                <el-option v-for="dict in dict.type.leave" :key="dict.value" :value="dict.value" :label="$t(dict.name)"/>
              </el-select>
            </el-form-item>
            <el-form-item label="请假时间" label-width="120">
              <el-date-picker v-model="allDateRange" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="datetimerange"
                              range-separator="-" start-placeholder="开始时间" end-placeholder="结束时间"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleAllQuery">{{$t('commons.button.search')}}</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetAllQuery">{{$t('commons.button.reset')}}</el-button>
            </el-form-item>
          </el-form>
          <el-row class="mb8">
            <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete"
                       :disabled="multiple">{{$t('commons.button.delete')}}</el-button>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getAllLeaves"/>
          </el-row>
        </el-row>
        <!-- 所有请假 -->
        <el-table v-loading="allLoading" :data="allLeaves" @selection-change="handleSelectionChange"
                  :header-cell-style="{'text-align':'center'}">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="55">
            <template slot-scope="scope">
              <span>{{(allParams.page - 1) * allParams.pageSize + scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column label="请假类型" align="center">
            <template slot-scope="{row: {leaveType}}">
              <template v-for="item in dict.type.leave">
                <span v-if="leaveType === item.value">{{ $t(item.name) }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column label="开始时间" align="center" prop="beginTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.beginTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="结束时间" align="center" prop="endTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.endTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="请假原因" align="center" prop="reason" width="200" :show-overflow-tooltip="true" />
          <el-table-column label="申请人" align="center" prop="applyUserName" :show-overflow-tooltip="true" />
          <el-table-column label="申请时间" align="center" prop="applyTime" width="180" />
          <el-table-column label="办理进度" align="center" prop="processTask">
            <template slot-scope="scope">
              <span v-if="scope.row.processStatus === 0">已撤销</span>
              <span v-else-if="scope.row.processStatus === 2">已审批</span>
              <span v-else>{{ scope.row.processTask }}</span>
            </template>
          </el-table-column>
          <el-table-column label="当前办理人" align="center" prop="processTaskUser" :show-overflow-tooltip="true" />
          <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding" width="200">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="handleDiagram(scope.row)"><svg-icon icon-class="flowhistory"/>流程进度</el-button>
              <el-button size="mini" type="text" @click="handleHistory(scope.row)"><svg-icon icon-class="history"/>流程记录</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="allTotal>0" :total="allTotal" :page.sync="allParams.page" :limit.sync="allParams.pageSize" @pagination="getAllLeaves"/>
      </el-tab-pane>

      <el-tab-pane label="我的请假" name="myLeave">
        <el-row>
          <el-form ref="myForm" :model="myParams" size="small" :inline="true" v-show="showSearch" label-width="auto">
            <el-form-item label="流程状态" prop="processStatus">
              <el-select v-model="myParams.processStatus" placeholder="选择流程状态">
                <el-option :key="1" :value="1" label="审批中" />
                <el-option :key="2" :value="2" label="已审批" />
                <el-option :key="0" :value="0" label="已撤销" />
              </el-select>
            </el-form-item>
            <el-form-item label="请假类型" prop="leaveType">
              <el-select v-model="myParams.leaveType" placeholder="选择请假类型">
                <el-option v-for="dict in dict.type.leave" :key="dict.value" :value="dict.value" :label="$t(dict.name)"/>
              </el-select>
            </el-form-item>
            <el-form-item label="请假时间" label-width="120">
              <el-date-picker v-model="myDateRange" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="datetimerange"
                              range-separator="-" start-placeholder="开始时间" end-placeholder="结束时间"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleMyQuery">{{$t('commons.button.search')}}</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetMyQuery">{{$t('commons.button.reset')}}</el-button>
            </el-form-item>
          </el-form>
          <el-row class="mb8">
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
            >{{$t('commons.button.create')}}</el-button>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getMyLeaves"/>
          </el-row>
        </el-row>
        <!-- 我的请假 -->
        <el-table v-loading="myLoading" :data="myLeaves" :header-cell-style="{'text-align':'center'}">
          <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="55">
            <template slot-scope="scope">
              <span>{{(myParams.page - 1) * myParams.pageSize + scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column label="请假类型" align="center">
            <template slot-scope="{row: {leaveType}}">
              <template v-for="item in dict.type.leave">
                <span v-if="leaveType === item.value">{{ $t(item.name) }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column label="开始时间" align="center" prop="beginTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.beginTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="结束时间" align="center" prop="endTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.endTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="请假原因" align="center" prop="reason" width="200" :show-overflow-tooltip="true" />
          <el-table-column label="申请人" align="center" prop="applyUserName" :show-overflow-tooltip="true" />
          <el-table-column label="申请时间" align="center" prop="applyTime" width="180" />
          <el-table-column label="办理进度" align="center" prop="processTask">
            <template slot-scope="scope">
              <span v-if="scope.row.processStatus === 0">已撤销</span>
              <span v-else-if="scope.row.processStatus === 2">已审批</span>
              <span v-else>{{ scope.row.processTask }}</span>
            </template>
          </el-table-column>
          <el-table-column label="当前办理人" align="center" prop="processTaskUser" :show-overflow-tooltip="true" />
          <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding" width="260">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="handleDiagram(scope.row)"><svg-icon icon-class="flowhistory"/>流程进度</el-button>
              <el-button size="mini" type="text" @click="handleHistory(scope.row)"><svg-icon icon-class="history"/>流程记录</el-button>
              <el-button :disabled="scope.row.processStatus !== 1" size="mini" type="text" @click="handlePress(scope.row)"><svg-icon icon-class="urge"/>催办</el-button>
              <el-button :disabled="scope.row.processStatus !== 1" size="mini" type="text" @click="handleRevocate(scope.row)"><svg-icon icon-class="revocate"/>撤销</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="myTotal>0" :total="myTotal" :page.sync="myParams.page" :limit.sync="myParams.pageSize" @pagination="getMyLeaves"/>
      </el-tab-pane>
    </el-tabs>

    <!--  新增  -->
    <el-dialog title="请假申请" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="请假类型" prop="leaveType">
          <el-select v-model="form.leaveType" placeholder="">
            <el-option v-for="dict in dict.type.leave" :key="dict.value" :value="dict.value" :label="$t(dict.name)"/>
          </el-select>
        </el-form-item>
        <el-form-item label="审批人" prop="deptApprover">
          <el-select v-model="form.deptApprover" placeholder="" style="width: 100%">
            <el-option v-for="item in approverOptions" :key="item.userId" :value="item.userId" :label="item.userName"/>
          </el-select>
        </el-form-item>
        <el-form-item label="请假人" prop="applyUserName">
          <el-input v-model="form.applyUserName" placeholder="" disabled="disabled"/>
        </el-form-item>
        <el-form-item label="请假时间" prop="dateRange">
          <el-date-picker v-model="dateRange" value-format="yyyy-MM-dd HH:mm:ss" type="datetimerange" style="width: 100%"
                          range-separator="-" start-placeholder="开始时间" end-placeholder="结束时间"/>
        </el-form-item>
        <el-form-item label="请假原因" prop="reason">
          <el-input v-model="form.reason" type="textarea" placeholder="..." />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm"
                   >{{$t('commons.button.confirm')}}</el-button>
        <el-button @click="cancel">{{$t('commons.button.cancel')}}</el-button>
      </div>
    </el-dialog>

    <!-- 流程进度 -->
    <el-dialog title="流程进度" :visible.sync="diagram.open" width="70%"  append-to-body>
      <el-image :src="diagram.src"  fit="contain"/>
    </el-dialog>

    <!-- 流程历史 -->
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
  </div>
</template>

<script>
import {checkPermit} from "@/utils/permission";
import {
  addLeave,
  delLeave,
  listAllLeave,
  listMyLeave,
  revocateLeave
} from "@/api/flow/leave";
import {getUserCandidates} from "@/api/system/user";
import {instanceHistory, instanceProgress} from "@/api/flow/manage/instance";
import {taskPress} from "@/api/flow/task";
import store from "@/store";

export default {
  name: "leave",
  dicts: ['leave'],
  data() {
    return {
      activeTab: 'myLeave',
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 我的请假
      myLoading: true,
      myTotal: 0,
      myLeaves: [],
      myDateRange: [],
      myParams: {
        page: 1,
        pageSize: 10
      },
      // 所有请假
      allLoading: true,
      allTotal: 0,
      allLeaves: [],
      allDateRange: [],
      allParams: {
        page: 1,
        pageSize: 10
      },
      // 是否显示弹出层
      open: false,
      // 表单参数
      dateRange: [],
      form: {
        leaveType: 1
      },
      // 审批人选项
      approverOptions: [],
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
    };
  },
  watch: {
    activeTab(tabName) {
      switch (tabName) {
        case 'allLeave':
          this.getAllLeaves();
          break;
        case 'myLeave':
          this.getMyLeaves();
          break;
        default:
          break;
      }
    }
  },
  created() {
    this.getMyLeaves();
  },
  computed: {
    rules() {
      return {
        deptApprover: [
          { required: true, message: '审批人不能为空', trigger: "blur" }
        ],
        reason: [
          { required: true, message: '请假原因不能为空', trigger: "blur" }
        ]
      };
    }
  },
  methods: {
    checkPermit,
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 重置所有 */
    resetAllQuery() {
      this.allDateRange = [];
      this.resetForm("allForm");
      this.handleAllQuery();
    },
    /** 搜索所有 */
    handleAllQuery() {
      this.allParams.page = 1;
      this.getAllLeaves();
    },
    /** 所有请假 */
    getAllLeaves() {
      this.allLoading = true;
      listAllLeave(this.addDateRange(this.allParams, this.allDateRange)).then(response => {
          this.allLeaves = response.data.list;
          this.allTotal = response.data.total;
          this.allLoading = false;
        }
      );
    },
    /** 重置我的 */
    resetMyQuery() {
      this.myDateRange = [];
      this.resetForm("myForm");
      this.handleMyQuery();
    },
    /** 搜索我的 */
    handleMyQuery() {
      this.myParams.page = 1;
      this.getMyLeaves();
    },
    /** 我的请假 */
    getMyLeaves() {
      this.myLoading = true;
      listMyLeave(this.addDateRange(this.myParams, this.myDateRange)).then(response => {
          this.myLeaves = response.data.list;
          this.myTotal = response.data.total;
          this.myLoading = false;
        }
      );
    },
    /** 流程进度 */
    handleDiagram(row){
      instanceProgress(row.processId).then(response => {
        this.diagram.src = 'data:image/jpg;base64,' + response.data;
        this.diagram.open = true;
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
    /** 表单重置 */
    reset() {
      this.form = {
        leaveType: 1
      };
      this.resetForm("form");
    },
    /** 取消 */
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 新增 */
    handleAdd() {
      this.reset();
      getUserCandidates().then(resp => {
        this.form.applyUserName = store.getters.name;
        if (resp.data && resp.data.length > 0) {
          this.approverOptions = resp.data;
          this.form.deptApprover = resp.data[0].userId;
        }
        this.open = true;
      });
    },
    /** 删除 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm("确认删除？").then(function() {
        return delLeave(ids);
      }).then(() => {
        this.getAllLeaves();
        this.$modal.msgSuccess(this.$t('commons.msg.success.delete'));
      }).catch(() => {});
    },
    /** 提交 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          addLeave(this.addDateRange(this.form, this.dateRange)).then(response => {
            this.$modal.msgSuccess(this.$t('commons.msg.success.create'));
            this.open = false;
            this.getMyLeaves();
          });
        }
      });
    },
    /** 催办 */
    handlePress(row){
      taskPress(row.taskId).then(response => {
        this.$modal.msgSuccess("已催办");
      });
    },
    /** 撤销 */
    handleRevocate(row){
      revocateLeave(row.id).then(response => {
        this.getMyLeaves();
        this.$modal.msgSuccess("已撤销");
      });
    },
  }
};
</script>
