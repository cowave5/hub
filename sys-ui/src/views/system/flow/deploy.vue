<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="流程key" prop="key">
        <el-input v-model="queryParams.key" placeholder="" clearable />
      </el-form-item>
      <el-form-item label="流程名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="" clearable />
      </el-form-item>
      <el-form-item label="版本" prop="latest">
        <el-select v-model="queryParams.latest" placeholder="">
          <el-option label="最新版本" value="true"/>
          <el-option label="全部版本" value="false"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{$t('button.search')}}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('button.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleUpload">部署</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="list"
              @selection-change="handleSelectionChange" :header-cell-style="{'text-align':'center'}">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t(`label.index`)" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="部署id" align="center" prop="deploymentId" width="320" :show-overflow-tooltip="true" />
      <el-table-column label="流程key" align="center" prop="key" :show-overflow-tooltip="true" />
      <el-table-column label="流程名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="资源文件" align="center" prop="resourceName" width="380" :show-overflow-tooltip="true" />
      <el-table-column label="版本" align="center" prop="version" :show-overflow-tooltip="true" />
      <el-table-column :label="$t(`label.option`)" align="center" class-name="small-padding" width="280">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handlePreview(scope.row)"><svg-icon icon-class="fd"/>流程定义</el-button>
          <el-button size="mini" type="text" @click="handleDiagram(scope.row)"><svg-icon icon-class="flow2"/>流程图</el-button>
          <el-button size="mini" type="text" @click="handleTranslate(scope.row)"><svg-icon icon-class="to"/>转为模型</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 部署 -->
    <el-dialog title="部署流程" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".bpmn, .xml, .zip"
        :headers="headers"
        :action="upload.url"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">{{$t('user.dialog.text_import1')}}<em>{{$t('user.dialog.text_import2')}}</em>{{$t('user.dialog.text_import3')}}</div>
        <div class="el-upload__tip text-center" slot="tip">
          <span>提示：仅允许导入“bpmn”、“xml”或“zip”格式文件！</span>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">{{$t('button.confirm')}}</el-button>
        <el-button @click="upload.open = false">{{$t('button.cancel')}}</el-button>
      </div>
    </el-dialog>

    <!-- 流程定义 -->
    <el-dialog title="流程定义" :visible.sync="preview.open" width="80%" top="5vh" append-to-body>
      <el-tabs v-model="preview.activeName">
        <el-tab-pane v-for="(value, key) in preview.data" :key="key" :label="key" :name="key">
          <el-link :underline="false" icon="el-icon-document-copy"
                   v-clipboard:copy="value" v-clipboard:success="clipboardSuccess" style="float:right">复制</el-link>
          <pre><code class="hljs" v-html="highlightedCode(value)"></code></pre>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>

    <!-- 流程图 -->
    <el-dialog title="流程图" :visible.sync="diagram.open" width="70%"  append-to-body>
      <el-image :src="diagram.src"  fit="contain"/>
    </el-dialog>
  </div>
</template>

<script>
import {checkPermit} from "@/utils/permission";
import {deleteDeploy, listDeploy, showDefinition, showDiagram, translateDeploy} from "@/api/system/flow/deploy";
import hljs from "highlight.js/lib/highlight";
import "highlight.js/styles/github-gist.css";
import cache from "@/plugins/cache";
hljs.registerLanguage("xml", require("highlight.js/lib/languages/xml"));

export default {
  name: "flow-Deploy",
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
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        name: undefined,
        key: undefined,
        latest: "true"
      },
      // 流程定义
      preview: {
        open: false,
        data: {},
        activeName: "流程定义"
      },
      // 流程图
      diagram: {
        open: false,
        src: undefined
      },
      // 部署
      upload: {
        open: false,
        // 是否禁用上传
        isUploading: false,
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/admin/api/v1/flow/deploy/upload"
      },
    };
  },
  created() {
    this.getList();
  },
  computed: {
    headers() {
      return  {Authorization: "Bearer " + cache.local.getAccessToken(), "Accept-Language": this.$i18n.locale}
    }
  },
  methods: {
    checkPermit,
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.deploymentId)
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
    /** 部署 */
    handleUpload(){
      this.upload.open = true;
    },
    /** 文件上传中 */
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    /** 文件上传成功 */
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow-y: scroll;overflow-x: auto;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", this.$t(`user.dialog.text_import7`), { dangerouslyUseHTMLString: true });
      this.getList();
    },
    /** 提交上传文件 */
    submitFileForm() {
      this.$refs.upload.submit();
    },
    /** 列表 */
    getList() {
      this.loading = true;
      listDeploy(this.queryParams).then(response => {
          this.list = response.data.list;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    /** 删除 */
    handleDelete(row) {
      const ids = row.deploymentId || this.ids;
      const msg = row.deploymentId ? "确认删除流程" + row.name + "[版本: " + row.version + "]" : "确认删除所选流程";
      this.$modal.confirm(msg).then(function() {
        return deleteDeploy(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t(`msg.success_delete`));
      }).catch(() => {});
    },
    /** 转为模型 */
    handleTranslate(row){
      translateDeploy(row.id).then(response => {
        this.$modal.msgSuccess(response.msg);
      });
    },
    /** 流程图 */
    handleDiagram(row){
      showDiagram(row.id).then(response => {
        this.diagram.src = 'data:image/jpg;base64,' + response.data;
        this.diagram.open = true;
      });
    },
    /** 流程定义 */
    handlePreview(row){
      showDefinition(row.deploymentId, row.resourceName).then(response => {
        this.preview.data = response.data;
        this.preview.open = true;
        this.preview.activeName = row.resourceName;
      });
    },
    /** 复制代码成功 */
    clipboardSuccess() {
      this.$modal.msgSuccess("复制成功");
    },
    /** 高亮显示 */
    highlightedCode(code) {
      const result = hljs.highlight("xml", code || "", true);
      return result.value || '&nbsp;';
    },
  }
};
</script>
