<template>
  <div class="app-container">
    <el-form size="small" :inline="true" v-show="showSearch" label-width="auto">
      <el-form-item label="Class类名">
        <el-input v-model="model.className" disabled="disabled"/>
      </el-form-item>
      <el-form-item label="模型注释">
        <el-input v-model="model.classComment" disabled="disabled"/>
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
      <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="字段名称" align="center" prop="fieldName" width="140" :show-overflow-tooltip="true"/>
      <el-table-column label="字段类型" align="center" prop="fieldTypeName" width="120" :show-overflow-tooltip="true"/>
      <el-table-column label="字段注释" align="center" prop="fieldComment" width="200" :show-overflow-tooltip="true"/>
      <el-table-column label="非空字段" align="center">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.isNotnull" :active-value=1 :inactive-value=0 @change="switchIsNotnull(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="集合字段" align="center">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.isCollect" :active-value=1 :inactive-value=0 @change="switchIsCollect(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="Excel导出" align="center">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.isExcel" :active-value=1 :inactive-value=0 @change="switchIsExcel(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="表字段" align="center" prop="columnName" width="120" :show-overflow-tooltip="true"/>
      <el-table-column label="where条件" align="center">
        <template slot-scope="scope" v-if="scope.row.columnName !== null">
          <el-switch v-model="scope.row.isWhere" :active-value=1 :inactive-value=0 @change="switchIsWhere(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="列表" align="center">
        <template slot-scope="scope" v-if="scope.row.columnName !== null">
          <el-switch v-model="scope.row.isList" :active-value=1 :inactive-value=0 @change="switchIsList(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="详情" align="center">
        <template slot-scope="scope" v-if="scope.row.columnName !== null">
          <el-switch v-model="scope.row.isInfo" :active-value=1 :inactive-value=0 @change="switchIsInfo(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="新增" align="center">
        <template slot-scope="scope" v-if="scope.row.columnName !== null">
          <el-switch v-model="scope.row.isInsert" :active-value=1 :inactive-value=0 @change="switchIsInsert(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="修改" align="center">
        <template slot-scope="scope" v-if="scope.row.columnName !== null">
          <el-switch v-model="scope.row.isEdit" :active-value=1 :inactive-value=0 @change="switchIsEdit(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding" width="120">
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
            <el-form-item label="字段名称" prop="fieldName">
              <el-input v-model="form.fieldName" placeholder="请输入字段名称" style="width: 180px;"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="字段类型" prop="fieldType">
              <el-select v-model="form.fieldType" style="width: 230px">
                <el-option v-for="item in typeOptions" :key="item.key" :value="item.key" :label="item.label"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="字段注释" prop="fieldComment">
              <el-input v-model="form.fieldComment" placeholder="请输入字段注释" style="width: 230px;"/>
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
            <el-form-item label="非空字段">
              <el-radio-group v-model="form.isNotnull">
                <el-radio v-for="dict in dict.type.yes_no" :key="dict.value" :label="dict.value">{{$t(dict.name)}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="集合字段">
              <el-radio-group v-model="form.isCollect">
                <el-radio v-for="dict in dict.type.yes_no" :key="dict.value" :label="dict.value">{{$t(dict.name)}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="Excel字段">
              <el-radio-group v-model="form.isExcel">
                <el-radio v-for="dict in dict.type.yes_no" :key="dict.value" :label="dict.value">{{$t(dict.name)}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="界面样式">
              <el-select v-model="form.htmlType" style="width: 230px">
                <el-option v-for="item in htmlOptions" :key="item.value" :value="item.value" :label="item.label"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="Excel列名" prop="excelName">
              <el-input v-model="form.excelName" placeholder="请输入Excel列名" style="width: 230px;"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Excel宽度" prop="excelWidth">
              <el-input-number v-model="form.excelWidth" controls-position="right" :min="1" style="width: 230px"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="Excel转换器" prop="excelConverter">
              <el-select v-model="form.excelConverter" style="width: 590px">
                <el-option v-for="item in excelOptions" :key="item.key" :value="item.key" :label="item.label"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="关联表字段">
              <el-select v-model="form.columnId" style="width: 590px" placeholder="请选择数据表">
                <el-option v-for="item in columnOptions" :key="item.key" :value="item.key" :label="item.label"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="where条件">
              <el-radio-group v-model="form.isWhere">
                <el-radio v-for="dict in dict.type.yes_no" :key="dict.value" :label="dict.value">{{$t(dict.name)}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="where类型" prop="whereType">
              <el-select v-model="form.whereType" style="width: 230px">
                <el-option v-for="item in whereOptions" :key="item.value" :value="item.value" :label="item.label"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="是否新增字段">
              <el-radio-group v-model="form.isInsert">
                <el-radio v-for="dict in dict.type.yes_no" :key="dict.value" :label="dict.value">{{$t(dict.name)}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否更新字段">
              <el-radio-group v-model="form.isEdit">
                <el-radio v-for="dict in dict.type.yes_no" :key="dict.value" :label="dict.value">{{$t(dict.name)}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="是否列表字段">
              <el-radio-group v-model="form.isList">
                <el-radio v-for="dict in dict.type.yes_no" :key="dict.value" :label="dict.value">{{$t(dict.name)}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否详情字段">
              <el-radio-group v-model="form.isInfo">
                <el-radio v-for="dict in dict.type.yes_no" :key="dict.value" :label="dict.value">{{$t(dict.name)}}</el-radio>
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
import {infoModel} from "@/api/meter/model";
import {
  addField,
  delField, excelConverter,
  infoField,
  listField,
  switchCollect, switchEdit,
  switchExcel, switchInfo, switchInsert, switchList, switchNotnull, switchWhere,
  typesField,
  updateField
} from "@/api/meter/field";
import {optionsColumn} from "@/api/meter/column";

export default {
  name: "Project",
  dicts: ['yes_no'],
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
      // 表单参数
      model: {},
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        fieldName: [
          { required: true, message: "字段名称不能为空", trigger: "blur" }
        ],
        fieldType: [
          { required: true, message: "字段类型不能为空", trigger: "blur" }
        ]
      },
      htmlOptions: [
        {value: "input", label: "input"},
        {value: "textarea", label: "textarea"},
        {value: "select", label: "select"},
        {value: "radio", label: "radio"},
        {value: "checkbox", label: "checkbox"},
        {value: "datetime", label: "datetime"},
        {value: "imageUpload", label: "imageUpload"},
        {value: "fileUpload", label: "fileUpload"},
        {value: "editor", label: "editor"},
      ],
      whereOptions: [
        {value: "==", label: "等于"},
        {value: "!=", label: "不等于"},
        {value: ">", label: "大于"},
        {value: ">=", label: "大于等于"},
        {value: "<", label: "小于"},
        {value: "<=", label: "小于等于"},
        {value: "like", label: "模糊匹配"},
      ],
      excelOptions: [],
      columnOptions: [],
      typeOptions: [],
    };
  },
  created() {
    const modelId = this.$route.params && this.$route.params.modelId;
    infoModel(modelId).then(rsp => {
      this.model = rsp.data;
      this.getList();
    });
    excelConverter().then(rsp => {
      this.excelOptions = rsp.data;
    });
  },
  methods: {
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        fieldName: undefined,
        fieldType: undefined,
        fieldComment: undefined,
        isCollect: 0,
        isNotnull: 0,
        htmlType: undefined,
        sort: 1,
        isExcel: 0,
        excelWidth: 20,
        excelName: undefined,
        excelConverter: undefined,
        columnId: undefined,
        isWhere: 0,
        whereType: undefined,
        isList: 1,
        isInfo: 1,
        isInsert: 1,
        isEdit: 1,
      };
      this.resetForm("form");
    },
    /** 列表 */
    getList() {
      this.loading = true;
      listField(this.model.id).then(response => {
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
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 关闭 */
    handleClose() {
      const obj = { path: "/meter/template/model" };
      this.$tab.closeOpenPage(obj);
    },
    /** 是否非空字段切换 */
    switchIsNotnull(row){
      switchNotnull(row.id, row.isNotnull).then(rsp => {});
    },
    /** 是否集合字段切换 */
    switchIsCollect(row){
      switchCollect(row.id, row.isCollect).then(rsp => {});
    },
    /** 是否Excel字段切换 */
    switchIsExcel(row){
      switchExcel(row.id, row.isExcel).then(rsp => {});
    },
    /** 是否where条件切换 */
    switchIsWhere(row){
      switchWhere(row.id, row.isWhere).then(rsp => {});
    },
    /** 是否列表字段切换 */
    switchIsList(row){
      switchList(row.id, row.isList).then(rsp => {});
    },
    /** 是否详情字段切换 */
    switchIsInfo(row){
      switchInfo(row.id, row.isInfo).then(rsp => {});
    },
    /** 是否新增字段切换 */
    switchIsInsert(row){
      switchInsert(row.id, row.isInsert).then(rsp => {});
    },
    /** 是否修改字段切换 */
    switchIsEdit(row){
      switchEdit(row.id, row.isEdit).then(rsp => {});
    },
    /** 新增 */
    handleAdd() {
      this.reset();
      typesField().then(rsp => {
        this.typeOptions = rsp.data;
      });
      optionsColumn(this.model.id).then(rsp => {
        this.columnOptions = rsp.data;
        this.columnOptions.push({"key": null, "label": "空"});
      });
      this.title = "新增字段";
      this.open = true;
    },
    /** 修改 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      optionsColumn(this.model.id).then(rsp => {
        this.columnOptions = rsp.data;
        this.columnOptions.push({"key": null, "label": "空"});
      });
      typesField().then(rsp => {
        this.typeOptions = rsp.data;
      });
      infoField(id).then(rsp => {
        this.form = rsp.data;
        this.open = true;
        this.title = "修改字段";
      });
    },
    /** 提交 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            updateField(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            this.form.modelId = this.model.id;
            addField(this.form).then(response => {
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
      const msg = row.id ? '确认删除字段[' + row.fieldName + ']?' : '确认删除所选字段？';
      this.$modal.confirm(msg).then(function() {
        return delField(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
  }
};
</script>
