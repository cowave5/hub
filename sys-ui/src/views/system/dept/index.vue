<template>
  <div class="app-container">
    <!--  筛选栏  -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item :label="$t('dept.label.name')" prop="deptName">
        <el-input v-model="queryParams.deptName" :placeholder="$t('dept.placeholder.name')"
                  clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item :label="$t('dept.label.phone')" prop="deptPhone">
        <el-input v-model="queryParams.deptPhone" :placeholder="$t('dept.placeholder.phone')"
                  clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          {{$t('commons.button.search')}}
        </el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('commons.button.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <!--  操作栏  -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-sort" size="mini" @click="toggleExpandAll">
          {{$t('commons.button.expand')}}/{{$t('commons.button.collapse')}}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain size="mini" icon="el-icon-plus" @click="handleAdd"
                   :disabled="!checkPermit(['sys:dept:create'])">
          {{$t('commons.button.create')}}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain size="mini" icon="el-icon-download" @click="handleExport"
                   :disabled="!checkPermit(['sys:dept:export'])">
          {{$t('commons.button.export')}}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain  size="mini" @click="showDiagram"
                   :disabled="!checkPermit(['sys:dept:diagram'])">
          <svg-icon icon-class="tree"/> {{$t('commons.button.diagram')}}
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :cols="cols"/>
    </el-row>

    <!--  列表数据  -->
    <el-table v-if="refreshTable" :data="list" row-key="deptId" :default-expand-all="isExpandAll"
              :tree-props="{children: 'children', hasChildren: 'hasChildren'}" v-loading="loading">
      <el-table-column v-if="cols[0].show" prop="deptName" :label="$t('dept.label.name')"/>
      <el-table-column v-if="cols[1].show" prop="deptPhone" :label="$t('dept.label.phone')"/>
      <el-table-column v-if="cols[2].show" :label="$t('dept.label.leader')">
        <template slot-scope="scope">
          <template v-if="scope.row.leaderList.length>0" v-for="item in scope.row.leaderList">
            <template>
              <div>{{ item.userName }}</div>
            </template>
          </template>
        </template>
      </el-table-column>
      <el-table-column v-if="cols[3].show" prop="deptAddr" :label="$t('dept.label.addr')"/>
      <el-table-column v-if="cols[4].show" :label="$t('commons.label.createTime')" prop="createTime" align="center">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="cols[5].show" :label="$t('commons.label.updateTime')" prop="updateTime" align="center">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding fixed-width" width="350">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">
            {{$t('commons.button.edit')}}
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-plus" @click="handleAdd(scope.row)"
                     :disabled="!checkPermit(['sys:dept:create'])">
            {{$t('commons.button.create')}}
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     :disabled="!checkPermit(['sys:dept:delete'])">
            {{$t('commons.button.delete')}}
          </el-button>
          <el-button size="mini" type="text" @click="chooseMembers(scope.row)">
            <svg-icon icon-class="peoples"/> {{$t('dept.button.members')}}
          </el-button>
          <el-button size="mini" type="text" @click="choosePosts(scope.row)">
            <svg-icon icon-class="tree"/> {{$t('dept.button.positions')}}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改 -->
    <el-dialog v-drag :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="auto">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('dept.label.name')" prop="deptName">
              <el-input v-model="form.deptName" :placeholder="$t('dept.placeholder.name')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('dept.label.phone')" prop="deptPhone">
              <el-input v-model="form.deptPhone" :placeholder="$t('dept.placeholder.phone')" maxlength="11" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('dept.label.parent')" prop="parentIds">
              <el-tree-select v-model="form.parentIds" :selectParams="deptSelectParams" :treeParams="deptTreeParams"
                              :treeRenderFun="treeRender" @searchFun="treeSearch" :disabled="parentDisable"
                              :styles="userTreeStyles" ref="deptTreeSelect"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('dept.label.addr')" prop="leader">
              <el-input v-model="form.deptAddr" :placeholder="$t('dept.placeholder.addr')" maxlength="20" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('commons.label.remark')" prop="email">
              <el-input v-model="form.remark" placeholder="..." maxlength="50" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" :disabled="!checkPermit(['sys:dept:edit'])">
          {{$t('commons.button.confirm')}}
        </el-button>
        <el-button @click="cancel">{{$t('commons.button.cancel')}}</el-button>
      </div>
    </el-dialog>

    <el-dialog v-drag :title="$t('dept.dialog.diagram')" :visible.sync="diagramOpen" width="80%" append-to-body>
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
import {getDeptList, getDeptInfo, delDept, addDept, updateDept, getDeptDiagramById} from "@/api/system/dept";
import {checkPermit} from "@/utils/permission";

export default {
  name: "Dept",
  dicts: [],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 表格树数据
      list: [],
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
      cols: [
        {key: 0, label: 'dept.label.name', show: true},
        {key: 1, label: 'dept.label.phone', show: true},
        {key: 2, label: 'dept.label.leader', show: true},
        {key: 3, label: 'dept.label.addr', show: true},
        {key: 4, label: 'commons.label.createTime', show: false},
        {key: 5, label: 'commons.label.updateTime', show: false},
      ],
      diagramHorizontal: true,
      diagramExpandAll: true,
    };
  },
  created() {
    this.getList();
  },
  computed: {
    rules() {
      return {
        parentIds: [
          { required: true, message: this.$t('dept.rules.parent'), trigger: "blur" }
        ],
        deptName: [
          { required: true, message: this.$t('dept.rules.name'), trigger: "blur" }
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
    /** 搜索 */
    handleQuery() {
      this.getList();
    },
    /** 重置 */
    resetQuery() {
      this.$refs.queryForm.resetFields();
      this.handleQuery();
    },
    /** 部门列表 */
    getList() {
      this.loading = true;
      getDeptList(this.queryParams).then(response => {
        this.list = this.handleTree(response.data.list, "deptId");
        this.loading = false;
      });
    },
    /** 新增 */
    handleAdd(row) {
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
      this.title = this.$t('dept.dialog.new')
      getDeptDiagramById(0).then(response => {
        this.deptTreeParams.data = response.data
        this.form.parentIds = row.deptId !== undefined ? [row.deptId] : []
        this.parentDisable = row.deptId !== undefined;
        this.open = true
      })
    },
    /** 修改 */
    handleUpdate(row) {
      this.parentDisable = false;
      this.title = this.$t('dept.dialog.edit');
      getDeptInfo(row.deptId).then(response => {
        getDeptDiagramById(1).then(resp2 => {
          this.deptTreeParams.data = resp2.data;
          this.form = response.data;
          this.open = true;
        });
      });
    },
    /** 删除 */
    handleDelete(row) {
      this.$modal.confirm(this.$t('dept.confirm.delete', { arg1: row.deptName })).then(function() {
        return delDept(row.deptId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('commons.msg.success.delete'));
      }).catch(() => {});
    },
    /** 导出 */
    handleExport() {
      this.download('/admin/api/v1/dept/export', {}, this.$t('dept.text.data') + `_${new Date().getTime()}.xlsx`)
    },
    /** 组织架构 */
    showDiagram() {
      getDeptDiagramById(0).then(response => {
        this.diagramData = response.data[0];
        this.diagramOpen = true;
        this.diagramExpand();
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
    /** 取消 */
    cancel() {
      this.open = false;
    },
    /** 提交 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.deptId !== undefined) {
            updateDept(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
              this.open = false;
              this.getList();
            });
          } else {
            addDept(this.form).then(() => {
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
              {data.id > 0 && <svg-icon icon-class="dept"/>}
              <span style="margin-left: 2px;">{data.label}</span>
            </div>
          </div>
      );
    },
  }
};
</script>
