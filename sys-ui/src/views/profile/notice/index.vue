<template>
  <div class="app-container">
    <!--  筛选栏  -->
    <el-form ref="queryForm" :model="queryParams" size="small" :inline="true" v-show="showSearch" label-width="auto">
      <el-form-item :label="$t('notice.label.title')" prop="noticeTitle">
        <el-input v-model="queryParams.noticeTitle" :placeholder="$t('notice.placeholder.title')" clearable/>
      </el-form-item>
      <el-form-item :label="$t('notice.label.type')" prop="noticeType">
        <el-select v-model="queryParams.noticeType" :placeholder="$t('notice.placeholder.type')" clearable>
          <el-option v-for="dict in dict.type.notice_type" :key="dict.value" :value="dict.value" :label="$t(dict.name)"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('commons.label.status')" prop="noticeStatus">
        <el-select v-model="queryParams.noticeStatus" :placeholder="$t('notice.placeholder.status')" clearable>
          <el-option v-for="dict in dict.type.notice_status" :key="dict.value" :value="dict.value" :label="$t(dict.name)"/>
        </el-select>
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">
          {{$t('commons.button.create')}}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" @click="handleUpdate" :disabled="single">
          {{$t('commons.button.edit')}}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete" :disabled="multiple">
          {{$t('commons.button.delete')}}
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"/>
    </el-row>

    <!--  列表数据  -->
    <el-table :data="list" @selection-change="handleSelectionChange" v-loading="loading">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('notice.label.title')" align="center" prop="noticeTitle" width="240" :show-overflow-tooltip="true"/>
      <el-table-column :label="$t('notice.label.type')" align="center" prop="noticeType">
        <template slot-scope="{row: {noticeType}}">
          <template v-for="item in dict.type.notice_type">
            <span v-if="noticeType === item.value">{{ $t(item.name) }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column :label="$t('notice.label.level')" align="center" prop="noticeLevel">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.notice_level" :value="scope.row.noticeLevel"/>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.status')" align="center" prop="noticeStatus">
        <template slot-scope="{row: {noticeStatus}}">
          <template v-for="item in dict.type.notice_status">
            <span v-if="noticeStatus === item.value">{{ $t(item.name) }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.createUser')" align="center" prop="createUserName">
        <template slot-scope="scope">
          <span v-if="scope.row.isSystem === 1">系统</span>
          <span v-else>{{ scope.row.createUserName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.createTime')" align="center" prop="createTime" width="160"/>
      <el-table-column :label="$t('notice.label.publishTime')" align="center" prop="publishTime" width="160"/>
      <el-table-column :label="$t('notice.label.read')" align="center" prop="statRead">
        <template slot-scope="scope">
          <span v-if="scope.row.statTotal > 0">{{ scope.row.statRead }}/{{ scope.row.statTotal }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding" width="260">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleInfo(scope.row)"
                     icon="el-icon-edit" >
            {{$t('commons.button.detail')}}
          </el-button>
          <el-button size="mini" type="text" @click="handleUpdate(scope.row)"
                     icon="el-icon-edit" :disabled="scope.row.noticeStatus !== 0">
            {{$t('commons.button.edit')}}
          </el-button>
          <el-button size="mini" type="text" @click="handlePublish(scope.row)"
                     icon="el-icon-s-promotion" :disabled="scope.row.noticeStatus !== 0">
            {{$t('commons.button.publish')}}
          </el-button>
          <el-button v-if="scope.row.noticeStatus !== 1" size="mini" type="text" @click="handleDelete(scope.row)"
                     icon="el-icon-delete">
            {{$t('commons.button.delete')}}
          </el-button>
          <el-button v-if="scope.row.noticeStatus === 1" size="mini" type="text" @click="handleDelete(scope.row)"
                     icon="el-icon-delete">
            {{$t('commons.button.recall')}}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 添加修改 -->
    <el-dialog v-drag :title="title" :visible.sync="open" width="880px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="公告标题" prop="noticeTitle">
              <el-input v-model="form.noticeTitle" placeholder="请输入公告标题" style="width: 230px;"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公告类型" prop="noticeType">
              <el-select v-model="form.noticeType" placeholder="请选择公告类型" style="width: 230px;">
                <el-option v-for="dict in dict.type.notice_type" :key="dict.value" :value="dict.value" :label="$t(dict.name)"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公告级别">
              <el-select v-model="form.noticeLevel" placeholder="请选择公告级别" style="width: 230px;">
                <el-option v-for="dict in dict.type.notice_level" :key="dict.value" :value="dict.value" :label="$t(dict.name)"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="全员发送">
              <el-radio-group v-model="form.goalsAll">
                <el-radio v-for="dict in dict.type.yes_no" :key="dict.value" :label="dict.value">{{$t(dict.name)}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="选择部门">
              <el-tree-select v-model="form.goalsDept" :selectParams="deptSelectParams" :treeParams="deptTreeParams"
                              :treeRenderFun="userTreeRender" @searchFun="deptTreeSearch"
                              :styles="userTreeStyles" ref="deptTreeSelect"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="选择角色">
              <el-select v-model="form.goalsRole" multiple placeholder="请选择角色" style="width: 85%;">
                <el-option v-for="item in roleOptions" :key="item.roleId" :label="item.roleName" :value="item.roleId"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="选择用户">
              <el-tree-select v-model="form.goalsUser" :selectParams="userSelectParams" :treeParams="userTreeParams"
                              :treeRenderFun="userTreeRender" @searchFun="userTreeSearch"
                              :styles="userTreeStyles" ref="userTreeSelect"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="内容">
              <editor v-model="form.content" :min-height="240"
                      @notice="recordAttach" ownerModule="module_notice" attachType="image"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确定</el-button>
        <el-button @click="cancel">取消</el-button>
      </div>
    </el-dialog>

    <notice-info ref="noticeInfo" @ok="handleQuery" />
  </div>
</template>

<script>
import {getNoticeList, getNoticeInfo, delNotice, addNotice, updateNotice, publishNotice} from "@/api/system/notice";
import {getDeptDiagramById, getDeptUserDiagram} from "@/api/system/dept";
import { getRoleList } from '@/api/system/role'

export default {
  name: "Notice",
  dicts: ['notice_status', 'notice_level', 'notice_type', 'yes_no'],
  components: { noticeInfo: ()=> import('./info.vue')},
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
      // 公告表格数据
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        noticeTitle: undefined,
        noticeType: undefined,
        noticeStatus: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        noticeTitle: [
          { required: true, message: "公告标题不能为空", trigger: "blur" }
        ],
        noticeType: [
          { required: true, message: "公告类型不能为空", trigger: "change" }
        ]
      },
      // 角色选项
      roleOptions: [],
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
      userSelectParams: {
        multiple: true,
        clearable: true,
        placeholder: '请选择用户'
      },
      // 用户Tree
      userTreeParams: {
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
        width: '85%'
      },
    };
  },
  created() {
    this.getList();
    this.getRoleOptions();
  },
  methods: {
    /** 表单重置 */
    reset() {
      this.form = {
        noticeId: undefined,
        noticeTitle: undefined,
        noticeType: undefined,
        noticeLevel: undefined,
        content: undefined,
        goalsAll: 0,
        goalsDept: [],
        goalsRole: [],
        goalsUser: [],
        attaches: []
      };
      this.resetForm("form");
    },
    /** 附件记录 */
    recordAttach(attach) {
      this.form.attaches.push({
        "attachId" : attach.attachId,
        "attachPath" : attach.attachPath
      });
    },
    /** 用户Tree */
    userTreeRender(h, { node, data, store }) {
      return (
        <span class='custom-tree-node'><span>{node.label}</span></span>
      );
    },
    /** 用户Tree */
    userTreeSearch(value) {
      this.$refs.userTreeSelect.$refs.tree.filter(value);
      // 自行判断
      // this.$refs.userTreeSelect.filterFun(value);
      // 后台查询
      // this.$refs.userTreeSelect.treeDataUpdateFun(treeData);
    },
    /** 部门Tree */
    deptTreeSearch(value) {
      this.$refs.deptTreeSelect.$refs.tree.filter(value);
    },
    /** 公告列表 */
    getList() {
      this.loading = true;
      getNoticeList(this.queryParams).then(response => {
        this.list = response.data.list;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 取消 */
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 搜索 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.noticeId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 获取角色选项 */
    getRoleOptions(){
      getRoleList().then(resp => {
        this.roleOptions = resp.data.list
      });
    },
    /** 获取用户选项 */
    getUserOptions(){
      getDeptUserDiagram().then(resp => {
        this.userTreeParams.data = resp.data
      });
    },
    /** 新增 */
    handleAdd() {
      this.reset();
      getDeptDiagramById(1).then(response => {
        this.deptTreeParams.data = response.data
        getDeptUserDiagram().then(resp => {
          this.userTreeParams.data = resp.data
          this.title = "新增公告";
          this.open = true;
        });
      });
    },
    /** 修改 */
    handleUpdate(row) {
      this.reset();
      const noticeId = row.noticeId || this.ids
      getDeptDiagramById(1).then(response => {
        this.deptTreeParams.data = response.data
        getDeptUserDiagram().then(resp => {
          this.userTreeParams.data = resp.data
          getNoticeInfo(noticeId).then(rsp => {
            this.form = rsp.data;
            if(rsp.data.goalsDept === null){
              this.form.goalsDept = [];
            }
            if(rsp.data.goalsUser === null){
              this.form.goalsUser = [];
            }
            this.open = true;
            this.title = "修改公告";
          });
        });
      });
    },
    /** 提交 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.noticeId !== undefined) {
            updateNotice(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addNotice(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除 */
    handleDelete(row) {
      const noticeIds = row.noticeId || this.ids
      let action = row.noticeStatus === 1 ? '撤回' : '删除';
      const msg = row.noticeId ? '确认' + action + '公告[' + row.noticeTitle + ']?' : '确认' + action + '所选公告？';
      this.$modal.confirm(msg).then(function() {
        return delNotice(noticeIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 发布 */
    handlePublish(row) {
      const noticeId = row.noticeId
      this.$modal.confirm('确认发布公告[' + row.noticeTitle + ']?').then(function() {
        return publishNotice(noticeId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("发布成功");
      }).catch(() => {});
    },
    /** 详情 */
    handleInfo(row){
      this.$refs.noticeInfo.show(row.noticeId);
    }
  }
};
</script>
