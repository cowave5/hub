<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 部门树 -->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input v-model="deptName" :placeholder="$t(`user.placeholder.dept`)" clearable size="small" prefix-icon="el-icon-search" style="margin-bottom: 20px"/>
        </div>
        <div class="head-container">
          <el-tree :data="deptOptions" :props="defaultProps" :expand-on-click-node="false" :filter-node-method="filterNode"
                   ref="tree" default-expand-all highlight-current @node-click="handleNodeClick"/>
        </div>
      </el-col>

      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item :label="$t(`post.label.name`)" prop="postName">
            <el-input v-model="queryParams.postName" :placeholder="$t(`post.placeholder.name`)" clearable @keyup.enter.native="handleQuery"/>
          </el-form-item>
          <el-form-item :label="$t(`post.label.type`)" prop="postType">
            <el-select v-model="queryParams.postType" :placeholder="$t(`post.placeholder.type`)" clearable>
              <el-option v-if="$i18n.locale==='zh'" v-for="dict in dict.type.post_type" :key="dict.value" :label="`${dict.label}`" :value="dict.value"/>
              <el-option v-if="$i18n.locale==='en'" v-for="dict in dict.type.post_type" :key="dict.value" :label="`${dict.labelEn}`" :value="dict.value"/>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{$t('button.search')}}</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('button.reset')}}</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                       :disabled="!checkPermit(['sys:post:new'])">{{$t('route.system.post.new')}}</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="el-icon-edit" size="mini" @click="handleUpdate"
                       :disabled="single || !checkPermit(['sys:post:edit'])">{{$t('route.system.post.edit')}}</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete"
                       :disabled="multiple || !checkPermit(['sys:post:delete'])">{{$t('route.system.post.delete')}}</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                       :disabled="!checkPermit(['sys:post:export'])">{{$t('route.system.post.export')}}</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="primary" plain  size="mini" @click="showDiagram"
                       :disabled="!checkPermit(['sys:post:diagram'])"><svg-icon icon-class="tree"/>{{$t('route.system.post.diagram')}}</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="el-icon-refresh" size="mini" @click="handleRefreshCache"
                       :disabled="!checkPermit(['sys:post:cache'])">{{$t('route.system.post.cache')}}</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="postList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center"/>
          <el-table-column :label="$t(`label.index`)" type="index" align="center" width="55">
            <template slot-scope="scope">
              <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t(`post.label.name`)" align="center" prop="postName"/>
          <el-table-column :label="$t(`post.label.type`)" align="center" prop="postType">
            <template slot-scope="{row: {postType}}">
              <template v-for="item in dict.type.post_type">
                <span v-if="postType === item.value && $i18n.locale==='zh'">{{ item.label }}</span>
                <span v-if="postType === item.value && $i18n.locale==='en'">{{ item.labelEn }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column :label="$t(`label.status`)" align="center" prop="postStatus">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.sys_is_enable" :value="scope.row.postStatus"/>
            </template>
          </el-table-column>
          <el-table-column :label="$t(`label.remark`)" align="center" prop="remark"/>
          <el-table-column :label="$t(`label.date_create`)" align="center" prop="createTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t(`label.readonly`)" align="center" prop="readOnly">
            <template slot-scope="scope">
              <el-switch :disabled="!checkPermit(['sys:common:readonly'])" v-model="scope.row.readOnly" :active-value=1 :inactive-value=0 @change="handleReadOnly(scope.row)"/>
            </template>
          </el-table-column>
          <el-table-column :label="$t(`label.option`)" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">{{$t('route.system.post.edit')}}</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                         :disabled="scope.row.readOnly === 1 || !checkPermit(['sys:post:delete'])">{{$t('route.system.post.delete')}}</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>
      </el-col>
    </el-row>

    <!-- 添加或修改岗位对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="auto">
        <el-form-item :label="$t(`post.label.parent`)" prop="parentId">
          <treeselect v-model="form.parentId" :options="postOptions" :placeholder="$t(`post.placeholder.parent`)"/>
        </el-form-item>
        <el-form-item :label="$t(`post.label.type`)" prop="postType">
          <el-select v-model="form.postType" :placeholder="$t(`post.placeholder.type`)" clearable style="width: 100%;">
            <el-option v-if="$i18n.locale==='zh'" v-for="dict in dict.type.post_type" :key="dict.value" :label="`${dict.label}`" :value="dict.value"/>
            <el-option v-if="$i18n.locale==='en'" v-for="dict in dict.type.post_type" :key="dict.value" :label="`${dict.labelEn}`" :value="dict.value"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t(`post.label.name`)" prop="postName">
          <el-input v-model="form.postName" :placeholder="$t(`post.placeholder.name`)" />
        </el-form-item>
        <el-form-item :label="$t(`post.label.level`)" prop="postLevel">
          <el-input-number v-model="form.postLevel" controls-position="right" :min="0"/>
        </el-form-item>
        <el-form-item :label="$t(`post.label.status`)" prop="status">
          <el-radio-group v-model="form.postStatus">
            <el-radio v-if="$i18n.locale==='zh'" v-for="dict in dict.type.sys_is_enable" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
            <el-radio v-if="$i18n.locale==='en'" v-for="dict in dict.type.sys_is_enable" :key="dict.value" :label="dict.value">{{dict.labelEn}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t(`label.remark`)" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="..." />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm"
                   :disabled="form.readOnly === 1 || !checkPermit(['sys:post:edit'])">{{$t('button.confirm')}}</el-button>
        <el-button @click="cancel">{{$t('button.cancel')}}</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="$t(`post.dialog.diagram`)" :visible.sync="diagramOpen" width="80%" append-to-body>
      <div class="dialog-content">
        <organization-chart :datasource="diagramData">
          <template slot-scope="{ nodeData }">
            <div class="title">{{nodeData.label}}</div>
          </template>
        </organization-chart>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listPost, getPost, delPost, addPost, updatePost, getTree, refreshCache, changeReadonly} from "@/api/system/post";
import {treeselect} from "@/api/system/dept";
import OrganizationChart from 'vue-organization-chart'
import 'vue-organization-chart/dist/orgchart.css'
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {checkPermit} from "@/utils/permission";

export default {
  name: "Post",
  dicts: ['sys_is_enable', 'post_type'],
  components: {Treeselect, OrganizationChart},
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
      // 部门名称
      deptName: undefined,
      // 部门选项
      deptOptions: undefined,
      // 岗位树选项
      postOptions: undefined,
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 总条数
      total: 0,
      // 岗位表格数据
      postList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        deptId: undefined,
        postName: undefined,
        postType: undefined
      },
      // 表单参数
      form: {},
      diagramOpen: false,
      diagramData:{}
    };
  },
  watch: {
    /** 根据名称筛选部门树 */
    deptName(val) {
      this.$refs.tree.filter(val);
    }
  },
  created() {
    this.getList();
    this.getDeptOptions();
  },
  computed: {
    rules() {
      return {
        postName: [
          { required: true, message: this.$t(`post.rules.name`), trigger: "blur" }
        ],
        postType: [
          { required: true, message: this.$t(`post.rules.type`), trigger: "blur" }
        ],
        postLevel: [
          { required: true, message: this.$t(`post.rules.level`), trigger: "blur" }
        ]
      };
    }
  },
  methods: {
    checkPermit,
    /** 筛选节点 */
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    /** 点击部门树 */
    handleNodeClick(data) {
      this.queryParams.deptId = data.id;
      this.handleQuery();
    },
    /** 部门树 */
    getDeptOptions() {
      treeselect().then(resp => {
        this.deptOptions = resp.data;
      });
    },
    /** 岗位列表 */
    getList() {
      this.loading = true;
      listPost(this.queryParams).then(response => {
        this.postList = response.data.list;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 表单重置 */
    reset() {
      this.form = {
        parentId: undefined,
        postId: undefined,
        postType: undefined,
        postName: undefined,
        postLevel: 1,
        postStatus: 1,
        remark: undefined
      };
      this.resetForm("form");
    },
    /** 搜索 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.postId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 重置 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增 */
    handleAdd() {
      this.reset();
      getTree().then(response => {
        this.postOptions = [response.data];
        this.open = true;
        this.title = this.$t(`post.dialog.title_new`);
      });
    },
    /** 修改 */
    handleUpdate(row) {
      this.reset();
      const postId = row.postId || this.ids
      getPost(postId).then(response => {
          getTree().then(resp => {
            this.postOptions = [resp.data];
            this.form = response.data;
            this.open = true;
            this.title = this.$t(`post.dialog.title_edit`);
          });
      });
    },
    /** 删除 */
    handleDelete(row) {
      const postIds = row.postId || this.ids;
      const msg = row.postId ? this.$t(`post.msg.confirm_delete`, { var1: row.postName }) : this.$t(`post.msg.select_delete`);
      this.$modal.confirm(msg).then(function() {
        return delPost(postIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t(`msg.success_delete`));
      }).catch(() => {});
    },
    /** 岗位只读修改 */
    handleReadOnly(row) {
      let text = row.readOnly === 1 ? this.$t(`content.set`) : this.$t(`content.cancel`);
      this.$modal.confirm(this.$t(`post.msg.confirm_readonly`, { var1: text, var2: row.postName })).then(function() {
        return changeReadonly(row.postId, row.readOnly, row.postName);
      }).then(() => {
        this.$modal.msgSuccess(text + this.$t(`content.success`));
      }).catch(function() {
        row.readOnly = row.readOnly === 0 ? 1 : 0;
      });
    },
    /** 导出 */
    handleExport() {
      this.download('/admin/api/v1/post/export', {}, this.$t(`post.excel`) + `_${new Date().getTime()}.xlsx`)
    },
    /** 组织架构 */
    showDiagram() {
      getTree().then(response => {
        this.diagramData = response.data;
        this.diagramOpen = true;
      });
    },
    /** 刷新缓存 */
    handleRefreshCache(){
      refreshCache().then(() => {
        this.$modal.msgSuccess(this.$t(`msg.success_refresh`));
      });
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
          if (this.form.postId !== undefined) {
            updatePost(this.form).then(response => {
              this.$modal.msgSuccess(this.$t(`msg.success_edit`));
              this.open = false;
              this.getList();
            });
          } else {
            addPost(this.form).then(response => {
              this.$modal.msgSuccess(this.$t(`msg.success_create`));
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
  }
};
</script>

<style>
.dialog-content {
  height: 700px;
  overflow-x: scroll;
}

.orgchart-container {
  position: relative;
  display: inline-block;
  height: 680px;
  width: calc(100% - 24px);
  overflow: auto;
  text-align: center;
}

.orgchart {
  display: inline-block;
  height: 100%;
  width: 100%;
  overflow: scroll;
  -webkit-touch-callout: none;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
  background: #fff;
  background-size: 10px 10px;
}

.orgchart .node {
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  display: inline-block;
  position: relative;
  margin: 0;
  padding: 3px;
  border: 2px dashed transparent;
  text-align: center;
  width: auto;
}

.orgchart .node .title {
  font-size: 15px;
  font-family: Arial;
  padding: 12px;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 5px;
  line-height: 0;
  flex-direction: row-reverse;
  background: #0e5e59;
}
</style>
