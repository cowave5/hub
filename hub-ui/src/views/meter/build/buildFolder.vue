<template>
  <div>
    <div class="head-container" style="display: flex; align-items: stretch; margin-bottom: 20px;">
      <el-input v-model="folderName" placeholder="搜索" @input="searchFolder"
                clearable size="small" prefix-icon="el-icon-search" style="flex: 1;">
      </el-input>
      <el-dropdown size="mini"
                   style="border: none; margin-left: 0; background: transparent; padding: 0; height: auto; cursor: pointer;"
                   class="more-dropdown" @command="(command) => handleCommand(command)">
            <span class="icon-wrapper">
              <svg-icon icon-class="more" style="height: 1.8em; width: 1.8em;"/>
            </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="handleFolderCreate">
            <svg-icon icon-class="folder"/>
            创建目录
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <div class="head-container">
      <el-tree ref="tree" :data="folderOptions" :props="defaultProps" :filter-node-method="filterNode"
               :expand-on-click-node="false" default-expand-all highlight-current
               draggable @node-drop="handleNodeDrop" node-key="id"
               @mouseleave.native="hoverFolderId = null" @node-click="handleNodeClick">
        <template #default="{ node, data }">
              <span style="display: flex; align-items: center; justify-content: space-between; width: 100%;"
                    @mouseenter="hoverFolderId = node.data.id">
                <span v-if="editingFolderId !== node.data.id">{{ node.label }}</span>
                <input v-else v-model="editingFolderName" :ref="'renameInput_' + node.data.id"
                       @blur="confirmFolderName(node)" @keyup.enter="$event.target.blur" style="width: 80%;"/>
                <el-dropdown trigger="click" class="more-dropdown" @command="(command) => handleCommand(command, node)"
                             size="mini"
                             style="border: none; margin-left: 0; background: transparent; padding: 0; height: auto; cursor: pointer;">
                  <span class="icon-wrapper2" v-show="hoverFolderId === node.data.id">
                    <svg-icon icon-class="more2"/>
                  </span>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="handleFolderCreate">
                      <svg-icon icon-class="folder"/> 创建目录
                    </el-dropdown-item>
                    <el-dropdown-item command="handleFolderDelete" icon="el-icon-delete"
                                      v-if="node.data.visibility === 0 || node.data.folderRole > 1">删除目录
                    </el-dropdown-item>
                    <el-dropdown-item command="handleFolderRename" icon="el-icon-edit"
                                      v-if="node.data.visibility === 0 || node.data.folderRole > 1">重命名
                    </el-dropdown-item>
                    <el-dropdown-item command="handleFolderVisibility"
                                      v-if="node.data.visibility === 0 || node.data.folderRole > 1">
                      <svg-icon icon-class="eye-open"/> 访问限制
                    </el-dropdown-item>
                    <el-dropdown-item command="handleFolderMembers"
                                      v-if="node.data.visibility === 1 && node.data.folderRole > 1">
                      <svg-icon icon-class="online"/> 成员设置
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </span>
        </template>
      </el-tree>
    </div>

    <!--  创建目录  -->
    <el-dialog title="创建目录" :visible.sync="folderOpen" width="700px" append-to-body>
      <el-form ref="folder" :model="folder" :rules="folderRules" @submit.native.prevent label-width="120px">
        <el-form-item label="目录名称" prop="folderName">
          <el-input v-model="folder.folderName"/>
        </el-form-item>
        <el-form-item label="可见性" prop="isDefault">
          <el-radio-group v-model="folder.visibility" :disabled="visibilityDisabled">
            <el-radio v-for="item in visibility" :key="item.value" :label="item.value">{{ item.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFolder">
          {{ $t('commons.button.confirm') }}
        </el-button>
        <el-button @click="cancelFolder">{{ $t('commons.button.cancel') }}</el-button>
      </div>
    </el-dialog>

    <!-- 修改访问限制 -->
    <el-dialog title="修改访问限制" :visible.sync="visibilityOpen" width="700px" append-to-body>
      <el-form ref="folder" :model="folderVisibility" :rules="folderRules" label-width="120px">
        <el-form-item label="目录名称" prop="folderName">
          <el-input v-model="folderVisibility.folderName" disabled="disabled"/>
        </el-form-item>
        <el-form-item label="可见性" prop="isDefault">
          <el-radio-group v-model="folderVisibility.visibility" :disabled="visibilityDisabled">
            <el-radio v-for="item in visibility" :key="item.value" :label="item.value">{{ item.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFolderVisibility">
          {{ $t('commons.button.confirm') }}
        </el-button>
        <el-button @click="cancelFolderVisibility">{{ $t('commons.button.cancel') }}</el-button>
      </div>
    </el-dialog>

    <!-- 目录成员 -->
    <el-drawer :visible.sync="membersOpen" direction="rtl" size="75%">
      <template #title>
        <h2 style="margin: 0;">[{{ folderMembers.folderName }}] 目录成员</h2>
      </template>
      <div class="app-container">
        <el-row :gutter="10" type="flex" justify="space-between" align="middle" style="margin: 0 5px;">
          <el-form ref="queryMembers" :model="membersParams" size="small" @submit.native.prevent :inline="true"
                   v-show="showSearch">
            <el-form-item :label="$t('user.label.name')" prop="userName">
              <el-input v-model="membersParams.userName" :placeholder="$t('user.placeholder.name')" clearable/>
            </el-form-item>
          </el-form>
          <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleSelectFolderMembers">
            添加成员
          </el-button>
        </el-row>

        <!--    成员列表    -->
        <el-table :data="folderMemberList.slice((membersParams.page-1)*membersParams.pageSize,membersParams.page*membersParams.pageSize)">
          <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="55">
            <template slot-scope="scope">
              <span>{{ (membersParams.page - 1) * membersParams.pageSize + scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('user.label.name')" prop="userName" align="center"/>
          <el-table-column label="成员角色" prop="userRole" align="center">
            <template slot-scope="scope">
              <el-select v-model="scope.row.userRole" @change="(val)=>handleMemberRole(val, scope.row)"
                         :disabled="scope.row.userCode === folderMembers.ownerCode">
                <el-option v-for="item in member_role" :key="item.value" :value="item.value" :label="item.label"/>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-circle-close"
                         @click="handleRemoveFolderMembers(scope.row)"
                         :disabled="scope.row.userCode === folderMembers.ownerCode">删除
              </el-button>
              <el-button size="mini" type="text" icon="el-icon-circle-close"
                         @click="handleTransferFolderMembers(scope.row)"
                         :disabled="scope.row.userCode === folderMembers.ownerCode">转让
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="folderMemberTotal>0" :total="folderMemberTotal"
                    :page.sync="membersParams.page" :limit.sync="membersParams.pageSize"/>
      </div>
    </el-drawer>

    <!-- 目录成员选择 -->
    <el-dialog title="选择目录成员" :visible.sync="membersSelectOpen" width="800px" append-to-body>
      <el-form ref="selectMembers" :model="membersQuery" size="small" @submit.native.prevent :inline="true"
               v-show="showSearch">
        <el-form-item :label="$t('user.label.name')" prop="userName">
          <el-input v-model="membersQuery.userName" :placeholder="$t('user.placeholder.name')"
                    clearable @keyup.enter.native="handleSelectQuery"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleSelectQuery">
            {{ $t('commons.button.search') }}
          </el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetSelectQuery">{{
              $t('commons.button.reset')
            }}
          </el-button>
        </el-form-item>
      </el-form>

      <!--   用户选项   -->
      <el-table ref="selectMembersTable" :data="selectMemberList" @selection-change="handleMemberSelectionChange"
                row-key="userCode" v-loading="loading" :header-cell-style="{'text-align':'center'}">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column :label="$t('user.label.name')" prop="userName" align="center"/>
        <el-table-column label="成员角色" prop="userRole" align="center">
          <template slot-scope="scope">
            <el-select v-model="scope.row.userRole" @change="handleMemberSelectionRole(scope.row)">
              <el-option v-for="item in member_role" :key="item.value" :value="item.value" :label="item.label"/>
            </el-select>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="selectMemberTotal>0" :total="selectMemberTotal"
                  :limit.sync="membersQuery.pageSize" :page.sync="membersQuery.page"
                  @pagination="querySelectMembers"/>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="confirmSelectFolderMembers">{{ $t('commons.button.confirm') }}</el-button>
        <el-button @click="cancelSelectFolderMembers">{{ $t('commons.button.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {member_role, visibility} from "@/utils/constants";
import {
  buildFolderDrag,
  buildFolderMembers,
  buildFolderMembersAdd,
  buildFolderMembersDelete,
  buildFolderMembersTransfer,
  buildFolderMembersUpdate,
  buildFolderRename, buildFolderSelectList,
  buildFolderTree,
  buildFolderVisibility,
  creatBuildFolder,
  deleteBuildFolder
} from "@/api/meter/build";
import {getUserMemberOptions} from "@/api/system/user";
export default {
  name: "BuildFolder",
  data() {
    return {
      visibility: visibility,
      member_role: member_role,
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      folderOpen: false,
      visibilityOpen: false,
      membersOpen: false,
      membersSelectOpen: false,
      folderName: undefined,
      folder: {},
      folderVisibility: {
        visibility: undefined,
      },
      folderMembers: {
        ownerCode: undefined,
      },
      selectMemberList: [],
      selectMemberTotal: 0,
      membersQuery: {
        page: 1,
        pageSize: 10,
      },
      originMemberList: [],
      membersParams: {
        page: 1,
        pageSize: 10,
      },
      membersMultiple: true,
      folderOptions: undefined,
      visibilityDisabled: false,
      backupFolderOptions: null,
      hoverFolderId: null,
      editingFolderId: null,
      editingFolderName: '',
      currentFolderId: null,
      currentFolderVisibility: null,
      defaultProps: {
        children: "children",
        label: "label"
      },
    }
  },
  created() {
    this.getFolderOptions();
  },
  computed: {
    folderRules() {
      return {
        folderName: [
          { required: true, message: "目录名称不能为空", trigger: "blur" }
        ],
      }
    },
    folderMemberList() {
      if (!this.membersParams.userName) {
        return this.originMemberList;
      }
      return this.originMemberList.filter(item =>
        item.userName.includes(this.membersParams.userName)
      );
    },
    folderMemberTotal() {
      return this.folderMemberList.length;
    }
  },
  methods: {
    searchFolder(val) {
      this.$refs.tree.filter(val);
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    /** 选择目录 */
    handleNodeClick(data, node, component) {
      this.currentFolderId = data.id;
      this.currentFolderRole = data.folderRole;
      this.currentFolderVisibility = data.visibility;
      this.$emit('folder-click', {
        folderId: data.id,
        folderRole: data.folderRole,
      });
    },
    /** 获取目录树 */
    getFolderOptions() {
      buildFolderTree().then(resp => {
        this.folderOptions = resp.data;
        this.backupFolderOptions = JSON.parse(JSON.stringify(resp.data));
        if (!resp.data || resp.data.length === 0) {
          this.currentFolderId = null;
        } else if (this.currentFolderId == null) {
          const firstNodeId = resp.data[0].id;
          const firstNodeRole = resp.data[0].folderRole;
          this.$nextTick(() => {
            this.$refs.tree.setCurrentKey(firstNodeId);
            this.currentFolderId = firstNodeId;
          });
          this.$emit('folder-click', {
            folderId: firstNodeId,
            folderRole: firstNodeRole,
          });
        } else {
          this.$nextTick(() => {
            this.$refs.tree.setCurrentKey(this.currentFolderId);
          });
        }
      });
    },
    /** 目录操作 */
    handleCommand(command, node) {
      switch (command) {
        case "handleFolderCreate":
          this.handleFolderCreate(node);
          break;
        case "handleFolderDelete":
          this.handleFolderDelete(node);
          break;
        case "handleFolderRename":
          this.handleFolderRename(node);
          break;
        case "handleFolderVisibility":
          this.handleFolderVisibility(node);
          break;
        case "handleFolderMembers":
          this.handleFolderMembers(node);
          break;
        default:
          break;
      }
    },
    /** 目录拖拽 */
    handleNodeDrop(draggingNode, dropNode, type, event) {
      const dropData = dropNode.data;
      let parentId = null;
      let childrenIds = [];
      if (type === 'inner') {
        parentId = dropData.id;
        const subNodes = dropData.children || [];
        childrenIds = subNodes.map(item => item.id);
      } else if (type === 'before' || type === 'after') {
        parentId = dropData.pid;
        if(parentId === 0){
          childrenIds = this.folderOptions.map(item => item.id);
        } else {
          const subNodes = dropNode.parent.data.children || [];
          childrenIds = subNodes.map(item => item.id);
        }
      }
      const folderDrag = {
        parentId: parentId,
        folderId: draggingNode.data.id,
        childrenIds: childrenIds
      };
      buildFolderDrag(folderDrag).then(() => {
      }).catch(() => {
        this.folderOptions = this.backupFolderOptions;
      });
    },
    /** 创建目录 */
    handleFolderCreate(node){
      this.folder = {
        parentId: node ? node.data.id : 0,
        visibility: node ? node.data.visibility : 0,
        folderName: undefined
      };
      //如果当前目录是private，则下级目录只能是private
      this.visibilityDisabled = this.folder.visibility === 1;
      this.folderOpen = true;
    },
    /** 确认创建目录 */
    submitFolder: function() {
      this.$refs["folder"].validate(valid => {
        if (valid) {
          creatBuildFolder(this.folder).then(() => {
            this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
            this.folderOpen = false;
            this.getFolderOptions();
          });
        }
      });
    },
    /** 取消创建目录 */
    cancelFolder() {
      this.folderOpen = false;
    },
    /** 修改目录名称 */
    handleFolderRename(node){
      this.editingFolderId = node.data.id;
      this.editingFolderName = node.data.label;
      this.$nextTick(() => {
        const input = this.$refs['renameInput_' + node.data.id];
        if (input) input.focus();
      });
    },
    /** 确认修改目录名称 */
    async confirmFolderName(node) {
      const folderRename = {
        folderId: this.editingFolderId,
        folderName: this.editingFolderName
      };
      try {
        await buildFolderRename(folderRename);
        node.data.label = this.editingFolderName;
      } catch (e) {
        console.log("folder重命名失败", e)
      }
      this.editingFolderId = undefined;
      this.editingFolderName = '';
    },
    /** 修改访问限制 */
    handleFolderVisibility(node){
      this.folderVisibility.folderId = node.data.id;
      this.folderVisibility.folderName = node.data.label;
      this.folderVisibility.visibility = node.data.visibility;
      this.folderVisibility.nodeVisibility = node.data.visibility;
      // 如果上级是private，则不允许修改
      let parent = node.parent;
      this.visibilityDisabled = parent != null && parent.data.visibility === 1;
      this.visibilityOpen = true;
    },
    /** 确认修改访问限制 */
    submitFolderVisibility(){
      if(this.folderVisibility.visibility === this.folderVisibility.nodeVisibility){
        this.visibilityOpen = false;
        return;
      }
      buildFolderVisibility(this.folderVisibility).then(() => {
        this.getFolderOptions();
        this.visibilityOpen = false;
      }).catch(() => {
        console.log("修改访问限制失败");
      });
    },
    /** 取消修改访问限制 */
    cancelFolderVisibility(){
      this.visibilityOpen = false;
    },
    /** 删除目录 */
    handleFolderDelete(node) {
      this.$modal.confirm('确认删除目录及下面所有内容？').then(function () {
        return deleteBuildFolder(node.data.id);
      }).then(() => {
        if (node.data.id === this.currentFolderId) {
          this.currentFolderId = null;
          this.getFolderOptions();
        }
      }).catch(err => {
        console.log("删除目录失败", err);
      });
    },
    /** 设置目录成员 */
    handleFolderMembers(node){
      this.folderMembers.folderId = node.data.id;
      this.folderMembers.folderName = node.data.label;
      this.folderMembers.ownerCode = node.data.ownerCode;
      // 成员查询
      let parent = node.parent;
      this.membersParams.parentPrivate = parent != null && parent.data.visibility === 1;
      this.membersParams.parentId = parent != null ? parent.data.id : 0;
      buildFolderMembers(node.data.id).then(response => {
        this.originMemberList = response.data.list;
      });
      this.membersOpen = true;
    },
    /** 设置成员角色 */
    handleMemberRole(val, row){
      const data = {
        folderId: this.folderMembers.folderId,
        userCode: row.userCode,
        userRole: val
      }
      buildFolderMembersUpdate(data).then(() => {});
    },
    /** 转让目录 */
    handleTransferFolderMembers(row){
      const data = {
        folderId: this.folderMembers.folderId,
        userCode: row.userCode,
        userName: row.userName,
      }
      buildFolderMembersTransfer(data).then(() => {
        this.$set(this.folderMembers, 'ownerCode', row.userCode);
        this.$set(row, 'userRole', 2);
        this.getFolderOptions();
      });
    },
    /** 删除目录成员 */
    handleRemoveFolderMembers(row) {
      const data = {
        folderId: this.folderMembers.folderId,
        userCode: row.userCode,
      }
      buildFolderMembersDelete(data).then(() => {
        buildFolderMembers(this.folderMembers.folderId).then(response => {
          this.originMemberList = response.data.list;
        });
      });
    },
    /** 选择目录成员 */
    handleSelectFolderMembers(){
      this.handleSelectQuery();
      this.membersSelectOpen = true;
    },
    /** 查询选择成员 */
    handleSelectQuery() {
      this.membersQuery.page = 1;
      this.selectMemberList = [];
      if(this.membersParams.parentPrivate) {
        // 从上级目录成员中选择
        this.querySelectMembers();
      } else {
        // 从user库中选择
        this.membersQuery.userCodes = this.originMemberList.map(member => member.userCode);
        getUserMemberOptions(this.membersQuery).then(response => {
          this.selectMemberList = response.data.list;
          this.selectMemberTotal = response.data.total;
          this.loading = false;
        });
      }
    },
    /** 查询选择成员 */
    querySelectMembers() {
      this.membersQuery.parentId = this.membersParams.parentId;
      this.membersQuery.folderId = this.folderMembers.folderId;
      buildFolderSelectList(this.membersQuery).then(response => {
        this.selectMemberList = response.data.list
        this.selectMemberTotal = response.data.total;
        this.loading = false;
      });
    },
    /** 查询选择成员 */
    resetSelectQuery() {
      this.$refs.selectMembers.resetFields();
      this.handleSelectQuery();
    },
    /** 选择成员 */
    handleMemberSelectionChange(selectedRows){
      this.folderMembers.members = selectedRows.map(row => ({
        userCode: row.userCode,
        userName: row.userName,
        userRole: row.userRole
      }));
    },
    /** 选择成员角色 */
    handleMemberSelectionRole(row) {
      const selectedRows = this.$refs.selectMembersTable.selection || [];
      this.handleMemberSelectionChange(selectedRows);
    },
    /** 确认选择目录成员 */
    confirmSelectFolderMembers(){
      buildFolderMembersAdd(this.folderMembers).then(() => {
        buildFolderMembers(this.folderMembers.folderId).then(response => {
          this.originMemberList = response.data.list;
        });
      });
      this.membersSelectOpen = false;
    },
    /** 取消选择目录成员 */
    cancelSelectFolderMembers(){
      this.membersSelectOpen = false;
    },
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.icon-wrapper {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 4px 0px;
  border-radius: 4px;
}

.icon-wrapper2 {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 4px 6px;
  border-radius: 4px;
}

.icon-wrapper:hover {
  background-color: rgb(238, 237, 239);
}

.icon-wrapper2:hover {
  background-color: rgb(238, 237, 239);
}
</style>
