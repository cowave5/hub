<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 部门树 -->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input v-model="deptName" :placeholder="$t('dept.placeholder.name')"
                    clearable size="small" prefix-icon="el-icon-search" style="margin-bottom: 20px"/>
        </div>
        <div class="head-container">
          <el-tree ref="tree" :data="deptOptions" :props="defaultProps" default-expand-all highlight-current
                   :expand-on-click-node="false" :filter-node-method="filterNode" @node-click="handleNodeClick"/>
        </div>
      </el-col>

      <!--   筛选栏   -->
      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="auto">
          <el-form-item :label="$t('post.label.name')" prop="postName">
            <el-input v-model="queryParams.postName" :placeholder="$t('post.placeholder.name')"
                      clearable @keyup.enter.native="handleQuery"/>
          </el-form-item>
          <el-form-item :label="$t('post.label.type')" prop="postType">
            <el-select v-model="queryParams.postType" :placeholder="$t('post.placeholder.type')" clearable>
              <el-option v-for="dict in dict.type.post_type" :key="dict.code" :value="dict.code" :label="$t(dict.name)"/>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
              {{$t('commons.button.search')}}
            </el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('commons.button.reset')}}</el-button>
          </el-form-item>
        </el-form>

        <!--    操作栏    -->
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                       :disabled="!checkPermit(['hub:post:create'])">
              {{$t('commons.button.create')}}
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="el-icon-edit" size="mini" @click="handleUpdate"
                       :disabled="single || !checkPermit(['hub:post:edit'])">
              {{$t('commons.button.edit')}}
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete"
                       :disabled="multiple || !checkPermit(['hub:post:delete'])">
              {{$t('commons.button.delete')}}
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                       :disabled="!checkPermit(['hub:post:export'])">
              {{$t('commons.button.export')}}
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="primary" plain  size="mini" @click="showDiagram"
                       :disabled="!checkPermit(['hub:post:diagram'])">
              <svg-icon icon-class="tree"/> {{$t('commons.button.diagram')}}
            </el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :cols="cols"/>
        </el-row>

        <!--    列表数据    -->
        <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center"/>
          <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="55">
            <template slot-scope="scope">
              <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column v-if="cols[0].show" :label="$t('post.label.name')" align="center" prop="postName"/>
          <el-table-column v-if="cols[1].show" :label="$t('post.label.type')" align="center" prop="postType">
            <template slot-scope="{row: {postType}}">
              <template v-for="item in dict.type.post_type">
                <span v-if="postType === item.code">{{ $t(item.name) }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column v-if="cols[2].show" :label="$t('commons.label.status')" align="center" prop="postStatus">
            <template slot-scope="{row: {postStatus}}">
              <template v-for="item in enable_disable">
                <span v-if="postStatus === item.value">{{ $t(item.label) }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column v-if="cols[3].show" :label="$t('commons.label.remark')" align="center" prop="remark"/>
          <el-table-column v-if="cols[4].show" :label="$t('commons.label.createTime')" align="center" prop="createTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column v-if="cols[5].show" :label="$t('commons.label.updateTime')" align="center" prop="updateTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.updateTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">
                {{$t('commons.button.edit')}}
              </el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                         :disabled="!checkPermit(['hub:post:delete'])">
                {{$t('commons.button.delete')}}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total>0" :total="total" :limit.sync="queryParams.pageSize"
                    :page.sync="queryParams.page" @pagination="getList"/>
      </el-col>
    </el-row>

    <!-- 添加或修改 -->
    <el-dialog v-drag :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="auto">
        <el-form-item :label="$t('post.label.parent')" prop="parentId">
          <treeselect v-model="form.parentId" :options="postOptions" :placeholder="$t('post.placeholder.parent')"/>
        </el-form-item>
        <el-form-item :label="$t('post.label.type')" prop="postType">
          <el-select v-model="form.postType" :placeholder="$t('post.placeholder.type')" clearable style="width: 100%;">
            <el-option v-for="dict in dict.type.post_type" :key="dict.code" :value="dict.code" :label="$t(dict.name)"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('post.label.name')" prop="postName">
          <el-input v-model="form.postName" :placeholder="$t('post.placeholder.name')" />
        </el-form-item>
        <el-form-item :label="$t('post.label.level')" prop="postLevel">
          <el-input-number v-model="form.postLevel" controls-position="right" :min="0"/>
        </el-form-item>
        <el-form-item :label="$t('post.label.status')" prop="status">
          <el-radio-group v-model="form.postStatus">
            <el-radio v-for="item in enable_disable" :key="item.value" :label="item.value">{{$t(item.label)}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('commons.label.remark')" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="..." />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm"
                   :disabled="!checkPermit(['hub:post:edit'])">
          {{$t('commons.button.confirm')}}
        </el-button>
        <el-button @click="cancel">{{$t('commons.button.cancel')}}</el-button>
      </div>
    </el-dialog>

    <el-dialog v-drag :title="$t('post.dialog.diagram')" :visible.sync="diagramOpen" width="80%" append-to-body>
      <div style="margin-left:30px;">
        <el-row :gutter="20">
          <el-col :span="3">
            <el-switch v-model="diagramHorizontal" :width="50" active-text="竖排" inactive-text="横排"
                       style="margin-top:8px;"/>
          </el-col>
          <el-col :span="3">
            <el-switch v-model="diagramExpandAll" :width="50" active-text="展开"
                       inactive-text="折叠" style="margin:8px;" @change="diagramExpand"/>
          </el-col>
        </el-row>
      </div>
      <div style="font-size:12px; margin-top:30px; display: flex; justify-content: center;">
        <el-scrollbar style="width: fit-content;" class="el-org-tree">
          <vue2-org-tree
              :data="diagramData"
              :collapsable="true"
              :horizontal="!diagramHorizontal"
              :default-expand-level=2
              :render-content="diagramRender"
              @on-expand="onExpand"/>
        </el-scrollbar>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {listPost, getPost, delPost, addPost, updatePost, getPostDiagram} from "@/api/system/post";
import {getDeptDiagram} from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {checkPermit} from "@/utils/permission";
import { enable_disable } from '@/utils/constants';
export default {
  name: "Post",
  dicts: ['post_type'],
  components: {Treeselect},
  data() {
    return {
      enable_disable: enable_disable,
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
      list: [],
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
      diagramData: {},
      cols: [
        {key: 0, label: 'post.label.name', show: true},
        {key: 1, label: 'post.label.type', show: true},
        {key: 2, label: 'commons.label.status', show: true},
        {key: 3, label: 'commons.label.remark', show: true},
        {key: 4, label: 'commons.label.createTime', show: true},
        {key: 5, label: 'commons.label.updateTime', show: false},
      ],
      diagramHorizontal: true,
      diagramExpandAll: true,
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
          { required: true, message: this.$t('post.rules.name'), trigger: "blur" }
        ],
        postType: [
          { required: true, message: this.$t('post.rules.type'), trigger: "blur" }
        ],
        postLevel: [
          { required: true, message: this.$t('post.rules.level'), trigger: "blur" }
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
      getDeptDiagram().then(resp => {
        this.deptOptions = resp.data;
      });
    },
    /** 岗位列表 */
    getList() {
      this.loading = true;
      listPost(this.queryParams).then(response => {
        this.list = response.data.list;
        this.total = response.data.total;
        this.loading = false;
      });
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
      this.$refs.queryForm.resetFields();
      this.queryParams.deptId = null;
      this.handleQuery();
    },
    /** 新增 */
    handleAdd() {
      this.form = {
        parentId: undefined,
        postId: undefined,
        postType: undefined,
        postName: undefined,
        postLevel: 1,
        postStatus: 1,
        remark: undefined
      };
      this.title = this.$t('post.dialog.new');
      getPostDiagram().then(response => {
        this.postOptions = [response.data];
        this.open = true;
      });
    },
    /** 修改 */
    handleUpdate(row) {
      const postId = row.postId || this.ids
      this.title = this.$t('post.dialog.edit');
      getPost(postId).then(response => {
          getPostDiagram().then(resp => {
            this.postOptions = [resp.data];
            this.form = response.data;
            this.open = true;
          });
      });
    },
    /** 删除 */
    handleDelete(row) {
      const postIds = row.postId || this.ids;
      const msg = row.postId
          ? this.$t('post.confirm.delete', { arg1: row.postName })
          : this.$t('post.confirm.delete_select');
      this.$modal.confirm(msg).then(function() {
        return delPost(postIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('commons.msg.success.delete'));
      }).catch(() => {});
    },
    /** 导出 */
    handleExport() {
      this.download('/admin/api/v1/post/export',
          {...this.queryParams}, this.$t('post.text.data') + `_${new Date().getTime()}.xlsx`)
    },
    /** 组织架构 */
    showDiagram() {
      getPostDiagram().then(response => {
        this.diagramData = response.data;
        this.diagramOpen = true;
        this.diagramExpand();
      });
    },
    /** 取消 */
    cancel() {
      this.open = false;
    },
    /** 提交 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.postId !== undefined) {
            updatePost(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
              this.open = false;
              this.getList();
            });
          } else {
            addPost(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('commons.msg.success.create'));
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    diagramExpand() {
      this.toggleExpand(this.diagramData, this.diagramExpandAll)
    },
    toggleExpand(data, val) {
      if (Array.isArray(data)) {
        data.forEach(item => {
          this.$set(item, "expand", val);
          if (item.children) {
            this.toggleExpand(item.children, val);
          }
        });
      } else {
        this.$set(data, "expand", val);
        if (data.children) {
          this.toggleExpand(data.children, val);
        }
      }
    },
    onExpand(e, data) {
      console.log(1)
      if ("expand" in data) {
        data.expand = !data.expand;
        if (!data.expand && data.children) {
          this.collapse(data.children);
        }
      } else {
        this.$set(data, "expand", true);
      }
    },
    collapse(list) {
      console.log(2)
      list.forEach(child => {
        if (child.expand) {
          child.expand = false;
        }
        child.children && this.collapse(child.children);
      });
    },
    diagramRender(h, data) {
      return (
          <div>
            <div>
              {data.id > 0 && <svg-icon icon-class="post"/>}
              <span style="margin-left: 2px;">{data.label}</span>
            </div>
          </div>
      );
    },
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.org-tree-node-label {
  white-space: nowrap;
}

</style>
