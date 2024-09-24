<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" >
      <el-form-item label="项目编码" prop="projectCode">
        <el-input v-model="queryParams.projectCode" placeholder="请输入项目编码" clearable/>
      </el-form-item>
      <el-form-item label="应用名称" prop="appName">
        <el-input v-model="queryParams.appName" placeholder="请输入应用名称" clearable/>
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
      <el-table-column label="项目编码" align="center" prop="projectCode" width="160"/>
      <el-table-column label="应用编码" align="center" prop="appCode" width="160"/>
      <el-table-column label="应用名称" align="center" prop="appName" :show-overflow-tooltip="true"/>
      <el-table-column label="应用版本" align="center" prop="appVersion"/>
      <el-table-column label="Http端口" align="center" prop="httpPort"/>
      <el-table-column label="Http路径" align="center" prop="httpContext"/>
      <el-table-column label="数据库" align="center" width="160" :show-overflow-tooltip="true">
        <template slot-scope="scope" v-if="scope.row.dbName !== null">
          <div>{{ scope.row.dbName }}｜{{ scope.row.dbCode }}</div>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="160"/>
      <el-table-column label="操作" align="center" class-name="small-padding" width="260">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          <el-button size="mini" type="text" icon="el-icon-download" @click="handleTemplate(scope.row)">工程模版</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 添加或修改 -->
    <el-dialog :title="title" :visible.sync="open" width="760px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="auto">
        <el-row>
          <el-col :span="12">
            <el-form-item label="项目编码" prop="projectCode">
              <el-input v-model="form.projectCode" placeholder="英文简写" style="width: 230px;"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="应用名称" prop="appName">
              <el-input v-model="form.appName" placeholder="请输入应用名称" style="width: 230px;"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="应用编码" prop="appCode">
              <el-input v-model="form.appCode" placeholder="英文简写" style="width: 230px;"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="应用版本" prop="appVersion">
              <el-input v-model="form.appVersion" placeholder="pom.xml version" style="width: 230px;"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="Http端口" prop="httpPort">
              <el-input v-model="form.httpPort" placeholder="请输入Http端口" style="width: 230px;"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Http路径" prop="httpContext">
              <el-input v-model="form.httpContext" placeholder="请输入Http路径" style="width: 230px;"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="数据库" prop="dbId">
              <el-select v-model="form.dbId" style="width: 230px" placeholder="请选择数据库">
                <el-option v-for="item in dbOptions" :key="item.key" :value="item.key" :label="item.label"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="接口鉴权">
              <el-radio-group v-model="form.isSecurity">
                <el-radio v-if="$i18n.locale==='zh'" v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
                <el-radio v-if="$i18n.locale==='en'" v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{dict.labelEn}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="数据模型" prop="dbId">
            <el-select v-model="form.modelList" multiple style="width: 590px;" placeholder="请选择数据库">
              <el-option v-for="item in modelOptions" :key="item.key" :value="item.key" :label="item.label"/>
            </el-select>
          </el-form-item>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="appDesc">
              <el-input v-model="form.appDesc" type="textarea" placeholder="..." style="width: 590px;"/>
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
import {addApplication, delApplication, infoApplication, listApplication, updateApplication} from "@/api/tool/application";
import {optionsDb} from "@/api/tool/db";
import {optionsModel} from "@/api/tool/model";

export default {
  name: "Application",
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
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        projectCode: undefined,
        appName: undefined
      },
      // 列表页项目选择
      projectOptions: [],
      // 数据库选择
      dbOptions: [],
      modelOptions: [],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        projectCode: [
          { required: true, message: "项目编码不能为空", trigger: "blur" }
        ],
        appName: [
          { required: true, message: "应用名称不能为空", trigger: "blur" }
        ],
        appCode: [
          { required: true, message: "应用编码不能为空", trigger: "blur" }
        ],
        appVersion: [
          { required: true, message: "应用版本不能为空", trigger: "blur" }
        ],
        httpPort: [
          { required: true, message: "Http端口不能为空", trigger: "blur" }
        ],
        httpContext: [
          { required: true, message: "Http路径不能为空", trigger: "blur" }
        ]
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
        projectCode: undefined,
        dbId: null,
        appName: undefined,
        appCode: undefined,
        modelList: [],
        isSecurity: 0,
        httpPort: 8080,
        httpContext: '/',
        appVersion: '1.0.0',
        appDesc: undefined
      };
      this.resetForm("form");
    },
    /** 列表 */
    getList() {
      this.loading = true;
      listApplication(this.queryParams).then(response => {
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
    /** 工程模板 */
    handleTemplate(row){
      this.$download.zip("/code/api/v1/app/template/" + row.id, row.projectCode + "-" + row.appCode);
    },
    /** 新增 */
    handleAdd() {
      this.reset();
      optionsDb().then(response2 => {
        this.dbOptions = response2.data;
        this.dbOptions.push({"key":null, "label":"空"})
      });
      optionsModel().then(resp => {
        this.modelOptions = resp.data;
      });
      this.title = "新增应用";
      this.open = true;
    },
    /** 修改 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      optionsDb().then(response2 => {
        this.dbOptions = response2.data;
        this.dbOptions.push({"key":null, "label":"空"})
      });
      optionsModel().then(resp => {
        this.modelOptions = resp.data;
      });
      infoApplication(id).then(rsp => {
        this.form = rsp.data;
        this.open = true;
        this.title = "修改应用";
      });
    },
    /** 提交 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id !== undefined) {
            updateApplication(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addApplication(this.form).then(response => {
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
      const noticeIds = row.id || this.ids
      const msg = row.id ? '确认删除应用[' + row.appName + ']?' : '确认删除所选应用？';
      this.$modal.confirm(msg).then(function() {
        return delApplication(noticeIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
  }
};
</script>
