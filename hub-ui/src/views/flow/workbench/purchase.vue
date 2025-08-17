<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="auto">
      <el-form-item label="采购状态" label-width="120">
        <el-select v-model="queryParams.processStatus" placeholder="选择采购状态">
          <el-option :key="1" :value="1" label="部门审批" />
          <el-option :key="2" :value="2" label="财务审批" />
          <el-option :key="3" :value="3" label="总经理审批" />
          <el-option :key="4" :value="4" label="审批驳回" />
          <el-option :key="5" :value="5" label="待付款" />
          <el-option :key="6" :value="6" label="待收货" />
          <el-option :key="7" :value="7" label="已收货" />
          <el-option :key="8" :value="8" label="已撤销" />
        </el-select>
      </el-form-item>
      <el-form-item label="申请时间" label-width="120">
        <el-date-picker v-model="dateRange" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" start-placeholder="开始时间" end-placeholder="结束时间"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{$t('commons.button.search')}}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('commons.button.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   >{{$t('commons.button.create')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete"
                   :disabled="multiple">{{$t('commons.button.delete')}}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="list"
              @selection-change="handleSelectionChange" :header-cell-style="{'text-align':'center'}">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="采购清单" align="center" prop="content" :show-overflow-tooltip="true" />
      <el-table-column label="总金额" align="center" prop="money" :show-overflow-tooltip="true" />
      <el-table-column label="申请人" align="center" prop="applyUserName" />
      <el-table-column label="申请时间" align="center" prop="applyTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.applyTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="办理进度" align="center" prop="remark" :show-overflow-tooltip="true">
        <template slot-scope="{row: {processStatus}}">
          <span v-if="processStatus === 1">部门审批</span>
          <span v-else-if="processStatus === 2">财务审批</span>
          <span v-else-if="processStatus === 3">总经理审批</span>
          <span v-else-if="processStatus === 4">审批驳回</span>
          <span v-else-if="processStatus === 5">待付款</span>
          <span v-else-if="processStatus === 6">待收货</span>
          <span v-else-if="processStatus === 7">已收货</span>
          <span v-else-if="processStatus === 8">已撤销</span>
        </template>
      </el-table-column>
      <el-table-column label="当前办理人" align="center" prop="processTaskUser" :show-overflow-tooltip="true" />
      <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding" width="260">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleDiagram(scope.row)"><svg-icon icon-class="flowhistory"/>流程进度</el-button>
          <el-button size="mini" type="text" @click="handleHistory(scope.row)"><svg-icon icon-class="history"/>流程记录</el-button>
          <el-button :disabled="scope.row.processStatus !== 1" size="mini" type="text" @click="handlePress(scope.row)"><svg-icon icon-class="urge"/>催办</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 新增 -->
    <el-dialog title="申请采购" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="采购清单" prop="content">
          <el-input v-model="form.content" type="textarea" placeholder="" :rows="8"/>
        </el-form-item>
        <el-form-item label="总金额" prop="money">
          <el-input v-model="form.money" placeholder="" />
        </el-form-item>
        <el-form-item label="部门领导" prop="manager">
          <el-select v-model="form.manager" placeholder="" style="width: 100%">
            <el-option v-for="item in managerOptions" :key="item.userId" :label="item.userName" :value="item.userId"/>
          </el-select>
        </el-form-item>
        <el-form-item label="财务" prop="finance">
          <el-select v-model="form.finance" placeholder="" style="width: 100%">
            <el-option v-for="item in financeOptions" :key="item.userId" :label="item.userName" :value="item.userId"/>
          </el-select>
        </el-form-item>
        <el-form-item label="总经理" prop="general">
          <el-select v-model="form.general" placeholder="" style="width: 100%">
            <el-option v-for="item in generalOptions" :key="item.userId" :label="item.userName" :value="item.userId"/>
          </el-select>
        </el-form-item>
        <el-form-item label="出纳" prop="cashier">
          <el-select v-model="form.cashier" placeholder="" style="width: 100%">
            <el-option v-for="item in cashierOptions" :key="item.userId" :label="item.userName" :value="item.userId"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{$t('commons.button.confirm')}}</el-button>
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
import {addPurchase, delPurchase, listPurchase} from "@/api/flow/purchase";
import {getUserCandidates} from "@/api/system/user";
import {getPostCandidatesByCode} from "@/api/system/post";
import {instanceHistory, instanceProgress} from "@/api/flow/manage/instance";

export default {
  name: "",
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
      // 显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10
      },
      // 表单参数
      form: {},
      managerOptions: [],
      financeOptions: [],
      generalOptions: [],
      cashierOptions: [],
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
  created() {
    this.getList();
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

      };
      this.resetForm("form");
    },
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    getManagerOptions(){
      getUserCandidates().then(resp => {
        if (resp.data && resp.data.length > 0) {
          this.managerOptions = resp.data;
          this.form.manager = resp.data[0].userId;
        }
      });
    },
    getFinanceOptions(){
      getPostCandidatesByCode('ACCT').then(resp => {
        if (resp.data && resp.data.length > 0) {
          this.financeOptions = resp.data;
          this.form.finance = resp.data[0].userId;
        }
      });
    },
    getGeneralOptions(){
      getPostCandidatesByCode('GM').then(resp => {
        if (resp.data && resp.data.length > 0) {
          this.generalOptions = resp.data;
          this.form.general = resp.data[0].userId;
        }
      });
    },
    getCashierOptions(){
      getPostCandidatesByCode('AC').then(resp => {
        if (resp.data && resp.data.length > 0) {
          this.cashierOptions = resp.data;
          this.form.cashier = resp.data[0].userId;
        }
      });
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
        pageSize: 10
      },
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 列表 */
    getList() {
      this.loading = true;
      listPurchase(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.list = response.data.list;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    /** 新增 */
    handleAdd() {
      this.reset();
      this.getManagerOptions();
      this.getFinanceOptions();
      this.getGeneralOptions();
      this.getCashierOptions();
      this.open = true;
    },
    /** 删除 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm("确认删除？").then(function() {
        return delPurchase(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('commons.msg.success.delete'));
      }).catch(() => {});
    },
    /** 取消 */
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 提交 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          addPurchase(this.form).then(response => {
            this.$modal.msgSuccess("采购申请已提交");
            this.open = false;
            this.getList();
          });
        }
      });
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
  }
};
</script>
