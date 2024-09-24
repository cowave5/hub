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

      <!-- 筛选条件 -->
      <el-col :span="20" :xs="24">
        <el-form :model="params" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="auto">
          <el-form-item :label="$t(`user.label.name`)" prop="userName">
            <el-input v-model="params.userName" :placeholder="$t(`user.placeholder.name`)" clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
          </el-form-item>
          <el-form-item :label="$t(`user.label.phone`)" prop="userPhone">
            <el-input v-model="params.userPhone" :placeholder="$t(`user.placeholder.phone`)" clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
          </el-form-item>
          <el-form-item :label="$t(`user.label.rank`)" prop="roleId">
            <el-select v-model="params.rank" :placeholder="$t(`user.placeholder.rank`)" style="width: 230px;">
              <el-option v-if="$i18n.locale==='zh'" v-for="dict in dict.type.post_level" :key="dict.value" :label="`${dict.value}/${dict.label}`" :value="dict.value"/>
              <el-option v-if="$i18n.locale==='en'" v-for="dict in dict.type.post_level" :key="dict.value" :label="`${dict.value}/${dict.labelEn}`" :value="dict.value"/>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{$t('button.search')}}</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('button.reset')}}</el-button>
          </el-form-item>
        </el-form>

        <!-- 操作栏 -->
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                       :disabled="!checkPermit(['sys:user:new'])">{{$t('route.system.user.new')}}</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="el-icon-edit" size="mini" @click="handleUpdate"
                       :disabled="single || !checkPermit(['sys:user:edit'])">{{$t('route.system.user.edit')}}</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete"
                       :disabled="multiple || !checkPermit(['sys:user:delete'])">{{$t('route.system.user.delete')}}</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="el-icon-upload2" size="mini" @click="handleImport"
                       :disabled="!checkPermit(['sys:user:import'])">{{$t('route.system.user.import')}}</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                       :disabled="!checkPermit(['sys:user:export'])">{{$t('route.system.user.export')}}</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="primary" plain size="mini" @click="showDiagram"
                       :disabled="!checkPermit(['sys:user:diagram'])"><svg-icon icon-class="tree"/>{{$t('route.system.user.diagram')}}</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="el-icon-refresh" size="mini" @click="handleRefreshCache"
                       :disabled="!checkPermit(['sys:user:cache'])">{{$t('route.system.user.cache')}}</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :columns="columns"></right-toolbar>
        </el-row>

        <!-- Table页 -->
        <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange" :header-cell-style="{'text-align':'center'}">
          <el-table-column type="selection" align="center" width="50"/>
          <el-table-column :label="$t(`label.index`)" type="index" align="center" width="55">
            <template slot-scope="scope">
              <span>{{(params.page - 1) * params.pageSize + scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t(`user.label.id`)" align="center" key="userId" prop="userId" v-if="columns[0].visible" />
          <el-table-column :label="$t(`user.label.name`)" align="center" key="userName" prop="userName" v-if="columns[1].visible" :show-overflow-tooltip="true" />
          <el-table-column :label="$t(`user.label.account`)" align="center" key="userAccount" prop="userAccount" v-if="columns[2].visible" :show-overflow-tooltip="true" width="100" />
          <el-table-column :label="$t(`user.label.sex`)" align="center" v-if="columns[3].visible">
            <template slot-scope="{row: {userSex}}">
              <template v-for="item in dict.type.user_sex">
                <span v-if="userSex === item.value && $i18n.locale==='zh'">{{ item.label }}</span>
                <span v-if="userSex === item.value && $i18n.locale==='en'">{{ item.labelEn }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column :label="$t(`user.label.phone`)" align="left" key="userPhone" prop="userPhone" v-if="columns[4].visible" width="110" />
          <el-table-column :label="$t(`user.label.email`)" align="left" key="userEmail" prop="userEmail" v-if="columns[5].visible" :show-overflow-tooltip="true" width="150" />
          <el-table-column :label="$t(`user.label.dept`)" align="left" v-if="columns[6].visible" :show-overflow-tooltip="true" width="160" >
            <template slot-scope="scope">
              <template v-if="scope.row.deptList.length>0" >
                <template v-for="item in scope.row.deptList">
                  <template v-if="item.isDefault === 1">
                    <div style="color: #004d8c">{{ item.deptName + '/' + (item.postName == null ? '' : item.postName) }}</div>
                  </template>
                  <template v-else>
                    <div>{{ item.deptName + '/' + (item.postName == null ? '' : item.postName) }}</div>
                  </template>
                </template>
              </template>
              <template v-else-if="scope.row.isLdap === 1">
                <div>{{ scope.row.userDept + '/' + scope.row.userPost }}</div>
              </template>
            </template>
          </el-table-column>
          <el-table-column :label="$t(`user.label.rank`)" align="center" v-if="columns[7].visible" :show-overflow-tooltip="true" width="140">
            <template slot-scope="{row: {rank}}">
              <template v-for="item in dict.type.post_level">
                <span v-if="rank === item.value && $i18n.locale==='zh'">{{ item.value }}/{{ item.label }}</span>
                <span v-if="rank === item.value && $i18n.locale==='en'">{{ item.value }}/{{ item.labelEn }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column :label="$t(`user.label.status`)" align="center" prop="userStatus"  v-if="columns[8].visible" width="60">
            <template slot-scope="scope">
              <el-switch v-if="checkPermit(['sys:user:status'])" v-model="scope.row.userStatus" :active-value=1 :inactive-value=0 @change="handleStatusChange(scope.row)"/>
              <el-switch v-else disabled v-model="scope.row.userStatus" :active-value=1 :inactive-value=0 @change="handleStatusChange(scope.row)"/>
            </template>
          </el-table-column>
          <el-table-column :label="$t(`label.readonly`)" align="center" prop="readOnly">
            <template slot-scope="scope">
              <el-switch :disabled="!checkPermit(['sys:common:readonly'])" v-model="scope.row.readOnly" :active-value=1 :inactive-value=0 @change="handleReadOnly(scope.row)"/>
            </template>
          </el-table-column>
          <el-table-column :label="$t(`label.option`)" align="center" width="160" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">{{$t('route.system.user.edit')}}</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                         :disabled="scope.row.readOnly === 1 || !checkPermit(['sys:user:delete'])">{{$t('route.system.user.delete')}}</el-button>
              <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)">
                <span class="el-dropdown-link">
                  <i class="el-icon-d-arrow-right el-icon--right"></i>{{$t('button.more')}}
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="handleResetPwd" icon="el-icon-key"
                                    :disabled="scope.row.readOnly === 1 || !checkPermit(['sys:user:passwd'])">{{$t('route.system.user.passwd')}}</el-dropdown-item>
                  <el-dropdown-item command="handleAuthRole" icon="el-icon-circle-check"
                                    :disabled="scope.row.readOnly === 1 || !checkPermit(['sys:user:grant'])">{{$t('route.system.user.grant')}}</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total>0" :total="total" :page.sync="params.page" :limit.sync="params.pageSize" @pagination="getList"/>
      </el-col>
    </el-row>

    <!-- 新增编辑对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="750px" append-to-body >
      <el-tabs v-model="activeTab">
        <el-tab-pane label="基本信息" name="basic">
          <el-form ref="form" :model="form" :rules="rules" label-width="110px" style="height: 420px">
            <el-row>
              <el-col :span="12">
                <el-form-item :label="$t(`user.label.account`)" prop="userAccount">
                  <el-input v-model="form.userAccount" :placeholder="$t(`user.placeholder.account`)" maxlength="30" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t(`user.label.name`)" prop="userName">
                  <el-input v-model="form.userName" :placeholder="$t(`user.placeholder.name`)" maxlength="30" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item v-if="form.userId === undefined" :label="$t(`user.label.passwd`)" prop="userPasswd">
                  <el-input v-model="form.userPasswd" :placeholder="$t(`user.placeholder.passwd`)" type="password" maxlength="20" show-password/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item v-if="form.userId === undefined" :label="$t(`user.label.status`)">
                  <el-radio-group v-model="form.userStatus">
                    <el-radio v-if="$i18n.locale==='zh'" v-for="dict in dict.type.sys_is_enable" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
                    <el-radio v-if="$i18n.locale==='en'" v-for="dict in dict.type.sys_is_enable" :key="dict.value" :label="dict.value">{{dict.labelEn}}</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item :label="$t(`user.label.phone`)" prop="userPhone">
                  <el-input v-model="form.userPhone" :placeholder="$t(`user.placeholder.phone`)" maxlength="11" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t(`user.label.email`)" prop="email">
                  <el-input v-model="form.userEmail" :placeholder="$t(`user.placeholder.email`)" maxlength="50" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item :label="$t(`user.label.rank`)">
                  <el-select v-model="form.rank" :placeholder="$t(`user.placeholder.rank`)" style="width: 230px;">
                    <el-option v-if="$i18n.locale==='zh'" v-for="dict in dict.type.post_level" :key="dict.value" :label="`${dict.value}/${dict.label}`" :value="dict.value"/>
                    <el-option v-if="$i18n.locale==='en'" v-for="dict in dict.type.post_level" :key="dict.value" :label="`${dict.value}/${dict.labelEn}`" :value="dict.value"/>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t(`user.label.sex`)">
                  <el-select v-model="form.userSex" :placeholder="$t(`user.placeholder.sex`)" style="width: 230px;">
                    <el-option v-if="$i18n.locale==='zh'" v-for="dict in dict.type.user_sex" :key="dict.value" :label="dict.label" :value="dict.value"/>
                    <el-option v-if="$i18n.locale==='en'" v-for="dict in dict.type.user_sex" :key="dict.value" :label="dict.labelEn" :value="dict.value"/>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item :label="$t(`user.label.role`)">
                  <el-select v-model="form.roleIds" multiple :placeholder="$t(`user.placeholder.role`)" style="width: 560px;">
                    <el-option v-for="item in roleOptions" :key="item.roleId" :label="item.roleName" :value="item.roleId"/>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item :label="$t(`user.label.dept`)" prop="deptId">
                  <treeselect v-model="form.deptPostIds" :options="postOptions" :multiple="true" :disable-branch-nodes="true"
                              :placeholder="$t(`user.placeholder.dept_choose`)">
                    <div slot="value-label" slot-scope="{ node }">{{ node.raw.content }}</div>
                  </treeselect>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item :label="$t(`user.label.report`)">
                  <treeselect v-model="form.parentIds" :options="userOptions" :multiple="true" :disable-branch-nodes="true"
                              :placeholder="$t(`user.placeholder.report`)" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item :label="$t(`label.remark`)">
                  <el-input v-model="form.remark" type="textarea" placeholder="..."/>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>
        <el-tab-pane v-if="form.userType === 1" label="Ldap信息" name="ldap">
          <el-form ref="ldapForm" :model="ldapForm" label-width="110px" style="height: 420px">
            <el-row>
              <el-col :span="12">
                <el-form-item :label="$t(`user.label.account`)">
                  <el-input v-model="ldapForm.userAccount" disabled="disabled" maxlength="30" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t(`user.label.name`)">
                  <el-input v-model="ldapForm.userName" disabled="disabled" maxlength="30" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item :label="$t(`user.label.phone`)">
                  <el-input v-model="ldapForm.userPhone" disabled="disabled" maxlength="11" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t(`user.label.email`)">
                  <el-input v-model="ldapForm.userEmail" disabled="disabled" maxlength="50" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item label="用户部门">
                  <el-input v-model="ldapForm.userDept" disabled="disabled" maxlength="11" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="用户岗位">
                  <el-input v-model="ldapForm.userPost" disabled="disabled" maxlength="50" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="汇报对象">
                  <el-input v-model="ldapForm.userLeader" disabled="disabled" maxlength="11" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item label="用户DN">
                  <el-input v-model="ldapForm.userInfo" disabled="disabled" maxlength="11" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>
      </el-tabs>
      <div v-if="activeTab === 'basic'" slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm"
                   :disabled="form.readOnly === 1 || !checkPermit(['sys:user:edit'])">{{$t('button.confirm')}}</el-button>
        <el-button @click="cancel">{{$t('button.cancel')}}</el-button>
      </div>
    </el-dialog>

    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">{{$t('user.dialog.text_import1')}}<em>{{$t('user.dialog.text_import2')}}</em>{{$t('user.dialog.text_import3')}}</div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" /> {{$t('user.dialog.text_import4')}}
          </div>
          <span>{{$t('user.dialog.text_import5')}}</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">{{$t('user.dialog.text_import6')}}</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">{{$t('button.confirm')}}</el-button>
        <el-button @click="upload.open = false">{{$t('button.cancel')}}</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="$t(`user.dialog.diagram`)" :visible.sync="diagramOpen" width="80%" append-to-body>
      <div class="dialog-content">
        <organization-chart :datasource="diagramData">
          <template slot-scope="{ nodeData }">
            <div class="title">{{nodeData.label}}</div>
            <div class="content">{{nodeData.content}}</div>
          </template>
        </organization-chart>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addUser,
  changeStatus,
  delUser,
  getPosts,
  getRoles,
  getUser,
  getUsers,
  listUser,
  resetUserPwd,
  updateUser,
  getTree,
  refreshCache,
  changeReadonly
} from "@/api/system/user";
import {treeselect} from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import OrganizationChart from 'vue-organization-chart'
import 'vue-organization-chart/dist/orgchart.css'
import {checkPermit} from "@/utils/permission";
import {infoUser} from "@/api/system/ldap";
import cache from "@/plugins/cache";

export default {
  name: "User",
  dicts: ['sys_is_enable', 'user_sex', 'post_level'],
  components: { Treeselect, OrganizationChart },
  data() {
    return {
      activeTab: 'basic',
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
      // 用户表格数据
      userList: null,
      // 弹出层标题
      title: "",
      // 部门选项
      deptOptions: undefined,
      // 角色选项
      roleOptions: [],
      // 岗位选项
      postOptions: [],
      // 用户选项
      userOptions: [],
      // 是否显示弹出层
      open: false,
      // 部门名称
      deptName: undefined,
      // 默认密码
      initPassword: undefined,
      // 表单参数
      form: {},
      ldapForm: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/admin/api/v1/user/import"
      },
      // 查询参数
      params: {
        page: 1,
        pageSize: 10,
        userName: undefined,
        userPhone: undefined,
        rank: undefined,
        deptId: undefined
      },
      diagramOpen: false,
      diagramData:{},
      columns:  [
            {key: 0, label: this.$t(`user.label.id`), visible: false},
            {key: 1, label: this.$t(`user.label.name`), visible: true},
            {key: 2, label: this.$t(`user.label.account`), visible: true},
            {key: 3, label: this.$t(`user.label.sex`), visible: true},
            {key: 4, label: this.$t(`user.label.phone`), visible: true},
            {key: 5, label: this.$t(`user.label.email`), visible: true},
            {key: 6, label: this.$t(`user.label.dept`), visible: true},
            {key: 7, label: this.$t(`user.label.role`), visible: true},
            {key: 8, label: this.$t(`user.label.status`), visible: true},
          ]
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
    this.getRoleOptions();
    this.getPostOptions();
    this.getUserOptions();
    this.getConfigKey("sys.user.initPassword").then(resp => {
      this.initPassword = resp.data;
    });
  },
  computed: {
    rules() {
      return {
        userAccount: [
          {required: true, message: this.$t(`user.rules.account`), trigger: "blur"}
        ],
        userName: [
          {required: true, message: this.$t(`user.rules.name1`), trigger: "blur"},
          {min: 2, max: 20, message: this.$t(`user.rules.name2`), trigger: 'blur'}
        ],
        userPasswd: [
          {required: true, message: this.$t(`user.rules.pwd1`), trigger: "blur"},
          {min: 6, max: 20, message: this.$t(`user.rules.pwd2`), trigger: 'blur'}
        ],
        userEmail: [
          {type: "email", message: this.$t(`user.rules.email`), trigger: ["blur", "change"]}
        ],
        userPhone: [
          {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: this.$t(`user.rules.phone`), trigger: "blur"}
        ]
      };
    },
    headers() {
      return  {Authorization: "Bearer " + cache.local.getAccessToken(), "Accept-Language": this.$i18n.locale}
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
      this.params.deptId = data.id;
      this.handleQuery();
    },
    /** 获取部门树 */
    getDeptOptions() {
      treeselect().then(resp => {
        this.deptOptions = resp.data;
      });
    },
    /** 表单重置 */
    reset() {
      this.form = {
        userId: undefined,
        deptId: undefined,
        userName: undefined,
        userAccount: undefined,
        userPasswd: this.initPassword,
        userPhone: undefined,
        userEmail: undefined,
        userSex: 0,
        userStatus: 1,
        rank: undefined,
        remark: undefined,
        roleIds: [],
        parentIds: [],
        deptPostIds: []
      };
      this.resetForm("form");
    },
    /** 搜索 */
    handleQuery() {
      this.params.page = 1;
      this.getList();
    },
    /** 重置 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 列表 */
    getList() {
      this.loading = true;
      listUser(this.params).then(resp => {
          this.userList = resp.data.list;
          this.total = resp.data.total;
          this.loading = false;
        }
      );
    },
    /** 新增 */
    handleAdd() {
      this.reset();
      this.title = this.$t(`user.dialog.title_new`);
      this.open = true;
    },
    /** 修改 */
    handleUpdate(row) {
      this.reset();
      const userId = row.userId || this.ids;
      getUser(userId).then(resp => {
        this.form = resp.data;
        if(this.form.userType === 1){
          infoUser(this.form.userId).then(response => {
            this.ldapForm = response.data;
          });
        }
        this.title = this.$t(`user.dialog.title_edit`);
        this.open = true;
      });
    },
    /** 删除 */
    handleDelete(row) {
      const userIds = row.userId || this.ids;
      const msg = row.userId ? this.$t(`user.msg.confirm_delete`, { var1: row.userName }) : this.$t(`user.msg.select_delete`);
      this.$modal.confirm(msg).then(function() {
        return delUser(userIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t(`msg.success_delete`));
      }).catch(() => {});
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
    /** 获取部门岗位树 */
    getPostOptions(){
      getPosts().then(resp => {
        this.postOptions = resp.data
      });
    },
    /** 获取部门人员树 */
    getUserOptions(){
      getUsers().then(resp => {
        this.userOptions = resp.data
      });
    },
    /** 获取角色选项 */
    getRoleOptions(){
      getRoles().then(resp => {
        this.roleOptions = resp.data.list
      });
    },
    /** 更多操作 */
    handleCommand(command, row) {
      switch (command) {
        case "handleResetPwd":
          this.handleResetPwd(row);
          break;
        case "handleAuthRole":
          this.handleAuthRole(row);
          break;
        default:
          break;
      }
    },
    /** 用户状态修改 */
    handleStatusChange(row) {
      let text = row.userStatus === 1 ? this.$t(`content.enable`) : this.$t(`content.disable`);
      this.$modal.confirm(this.$t(`user.msg.confirm_status`, { var1: text, var2: row.userName })).then(function() {
        return changeStatus(row.userId, row.userStatus, row.userName);
      }).then(() => {
        this.$modal.msgSuccess(text + this.$t(`content.success`));
      }).catch(function() {
        row.userStatus = row.userStatus === 0 ? 1 : 0;
      });
    },
    /** 用户只读修改 */
    handleReadOnly(row) {
      let text = row.readOnly === 1 ? this.$t(`content.set`) : this.$t(`content.cancel`);
      this.$modal.confirm(this.$t(`user.msg.confirm_readonly`, { var1: text, var2: row.userName })).then(function() {
        return changeReadonly(row.userId, row.readOnly, row.userName);
      }).then(() => {
        this.$modal.msgSuccess(text + this.$t(`content.success`));
      }).catch(function() {
        row.readOnly = row.readOnly === 0 ? 1 : 0;
      });
    },
    /** 重置密码 */
    handleResetPwd(row) {
      this.$prompt(this.$t(`user.msg.reset_pwd`, { var1: row.userName }), this.$t(`user.dialog.title_pwd`), {
        closeOnClickModal: false,
        inputPattern: /^.{6,20}$/,
        inputErrorMessage: this.$t(`user.rules.pwd2`)
      }).then(({ value }) => {
          resetUserPwd(row.userId, value, row.userName).then(response => {
            this.$modal.msgSuccess(this.$t(`msg.success_reset`));
          });
        }).catch(() => {});
    },
    /** 分配角色 */
    handleAuthRole: function(row) {
      const userId = row.userId;
      this.$router.push("/system/user-auth/role/" + userId);
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
          if (this.form.userId !== undefined) {
            updateUser(this.form).then(response => {
              this.$modal.msgSuccess(this.$t(`msg.success_edit`));
              this.open = false;
              this.getList();
            });
          } else {
            addUser(this.form).then(response => {
              this.$modal.msgSuccess(this.$t(`msg.success_create`));
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 导出 */
    handleExport() {
      this.download('/admin/api/v1/user/export', {
        ...this.params
      }, this.$t(`user.excel`) + `_${new Date().getTime()}.xlsx`)
    },
    /** 导入 */
    handleImport() {
      this.upload.title = this.$t(`user.dialog.title_import`);
      this.upload.open = true;
    },
    /** 下载模板 */
    importTemplate() {
      this.download('/admin/api/v1/user/export/template', {
      }, this.$t(`user.template`) + `.xlsx`)
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
