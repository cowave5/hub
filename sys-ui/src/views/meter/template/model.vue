<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="模型名称" prop="modelName">
        <el-input v-model="queryParams.modelName" placeholder="请输入模型名称" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
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
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="Class类名" align="center" prop="className" :show-overflow-tooltip="true"/>
      <el-table-column label="Class注释" align="center" prop="classComment" :show-overflow-tooltip="true"/>
      <el-table-column label="Api路径" align="center" prop="apiContext"/>
      <el-table-column label="继承Access" align="center" width="100">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.isAccess" :active-value=1 :inactive-value=0 @change="switchIsAccess(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="支持Excel导出" align="center">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.isExcel" :active-value=1 :inactive-value=0 @change="switchIsExcel(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="记录操作日志" align="center">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.isLog" :active-value=1 :inactive-value=0 @change="switchIsLog(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column label="数据库表"   align="center" prop="tableName"/>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180"/>
      <el-table-column label="操作" align="center" class-name="small-padding" width="320">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          <el-button size="mini" type="text" icon="el-icon-s-unfold" @click="handleField(scope.row)">属性列表</el-button>
          <el-button size="mini" type="text" icon="el-icon-view" @click="handlePreview(scope.row)">代码预览</el-button>
          <el-button size="mini" type="text" icon="el-icon-download" @click="handleTemplate(scope.row)">代码模板</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 添加或修改 -->
    <el-dialog :title="title" :visible.sync="open" width="760px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="auto">
        <el-row>
          <el-col :span="12">
            <el-form-item label="Class类名" prop="className">
              <el-input v-model="form.className" placeholder="请输入类名称" style="width: 230px;"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Class注释" prop="classComment">
              <el-input v-model="form.classComment" placeholder="请输入类注释" style="width: 230px;"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="Api路径" prop="apiContext">
              <el-input v-model="form.apiContext" placeholder="示例：/api/v1/{model}" style="width: 230px;"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Api权限前缀" prop="authPrefix">
              <el-input v-model="form.authPrefix" placeholder="示例：{app}:{model}:" style="width: 230px;"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="继承Access">
              <el-radio-group v-model="form.isAccess">
                <el-radio v-for="dict in dict.type.yes_no" :key="dict.value" :label="dict.value">{{$t(dict.name)}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="支持Excel导出">
              <el-radio-group v-model="form.isExcel">
                <el-radio v-for="dict in dict.type.yes_no" :key="dict.value" :label="dict.value">{{$t(dict.name)}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="记录操作日志">
              <el-radio-group v-model="form.isLog">
                <el-radio v-for="dict in dict.type.yes_no" :key="dict.value" :label="dict.value">{{$t(dict.name)}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="操作日志类型" prop="logType">
              <el-select v-model="form.logType" placeholder="选择日志类型" style="width: 230px;">
                <el-option v-for="item in typeOptions" :key="item.key" :value="item.key" :label="item.label"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="..." style="width: 590px;"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确定</el-button>
        <el-button @click="cancel">取消</el-button>
      </div>
    </el-dialog>

    <!-- 代码预览 -->
    <el-dialog title="代码预览" :visible.sync="preview.open" width="60%" top="5vh" append-to-body class="scrollbar">
      <el-tabs v-model="preview.activeName">
        <el-tab-pane v-for="(value, key) in preview.data" :key="key"
                     :label="key.substring(key.lastIndexOf('/')+1,key.indexOf('.vm'))"
                     :name="key.substring(key.lastIndexOf('/')+1,key.indexOf('.vm'))">
          <el-link :underline="false" icon="el-icon-document-copy"
                   v-clipboard:copy="value" v-clipboard:success="clipboardSuccess" style="float:right">复制</el-link>
          <pre><code class="hljs" v-html="highlightedCode(value, key)"></code></pre>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>
import {
  addModel,
  delModel,
  infoModel,
  listModel,
  preview,
  switchAccess,
  switchExcel,
  switchLog,
  updateModel
} from "@/api/meter/model";
import hljs from "highlight.js/lib/highlight";
import "highlight.js/styles/github-gist.css";
hljs.registerLanguage("java", require("highlight.js/lib/languages/java"));
hljs.registerLanguage("xml", require("highlight.js/lib/languages/xml"));
hljs.registerLanguage("html", require("highlight.js/lib/languages/xml"));
hljs.registerLanguage("vue", require("highlight.js/lib/languages/xml"));
hljs.registerLanguage("javascript", require("highlight.js/lib/languages/javascript"));
hljs.registerLanguage("sql", require("highlight.js/lib/languages/sql"));

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
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 30,
        appId: undefined,
        modelName: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        appId: [
          { required: true, message: "应用不能为空", trigger: "blur" }
        ],
        modelName: [
          { required: true, message: "模型名称不能为空", trigger: "blur" }
        ],
        className: [
          { required: true, message: "Class类名不能为空", trigger: "blur" }
        ],
        apiContext: [
          { required: true, message: "Api路径不能为空", trigger: "blur" }
        ],
      },
      // 预览参数
      preview: {
        open: false,
        data: {},
        activeName: "entity.java"
      },
      typeOptions: [],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        modelName: undefined,
        authPrefix: undefined,
        className: undefined,
        classComment: undefined,
        commentAuthor: undefined,
        apiContext: undefined,
        appId: null,
        tableId: null,
        isAccess: 0,
        isExcel: 0,
        isLog: 0,
        logType: undefined,
        remark: undefined,
      };
      this.resetForm("form");
    },
    /** 列表 */
    getList() {
      this.loading = true;
      listModel(this.queryParams).then(response => {
        this.list = response.data.list;
        this.total = response.data.total;
        this.loading = false;
      });
      this.getOptions();
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
    /** 字段列表 */
    handleField(row){
      this.$router.push({ path: '/meter/template-field/model/' + row.id })
    },
    /** 代码模版 */
    handleTemplate(row){
      this.$download.zip("/meter/api/v1/model/template/" + row.id, "code");
    },
    /** 是否导出Excel切换 */
    switchIsExcel(row){
      switchExcel(row.id, row.isExcel).then(rsp => {});
    },
    /** 是否继承Access切换 */
    switchIsAccess(row){
      switchAccess(row.id, row.isAccess).then(rsp => {});
    },
    /** 是否记录日志切换 */
    switchIsLog(row){
      switchLog(row.id, row.isLog).then(rsp => {});
    },
    /** 新增 */
    handleAdd() {
      this.reset();
      this.title = "新增模型";
      this.open = true;
    },
    /** 修改 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      infoModel(id).then(rsp => {
        this.form = rsp.data;
        this.open = true;
        this.title = "修改模型";
      });
    },
    /** 提交 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            updateModel(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addModel(this.form).then(response => {
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
      const msg = row.id ? '确认删除模型[' + row.modelName + ']?' : '确认删除所选模型？';
      this.$modal.confirm(msg).then(function() {
        return delModel(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 代码预览 */
    handlePreview(row){
      preview(row.id).then(response => {
        this.preview.data = response.data;
        this.preview.open = true;
        this.preview.activeName = "entity.java";
      });
    },
    /** 复制代码成功 */
    clipboardSuccess() {
      this.$modal.msgSuccess("复制成功");
    },
    /** 高亮显示 */
    highlightedCode(code, key) {
      const vmName = key.substring(key.lastIndexOf("/") + 1, key.indexOf(".vm"));
      var language = vmName.substring(vmName.indexOf(".") + 1, vmName.length);
      const result = hljs.highlight(language, code || "", true);
      return result.value || '&nbsp;';
    },
  }
};
</script>
