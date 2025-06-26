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
          <el-tree ref="tree" :data="deptOptions" :props="defaultProps" :filter-node-method="filterNode"
                   :expand-on-click-node="false" default-expand-all highlight-current @node-click="handleNodeClick"/>
        </div>
      </el-col>

      <!-- 筛选栏 -->
      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="auto">
          <el-form-item :label="$t('user.label.name')" prop="userName">
            <el-input v-model="queryParams.userName" :placeholder="$t('user.placeholder.name')"
                      clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
          </el-form-item>
          <el-form-item :label="$t('user.label.phone')" prop="userPhone">
            <el-input v-model="queryParams.userPhone" :placeholder="$t('user.placeholder.phone')"
                      clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
          </el-form-item>
          <el-form-item :label="$t('user.label.rank')" prop="userRank">
            <el-select v-model="queryParams.userRank" :placeholder="$t('user.placeholder.rank')" style="width: 230px;">
              <el-option v-for="dict in dict.type.post_level" :key="dict.code" :value="dict.code" :label="`${dict.code}/${$t(dict.name)}`"/>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
              {{$t('commons.button.search')}}
            </el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">
              {{$t('commons.button.reset')}}
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 操作栏 -->
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain  size="mini" icon="el-icon-plus" @click="handleAdd"
                       :disabled="!checkPermit(['sys:user:create'])">
              {{$t('commons.button.create')}}
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain size="mini" icon="el-icon-edit" @click="handleUpdate"
                       :disabled="single || !checkPermit(['sys:user:edit'])">
              {{$t('commons.button.edit')}}
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain size="mini" icon="el-icon-delete" @click="handleDelete"
                       :disabled="multiple || !checkPermit(['sys:user:delete'])">
              {{$t('commons.button.delete')}}
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain size="mini" icon="el-icon-upload2" @click="handleImport"
                       :disabled="!checkPermit(['sys:user:import'])">
              {{$t('commons.button.import')}}
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain size="mini" icon="el-icon-download" @click="handleExport"
                       :disabled="!checkPermit(['sys:user:export'])">
              {{$t('commons.button.export')}}
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="primary" plain size="mini" @click="showDiagram"
                       :disabled="!checkPermit(['sys:user:diagram'])">
              <svg-icon icon-class="tree"/> {{$t('commons.button.diagram')}}
            </el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :cols="cols"/>
        </el-row>

        <!-- 表格数据 -->
        <el-table :data="list" @selection-change="selectRow" v-loading="loading" :header-cell-style="{'text-align':'center'}">
          <el-table-column :selectable='selectable' type="selection" width="50"/>
          <el-table-column :label="$t('commons.label.index')" type="index" width="55">
            <template slot-scope="scope">
              <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column v-if="cols[0].show" :label="$t('user.label.name')" prop="userName" align="center" width="100" :show-overflow-tooltip="true"/>
          <el-table-column v-if="cols[1].show" :label="$t('user.label.account')" prop="userAccount" align="center" width="100" :show-overflow-tooltip="true"/>
          <el-table-column v-if="cols[2].show" :label="$t('user.label.sex')" align="center" width="80">
            <template slot-scope="{row: {userSex}}">
              <template v-for="item in dict.type.sex">
                <span v-if="userSex === item.value">{{$t(item.name)}}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column v-if="cols[3].show" :label="$t('user.label.phone')" prop="userPhone" align="center" width="120"/>
          <el-table-column v-if="cols[4].show" :label="$t('user.label.email')" prop="userEmail" align="center" :show-overflow-tooltip="true"/>
          <el-table-column v-if="cols[5].show" :label="$t('user.label.dept')" align="center" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <template v-if="scope.row.deptPosts.length>0" >
                <template v-for="(item, index) in scope.row.deptPosts">
                  <template v-if="item.isDefault === 1">
                    <span style="color: #004d8c" v-if="item.deptName">{{ item.deptName }}</span>
                    <span style="color: #004d8c" v-if="item.deptName && item.postName">/</span>
                    <span style="color: #004d8c" v-if="item.postName">{{ item.postName }}</span>
                  </template>
                  <template v-else>
                    <span v-if="item.deptName">{{ item.deptName }}</span>
                    <span v-if="item.deptName && item.postName">/</span>
                    <span v-if="item.postName">{{ item.postName }}</span>
                  </template>
                  <br v-if="index < scope.row.deptPosts.length - 1">
                </template>
              </template>
            </template>
          </el-table-column>
          <el-table-column v-if="cols[6].show" :label="$t('user.label.rank')" align="center" :show-overflow-tooltip="true" width="180">
            <template slot-scope="{row: {userRank}}">
              <template v-for="item in dict.type.post_level">
                <span v-if="userRank === item.code">{{ item.code }}/{{ $t(item.name) }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column v-if="cols[7].show" :label="$t('user.label.status')" prop="userStatus" align="center" width="60">
            <template slot-scope="scope">
              <el-switch v-model="scope.row.userStatus" @change="handleStatusChange(scope.row)"
                         :active-value=1 :inactive-value=0 :disabled="scope.row.userId === 1 || !checkPermit(['sys:user:status'])"/>
            </template>
          </el-table-column>
          <el-table-column v-if="cols[8].show" :label="$t('commons.label.createTime')" prop="createTime">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column v-if="cols[9].show" :label="$t('commons.label.updateTime')" prop="updateTime">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.updateTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('commons.label.options')" align="center" width="180" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                         :disabled="scope.row.userId === 1">
                {{$t('commons.button.edit')}}
              </el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                         :disabled="scope.row.userId === 1 || !checkPermit(['sys:user:delete'])">
                {{$t('commons.button.delete')}}
              </el-button>
              <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)">
                <span class="el-dropdown-link">
                  <i class="el-icon-d-arrow-right el-icon--right"></i>{{$t('commons.button.more')}}
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="handleResetPwd" icon="el-icon-key"
                                    :disabled="scope.row.userId === 1 || !checkPermit(['sys:user:passwd'])">
                    {{$t('user.button.passwd')}}
                  </el-dropdown-item>
                  <el-dropdown-item command="handleAuthRole" icon="el-icon-circle-check"
                                    :disabled="scope.row.userId === 1 || !checkPermit(['sys:user:grant'])">
                    {{$t('user.button.grant')}}
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>
      </el-col>
    </el-row>

    <!-- 新增/编辑 -->
    <el-dialog v-drag :title="title" :visible.sync="open" width="750px" append-to-body >
      <el-tabs v-model="activeTab">
        <el-tab-pane :label="$t('user.text.basic')" name="basic">
          <el-form ref="form" :model="form" :rules="rules" label-width="110px" style="height: 420px">
            <el-row>
              <el-col :span="12">
                <el-form-item :label="$t('user.label.account')" prop="userAccount">
                  <el-input v-model="form.userAccount" :placeholder="$t('user.placeholder.account')" maxlength="30" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('user.label.name')" prop="userName">
                  <el-input v-model="form.userName" :placeholder="$t('user.placeholder.name')" maxlength="30" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item v-if="form.userId === undefined" :label="$t('user.label.passwd')" prop="userPasswd">
                  <el-input v-model="form.userPasswd" :placeholder="$t('user.placeholder.passwd')" type="password" maxlength="20" show-password/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item v-if="form.userId === undefined" :label="$t('user.label.status')">
                  <el-radio-group v-model="form.userStatus">
                    <el-radio v-for="dict in dict.type.enable_disable" :key="dict.value" :label="dict.value">{{$t(dict.name)}}</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item :label="$t('user.label.phone')" prop="userPhone">
                  <el-input v-model="form.userPhone" :placeholder="$t('user.placeholder.phone')" maxlength="11" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('user.label.email')" prop="email">
                  <el-input v-model="form.userEmail" :placeholder="$t('user.placeholder.email')" maxlength="50" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item :label="$t('user.label.rank')">
                  <el-select v-model="form.userRank" :placeholder="$t('user.placeholder.rank')" style="width: 100%;">
                    <el-option v-for="dict in dict.type.post_level" :key="dict.code" :value="dict.code" :label="`${dict.code}/${$t(dict.name)}`"/>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item :label="$t('user.label.sex')">
                  <el-select v-model="form.userSex" :placeholder="$t('user.placeholder.sex')" style="width: 100%;">
                    <el-option v-for="dict in dict.type.sex" :key="dict.value" :value="dict.value" :label="$t(dict.name)"/>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item :label="$t('user.label.role')">
                  <el-select v-model="form.roleIds" multiple :placeholder="$t('user.placeholder.role')" style="width: 100%;">
                    <el-option v-for="item in roleOptions" :key="item.roleId" :label="item.roleName" :value="item.roleId"/>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item :label="$t('user.label.dept')" prop="deptId">
                  <treeselect v-model="form.deptPostIds" :options="postOptions" :multiple="true"
                              :disable-branch-nodes="true" :placeholder="$t('user.placeholder.dept')">
                    <div slot="value-label" slot-scope="{ node }">{{ node.raw.content }}</div>
                  </treeselect>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item :label="$t('user.label.report')">
                  <el-tree-select v-model="form.parentIds" :selectParams="userSelectParams" :treeParams="userTreeParams"
                                  :treeRenderFun="treeRender" @searchFun="userTreeSearch"
                                  :styles="treeStyles" ref="userTreeSelect"/>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <el-form-item :label="$t('commons.label.remark')">
                  <el-input v-model="form.remark" type="textarea" placeholder="..."/>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>
      </el-tabs>
      <div v-if="activeTab === 'basic'" slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" :disabled="!checkPermit(['sys:user:edit'])">
          {{$t('commons.button.confirm')}}
        </el-button>
        <el-button @click="cancel">{{$t('commons.button.cancel')}}</el-button>
      </div>
    </el-dialog>

    <!-- 导入 -->
    <el-dialog v-drag :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
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
        <div class="el-upload__text">{{$t('user.dialog.import_text1')}}<em>{{$t('user.dialog.import_text2')}}</em>{{$t('user.dialog.import_text3')}}</div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" /> {{$t('user.dialog.import_text4')}}
          </div>
          <span>{{$t('user.dialog.import_text5')}}</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">{{$t('user.dialog.import_text6')}}</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">{{$t('commons.button.confirm')}}</el-button>
        <el-button @click="upload.open = false">{{$t('commons.button.cancel')}}</el-button>
      </div>
    </el-dialog>

    <!-- 组织关系 -->
    <el-dialog v-drag :title="$t('user.dialog.diagram')" :visible.sync="diagramOpen" width="80%" append-to-body>
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
import {
  addUser,
  updateUserStatus,
  delUser,
  getUserInfo,
  getUserList,
  updateUserPasswd,
  updateUser,
  getUserDiagram,
} from "@/api/system/user";
import { getDeptDiagram, getDeptPostDiagram, getDeptUserDiagram } from '@/api/system/dept'
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {checkPermit} from "@/utils/permission";
import cache from "@/plugins/cache";
import { getRoleList } from '@/api/system/role'
import {getConfigValue} from "@/api/system/config";

export default {
  name: "User",
  dicts: ['enable_disable', 'sex', 'post_level'],
  components: { Treeselect },
  data() {
    return {
      activeTab: 'basic',
      // 遮罩层
      loading: true,
      // 选中列表
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
      // 显示弹出层
      open: false,
      // 弹出层标题
      title: "",
      // 部门选项
      deptOptions: undefined,
      // 角色选项
      roleOptions: [],
      // 岗位选项
      postOptions: [],
      // 部门名称
      deptName: undefined,
      // 表单参数
      form: {},
      initPasswd: undefined,
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
      queryParams: {
        page: 1,
        pageSize: 10,
        userName: undefined,
        userPhone: undefined,
        userRank: undefined,
        deptId: undefined
      },
      diagramOpen: false,
      diagramData:{},
      cols: [
        {key: 0, label: 'user.label.name', show: true},
        {key: 1, label: 'user.label.account', show: true},
        {key: 2, label: 'user.label.sex', show: true},
        {key: 3, label: 'user.label.phone', show: true},
        {key: 4, label: 'user.label.email', show: true},
        {key: 5, label: 'user.label.dept', show: true},
        {key: 6, label: 'user.label.role', show: true},
        {key: 7, label: 'user.label.status', show: true},
        {key: 8, label: 'commons.label.createTime', show: false},
        {key: 9, label: 'commons.label.updateTime', show: false},
      ],
      diagramHorizontal: true,
      diagramExpandAll: true,
      treeStyles: {
        width: '100%'
      },
      userTreeParams: {
        clickParent: false,
        filterable: true,
        'check-strictly': true,
        'default-expand-all': false,
        'expand-on-click-node': false,
        data: [],
        props: {
          value: 'id',
          label: 'label',
          disabled: 'isDept',
          children: 'children',
        }
      },
      // 部门Tree
      userSelectParams: {
        multiple: true,
        clearable: true,
        placeholder: this.$t('user.placeholder.report')
      },
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
    this.getUserOptions();
    this.getConfigValue("sys.initPassword").then(resp => {
      this.initPasswd = resp.data;
    });
  },
  computed: {
    rules() {
      return {
        userAccount: [
          {required: true, message: this.$t('user.rules.account'), trigger: "blur"}
        ],
        userName: [
          {required: true, message: this.$t('user.rules.name'), trigger: "blur"},
          {min: 2, max: 20, message: this.$t('user.rules.name_len'), trigger: 'blur'}
        ],
        userPasswd: [
          {required: true, message: this.$t('user.rules.passwd'), trigger: "blur"},
          {min: 6, max: 20, message: this.$t('user.rules.passwd_len'), trigger: 'blur'}
        ],
        userEmail: [
          {type: "email", message: this.$t('user.rules.email'), trigger: ["blur", "change"]}
        ],
        userPhone: [
          {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: this.$t('user.rules.phone'), trigger: "blur"}
        ]
      };
    },
    headers() {
      return  {Authorization: "Bearer " + cache.local.getAccessToken(), "Accept-Language": this.$i18n.locale}
    }
  },
  methods: {
    checkPermit,
    selectable(row){
      return row.userId > 1;
    },
    /** 筛选节点 */
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    userTreeSearch(value) {
      this.$refs.userTreeSelect.$refs.tree.filter(value);
    },
    treeRender(h, { node, data, store }) {
      return (
        <span class='custom-tree-node'><span>{node.label}</span></span>
      );
    },
    /** 点击部门树 */
    handleNodeClick(data) {
      this.queryParams.deptId = data.id;
      this.handleQuery();
    },
    /** 获取部门树 */
    getDeptOptions() {
      getDeptDiagram().then(resp => {
        this.deptOptions = resp.data;
      });
    },
    /** 搜索 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置 */
    resetQuery() {
      this.$refs.queryForm.resetFields();
      this.queryParams.deptId = null;
      this.handleQuery();
    },
    /** 多选框 */
    selectRow(selection) {
      this.ids = selection.map(item => item.userId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 列表 */
    getList() {
      this.loading = true;
      getUserList(this.queryParams).then(resp => {
          this.list = resp.data.list;
          this.total = resp.data.total;
          this.loading = false;
        }
      );
    },
    /** 新增 */
    handleAdd() {
      this.title = this.$t('user.dialog.new');
      this.form = {
        userId: undefined,
        deptId: undefined,
        userName: undefined,
        userAccount: undefined,
        userPasswd: this.initPasswd,
        userPhone: undefined,
        userEmail: undefined,
        userSex: 0,
        userStatus: 1,
        userRank: undefined,
        remark: undefined,
        roleIds: [],
        parentIds: [],
        deptPostIds: []
      };
      Promise.all([
        getRoleList(),
        getDeptPostDiagram()
      ]).then(([roleResp, postResp]) => {
        this.roleOptions = roleResp.data.list
        this.postOptions = postResp.data;
        this.open = true;
      });
    },
    /** 修改 */
    handleUpdate(row) {
      this.title = this.$t('user.dialog.edit');
      const userId = row.userId || this.ids;
      Promise.all([
        getRoleList(),
        getDeptPostDiagram(),
        getUserInfo(userId)
      ]).then(([roleResp, postResp, infoResp]) => {
        this.roleOptions = roleResp.data.list
        this.postOptions = postResp.data;
        this.form = infoResp.data;
        this.open = true;
      });
    },
    /** 删除 */
    handleDelete(row) {
      const userIds = row.userId || this.ids;
      const msg = row.userId
          ? this.$t('user.confirm.delete', { arg1: row.userName })
          : this.$t('user.confirm.delete_select');
      this.$modal.confirm(msg).then(function() {
        return delUser(userIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('commons.msg.success.delete'));
      }).catch(() => {});
    },
    /** 组织架构 */
    showDiagram() {
      getUserDiagram().then(response => {
        this.diagramData = response.data;
        this.diagramOpen = true;
        this.diagramExpand();
      });
    },
    /** 获取部门人员树 */
    getUserOptions(){
      getDeptUserDiagram().then(resp => {
        this.userTreeParams.data = resp.data
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
      let text = row.userStatus === 1
          ? this.$t('user.confirm.status_enable', { arg1: row.userName })
          : this.$t('user.confirm.status_disable', { arg1: row.userName });
      this.$modal.confirm(text).then(function() {
        return updateUserStatus(row.userId, row.userStatus, row.userName);
      }).then(() => {
        this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
      }).catch(function() {
        row.userStatus = row.userStatus === 0 ? 1 : 0;
      });
    },
    /** 重置密码 */
    handleResetPwd(row) {
      this.$prompt(this.$t('user.confirm.passwd', { arg1: row.userName }), this.$t('user.dialog.passwd'), {
        closeOnClickModal: false,
        inputPattern: /^.{6,20}$/,
        inputErrorMessage: this.$t('user.rules.passwd_len')
      }).then(({ value }) => {
          updateUserPasswd(row.userId, value, row.userName).then(() => {
            this.$modal.msgSuccess(this.$t('commons.msg.success.reset'));
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
    },
    /** 提交 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.userId !== undefined) {
            updateUser(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
              this.open = false;
              this.getList();
            });
          } else {
            addUser(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('commons.msg.success.create'));
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
        ...this.queryParams
      }, this.$t('user.text.data') + `_${new Date().getTime()}.xlsx`)
    },
    /** 导入 */
    handleImport() {
      this.upload.title = this.$t('user.dialog.import');
      this.upload.open = true;
    },
    /** 下载模板 */
    importTemplate() {
      this.download('/admin/api/v1/user/export/template', {
      }, this.$t('user.text.template') + `.xlsx`)
    },
    /** 文件上传中 */
    handleFileUploadProgress() {
      this.upload.isUploading = true;
    },
    /** 文件上传成功 */
    handleFileSuccess(response) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(
          "<div style='overflow-y: scroll;overflow-x: auto;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>",
          this.$t('user.dialog.import_text7'),
          { dangerouslyUseHTMLString: true });
      this.getList();
    },
    /** 提交上传文件 */
    submitFileForm() {
      this.$refs.upload.submit();
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
              {data.id > 0 && <i class="el-icon-user-solid" style="margin-right: 2px;"></i>}
              <span>{data.label}</span>
            </div>
            <div style="font-size:12px; line-height:20px;">
              {data.rank}
              {this.dict.type.post_level
                  .filter(item => data.rank === item.code)
                  .map(item => (
                      <span>/{this.$t(item.name)}</span>
                  ))}
            </div>
          </div>
      );
    },
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
</style>
