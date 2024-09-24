<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item :label="$t(`dept.label.name`)" prop="deptName">
        <el-input v-model="queryParams.deptName" :placeholder="$t(`dept.placeholder.name`)" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item :label="$t(`dept.label.phone`)" prop="deptPhone">
        <el-input v-model="queryParams.deptPhone" :placeholder="$t(`dept.placeholder.phone`)" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{$t('button.search')}}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('button.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-sort" size="mini" @click="toggleExpandAll">
                   {{$t('button.expand')}}/{{$t('button.collapse')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   :disabled="!checkPermit(['sys:dept:new'])">{{$t('route.system.dept.new')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                   :disabled="!checkPermit(['sys:dept:export'])">{{$t('route.system.dept.export')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain  size="mini" @click="showDiagram"
                   :disabled="!checkPermit(['sys:dept:diagram'])"><svg-icon icon-class="tree"/> {{$t('route.system.dept.diagram')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-refresh" size="mini" @click="handleRefreshCache"
                   :disabled="!checkPermit(['sys:dept:cache'])">{{$t('route.system.dept.cache')}}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-if="refreshTable" v-loading="loading" :data="deptList" row-key="deptId"
              :default-expand-all="isExpandAll" :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
      <el-table-column prop="deptName" :label="$t(`dept.label.name`)"/>
      <el-table-column prop="deptPhone" :label="$t(`dept.label.phone`)"/>
      <el-table-column :label="$t(`dept.label.leader`)">
        <template slot-scope="scope">
          <template v-if="scope.row.leaderList.length>0" v-for="item in scope.row.leaderList">
            <template>
              <div>{{ item.userName }}</div>
            </template>
          </template>
        </template>
      </el-table-column>
      <el-table-column prop="deptAddr" :label="$t(`dept.label.addr`)"/>
      <el-table-column :label="$t(`label.readonly`)" align="center" prop="readOnly">
        <template slot-scope="scope">
          <el-switch :disabled="!checkPermit(['sys:common:readonly'])" v-model="scope.row.readOnly" :active-value=1 :inactive-value=0 @change="handleReadOnly(scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`label.option`)" align="center" class-name="small-padding fixed-width" width="350">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">{{$t('route.system.dept.edit')}}</el-button>
          <el-button size="mini" type="text" icon="el-icon-plus" @click="handleAdd(scope.row)"
                     :disabled="!checkPermit(['sys:dept:new'])">{{$t('route.system.dept.new')}}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     :disabled="scope.row.readOnly === 1 || !checkPermit(['sys:dept:delete'])">{{$t('route.system.dept.delete')}}</el-button>
          <el-button size="mini" type="text" @click="choosePosts(scope.row)"><svg-icon icon-class="tree"/>{{$t('route.system.dept.positions')}}</el-button>
          <el-button size="mini" type="text" @click="chooseMembers(scope.row)"><svg-icon icon-class="peoples"/>{{$t('route.system.dept.members')}}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改部门对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="auto">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t(`dept.label.name`)" prop="deptName">
              <el-input v-model="form.deptName" :placeholder="$t(`dept.placeholder.name`)" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t(`dept.label.phone`)" prop="deptPhone">
              <el-input v-model="form.deptPhone" :placeholder="$t(`dept.placeholder.phone`)" maxlength="11" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t(`dept.label.parent`)" prop="parentIds">
              <el-tree-select v-model="form.parentIds" :selectParams="deptSelectParams" :treeParams="deptTreeParams"
                              :treeRenderFun="treeRender" @searchFun="treeSearch" :disabled="parentDisable"
                              :styles="userTreeStyles" ref="deptTreeSelect"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t(`dept.label.addr`)" prop="leader">
              <el-input v-model="form.deptAddr" :placeholder="$t(`dept.placeholder.addr`)" maxlength="20" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t(`label.remark`)" prop="email">
              <el-input v-model="form.remark" placeholder="..." maxlength="50" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm"
                   :disabled="form.readOnly === 1 || !checkPermit(['sys:dept:edit'])">{{$t('button.confirm')}}</el-button>
        <el-button @click="cancel">{{$t('button.cancel')}}</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="$t(`dept.dialog.diagram`)" :visible.sync="diagramOpen" width="80%" append-to-body>
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
import {listDept, getDept, delDept, addDept, updateDept, getDeptTree, refreshCache, changeReadonly} from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import OrganizationChart from 'vue-organization-chart'
import 'vue-organization-chart/dist/orgchart.css'
import {checkPermit} from "@/utils/permission";

export default {
  name: "Dept",
  dicts: ['sys_is_enable'],
  components: { Treeselect, OrganizationChart },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 表格树数据
      deptList: [],
      // 部门树选项
      deptOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 查询参数
      queryParams: {
        deptName: undefined,
        deptPhone: undefined
      },
      parentDisable: false,
      // 表单参数
      form: {},
      diagramOpen: false,
      diagramData:{},
      // 部门Tree
      deptSelectParams: {
        multiple: true,
        clearable: true,
        placeholder: '请选择部门'
      },
      // 部门Tree
      deptTreeParams: {
        clickParent: false,
        filterable: true,
        'check-strictly': true,
        'default-expand-all': false,
        'expand-on-click-node': false,
        data: [],
        props: {
          children: 'children',
          label: 'label',
          disabled: 'isDept',
          value: 'id'
        }
      },
      // 用户Tree
      userTreeStyles: {
        width: '100%'
      },
    };
  },
  created() {
    this.getList();
  },
  computed: {
    rules() {
      return {
        parentIds: [
          { required: true, message: this.$t(`dept.rules.parent`), trigger: "blur" }
        ],
        deptName: [
          { required: true, message: this.$t(`dept.rules.name`), trigger: "blur" }
        ]
      };
    }
  },
  methods: {
    checkPermit,
    /** 部门Tree */
    treeRender(h, { node, data, store }) {
      return (
        <span class='custom-tree-node'><span>{node.label}</span></span>
      );
    },
    /** 部门Tree */
    treeSearch(value) {
      this.$refs.deptTreeSelect.$refs.tree.filter(value);
    },
    /** 展开/折叠 */
    toggleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    /** 表单重置 */
    reset() {
      this.form = {
        deptId: undefined,
        parentIds: [],
        deptName: undefined,
        orderNum: undefined,
        leader: undefined,
        phone: undefined,
        email: undefined,
        status: "0",

      };
      this.resetForm("form");
    },
    /** 搜索 */
    handleQuery() {
      this.getList();
    },
    /** 重置 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 部门列表 */
    getList() {
      this.loading = true;
      listDept(this.queryParams).then(response => {
        this.deptList = this.handleTree(response.data.list, "deptId");
        this.loading = false;
      });
    },
    /** 新增 */
    handleAdd(row) {
      this.reset();
      const deptId = row.deptId !== undefined ? row.deptId : "1";
      if(row.deptId !== undefined){
        this.parentDisable = true;
      }else{
        this.parentDisable = false;
      }
      getDeptTree(deptId).then(response => {
        this.deptTreeParams.data = response.data;
        this.form.parentIds = row.deptId !== null ? [row.deptId] : [];
        this.title = this.$t(`dept.dialog.title_new`);
        this.open = true;
      });
    },
    /** 修改 */
    handleUpdate(row) {
      this.reset();
      getDept(row.deptId).then(response => {
        getDeptTree("1").then(resp2 => {
          this.parentDisable = false;
          this.deptTreeParams.data = resp2.data;
          this.form = response.data;
          this.open = true;
          this.title = this.$t(`dept.dialog.title_edit`);
        });
      });
    },
    /** 删除 */
    handleDelete(row) {
      this.$modal.confirm(this.$t(`dept.msg.confirm_delete`, { var1: row.deptName })).then(function() {
        return delDept(row.deptId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t(`msg.success_delete`));
      }).catch(() => {});
    },
    /** 导出 */
    handleExport() {
      this.download('/admin/api/v1/dept/export', {}, this.$t(`dept.excel`) + `_${new Date().getTime()}.xlsx`)
    },
    /** 组织架构 */
    showDiagram() {
      getDeptTree("1").then(response => {
        this.diagramData = response.data[0];
        this.diagramOpen = true;
      });
    },
    /** 刷新缓存 */
    handleRefreshCache(){
      refreshCache().then(() => {
        this.$modal.msgSuccess(this.$t(`msg.success_refresh`));
      });
    },
    /** 岗位设置 */
    choosePosts: function(row) {
      const deptId = row.deptId;
      this.$router.push("/system/dept-post/dept/" + deptId);
    },
    /** 人员设置 */
    chooseMembers: function(row) {
      const deptId = row.deptId;
      this.$router.push("/system/dept-user/dept/" + deptId);
    },
    /** 部门只读修改 */
    handleReadOnly(row) {
      let text = row.readOnly === 1 ? this.$t(`content.set`) : this.$t(`content.cancel`);
      this.$modal.confirm(this.$t(`dept.msg.confirm_readonly`, { var1: text, var2: row.deptName })).then(function() {
        return changeReadonly(row.deptId, row.readOnly, row.deptName);
      }).then(() => {
        this.$modal.msgSuccess(text + this.$t(`content.success`));
      }).catch(function() {
        row.readOnly = row.readOnly === 0 ? 1 : 0;
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
          if (this.form.deptId !== undefined) {
            updateDept(this.form).then(response => {
              this.$modal.msgSuccess(this.$t(`msg.success_edit`));
              this.open = false;
              this.getList();
            });
          } else {
            addDept(this.form).then(response => {
              this.$modal.msgSuccess(this.$t(`msg.success_create`));
              this.open = false;
              this.getList();
            });
          }
        }
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.vue-treeselect__multi-value {
  height: 24px;
  padding: 0 4px;
  line-height: 22px;
}

.vue-treeselect__multi-value-item-container {
  display: inline-block;
  padding-top: 0px;
  padding-right: 0px;
  vertical-align: top;
}

.vue-treeselect__multi-value-item {
  background-color: #f4f4f5;
  border-color: #e9e9eb;
  color: #909399;
}

.vue-treeselect__multi-value-item-container {
  display: inline-block;
  padding-right: 3px;
  vertical-align: top;
}

.vue-treeselect__value-remove {
  color: #606266;
  padding-left: 3px;
  border-left: 1px solid #fff;
  line-height: 0;
}

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

.orgchart .node .content {
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  width: 100%;
  height: 20px;
  font-size: 11px;
  line-height: 18px;
  border: 1px solid rgba(217,83,79,.8);
  border-radius: 0 0 4px 4px;
  text-align: center;
  background-color: #fff;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  padding-left: 5px;
  padding-right: 5px;
}
</style>
