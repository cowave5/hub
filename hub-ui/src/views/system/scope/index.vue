<template>
  <div class="app-container">
    <!--  筛选栏  -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="auto">
      <el-form-item :label="$t('scope.label.module')" prop="opModule">
        <treeselect v-model="queryParams.scopeModule" :options="moduleOptions" style="width: 260px"
                    :disable-branch-nodes="true" :normalizer="normalizer"
                    :placeholder="$t('scope.placeholder.module')" :show-count="true"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{$t('commons.button.search')}}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('commons.button.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <!--  操作栏  -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   :disabled="!checkPermit(['hub:scope:create'])">
          {{$t('commons.button.create')}}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" @click="handleUpdate"
                   :disabled="single || !checkPermit(['hub:scope:edit'])">
          {{$t('commons.button.edit')}}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete"
                   :disabled="multiple || !checkPermit(['hub:scope:delete'])">
          {{$t('commons.button.delete')}}
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!--  列表数据  -->
    <el-table ref="tables" v-loading="loading" :data="list" @selection-change="handleSelectionChange" :header-cell-style="{'text-align':'center'}">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('scope.label.module')" align="center" prop="scopeModule">
        <template slot-scope="{row: {scopeModule}}">
          <template v-for="module in moduleOptions">
            <template v-for="type in module.children">
              <span v-if="scopeModule === type.key">{{ $t(type.label) }}</span>
            </template>
          </template>
        </template>
      </el-table-column>
      <el-table-column :label="$t('scope.label.name')" align="center" prop="scopeName"/>
      <el-table-column :label="$t('commons.label.status')" align="center" prop="userStatus">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.scopeStatus" @change="handleStatusChange(scope.row)"
                     :active-value=1 :inactive-value=0 :disabled="!checkPermit(['hub:scope:edit'])"/>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.createTime')" align="center" prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.updateTime')" align="center" prop="updateTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     :disabled="!checkPermit(['hub:scope:edit'])">
            {{$t('commons.button.edit')}}
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     :disabled="!checkPermit(['hub:scope:delete'])">
            {{$t('commons.button.delete')}}
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleView(scope.row)">
            {{$t('scope.button.scope')}}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 添加或修改 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item :label="$t('scope.label.name')" prop="scopeName">
          <el-input v-model="form.scopeName" />
        </el-form-item>
        <el-form-item :label="$t('scope.label.module')" prop="scopeModule">
          <treeselect v-model="form.scopeModule" :options="moduleOptions" style="width: 100%"
                      :disable-branch-nodes="true" :normalizer="normalizer"
                      :placeholder="$t('scope.placeholder.module')" :show-count="true"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" :disabled="!checkPermit(['hub:scope:edit'])">
          {{$t('commons.button.confirm')}}
        </el-button>
        <el-button @click="cancel">{{$t('commons.button.cancel')}}</el-button>
      </div>
    </el-dialog>

    <component ref="infoRef" :is="infoView" @ok="handleQuery" />
  </div>
</template>

<script>
import {checkPermit} from "@/utils/permission";
import { listTypeByGroup} from "@/api/system/dict";
import {addScope, deleteScope, editScope, intoScope, listScope, updateScopeStatus} from "@/api/system/scope";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: "Scope",
  dicts: [],
  components: { Treeselect },
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
      // 日期范围
      dateRange: [],
      // 查询参数
      form: {
        scopeName: undefined,
        scopeModule: undefined,
      },
      queryParams: {
        page: 1,
        pageSize: 10,
        scopeModule: undefined,
      },
      moduleOptions: [],
      typeOptions: [],
      viewKey: '',
      infoView: null
    };
  },
  created() {
    this.getOptions();
    this.getList();
  },
  computed: {
    rules() {
      return {
        scopeName: [
          { required: true, message: this.$t('scope.rules.name'), trigger: "blur" }
        ],
        scopeModule: [
          { required: true, message: this.$t('scope.rules.module'), trigger: "blur" }
        ],
      };
    }
  },
  methods: {
    checkPermit,
    normalizer(node) {
      if (!node.children || !node.children.length) {
        delete node.children;
      }
      return {
        id: node.key,
        label: this.$t(node.label),
        children: node.children
      };
    },
    getOptions() {
      listTypeByGroup("domain_module").then(response => {
        this.moduleOptions = response.data;
      });
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      listScope(this.queryParams).then(response => {
            this.list = response.data.list;
            this.total = response.data.total;
            this.loading = false;
          }
      );
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.scopeId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 删除 */
    handleDelete(row) {
      const scopeIds = row.scopeId || this.ids;
      this.$modal.confirm(this.$t('scope.confirm.delete_select')).then(function() {
        return deleteScope(scopeIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('commons.msg.success.delete'));
      }).catch(() => {});
    },
    /** 用户状态修改 */
    handleStatusChange(row) {
      let text = row.scopeStatus === 1
          ? this.$t('scope.confirm.status_enable', { arg1: row.scopeName })
          : this.$t('scope.confirm.status_disable', { arg1: row.scopeName });
      this.$modal.confirm(text).then(function() {
        return updateScopeStatus(row.scopeId, row.scopeStatus);
      }).then(() => {
        this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
      }).catch(function() {
        row.scopeStatus = row.scopeStatus === 0 ? 1 : 0;
      });
    },
    /** 新增 */
    handleAdd() {
      this.form = {
        scopeName: undefined,
        scopeModule: undefined,
      };
      this.title = this.$t('scope.dialog.new');
      this.open = true;
    },
    /** 修改 */
    handleUpdate(row) {
      const scopeId = row.scopeId || this.ids;
      this.title = this.$t('scope.dialog.edit');
      intoScope(scopeId).then(response => {
        this.form = response.data;
        this.open = true;
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
          if (this.form.scopeId !== undefined) {
            editScope(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
              this.open = false;
              this.getList();
            });
          } else {
            addScope(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('commons.msg.success.create'));
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 详情 */
    handleView(row) {
      let viewName = "scope_view_" + row.scopeModule;
      try {
        this.infoView = this.getDialog(viewName)
        this.$nextTick(() => {
          this.$refs.infoRef.show(row)
        });
      } catch (error) {
        this.$modal.msgWarning("没有对应的权限设计模板");
      }
    },
    getDialog(viewName) {
      return require(`@/views/system/scope/detail/${viewName}.vue`).default;
    },
  }
};
</script>

