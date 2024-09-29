<template>
  <div class="app-container">
    <!--  筛选栏  -->
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" v-show="showSearch">
      <el-form-item :label="$t('role.label.name')" prop="roleName">
        <el-input v-model="queryParams.roleName" :placeholder="$t('role.placeholder.name')"
                  clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item :label="$t('role.label.code')" prop="roleCode">
        <el-input v-model="queryParams.roleCode" :placeholder="$t('role.placeholder.code')"
                  clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" size="mini" icon="el-icon-search"
                   @click="handleQuery">{{$t('commons.button.search')}}</el-button>
        <el-button size="mini" icon="el-icon-refresh"
                   @click="resetQuery">{{$t('commons.button.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <!--  操作栏  -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain size="mini" icon="el-icon-plus" @click="handleAdd"
                   :disabled="!checkPermit(['sys:role:create'])">
          {{$t('commons.button.create')}}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain size="mini" icon="el-icon-edit" @click="handleUpdate"
                   :disabled="single || !checkPermit(['sys:role:edit'])">
          {{$t('commons.button.edit')}}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain size="mini" icon="el-icon-delete" @click="handleDelete"
                   :disabled="multiple || !checkPermit(['sys:role:delete'])">
          {{$t('commons.button.delete')}}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain size="mini" icon="el-icon-download" @click="handleExport"
                   :disabled="!checkPermit(['sys:role:export'])">
          {{$t('commons.button.export')}}
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :cols="cols"/>
    </el-row>

    <!--  列表数据  -->
    <el-table :data="list" v-loading="loading" @selection-change="handleSelectionChange">
      <el-table-column :selectable='selectable' type="selection" align="center" width="60" />
      <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="60" >
        <template slot-scope="scope">
          <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="cols[0].show" :label="$t('role.label.name')" prop="roleName" align="center" :show-overflow-tooltip="true"/>
      <el-table-column v-if="cols[1].show" :label="$t('role.label.code')" prop="roleCode" align="center" :show-overflow-tooltip="true"/>
      <el-table-column v-if="cols[2].show" :label="$t('commons.label.createTime')" prop="createTime" align="center">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="cols[3].show" :label="$t('commons.label.updateTime')" prop="updateTime" align="center">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding fixed-width" width="350">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">
            {{$t('commons.button.edit')}}
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     :disabled="scope.row.roleCode === 'sysAdmin' || !checkPermit(['sys:role:delete'])">
            {{$t('commons.button.delete')}}
          </el-button>
          <el-button size="mini" type="text" @click="handleMenuScope(scope.row)">
            <svg-icon icon-class="pscope"/>{{$t('role.button.menus')}}
          </el-button>
          <el-button size="mini" type="text" @click="handleDataScope(scope.row)"
                     :disabled="!checkPermit(['sys:role:scope'])">
            <svg-icon icon-class="vscope"/> {{$t('role.button.scope')}}
          </el-button>
          <el-button size="mini" type="text" @click="handleAuthUser(scope.row)">
            <svg-icon icon-class="peoples"/> {{$t('role.button.members')}}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 新增/修改 -->
    <el-dialog v-drag :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item :label="$t('role.label.name')" prop="roleName">
          <el-input v-model="form.roleName" :placeholder="$t('role.placeholder.name')" />
        </el-form-item>
        <el-form-item :label="$t('role.label.code')" prop="roleCode">
          <el-input v-model="form.roleCode" :placeholder="$t('role.placeholder.code')" />
        </el-form-item>
        <el-form-item :label="$t('commons.label.remark')" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="..."></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm"
                   :disabled="form.roleCode === 'sysAdmin' || !checkPermit(['sys:role:edit'])">
          {{$t('commons.button.confirm')}}
        </el-button>
        <el-button @click="cancel">{{$t('commons.button.cancel')}}</el-button>
      </div>
    </el-dialog>

    <!-- 菜单权限 -->
    <el-dialog v-drag :title="$t('role.button.menus')" :visible.sync="openMenuScope" width="700px" append-to-body>
      <el-form ref="menuForm" :model="menuForm" label-width="100px">
        <el-row>
          <el-col :span="8" :offset="0">
            <el-form-item :label="$t('role.label.name')" prop="roleName">
              <el-input  v-model="menuForm.roleName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="8" :offset="2">
            <el-form-item :label="$t('role.label.code')" prop="roleCode">
              <el-input v-model="menuForm.roleCode" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item :label="$t('menu.button.select')">
          <el-checkbox v-model="menuExpand" @change="handleCheckedTreeExpand($event, 'menu')">
            {{$t('commons.button.expand')}}/{{$t('commons.button.collapse')}}
          </el-checkbox>
          <el-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll($event, 'menu')">
            {{$t('commons.button.check')}}
          </el-checkbox>
          <el-checkbox v-model="menuCheckStrictly" @change="handleCheckedTreeConnect($event, 'menu')">
            {{$t('commons.button.parent')}}
          </el-checkbox>
          <el-tree ref="menuTree" :data="menuOptions" node-key="id" class="tree-border" show-checkbox
                   :check-strictly="!menuCheckStrictly" empty-text="..." :props="defaultProps">
            <span class="el-tree-node__label" slot-scope="{data}" style="display: flex; justify-content: space-between; width: 100%;">
              {{ $t(data.label) }}
              <el-select v-if="data.menuType === 'B'" v-model="data.scope" placeholder="选择数据权限" size="small" class="tree-node-select">
                <el-option v-for="option in dataPermits" :key="option.value" :label="option.label" :value="option.value"/>
              </el-select>
            </span>
          </el-tree>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitMenuScope"
                   :disabled="form.roleCode === 'sysAdmin' || !checkPermit(['sys:role:menus'])">
          {{$t('commons.button.confirm')}}
        </el-button>
        <el-button @click="cancelMenuScope">{{$t('commons.button.cancel')}}</el-button>
      </div>
    </el-dialog>

    <!-- 数据权限对话框 -->
    <el-dialog v-drag :title="$t('role.button.scope')" :visible.sync="openDataScope" width="500px" append-to-body>
      <span>Todo in the future.</span>
<!--      <el-form :model="form" label-width="100px">-->
<!--        <el-form-item :label="$t('role.label.name')">-->
<!--          <el-input v-model="form.roleName" :disabled="true" />-->
<!--        </el-form-item>-->
<!--        <el-form-item :label="$t('role.label.code')">-->
<!--          <el-input v-model="form.roleCode" :disabled="true" />-->
<!--        </el-form-item>-->
<!--      </el-form>-->
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitDataScope">{{$t('commons.button.confirm')}}</el-button>
        <el-button @click="cancelDataScope">{{$t('commons.button.cancel')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getRoleList,
  getRoleInfo,
  delRole,
  addRole,
  updateRole,
  updateRoleMenus
} from '@/api/system/role'
import {getMenuTree} from "@/api/system/menu";
import {checkPermit} from "@/utils/permission";

export default {
  name: "Role",
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
      // 列表数据
      list: [],
      // 弹出层标题
      title: "",
      // 新增修改对话框
      open: false,
      // 数据权限对话框
      openDataScope: false,
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        roleName: undefined,
        roleCode: undefined
      },
      // 表单参数
      form: {
        roleId: undefined,
        roleName: undefined,
        roleCode: undefined,
        remark: undefined,

        menuIds: [],
      },
      // 菜单权限对话框
      openMenuScope: false,
      menuOptions: [],
      menuExpand: false,
      menuNodeAll: false,
      menuCheckStrictly: true,
      defaultProps: {
        children: "children",
        label: "label"
      },
      menuForm: {
        roleId: undefined,
        roleName: undefined,
        roleCode: undefined,
        menuIds: [],
      },
      cols: [
        {key: 0, label: 'role.label.name', show: true},
        {key: 1, label: 'role.label.code', show: true},
        {key: 2, label: 'commons.label.createTime', show: true},
        {key: 3, label: 'commons.label.updateTime', show: false},
      ],
      dataPermits: [
        { value: 'option1', label: '选项1' },
        { value: 'option2', label: '选项2' }
      ]
    };
  },
  created() {
    this.getList();
  },
  computed: {
    rules() {
      return {
        roleName: [
          { required: true, message: this.$t('role.rules.name'), trigger: "blur" }
        ],
        roleCode: [
          { required: true, message: this.$t('role.rules.code'), trigger: "blur" }
        ],
      };
    }
  },
  methods: {
    checkPermit,
    selectable(row){
      return row.roleCode !== 'sysAdmin';
    },
    /** 搜索 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置 */
    resetQuery() {
      this.$refs.queryForm.resetFields();
      this.handleQuery();
    },
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.roleId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 角色列表 */
    getList() {
      this.loading = true;
      getRoleList(this.queryParams).then(response => {
          this.list = response.data.list;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    /** 新增 */
    handleAdd() {
      this.form = {
        roleId: undefined,
        roleName: undefined,
        roleCode: undefined,
        remark: undefined
      };
      this.title = this.$t('role.dialog.new');
      this.open = true;
    },
    /** 修改 */
    handleUpdate(row) {
      const roleId = row.roleId || this.ids
      getRoleInfo(roleId).then(response => {
        this.form = response.data;
        this.title = this.$t('role.dialog.edit');
        this.open = true;
      });
    },
    /** 删除 */
    handleDelete(row) {
      const roleIds = row.roleId || this.ids;
      const msg = row.roleId
          ? this.$t('role.confirm.delete', { arg1: row.roleName })
          : this.$t('role.confirm.delete_select');
      this.$modal.confirm(msg).then(function() {
        return delRole(roleIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('commons.msg.success.delete'));
      }).catch(() => {});
    },
    /** 导出 */
    handleExport() {
      this.download('/admin/api/v1/role/export', {}, `role_${new Date().getTime()}.xlsx`)
    },
    /** 新增修改取消 */
    cancel() {
      this.open = false;
    },
    /** 新增修改提交 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.roleId !== undefined) {
            updateRole(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
              this.open = false;
              this.getList();
            });
          } else {
            addRole(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('commons.msg.success.create'));
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 角色菜单 */
    handleMenuScope(row){
      this.menuExpand = false,
      this.menuNodeAll = false,
      getMenuTree().then(response => {
        this.menuOptions = response.data;
        getRoleInfo(row.roleId).then(response => {
          this.menuForm.roleId = response.data.roleId;
          this.menuForm.roleName = response.data.roleName;
          this.menuForm.roleCode = response.data.roleCode;
          // 选中
          let menuIds = response.data.menuIds;
          menuIds.forEach((v) => {
            this.$nextTick(()=>{
              this.$refs.menuTree.setChecked(v, true ,false);
            })
          })
        });
      });
      this.openMenuScope = true;
    },
    /** 角色菜单取消 */
    cancelMenuScope(){
      this.openMenuScope = false;
    },
    /** 角色菜单提交 */
    submitMenuScope(){
      this.menuForm.menuIds = this.getCheckedMenus();
      updateRoleMenus(this.menuForm).then(() => {
        this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
        this.openMenuScope = false;
        this.getList();
      });
    },
    /** 选中的菜单 */
    getCheckedMenus() {
      // 目前被选中的节点
      let checkedKeys = this.$refs.menuTree.getCheckedKeys();
      // 半选中的节点
      let halfCheckedKeys = this.$refs.menuTree.getHalfCheckedKeys();
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
      // let checkedMenus = checkedKeys.map(id => {
      //   let node = this.findNodeById(id, this.menuOptions) // 查找对应节点
      //   return {
      //     id: id,
      //     scope: node ? node.scope || '' : '' // 确保 scope 有值
      //   }
      // })
      return checkedKeys;
    },
    findNodeById(id, nodes) {
      for (let node of nodes) {
        if (node.id === id) return node
        if (node.children) {
          let found = this.findNodeById(id, node.children)
          if (found) return found
        }
      }
      return null
    },
    /** 展开/折叠 */
    handleCheckedTreeExpand(value, type) {
      let treeList = this.menuOptions;
      for (let i = 0; i < treeList.length; i++) {
        this.$refs.menuTree.store.nodesMap[treeList[i].id].expanded = value;
      }
    },
    /** 全选/全不选 */
    handleCheckedTreeNodeAll(value, type) {
      this.$refs.menuTree.setCheckedNodes(value ? this.menuOptions: []);
    },
    /** 父子联动 */
    handleCheckedTreeConnect(value, type) {
      this.menuCheckStrictly = value ? true: false;
    },
    /** 数据权限 */
    handleDataScope(row) {
      getRoleInfo(row.roleId).then(response => {
        this.openDataScope = true;
        // this.$nextTick(() => {
        //   roleDeptTreeselect.then(res => {
        //     this.$refs.dept.setCheckedKeys(res.checkedKeys);
        //   });
        // });
      });
    },
    /** 数据权限取消 */
    cancelDataScope() {
      this.openDataScope = false;
    },
    /** 数据权限提交 */
    submitDataScope: function() {
      this.openDataScope = false;

    },
    /** 分配用户 */
    handleAuthUser: function(row) {
      const roleId = row.roleId;
      this.$router.push("/system/role-auth/user/" + roleId);
    },
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.tree-node-select {
  ::v-deep .el-input__inner {
    height: 20px;
    border: none;
    box-shadow: none;
  }
}
</style>
