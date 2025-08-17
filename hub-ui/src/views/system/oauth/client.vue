<template>
  <div class="app-container">
    <!--  筛选栏  -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="客户端名称" prop="clientName">
        <el-input v-model="queryParams.clientName"
                  clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
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
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete"
                   :disabled="multiple || !checkPermit(['oauth:client:delete'])">
          {{$t('commons.button.delete')}}
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" :cols="cols"/>
    </el-row>

    <!--  列表数据  -->
    <el-table :data="list" @selection-change="handleSelectionChange"
              v-loading="loading"  :header-cell-style="{'text-align':'center'}">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t('commons.label.index')" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="客户端名称" align="center" prop="clientName" :show-overflow-tooltip="true" />
      <el-table-column label="回调地址" align="center" prop="redirectUrl" :show-overflow-tooltip="true" width="280" />
      <el-table-column label="支持的授权方式" align="center" prop="grantType" :show-overflow-tooltip="true">
        <template v-slot="scope">
          {{ scope.row.grantType.join(', ') }}
        </template>
      </el-table-column>
      <el-table-column label="授权范围" align="center" prop="authScope" :show-overflow-tooltip="true">
        <template v-slot="scope">
          {{ scope.row.authScope.join(', ') }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.createTime')" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     :disabled="!checkPermit(['oauth:client:delete'])">
            {{$t('commons.button.delete')}}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :limit.sync="queryParams.pageSize" :page.sync="queryParams.page" @pagination="getList"/>

    <!-- 添加 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <div class="dialog-description" style="margin-bottom: 20px; padding: 0 10px;">
        <p style="color: #999; font-size: 12px; margin-top: 5px;">
          获取授权码地址：http://www.cowave.com/oauth/authorize
        </p>
        <p style="color: #999; font-size: 12px; margin-top: 5px;">
          获取令牌地址：http://www.cowave.com:81/admin/api/v1/oauth/client/authorize/token
        </p>
        <p style="color: #999; font-size: 12px; margin-top: 5px;">
          获取用户信息地址：http://www.cowave.com:81/admin/api/v1/profile
        </p>
        <p style="color: #666; line-height: 1.5;">
          <i class="el-icon-info" style="color: #409EFF;"></i>
          提示：客户端注册后，请妥善保管client_id和client_secret，该信息只会显示一次
        </p>
        <template v-if="showCredentials">
          <p style="color: #666; line-height: 1.5;">
            <span style="font-weight: bold;">client_id：</span>{{ clientId }}
          </p>
          <p style="color: #666; line-height: 1.5;">
            <span style="font-weight: bold;">client_secret：</span>{{ clientSecret }}
          </p>
        </template>
      </div>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="客户端名称" prop="clientName">
          <el-input v-model="form.clientName" />
        </el-form-item>
        <el-form-item label="回调地址" prop="redirectUrl">
          <el-input v-model="form.redirectUrl" />
        </el-form-item>
        <el-form-item label="授权方式" prop="grantType">
          <el-select v-model="form.grantType" multiple style="width: 100%;">
            <el-option v-for="item in grantOptions" :key="item.value" :value="item.value" :label="item.label"/>
          </el-select>
        </el-form-item>
        <el-form-item label="授权范围" prop="authScope">
          <el-select v-model="form.authScope" multiple style="width: 100%;">
            <el-option v-for="item in scopeOptions" :key="item.value" :value="item.value" :label="item.value"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="!showCredentials">
        <el-button type="primary" @click="submitForm" :disabled="!checkPermit(['oauth:client:create'])">
          {{$t('commons.button.confirm')}}
        </el-button>
        <el-button @click="cancel">{{$t('commons.button.cancel')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {checkPermit} from "@/utils/permission";
import { addClient, delClient, getClientList } from '@/api/system/oauth'
export default {
  name: "oauth-client",
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
      // 表格数据
      list: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      showCredentials: false,
      clientId: undefined,
      clientSecret: undefined,
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        clientName: undefined
      },
      // 表单参数
      form: {
        grantType: ["authorization_code"],
        authScope: ["read_user"]
      },
      // 授权类型
      grantOptions: [
        {value: "authorization_code", label: "授权码模式"}
      ],
      // 授权范围
      scopeOptions: [
        {value: "read_user"},
        {value: "read_dept"}
      ],
    };
  },
  created() {
    this.getList();
  },
  computed: {
    rules() {
      return {
        clientName: [
          { required: true, message: "客户端名称不能为空", trigger: "blur" }
        ],
        redirectUrl: [
          { required: true, message: "回调地址不能为空", trigger: "blur" },
          {
            pattern: /^(https?:\/\/)[^\s]+$/i,
            message: "非法回调地址，请以 http:// 或 https:// 开头",
            trigger: "blur"
          }
        ],
        grantType: [
          { required: true, message: "授权方式不能为空", trigger: "blur" }
        ],
        authScope: [
          { required: true, message: "授权范围不能为空", trigger: "blur" }
        ]
      };
    }
  },
  methods: {
    checkPermit,
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
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
      getClientList(this.queryParams).then(response => {
          this.list = response.data.list;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    /** 新增 */
    handleAdd() {
      this.title = "新增授权客户端";
      this.open = true;
      this.$refs.form.resetFields();
    },
    /** 取消 */
    cancel() {
      this.open = false;
    },
    /** 提交 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          addClient(this.form).then(resp => {
            this.clientId = resp.data.clientId;
            this.clientSecret = resp.data.clientSecret;
            this.$modal.msgSuccess(this.$t('commons.msg.success.create'));
            this.showCredentials = true;
            this.getList();
          });
        }
      });
    },
    /** 删除 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      const msg = row.id
          ? "确认删除客户端'" + row.clientName + "'"
          : "确认删除所选客户端";
      this.$modal.confirm(msg).then(function() {
        return delClient(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('commons.msg.success.delete'));
      }).catch(() => {});
    },
  }
}
</script>
