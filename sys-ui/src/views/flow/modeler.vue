<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="auto">
      <el-form-item label="模型key" prop="key">
        <el-input v-model="queryParams.key" placeholder="" clearable />
      </el-form-item>
      <el-form-item label="模型名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{$t('commons.button.search')}}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('commons.button.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新建流程</el-button>
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
      <el-table-column label="模型id" align="center" prop="id" width="320" :show-overflow-tooltip="true" />
      <el-table-column label="模型key" align="center" prop="key" :show-overflow-tooltip="true" />
      <el-table-column label="模型名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="版本" align="center" prop="version" :show-overflow-tooltip="true" />
      <el-table-column label="模型分类" align="center" prop="category" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="lastUpdateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.lastUpdateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="text" icon="el-icon-s-promotion"
                     :disabled="scope.row.deploymentId !== null" @click="handleDeploy(scope.row)">发布</el-button>
          <el-button size="mini" type="text" icon="el-icon-download" @click="handleDownload(scope.row)">导出</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 添加或修改 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="模型标识" prop="key">
          <el-input v-model="form.key" placeholder="" />
        </el-form-item>
        <el-form-item label="模型名称" prop="name">
          <el-input v-model="form.name" placeholder="" />
        </el-form-item>
        <el-form-item label="模型分类" prop="category">
          <el-input v-model="form.category" placeholder="" />
        </el-form-item>
        <el-form-item label="模型描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm"
                   >{{$t('commons.button.confirm')}}</el-button>
        <el-button @click="cancel">{{$t('commons.button.cancel')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {checkPermit} from "@/utils/permission";
import {addModel, deleteModel, deployModel, listModel} from "@/api/flow/manage/modeler";

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
        name: undefined,
        key: undefined,
        configType: undefined
      },
      // 表单参数
      form: {},
    };
  },
  created() {
    this.getList();
  },
  computed: {
    rules() {
      return {
        key: [
          { required: true, message: "模型标识不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "模型名称不能为空", trigger: "blur" }
        ]
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
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 列表 */
    getList() {
      this.loading = true;
      listModel(this.queryParams).then(response => {
          this.list = response.data.list;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    /** 编辑 */
    handleEdit: function(row) {
      const id = row.id;
      this.$router.push("/flow/manage-modeler/designer/" + id);
    },
    /** 发布 */
    handleDeploy(row) {
      const id = row.id;
      deployModel(id).then(response => {
        this.getList();
        this.$modal.msgSuccess("发布成功");
      });
    },
    /** 导出 */
    handleDownload(row) {
      const id = row.id;
      this.download('/admin/api/v1/flow/model/export/' + id, {
        ...this.queryParams
      }, `flow_${new Date().getTime()}.xml`)
    },
    /** 删除 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      const msg = row.id ? "确认删除流程[" + row.name + "]" : "确认删除所选流程";
      this.$modal.confirm(msg).then(function() {
        return deleteModel(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('commons.msg.success.delete'));
      }).catch(() => {});
    },
    /** 新增 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新建流程";
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
          addModel(this.form).then(response => {
            this.$modal.msgSuccess(this.$t('commons.msg.success.create'));
            this.open = false;
            this.getList();
          });
        }
      });
    }
  }
};
</script>
