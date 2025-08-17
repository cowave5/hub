<template>
  <div class="app-container">
    <!--  筛选栏  -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" @submit.native.prevent v-show="showSearch" label-width="100px">
      <el-form-item :label="$t('tenant.label.id')" prop="tenantName">
        <el-input v-model="queryParams.tenantId" clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   :disabled="!checkPermit(['hub:tenant:create'])">
          {{$t('commons.button.create')}}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" @click="handleUpdate" :disabled="single">
          {{$t('commons.button.edit')}}
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"/>
    </el-row>

    <!--  列表数据  -->
    <el-table :data="list" @selection-change="handleSelectionChange"
              v-loading="loading"  :header-cell-style="{'text-align':'center'}">
      <el-table-column type="selection" :selectable='selectable' width="55" align="center" />
      <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('tenant.label.id')" align="center" prop="tenantId" :show-overflow-tooltip="true" />
      <el-table-column :label="$t('tenant.label.name')" align="center" prop="tenantName" :show-overflow-tooltip="true" />
      <el-table-column :label="$t('tenant.label.staff')" align="center" prop="userCount" >
        <template slot-scope="scope">
          <span>{{ scope.row.userCount }} / {{ scope.row.userLimit }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('tenant.label.addr')" align="center" prop="tenantAddr" />
      <el-table-column :label="$t('tenant.label.expire')" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('tenant.label.status')" prop="tenantStatus" align="center" width="60">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status" @change="handleStatusChange(scope.row)"
                     :active-value=1 :inactive-value=0 :disabled="scope.row.tenantId === 'system' || !checkPermit(['hub:tenant:status'])"/>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.createTime')" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                     :disabled="scope.row.tenantId === 'system'">
            {{$t('commons.button.edit')}}
          </el-button>
          <el-button size="mini" type="text" @click="handleManager(scope.row)" :disabled="scope.row.tenantId === 'system'">
            <svg-icon icon-class="manager"/> {{$t('tenant.button.manager')}}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :limit.sync="queryParams.pageSize" :page.sync="queryParams.page" @pagination="getList"/>

    <!-- 添加或修改 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-row>
              <el-col :span="24">
                <el-form-item :label="$t('tenant.label.id')" prop="tenantId">
                  <el-input v-model="form.tenantId" :disabled="isUpdate"/>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item :label="$t('tenant.label.title')" prop="title">
                  <el-input v-model="form.title"/>
                </el-form-item>
              </el-col>
              <el-col :span="24">
                <el-form-item :label="$t('tenant.label.name')" prop="tenantName">
                  <el-input v-model="form.tenantName"/>
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('tenant.label.logo')" prop="logo">
              <imageUpload v-model="imageUrl" tenantId="system" ownerModule="module_tenant" attachType="logo" :headers="headers"
                           @uploadCallback="handleLogoUploaded"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('tenant.label.staff')" prop="userLimit">
              <el-select v-model="form.userLimit" style="width: 100%;">
                <el-option :key="500" :value="500" label="1 ~ 500" />
                <el-option :key="1000" :value="1000" label="500 ~ 1000" />
                <el-option :key="5000" :value="5000" label="1000 ~ 5000" />
                <el-option :key="5000" :value="10000" label="5000 ~ 10000" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('tenant.label.expire')" prop="expireTime">
              <el-date-picker v-model="form.expireTime" clearable style="width: 100%"
                        value-format="yyyy-MM-dd" type="datetime" placeholder=""/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('tenant.label.phone')" prop="tenantPhone">
              <el-input v-model="form.tenantPhone"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('tenant.label.email')" prop="tenantEmail">
              <el-input v-model="form.tenantEmail"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('tenant.label.contact')" prop="tenantUser">
              <el-input v-model="form.tenantUser"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('tenant.label.addr')" prop="tenantAddr">
              <el-input v-model="form.tenantAddr"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" :disabled="!checkPermit(['hub:config:edit'])">
          {{$t('commons.button.confirm')}}
        </el-button>
        <el-button @click="cancel">{{$t('commons.button.cancel')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import {checkPermit} from "@/utils/permission";
import {addTenant, editTenant, getTenant, listTenant, switchTenantStatus} from "@/api/system/tenant";
import cache from "@/plugins/cache";

export default {
  name: "Tenant",
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
      // 参数表格数据
      list: [],
      // 弹出层标题
      title: "",
      open: false,
      isUpdate: false,
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        tenantId: undefined
      },
      // 表单参数
      form: {},
      imageUrl: undefined,
    };
  },
  created() {
    this.getList();
  },
  computed: {
    rules() {
      return {
        tenantId: [
          { required: true, message: this.$t('tenant.rules.id'), trigger: "blur" }
        ],
        title: [
          { required: true, message: this.$t('tenant.rules.title'), trigger: "blur" }
        ],
        tenantName: [
          { required: true, message: this.$t('tenant.rules.name'), trigger: "blur" }
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
      return row.tenantId !== 'system';
    },
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.tenantId)
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
      this.$refs.queryForm.resetFields();
      this.handleQuery();
    },
    /** 列表 */
    getList() {
      this.loading = true;
      listTenant(this.queryParams).then(response => {
          this.list = response.data.list;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    /** 租户状态修改 */
    handleStatusChange(row) {
      let text = row.status === 1
          ? this.$t('tenant.confirm.status_enable', { arg1: row.tenantName })
          : this.$t('tenant.confirm.status_disable', { arg1: row.tenantName });
      this.$modal.confirm(text).then(function() {
        return switchTenantStatus(row.tenantId, row.tenantName, row.status);
      }).then(() => {
        this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
      }).catch(function() {
        row.status = row.status === 0 ? 1 : 0;
      });
    },
    /** 上传图片回调 */
    removeCallback(){
      this.form.logo = null;
      this.form.attachId = null;
    },
    /** 上传图片回调 */
    handleLogoUploaded(attach){
      this.form.logo = attach.viewUrl;
      this.form.attachId = attach.attachId;
    },
    /** 新增 */
    handleAdd() {
      this.title = this.$t('tenant.dialog.new');
      this.form = {
        tenantId: undefined,
        tenantName: undefined,
        attachId: undefined,
        logo: undefined,
        title: undefined,
        userLimit: 500,
        expireTime: undefined,
        tenantPhone: undefined,
        tenantEmail: undefined,
        tenantUser: undefined,
        tenantAddr: undefined,
      };
      this.isUpdate = false;
      this.open = true;
    },
    /** 修改 */
    handleUpdate(row) {
      this.title = this.$t('tenant.dialog.edit');
      const tenantId = row.tenantId || this.ids
      getTenant(tenantId).then(response => {
        this.imageUrl = response.data.logo;
        this.form = response.data;
        this.isUpdate = true;
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
          if (this.isUpdate) {
            editTenant(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
              this.open = false;
              this.getList();
            });
          } else {
            addTenant(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('commons.msg.success.create'));
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 设置管理员 */
    handleManager: function(row) {
      const tenantId = row.tenantId;
      this.$router.push("/system/tenant-manager/tenant/" + tenantId);
    },
  }
};
</script>
