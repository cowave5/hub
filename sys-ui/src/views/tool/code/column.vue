<template>
  <div class="app-container">
    <el-form size="small" :inline="true" label-width="auto">
      <el-form-item label="数据库类型">
        <el-input v-model="this.table.dbType" disabled="disabled"/>
      </el-form-item>
      <el-form-item label="数据库实例">
        <el-input v-model="this.table.dbCode" disabled="disabled"/>
      </el-form-item>
      <el-form-item label="表名">
        <el-input v-model="this.table.tableName" disabled="disabled"/>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-close" size="mini" @click="handleClose">关闭</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange" :header-cell-style="{'text-align':'center'}">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t(`label.index`)" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="字段名称" align="center" prop="columnName" width="180"/>
      <el-table-column label="字段类型" align="center" prop="columnTypename" width="140"/>
      <el-table-column label="字段注释" align="center" prop="columnComment" width="240" :show-overflow-tooltip="true"/>
      <el-table-column label="默认值" align="center" prop="columnDefault" width="240"/>
      <el-table-column label="非空" align="center">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.isNotnull" :active-value=1 :inactive-value=0 @change="switchNotnull(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="主键" align="center">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.isPrimary" :active-value=1 :inactive-value=0 @change="switchPrimary(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="自增" align="center">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.isIncrement" :active-value=1 :inactive-value=0 @change="switchIncrement(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="160"/>
      <el-table-column label="操作" align="center" class-name="small-padding" width="160">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改 -->
    <el-dialog :title="title" :visible.sync="open" width="760px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="auto">
        <el-row>
          <el-col :span="12">
            <el-form-item label="字段名称" prop="columnName">
              <el-input v-model="form.columnName" placeholder="请输入字段名称" style="width: 230px;"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="字段类型" prop="columnType">
              <el-select v-model="form.columnType" style="width: 230px">
                <el-option v-for="item in typeOptions" :key="item" :value="item" :label="item"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="默认值" prop="columnDefault">
              <el-input v-model="form.columnDefault" placeholder="请输入字段默认值" style="width: 230px;"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="字段注释" prop="columnComment">
              <el-input v-model="form.columnComment" placeholder="请输入字段注释" style="width: 230px;"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="字段长度" prop="columnPrecision">
              <el-input-number v-model="form.columnPrecision" controls-position="right" :min="1" style="width: 230px"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="字段精度" prop="columnScale">
              <el-input-number v-model="form.columnScale" controls-position="right" :min="1" style="width: 230px"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="是否主键">
              <el-radio-group v-model="form.isPrimary">
                <el-radio v-if="$i18n.locale==='zh'" v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
                <el-radio v-if="$i18n.locale==='en'" v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{dict.labelEn}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="字段排序" prop="sort">
              <el-input-number v-model="form.sort" controls-position="right" :min="1" style="width: 230px"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="是否非空">
              <el-radio-group v-model="form.isNotnull">
                <el-radio v-if="$i18n.locale==='zh'" v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
                <el-radio v-if="$i18n.locale==='en'" v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{dict.labelEn}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否自增">
              <el-radio-group v-model="form.isIncrement">
                <el-radio v-if="$i18n.locale==='zh'" v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
                <el-radio v-if="$i18n.locale==='en'" v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{dict.labelEn}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确定</el-button>
        <el-button @click="cancel">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addColumn,
  delColumn,
  infoColumn,
  listColumn, switchIncrement,
  switchNotnull, switchPrimary,
  typesColumn,
  updateColumn
} from "@/api/tool/column";
import {infoTable} from "@/api/tool/table";

export default {
  name: "Project",
  dicts: ['sys_yes_no'],
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
      // 库表信息
      table: {},
      // 字段类型选项
      typeOptions: [],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        columnName: [
          { required: true, message: "字段名称不能为空", trigger: "blur" }
        ],
        columnType: [
          { required: true, message: "字段类型不能为空", trigger: "blur" }
        ]
      },
    };
  },
  created() {
    const tableId = this.$route.params && this.$route.params.tableId;
    infoTable(tableId).then(rsp => {
      this.table = rsp.data;
      this.getList();
    });
  },
  methods: {
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        columnName: undefined,
        columnType: null,
        columnDefault: undefined,
        columnPrecision: undefined,
        columnScale: undefined,
        sort: 1,
        isPrimary: 0,
        isNotnull: 0,
        isIncrement: 0,
        columnComment: undefined,
      };
      this.resetForm("form");
    },
    /** 列表 */
    getList() {
      this.loading = true;
      listColumn(this.table.id).then(response => {
        this.list = response.data.list;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 取消 */
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 搜索 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 关闭 */
    handleClose() {
      const obj = { path: "/tool/code/table" };
      this.$tab.closeOpenPage(obj);
    },
    /** 非空 */
    switchNotnull(row){
      switchNotnull(row.id, row.isNotnull).then(response => {});
    },
    /** 主键 */
    switchPrimary(row){
      switchPrimary(row.id, row.isPrimary).then(response => {});
    },
    /** 自增 */
    switchIncrement(row){
      switchIncrement(row.id, row.isIncrement).then(response => {});
    },
    /** 新增 */
    handleAdd() {
      this.reset();
      typesColumn(this.table.dbType).then(response => {
        this.typeOptions = response.data;
        this.title = "新增字段";
        this.open = true;
      });
    },
    /** 修改 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      typesColumn(this.table.dbType).then(response => {
        this.typeOptions = response.data;
        infoColumn(id).then(rsp => {
          this.form = rsp.data;
          if(this.form.columnPrecision === null){
            this.form.columnPrecision = undefined;
          }
          if(this.form.columnScale === null){
            this.form.columnScale = undefined;
          }
          this.open = true;
          this.title = "修改字段";
        });
      });
    },
    /** 提交 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            updateColumn(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.form.tableId = this.table.id;
            addColumn(this.form).then(response => {
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
      const ids = row.id || this.ids
      const msg = row.id ? '确认删除字段[' + row.columnName + ']?' : '确认删除所选字段？';
      this.$modal.confirm(msg).then(function() {
        return delColumn(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
  }
};
</script>
