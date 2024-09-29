<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" >
      <el-form-item label="会议室" prop="meetingRoom">
        <el-select v-model="queryParams.meetingRoom" placeholder="选择会议室">
          <el-option key="109接待大厅" value="109接待大厅" label="109接待大厅" />
          <el-option key="403白鲸会议室" value="403白鲸会议室" label="403白鲸会议室" />
          <el-option key="404麦克斯韦" value="404麦克斯韦" label="404麦克斯韦" />
          <el-option key="405惠更斯" value="405惠更斯" label="405惠更斯" />
        </el-select>
      </el-form-item>
      <el-form-item label="会议状态" label-width="120">
        <el-select v-model="queryParams.processStatus" placeholder="选择会议状态">
          <el-option :key="1" :value="1" label="待签到" />
          <el-option :key="2" :value="2" label="会议中" />
          <el-option :key="3" :value="3" label="已结束" />
        </el-select>
      </el-form-item>
      <el-form-item label="会议时间" label-width="120">
        <el-date-picker v-model="dateRange" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="datetimerange"
                        range-separator="-" start-placeholder="开始时间" end-placeholder="结束时间"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{$t('commons.button.search')}}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('commons.button.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete" :disabled="multiple">删除</el-button>
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
      <el-table-column label="会议室" align="center" prop="meetingRoom" :show-overflow-tooltip="true" />
      <el-table-column label="会议主题" align="center" prop="meetingTopic" :show-overflow-tooltip="true" />
      <el-table-column label="发起人" align="center" prop="applyUserName" />
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
      <el-table-column label="会议状态" align="center" prop="processStatus">
        <template slot-scope="{row: {processStatus}}">
          <span v-if="processStatus === 1">待签到</span>
          <span v-else-if="processStatus === 2">会议中</span>
          <span v-else-if="processStatus === 3">已结束</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 新增 -->
    <el-dialog title="预约会议" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="会议主题" prop="meetingTopic">
          <el-input v-model="form.meetingTopic" placeholder="" style="width: 100%" />
        </el-form-item>
        <el-form-item label="会议时间" prop="dateRange">
          <el-date-picker v-model="form.dateRange" value-format="yyyy-MM-dd HH:mm:ss" type="datetimerange" style="width: 100%"
                          range-separator="-" start-placeholder="开始时间" end-placeholder="结束时间"/>
        </el-form-item>
        <el-form-item label="会议室" prop="meetingRoom">
          <el-select v-model="form.meetingRoom" placeholder="选择会议室" style="width: 100%">
            <el-option key="109接待大厅" value="109接待大厅" label="109接待大厅" />
            <el-option key="403白鲸会议室" value="403白鲸会议室" label="403白鲸会议室" />
            <el-option key="404麦克斯韦" value="404麦克斯韦" label="404麦克斯韦" />
            <el-option key="405惠更斯" value="405惠更斯" label="405惠更斯" />
          </el-select>
        </el-form-item>
        <el-form-item label="发起人" prop="applyUserName">
          <el-input v-model="form.applyUserName" placeholder="" disabled="disabled"/>
        </el-form-item>
        <el-form-item label="参与人员" prop="members">
          <treeselect v-model="form.members" :options="userOptions"
                      :multiple="true" :disable-branch-nodes="true" placeholder="" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{$t('commons.button.confirm')}}</el-button>
        <el-button @click="cancel">{{$t('commons.button.cancel')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import store from "@/store";
import {checkPermit} from "@/utils/permission";
import {addMeeting, delMeeting, listMeeting} from "@/api/flow/meeting";
import {getDeptUserDiagram} from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "",
  dicts: [],
  components: {
    Treeselect
  },
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
        meetingRoom: undefined
      },
      // 表单参数
      form: {},
      // 参与人员
      userOptions: [],
    };
  },
  created() {
    this.getList();
    this.getUserOptions();
  },
  computed: {
    rules() {
      return {
        meetingTopic: [
          { required: true, message: '会议主题不能为空', trigger: "blur" }
        ],
        dateRange: [
          { required: true, message: '会议时间不能为空', trigger: "blur" }
        ],
        meetingRoom: [
          { required: true, message: '会议室不能为空', trigger: "blur" }
        ],
        members: [
          { required: true, message: '参与人员不能为空', trigger: "blur" }
        ],
      };
    }
  },
  methods: {
    checkPermit,
    /** 表单重置 */
    reset() {
      this.form = {};
      this.resetForm("form");
    },
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 获取部门人员树 */
    getUserOptions(){
      getDeptUserDiagram().then(resp => {
        this.userOptions = resp.data
      });
    },
    /** 重置 */
    resetQuery() {
      this.dateRange = [];
      this.queryParams = {
        page: 1,
        pageSize: 10,
        meetingRoom: undefined
      };
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 搜索 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 列表 */
    getList() {
      this.loading = true;
      listMeeting(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.list = response.data.list;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    /** 新增 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.form.applyUserName = store.getters.name;
    },
    /** 删除 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm("确认删除？").then(function() {
        return delMeeting(ids);
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
          this.form.beginTime = this.form.dateRange[0];
          this.form.endTime = this.form.dateRange[0];
          addMeeting(this.form).then(response => {
            this.$modal.msgSuccess("已预约会议");
            this.open = false;
            this.getList();

          });
        }
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
