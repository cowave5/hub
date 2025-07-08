<template>
  <div class="app-container">
    <!--  筛选栏  -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="auto">
      <el-form-item :label="$t('config.label.name')" prop="configName">
        <el-input v-model="queryParams.configName" :placeholder="$t('config.placeholder.name')"
                  clearable style="width: 240px" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item :label="$t('commons.label.createTime')" label-width="120">
        <el-date-picker v-model="dateRange" style="width: 240px" type="daterange"
                        value-format="yyyy-MM-dd HH:mm:ss" range-separator="-"
                        :start-placeholder="$t('commons.label.beginDate')" :end-placeholder="$t('commons.label.endDate')"/>
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
                   :disabled="!checkPermit(['sys:config:create'])">
          {{$t('commons.button.create')}}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" @click="handleUpdate" :disabled="single">
          {{$t('commons.button.edit')}}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="handleDelete"
                   :disabled="multiple || !checkPermit(['sys:config:delete'])">
          {{$t('commons.button.delete')}}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                   :disabled="!checkPermit(['sys:config:export'])">
          {{$t('commons.button.export')}}
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-refresh" size="mini" @click="handleRefreshCache"
                   :disabled="!checkPermit(['sys:config:reset'])">
          {{$t('config.button.reset')}}
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
      <el-table-column v-if="cols[0].show" :label="$t('config.label.name')" align="center" prop="configName" :show-overflow-tooltip="true" />
      <el-table-column v-if="cols[1].show" :label="$t('config.label.key')" align="center" prop="configKey" :show-overflow-tooltip="true" />
      <el-table-column v-if="cols[2].show" :label="$t('config.label.value')" align="center" prop="configValue" />
      <el-table-column v-if="cols[3].show" :label="$t('dict.label.valueType')" align="center" prop="valueType" />
      <el-table-column v-if="cols[4].show" :label="$t('config.label.remark')" align="left" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column v-if="cols[5].show" :label="$t('config.label.default')" align="center" prop="isDefault">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.yes_no" :value="scope.row.isDefault"/>
        </template>
      </el-table-column>
      <el-table-column v-if="cols[6].show" :label="$t('commons.label.createTime')" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column v-if="cols[7].show" :label="$t('commons.label.updateTime')" align="center" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('commons.label.options')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">
            {{$t('commons.button.edit')}}
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     :disabled="!checkPermit(['sys:config:delete'])">
            {{$t('commons.button.delete')}}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :limit.sync="queryParams.pageSize" :page.sync="queryParams.page" @pagination="getList"/>

    <!-- 添加或修改 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item :label="$t('config.label.key')" prop="configKey">
          <el-input v-model="form.configKey" :placeholder="$t('config.placeholder.key')" />
        </el-form-item>
        <el-form-item :label="$t('config.label.name')" prop="configName">
          <el-input v-model="form.configName" :placeholder="$t('config.placeholder.name')" />
        </el-form-item>
        <el-form-item :label="$t('config.label.value')" prop="configValue">
          <el-input v-model="form.configValue" :placeholder="$t('config.placeholder.value')" />
        </el-form-item>
        <el-form-item :label="$t('config.label.default')" prop="configType">
          <el-radio-group v-model="form.isDefault">
            <el-radio v-for="dict in dict.type.yes_no" :key="dict.value" :label="dict.value">{{$t(dict.name)}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('dict.label.valueType')" prop="valueType">
          <el-select v-model="form.valueType" style="width: 100%;">
            <el-option v-for="item in valueOptions" :key="item.value" :value="item.value" :label="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item prop="valueParser">
          <span slot="label">
            <el-tooltip :content="$t('dict.text.parser')" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
            {{$t('dict.label.parser')}}
          </span>
          <el-input v-model="form.valueParser" :placeholder="$t('dict.placeholder.parser')" />
        </el-form-item>
        <el-form-item :label="$t('config.label.remark')" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="..." />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" :disabled="!checkPermit(['sys:config:edit'])">
          {{$t('commons.button.confirm')}}
        </el-button>
        <el-button @click="cancel">{{$t('commons.button.cancel')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getConfigList, getConfigInfo, delConfig, addConfig, updateConfig, refreshConfig } from "@/api/system/config";
import {checkPermit} from "@/utils/permission";

export default {
  name: "Config",
  dicts: ['yes_no'],
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
      // 类型
      valueOptions: [
        {value: "int32"},
        {value: "int64"},
        {value: "bool"},
        {value: "float"},
        {value: "double"},
        {value: "decimal"},
        {value: "datetime"},
        {value: "custom"},
      ],
      // 表单参数
      form: {},
      cols: [
        {key: 0, label: 'config.label.name', show: true},
        {key: 1, label: 'config.label.key', show: true},
        {key: 2, label: 'config.label.value', show: true},
        {key: 3, label: 'dict.label.valueType', show: true},
        {key: 4, label: 'commons.label.remark', show: true},
        {key: 5, label: 'config.label.default', show: true},
        {key: 6, label: 'commons.label.createTime', show: true},
        {key: 7, label: 'commons.label.updateTime', show: false},
      ],
    };
  },
  created() {
    this.getList();
  },
  computed: {
    rules() {
      return {
        configName: [
          { required: true, message: this.$t('config.rules.name'), trigger: "blur" }
        ],
        configKey: [
          { required: true, message: this.$t('config.rules.key'), trigger: "blur" }
        ],
        configValue: [
          { required: true, message: this.$t('config.rules.value'), trigger: "blur" }
        ]
      };
    }
  },
  methods: {
    checkPermit,
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
      this.$refs.queryForm.resetFields();
      this.handleQuery();
    },
    /** 列表 */
    getList() {
      this.loading = true;
      getConfigList(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.list = response.data.list;
          this.total = response.data.total;
          this.loading = false;
        }
      );
    },
    /** 新增 */
    handleAdd() {
      this.form = {
        configId: undefined,
        configName: undefined,
        configKey: undefined,
        configValue: undefined,
        valueType: undefined,
        valueParser: undefined,
        isDefault: 1,
        remark: undefined
      };
      this.title = this.$t('config.dialog.new');
      this.open = true;
    },
    /** 修改 */
    handleUpdate(row) {
      const configId = row.configId || this.ids
      this.title = this.$t('config.dialog.edit');
      getConfigInfo(configId).then(response => {
        this.form = response.data;
        this.open = true;
      });
    },
    /** 删除 */
    handleDelete(row) {
      const configIds = row.configId || this.ids;
      const msg = row.configId
          ? this.$t('config.confirm.delete', { arg1: row.configName })
          : this.$t('config.confirm.delete_select');
      this.$modal.confirm(msg).then(function() {
        return delConfig(configIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(this.$t('commons.msg.success.delete'));
      }).catch(() => {});
    },
    /** 导出 */
    handleExport() {
      this.download('/admin/api/v1/config/export', {
        ...this.queryParams
      }, this.$t('config.text.data') + `_${new Date().getTime()}.xlsx`)
    },
    /** 重置配置 */
    handleRefreshCache() {
      refreshConfig().then(() => {
        this.$modal.msgSuccess(this.$t('commons.msg.success.reset'));
        this.getList();
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
          if (this.form.configId !== undefined) {
            updateConfig(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('commons.msg.success.edit'));
              this.open = false;
              this.getList();
            });
          } else {
            addConfig(this.form).then(() => {
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
