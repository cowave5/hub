<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t(`config.label.name`)" prop="configName">
        <el-input v-model="queryParams.configName" :placeholder="$t(`config.placeholder.name`)"
                  clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item :label="$t(`label.date_create`)" label-width="120">
        <el-date-picker v-model="dateRange" style="width: 240px" value-format="yyyy-MM-dd HH:mm:ss" type="daterange"
                        range-separator="-" :start-placeholder="$t(`label.date_begin`)" :end-placeholder="$t(`label.date_end`)"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{$t('button.search')}}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{$t('button.reset')}}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                   :disabled="!checkPermit(['sys:config:new'])">{{$t('route.system.config.new')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" @click="handleUpdate"
                   :disabled="single || !checkPermit(['sys:config:edit'])">{{$t('route.system.config.edit')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete"
                   :disabled="multiple || !checkPermit(['sys:config:delete'])">{{$t('route.system.config.delete')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                   :disabled="!checkPermit(['sys:config:export'])">{{$t('route.system.config.export')}}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-refresh" size="mini" @click="handleRefreshCache"
                   :disabled="!checkPermit(['sys:config:cache'])">{{$t('route.system.config.cache')}}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="configList"
              @selection-change="handleSelectionChange" :header-cell-style="{'text-align':'center'}">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column :label="$t(`label.index`)" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(queryParams.page - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`config.label.name`)" align="left" prop="configName" :show-overflow-tooltip="true" />
      <el-table-column :label="$t(`config.label.key`)" align="left" prop="configKey" :show-overflow-tooltip="true" />
      <el-table-column :label="$t(`config.label.value`)" align="center" prop="configValue" />
      <el-table-column :label="$t(`config.label.remark`)" align="left" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column :label="$t(`config.label.default`)" align="center" prop="isDefault">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_no" :value="scope.row.isDefault"/>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`label.date_create`)" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t(`label.option`)" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">{{$t('route.system.config.edit')}}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     :disabled="!checkPermit(['sys:config:delete'])">{{$t('route.system.config.delete')}}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item :label="$t(`config.label.key`)" prop="configKey">
          <el-input v-model="form.configKey" :placeholder="$t(`config.placeholder.key`)" />
        </el-form-item>
        <el-form-item :label="$t(`config.label.name`)" prop="configName">
          <el-input v-model="form.configName" :placeholder="$t(`config.placeholder.name`)" />
        </el-form-item>
        <el-form-item :label="$t(`config.label.value`)" prop="configValue">
          <el-input v-model="form.configValue" :placeholder="$t(`config.placeholder.value`)" />
        </el-form-item>
        <el-form-item :label="$t(`config.label.default`)" prop="configType">
          <el-radio-group v-model="form.isDefault">
            <el-radio v-if="$i18n.locale==='zh'" v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
            <el-radio v-if="$i18n.locale==='en'" v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{dict.labelEn}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="valueParser">
          <span slot="label">
            <el-tooltip :content="$t(`config.content.parser`)" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
            {{$t('config.label.parser')}}
          </span>
          <el-input v-model="form.valueParser" :placeholder="$t(`config.placeholder.parser`)" />
        </el-form-item>
        <el-form-item prop="valueParam">
          <span slot="label">
            <el-tooltip :content="$t(`config.content.param`)" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
            {{ $t('config.label.param') }}
          </span>
          <el-input v-model="form.valueParam" :placeholder="$t(`config.placeholder.param`)"/>
        </el-form-item>
        <el-form-item :label="$t(`config.label.remark`)" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="..." />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm"
                   :disabled="!checkPermit(['sys:config:edit'])">{{$t('button.confirm')}}</el-button>
        <el-button @click="cancel">{{$t('button.cancel')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listConfig, getConfig, delConfig, addConfig, updateConfig, refreshCache } from "@/api/system/config";
import {checkPermit} from "@/utils/permission";

export default {
  name: "Config",
  dicts: ['sys_yes_no'],
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
      configList: [],
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
      // 表单参数
      form: {},
    };
  },
  created() {
    this.getList();
  },
  computed: {
    rules() {
      return {
        configName: [
          { required: true, message: this.$t(`config.rules.name`), trigger: "blur" }
        ],
        configKey: [
          { required: true, message: this.$t(`config.rules.key`), trigger: "blur" }
        ],
        configValue: [
          { required: true, message: this.$t(`config.rules.value`), trigger: "blur" }
        ]
      };
    }
  },
  methods: {
    checkPermit,
    /** 表单重置 */
    reset() {
      this.form = {
        configId: undefined,
        configName: undefined,
        configKey: undefined,
        configValue: undefined,
        isDefault: 1,
        remark: undefined
      };
      this.resetForm("form");
    },
    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.configId)
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 列表 */
    getList() {
      this.loading = true;
      listConfig(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.configList = response.data.list;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    /** 新增 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = this.$t(`config.dialog.title_new`);
    },
    /** 修改 */
    handleUpdate(row) {
      this.reset();
      const configId = row.configId || this.ids
      getConfig(configId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = this.$t(`config.dialog.title_edit`);
      });
    },
    /** 删除 */
    handleDelete(row) {
      const configIds = row.configId || this.ids;
      const msg = row.configId ? this.$t(`config.msg.confirm_delete`, { var1: row.configName }) : this.$t(`config.msg.select_delete`);
      this.$modal.confirm(msg).then(function() {
        return delConfig(configIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t(`msg.success_delete`));
      }).catch(() => {});
    },
    /** 导出 */
    handleExport() {
      this.download('/admin/api/v1/config/export', {
        ...this.queryParams
      }, this.$t(`config.excel`) + `_${new Date().getTime()}.xlsx`)
    },
    /** 刷新缓存 */
    handleRefreshCache() {
      refreshCache().then(() => {
        this.$modal.msgSuccess(this.$t(`msg.success_refresh`));
      });
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
          if (this.form.configId !== undefined) {
            updateConfig(this.form).then(response => {
              this.$modal.msgSuccess(this.$t(`msg.success_edit`));
              this.open = false;
              this.getList();
            });
          } else {
            addConfig(this.form).then(response => {
              this.$modal.msgSuccess(this.$t(`msg.success_create`));
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
