<template>
  <div class="app-container">
    <el-row :gutter="20" class="custom-row">
      <el-col :span="4" :xs="24">
        <build-folder @folder-click="handleFolderClick"/>
      </el-col>
      <el-col :span="20" :xs="24">
        <!--  筛选栏  -->
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
                 label-width="auto">
          <el-form-item label="构建名称" prop="credentialName">
            <el-input v-model="queryParams.credentialName" placeholder="请输入构建名称"
                      clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
          </el-form-item>
          <el-form-item label="构建状态" prop="credentialName">
            <el-input v-model="queryParams.credentialName" placeholder="请选择构建状态"
                      clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
          </el-form-item>
          <el-form-item label="构建时间" label-width="120">
            <el-date-picker v-model="dateRange" style="width: 240px" type="daterange"
                            value-format="yyyy-MM-dd HH:mm:ss" range-separator="-"
                            :start-placeholder="$t('commons.label.beginDate')"
                            :end-placeholder="$t('commons.label.endDate')"/>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
              {{ $t('commons.button.search') }}
            </el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{
                $t('commons.button.reset')
              }}
            </el-button>
          </el-form-item>
        </el-form>

        <!--  操作栏  -->
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                       :disabled="currentFolderId == null || !checkPermit(['sys:credential:create'])">
              {{ $t('commons.button.create') }}
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="el-icon-edit" size="mini" @click="handleUpdate" :disabled="single">
              {{ $t('commons.button.edit') }}
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete"
                       :disabled="multiple || !checkPermit(['sys:credential:delete'])">
              {{ $t('commons.button.delete') }}
            </el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"/>
        </el-row>

        <!--  列表数据  -->
        <el-table :data="list" @selection-change="handleSelectionChange"
                  v-loading="loading" :header-cell-style="{'text-align':'center'}">
          <el-table-column type="selection" width="55" align="center"/>
          <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="55">
            <template slot-scope="scope">
              <span>{{ (queryParams.page - 1) * queryParams.pageSize + scope.$index + 1 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="名称" align="center" prop="credentialName" :show-overflow-tooltip="true"/>
          <el-table-column label="上次成功" align="center" :show-overflow-tooltip="true">
            <template slot-scope="{row: {credentialType}}">
              <template v-for="item in credential_type">
                <span v-if="credentialType === item.value">{{ $t(item.label) }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column label="上次失败" align="center" :show-overflow-tooltip="true">
            <template slot-scope="{row: {credentialScope}}">
              <template v-for="item in credential_scope">
                <span v-if="credentialScope === item.value">{{ $t(item.label) }}</span>
              </template>
            </template>
          </el-table-column>
          <el-table-column label="上次持续时间" align="center" prop="createTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">
                {{ $t('commons.button.edit') }}
              </el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                         :disabled="!checkPermit(['sys:credential:delete'])">
                {{ $t('commons.button.delete') }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total>0" :total="total" :limit.sync="queryParams.pageSize" :page.sync="queryParams.page"
                    @pagination="getList"/>
      </el-col>
    </el-row>

    <!--  新增  -->
    <el-drawer :visible.sync="createOpen" direction="rtl" size="75%">
      <build-create />
    </el-drawer>
  </div>
</template>
<script>
import {checkPermit} from "@/utils/permission";
import {credential_scope, credential_type, visibility} from '@/utils/constants';
import {credentialAdd, credentialDel, credentialInfo, credentialList, credentialUpdate} from "@/api/meter/credential";
import BuildFolder from '@/views/meter/build/buildFolder.vue'
import BuildCreate from '@/views/meter/build/buildCreate.vue'
export default {
  name: "MeterBuild",
  dicts: [],
  components: {
    BuildFolder,
    BuildCreate
  },
  data() {
    return {
      credential_type: credential_type,
      credential_scope: credential_scope,
      visibility: visibility,
      currentFolderId: null,
      currentFolderRole: null,
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
      // 参数表格数据
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      createOpen: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        credentialName: undefined
      },
      // 表单参数
      form: {}
    };
  },
  created() {
    this.getList();
  },
  computed: {
    rules() {
      return {
        credentialName: [
          { required: true, message: "凭据名称不能为空", trigger: "blur" }
        ],
        username: [
          { required: true, message: "用户名不能为空", trigger: "blur" }
        ],
        secret: [
          { required: true, message: "密文不能为空", trigger: "blur" }
        ]
      };
    },
  },
  methods: {
    checkPermit,
    /** 选择目录 */
    handleFolderClick(payload) {
      this.currentFolderId = payload.folderId;
      this.currentFolderRole = payload.folderRole;
      this.queryParams.folderId = payload.folderId;
      this.handleQuery();
    },
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.credentialId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 搜索 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置 */
    resetQuery() {
      this.dateRange = [];
      this.$refs.queryForm.resetFields();
      this.handleQuery();
    },
    /** 列表 */
    getList() {
      this.loading = true;
      credentialList(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.list = response.data.list;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    /** 新增 */
    handleAdd() {

      this.createOpen = true;
    },
    /** 修改 */
    handleUpdate(row) {
      const credentialId = row.credentialId || this.ids
      this.title = "修改凭据";
      credentialInfo(credentialId).then(response => {
        this.form = response.data;
        this.open = true;
      });
    },
    /** 删除 */
    handleDelete(row) {
      const credentialIds = row.credentialId || this.ids;
      const msg = row.credentialId
          ? "确认删除所选凭据: " + row.credentialName
          : "确认删除所选凭据";
      this.$modal.confirm(msg).then(function() {
        return credentialDel(credentialIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('commons.msg.success.delete'));
      }).catch(() => {});
    },
    /** 取消 */
    cancel() {
      this.open = false;
    },
    /** 提交 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.credentialId !== undefined) {
            credentialUpdate(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
              this.open = false;
              this.getList();
            });
          } else {
            credentialAdd(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('commons.msg.success.create'));
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

<style rel="stylesheet/scss" lang="scss" scoped>
.custom-row {
  display: flex;
  flex-direction: row;
}
</style>
