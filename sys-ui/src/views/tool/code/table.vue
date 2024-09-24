<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="数据库">
        <el-select v-model="queryParams.dbId" placeholder="选择数据库实例">
          <el-option v-for="item in dbOptions" :key="item.key" :value="item.key" :label="item.label"/>
        </el-select>
      </el-form-item>
      <el-form-item label="表名">
        <el-input v-model="queryParams.tableName" placeholder="请输入表名" clearable/>
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
      <el-table-column :label="$t(`label.index`)" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="数据库" align="center">
        <template slot-scope="scope" v-if="scope.row.dbName !== null">
          <div>{{ scope.row.dbName }}｜{{ scope.row.dbCode }}</div>
        </template>
      </el-table-column>
      <el-table-column label="表名" align="center" prop="tableName"/>
      <el-table-column label="表注释" align="center" prop="tableComment"/>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="160"/>
      <el-table-column label="操作" align="center" class-name="mini-padding" width="340">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          <el-button size="mini" type="text" icon="el-icon-s-unfold" @click="handleColumn(scope.row)">表字段</el-button>
          <el-button size="mini" type="text" icon="el-icon-view" @click="handlePreview(scope.row)">sql定义</el-button>
          <el-button size="mini" type="text" icon="el-icon-upload" @click="handleModel(scope.row)">生成模型</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 添加或修改 -->
    <el-dialog :title="title" :visible.sync="open" width="760px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="auto">
        <el-row>
          <el-col :span="12">
            <el-form-item label="表名" prop="tableName">
              <el-input v-model="form.tableName" placeholder="请输入表名称" style="width: 230px;"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="表注释" prop="tableComment">
              <el-input v-model="form.tableComment" placeholder="请输入表注释" style="width: 230px;"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="数据库" prop="dbId">
              <el-select v-model="form.dbId" style="width: 590px" placeholder="请选择数据库">
                <el-option v-for="item in dbOptions" :key="item.key" :value="item.key" :label="item.label"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确定</el-button>
        <el-button @click="cancel">取消</el-button>
      </div>
    </el-dialog>

    <!-- 生成模型 -->
    <el-dialog title="生成模型" :visible.sync="modelOpen" width="760px" append-to-body>
      <el-form ref="modelForm" :model="modelForm" :rules="modelRules" label-width="auto">
        <el-row>
          <el-col :span="12">
            <el-form-item label="Class类名" prop="className">
              <el-input v-model="modelForm.className" placeholder="请输入类名称" style="width: 230px;"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Class注释" prop="classComment">
              <el-input v-model="modelForm.classComment" placeholder="请输入类注释" style="width: 230px;"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="Api路径" prop="apiContext">
              <el-input v-model="modelForm.apiContext" placeholder="一般格式：/api/v1/{modelName}" style="width: 230px;"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="接口权限符" prop="authPrefix">
              <el-input v-model="modelForm.authPrefix" placeholder="示例：sys:code:" style="width: 230px;"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="继承Access">
              <el-radio-group v-model="modelForm.isAccess">
                <el-radio v-if="$i18n.locale==='zh'" v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
                <el-radio v-if="$i18n.locale==='en'" v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{dict.labelEn}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="支持Excel导出">
              <el-radio-group v-model="modelForm.isExcel">
                <el-radio v-if="$i18n.locale==='zh'" v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
                <el-radio v-if="$i18n.locale==='en'" v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{dict.labelEn}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="关联数据库表" prop="tableId">
              <el-select v-model="modelForm.tableId" style="width: 590px" placeholder="请选择数据表" disabled="disabled">
                <el-option v-for="item in tableOptions" :key="item.key" :value="item.key" :label="item.label"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="modelForm.remark" type="textarea" placeholder="..." style="width: 590px;"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitModelForm">确定</el-button>
        <el-button @click="cancelModel">取消</el-button>
      </div>
    </el-dialog>

    <!-- DDL预览 -->
    <el-dialog title="DDL预览" :visible.sync="preview.open" width="60%" top="5vh" append-to-body class="scrollbar">
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
import {addTable, delTable, infoTable, listTable, preview, updateTable} from "@/api/tool/table";
import {optionsDb} from "@/api/tool/db";
import {generate} from "@/api/tool/model";
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
      modelOpen: false,
      // 数据库选择
      dbOptions: [],
      tableOptions: [],
      // 数据库类型
      dbTypeOptions: [
        {key: "oscar", label: "oscar"},
        {key: "mysql", label: "mysql"},
        {key: "postgresql", label: "postgresql"},
      ],
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 30,
        dbId: undefined,
        tableName: undefined
      },
      // 表单参数
      form: {},
      modelForm: {},
      // 表单校验
      rules: {
        dbId: [
          { required: true, message: "数据库不能为空", trigger: "blur" }
        ],
        tableName: [
          { required: true, message: "表名不能为空", trigger: "blur" }
        ],
      },
      modelRules: {
        appId: [
          { required: true, message: "应用不能为空", trigger: "blur" }
        ],
        modelName: [
          { required: true, message: "模型名称不能为空", trigger: "blur" }
        ],
        className: [
          { required: true, message: "Class类名不能为空", trigger: "blur" }
        ]
      },
      // 预览参数
      preview: {
        open: false,
        data: {},
        activeName: "oscar.sql"
      },
    };
  },
  created() {
    optionsDb().then(response2 => {
      this.dbOptions = response2.data;
    });
    this.getList();
  },
  methods: {
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        tableName: undefined,
        tableComment: undefined,
      };
      this.resetForm("form");
    },
    /** 列表 */
    getList() {
      this.loading = true;
      listTable(this.queryParams).then(response => {
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
      this.queryParams = {
          page: 1,
          pageSize: 30,
          dbId: undefined,
          tableName: undefined
      };
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
    handleColumn(row){
      this.$router.push({ path: '/tool/table-column/table/' + row.id })
    },
    /** 新增 */
    handleAdd() {
      this.reset();
      optionsDb().then(response2 => {
        this.dbOptions = response2.data;
      });
      this.title = "新增表";
      this.open = true;
    },
    /** 修改 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      optionsDb().then(response2 => {
        this.dbOptions = response2.data;
      });
      infoTable(id).then(rsp => {
        this.form = rsp.data;
        this.open = true;
        this.title = "修改表";
      });
    },
    /** 提交 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            updateTable(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTable(this.form).then(response => {
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
      const msg = row.id ? '确认删除表[' + row.tableName + ']?' : '确认删除所选表？';
      this.$modal.confirm(msg).then(function() {
        return delTable(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 生成模型 */
    handleModel(row){
      let modelName = row.tableName.replace(/_([a-z])/g, function (match, letter) {
        return letter.toUpperCase();
      });
      let className = modelName.charAt(0).toUpperCase() + modelName.slice(1);
      this.modelForm = {
        id: undefined,
        modelName: row.tableComment,
        authPrefix: undefined,
        className: className,
        classComment: row.tableComment,
        apiContext: '/api/v1/' + modelName,
        appId: null,
        tableId: row.id,
        isAccess: 0,
        isExcel: 0,
        remark: undefined,
      };
      this.resetForm("modelForm");
      this.tableOptions = [{key: row.id, label: row.tableName}]
      this.modelOpen = true;
    },
    /** 确认生成模型 */
    submitModelForm(){
      this.$refs["modelForm"].validate(valid => {
        if (valid) {
          generate(this.modelForm).then(response => {
            this.modelOpen = false;
            this.$modal.msgSuccess("生成成功");
          });
        }
      });
    },
    /** 取消生成模型 */
    cancelModel(){
      this.modelOpen = false;
    },
    /** DDL预览 */
    handlePreview(row){
      preview(row.id).then(response => {
        this.preview.data = response.data;
        this.preview.open = true;
        this.preview.activeName = "oscar.sql";
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
