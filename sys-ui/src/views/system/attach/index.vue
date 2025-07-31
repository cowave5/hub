<template>
  <div class="app-container">
    <!--  筛选栏  -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="auto">
      <el-form-item :label="$t('attach.label.tenant')" prop="ownerModule">
        <treeselect v-model="queryParams.ownerModule" :options="moduleOptions" style="width: 260px"
                    :disable-branch-nodes="true" :normalizer="normalizer"
                    :placeholder="$t('attach.placeholder.tenant')" :show-count="true"/>
      </el-form-item>
      <el-form-item :label="$t('attach.label.type')" prop="attachType" label-width="100px">
        <el-select v-model="queryParams.attachType" :placeholder="$t('attach.placeholder.type')" clearable>
          <el-option v-for="dict in dict.type.attach_type" :key="dict.code" :value="dict.code" :label="$t(dict.name)"/>
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('commons.label.createTime')" label-width="120">
        <el-date-picker v-model="dateRange" style="width: 240px" type="daterange"
                        value-format="yyyy-MM-dd HH:mm:ss" range-separator="-"
                        :start-placeholder="$t('commons.label.beginDate')" :end-placeholder="$t('commons.label.endDate')"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{$t('commons.button.search')}}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('commons.button.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <!--  操作栏  -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete"
                   :disabled="multiple || !checkPermit(['sys:attach:delete'])">
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
      <el-table-column :label="$t('attach.label.tenant')" align="center">
        <template slot-scope="{row: {tenantId}}">
          <template v-for="item in tenantOptions">
            <span v-if="tenantId === item.key">{{ $t(item.label) }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column :label="$t('attach.label.module')" align="center">
        <template slot-scope="{row: {ownerModule}}">
          <template v-for="module in moduleOptions">
            <template v-for="type in module.children">
              <span v-if="ownerModule === type.key">{{ $t(type.label) }}</span>
            </template>
          </template>
        </template>
      </el-table-column>
      <el-table-column :label="$t('attach.label.owner')" align="center" prop="ownerName" />
      <el-table-column :label="$t('attach.label.type')" align="center">
        <template slot-scope="{row: {attachType}}">
          <template v-for="item in dict.type.attach_type">
            <span v-if="attachType === item.code">{{ $t(item.name) }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column :label="$t('attach.label.name')" align="center" prop="attachName" />
      <el-table-column :label="$t('attach.label.size')" align="center">
        <template slot-scope="{row: {attachSize}}">
          {{ formatFileSize(attachSize) }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('attach.label.private')" align="center">
        <template slot-scope="{row: {isPrivate}}">
          <template v-for="item in yes_no">
            <span v-if="isPrivate === item.value">{{ $t(item.label) }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column :label="$t('attach.label.expire')" align="center" prop="expireTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.createTime')" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handlePreview(scope.row)"
                     :disabled="!checkPermit(['sys:attach:preview'])">
            {{$t('commons.button.preview')}}
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-download" @click="handleDownload(scope.row)"
                     :disabled="!checkPermit(['sys:attach:download'])">
            {{$t('commons.button.download')}}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :limit.sync="queryParams.pageSize" :page.sync="queryParams.page" @pagination="getList"/>

    <el-dialog title="预览" :visible.sync="open" append-to-body>
      <el-image :src="preViewUrl" style="display: block; max-width: 100%; max-height: 80vh;" />
    </el-dialog>
  </div>
</template>
<script>
import {checkPermit} from "@/utils/permission";
import {deleteAttach, listAttach, preViewAttach} from "@/api/system/attach";
import {listTenantOptions} from "@/api/system/tenant";
import {listTypeByGroup} from "@/api/system/dict";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { yes_no } from '@/utils/constants';
export default {
  name: "Attach",
  components: {Treeselect},
  dicts: ['attach_type'],
  data() {
    return {
      yes_no: yes_no,
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
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        page: 1,
        pageSize: 10,
        configName: undefined,
        configKey: undefined,
        configType: undefined
      },
      preViewUrl: undefined,
      // 表单参数
      form: {},
      // 租户选项
      tenantOptions: [],
      // 模块选项
      moduleOptions: [],
    };
  },
  created() {
    this.getList();
    this.getTenantOptions();
    this.getModuleOptions();
  },
  methods: {
    checkPermit,
    formatFileSize(bytes) {
      const k = 1024;
      const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB'];
      const i = Math.floor(Math.log(bytes) / Math.log(k));
      const size = bytes / Math.pow(k, i);
      const formattedSize = parseFloat(size.toFixed(2));
      return `${formattedSize} ${sizes[i]}`;
    },
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
    /** 获取租户选项 */
    getTenantOptions() {
      listTenantOptions().then(response => {
        this.tenantOptions = response.data;
      });
    },
    /** 获取模块选项 */
    getModuleOptions() {
      listTypeByGroup("domain_module").then(response => {
        this.moduleOptions = response.data;
      });
    },
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.attachId)
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
      listAttach(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.list = response.data.list;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    /** 删除 */
    handleDelete() {
      const attachIds = this.ids;
      this.$modal.confirm(this.$t('attach.confirm.delete_select')).then(function() {
        return deleteAttach(attachIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('commons.msg.success.delete'));
      }).catch(() => {});
    },
    /** 下载 */
    handleDownload(row) {
      this.$download.file("/admin/api/v1/attach/" + row.attachId, row.attachName)
    },
    /** 预览 */
    handlePreview(row) {
      preViewAttach(row.attachId).then(response => {
        this.preViewUrl = response.data;
        this.open = true;
      });
    },
  }
};
</script>
