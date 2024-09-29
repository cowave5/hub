<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="数据库类型" prop="projectId">
        <el-select v-model="queryParams.dbType" placeholder="请选择项目">
          <el-option v-for="item in dbOptions" :key="item.key" :value="item.key" :label="item.label"/>
        </el-select>
      </el-form-item>
      <el-form-item label="数据库实例" prop="dbCode">
        <el-input v-model="queryParams.dbCode" placeholder="请输入数据库实例" clearable/>
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
      <el-table-column label="数据库类型" align="center" prop="dbType"/>
      <el-table-column label="数据库名称" align="center" prop="dbName" :show-overflow-tooltip="true"/>
      <el-table-column label="数据库实例" align="center" prop="dbCode" :show-overflow-tooltip="true"/>
      <el-table-column label="Jdbc连接" align="center" prop="dbUrl" width="320" :show-overflow-tooltip="true"/>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180"/>
      <el-table-column label="操作" align="center" class-name="small-padding" width="380">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          <el-button size="mini" type="text" icon="el-icon-view" @click="handlePreview(scope.row)">sql预览</el-button>
          <el-button size="mini" type="text" icon="el-icon-download" @click="handleTemplate(scope.row)">sql模板</el-button>
          <el-button size="mini" type="text" icon="el-icon-refresh" @click="handleSynTable(scope.row)">从数据库同步</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 添加或修改 -->
    <el-dialog :title="title" :visible.sync="open" width="760px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="auto">
        <el-row>
          <el-col :span="12">
            <el-form-item label="数据库名称" prop="dbName">
              <el-input v-model="form.dbName" placeholder="请输入数据库名称" style="width: 230px;"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据库类型" prop="dbType">
              <el-select v-model="form.dbType" placeholder="选择数据库类型" style="width: 230px;" :disabled="this.form.id !== undefined">
                <el-option v-for="item in dbOptions" :key="item.key" :value="item.key" :label="item.label"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="数据库实例" prop="dbCode">
              <el-input v-model="form.dbCode" placeholder="请输入数据库实例" style="width: 230px;"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Jdbc连接" prop="dbUrl">
              <el-input v-model="form.dbUrl" placeholder="数据库url" style="width: 230px;"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户名" prop="dbUser">
              <el-input v-model="form.dbUser" placeholder="数据库用户" style="width: 230px;"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码" prop="dbPasswd">
              <el-input v-model="form.dbPasswd" placeholder="数据库密码" style="width: 230px;"/>
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

    <!-- 从数据库同步表 -->
    <el-dialog title="从数据库同步表" :visible.sync="synOpen" width="760px" append-to-body>
      <div style="position: absolute; left: 20px; top: 50px; color: #511fab; font-size: small;">
        ⚠️ 从数据库同步表结构将会覆盖当前已有数据，如果当前库表数据已在模型中进行了绑定则不可以同步
      </div>
      <el-form ref="synForm" :model="synForm" :rules="synRules" label-width="auto">
        <el-row>
          <el-col :span="12">
            <el-form-item label="数据库名称" prop="dbName">
              <el-input v-model="synForm.dbName" placeholder="请输入数据库名称" style="width: 230px;" disabled="disabled"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据库类型" prop="dbType">
              <el-select v-model="synForm.dbType" placeholder="选择数据库类型" style="width: 230px;" disabled="disabled">
                <el-option v-for="item in dbOptions" :key="item.key" :value="item.key" :label="item.label"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="数据库实例" prop="dbCode" disabled="disabled">
              <el-input v-model="synForm.dbCode" placeholder="请输入数据库实例" style="width: 230px;" disabled="disabled"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据库连接" prop="dbUrl">
              <el-input v-model="synForm.dbUrl" placeholder="数据库url" style="width: 230px;"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="数据库用户" prop="dbUser">
              <el-input v-model="synForm.dbUser" placeholder="数据库用户" style="width: 230px;"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据库密码" prop="dbPasswd">
              <el-input v-model="synForm.dbPasswd" placeholder="数据库密码" style="width: 230px;"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="synForm.remark" type="textarea" placeholder="..." style="width: 590px;" disabled="disabled"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitSynForm">确定</el-button>
        <el-button @click="cancelSyn">取消</el-button>
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
import {addDb, delDb, infoDb, listDb, preview, synTable, updateDb} from "@/api/meter/db";
import hljs from "highlight.js/lib/highlight";
import "highlight.js/styles/github-gist.css";
hljs.registerLanguage("java", require("highlight.js/lib/languages/java"));
hljs.registerLanguage("xml", require("highlight.js/lib/languages/xml"));
hljs.registerLanguage("html", require("highlight.js/lib/languages/xml"));
hljs.registerLanguage("vue", require("highlight.js/lib/languages/xml"));
hljs.registerLanguage("javascript", require("highlight.js/lib/languages/javascript"));
hljs.registerLanguage("sql", require("highlight.js/lib/languages/sql"));

export default {
  name: "DB",
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
      synOpen: false,
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        dbType: undefined,
        dbCode: undefined
      },
      // 数据库类型
      dbOptions: [
        {key: "oscar", label: "oscar"},
        {key: "mysql", label: "mysql"},
        {key: "postgresql", label: "postgresql"},
      ],
      // 表单参数
      form: {},
      synForm:{},
      // 表单校验
      rules: {
        dbName: [
          { required: true, message: "数据库名称不能为空", trigger: "blur" }
        ],
        dbCode: [
          { required: true, message: "数据库实例不能为空", trigger: "blur" }
        ],
        dbType: [
          { required: true, message: "数据库类型不能为空", trigger: "blur" }
        ]
      },
      synRules: {
        dbUrl: [
          { required: true, message: "数据库连接不能为空", trigger: "blur" }
        ],
        dbUser: [
          { required: true, message: "数据库用户不能为空", trigger: "blur" }
        ],
        dbPasswd: [
          { required: true, message: "数据库密码不能为空", trigger: "blur" }
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
    this.getList();
  },
  methods: {
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        dbName: undefined,
        dbCode: undefined,
        dbType: null,
        dbUrl: undefined,
        dbUser: undefined,
        dbPasswd: undefined,
        remark: undefined
      };
      this.resetForm("form");
    },
    /** 列表 */
    getList() {
      this.loading = true;
      listDb(this.queryParams).then(response => {
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
    /** 取消同步表 */
    cancelSyn() {
      this.synOpen = false;
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
          pageSize: 10,
          dbType: undefined,
          dbCode: undefined
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
    /** 数据库成员 */
    handleUsers(row) {

    },
    /** 新增 */
    handleAdd() {
      this.reset();
      this.title = "新增数据库";
      this.open = true;
    },
    /** 修改 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      infoDb(id).then(rsp => {
        this.form = rsp.data;
        this.open = true;
        this.title = "修改数据库";
      });
    },
    /** 提交 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            updateDb(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDb(this.form).then(response => {
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
      const msg = row.id ? '确认删除数据库[' + row.dbName + ']?' : '确认删除所选数据库？';
      this.$modal.confirm(msg).then(function () {
        return delDb(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 从数据库同步表 */
    handleSynTable(row) {
      this.resetForm("synForm");
      infoDb(row.id).then(rsp => {
        this.synForm = rsp.data;
        this.synOpen = true;
      });
    },
    /** 从数据库同步表 */
    submitSynForm() {
      this.$refs["synForm"].validate(valid => {
        if (valid) {
          synTable(this.synForm).then(response => {
            this.$modal.msgSuccess("同步完成");
            this.synOpen = false;
          });
        }
      });
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
    /** DDL模版 */
    handleTemplate(row) {
      this.$download.zip("/meter/api/v1/db/template/" + row.id, "sql");
    },
  }
};
</script>
